package com.vmware.scan.collector.gerrit.restcalls;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gson.stream.JsonReader;
import com.vmware.scan.collector.main.pojos.AlgorithmInputObject;
import com.vmware.scan.collector.main.pojos.CLFile;

public class GerritDataFileReader {
	private static Boolean isAbandonedOrNew = false;
	List<String> plusOneReviewers = new ArrayList<String>();
	List<String> plusTwoReviewers = new ArrayList<String>();

	private List<AlgorithmInputObject> getChangeLists(int limit) throws IOException {
		try {
			System.out.println("Getting changelists");
			URL url = new URL(
					"https://gerrit-vra.eng.vmware.com/changes/?q=project:vcac+status:merged&o=CURRENT_REVISION&o=CURRENT_FILES&o=LABELS&o=REVIEWED&n="
							+ limit);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			InputStreamReader isr = new InputStreamReader((conn.getInputStream()));
			BufferedReader br = new BufferedReader(isr);
			JsonReader reader = new JsonReader(br);
			reader.setLenient(true);
			return readChangeListArray(reader);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// reader.close();
		}
		return null;
	}

	public HashMap<String, List<CLFile>> getChangeIDDetails(String changesetID) {
		try {
			System.out.println("Getting ChangeID details of Changeset ID: " + changesetID);
			URL url = new URL("https://gerrit-vra.eng.vmware.com/changes/vcac~master~" + changesetID
					+ "?o=CURRENT_REVISION&o=CURRENT_FILES");

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			InputStreamReader isr = new InputStreamReader((conn.getInputStream()));
			BufferedReader br = new BufferedReader(isr);
			JsonReader reader = new JsonReader(br);
			String ownerID = null;
			List<CLFile> files = null;
			String status = null;
			reader.setLenient(true);
			reader.beginObject();
			while (reader.hasNext()) {
				String field = reader.nextName();
				if (field.equalsIgnoreCase("status")) {
					status = reader.nextString();
					isAbandonedOrNew = status.equals("ABANDONED") || status.equals("NEW");
					if (isAbandonedOrNew)
						break;
				} else if (field.equalsIgnoreCase("owner")) {
					ownerID = readOwnerObject(reader);
				} else if (field.equalsIgnoreCase("revisions")) {
					if (!isAbandonedOrNew)
						files = readRevisionsObject(reader);
					else
						files = null;
				} else {
					reader.skipValue();
				}
			}
			HashMap<String, List<CLFile>> changesetDetails = new HashMap<>();
			changesetDetails.put(ownerID, files);
			return changesetDetails;

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public HashMap<String, List<String>> getChangeIDReviewers(String changesetID) {
		try {
			HashMap<String, List<String>> changesetReviewers = new HashMap<String, List<String>>();
			if (!isAbandonedOrNew) {
				System.out.println("Getting ChangeID Reviewers of Changeset ID: " + changesetID);
				URL url = new URL(
						"https://gerrit-vra.eng.vmware.com/changes/vcac~master~" + changesetID + "/reviewers");

				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Accept", "application/json");

				if (conn.getResponseCode() != 200) {
					throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
				}

				plusOneReviewers = new ArrayList<String>();
				plusTwoReviewers = new ArrayList<String>();
				InputStreamReader isr = new InputStreamReader((conn.getInputStream()));
				BufferedReader br = new BufferedReader(isr);
				JsonReader reader = new JsonReader(br);
				reader.setLenient(true);
				reader.beginArray();
				while (reader.hasNext()) {
					changesetReviewers.putAll(parseReviewers(reader));
				}
				reader.endArray();
				return changesetReviewers;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public HashMap<String, List<String>> parseReviewers(JsonReader jsonReader) {
		HashMap<String, List<String>> reviewers = new HashMap<String, List<String>>();

		try {
			String codeReview = "";
			String accountName = "";
			String accountID = "";
			jsonReader.beginObject();
			while (jsonReader.hasNext()) {
				String field = jsonReader.nextName();
				if (field.equalsIgnoreCase("approvals")) {
					codeReview = readApprovalsObject(jsonReader);
				} else if (field.equalsIgnoreCase("_account_id")) {
					if (null != codeReview && (codeReview.equals("+2") || codeReview.equals("+1"))) {
						accountID = String.valueOf(jsonReader.nextLong());
					} else {
						jsonReader.skipValue();
					}
				} else if (field.equalsIgnoreCase("name")) {
					if (null != codeReview && (codeReview.equals("+2") || codeReview.equals("+1"))) {
						accountName = jsonReader.nextString();
					}
					if (null != codeReview && (codeReview.equals("+2"))) {
						plusTwoReviewers.add(accountID + ":" + accountName);
					} else if (null != codeReview && (codeReview.equals("+1"))) {
						plusOneReviewers.add(accountID + ":" + accountName);
					} else {
						jsonReader.skipValue();
					}
				} else {
					jsonReader.skipValue();
				}
			}
			jsonReader.endObject();
		} catch (IOException e) {
			e.printStackTrace();
		}
		reviewers.put("+1", plusOneReviewers);
		reviewers.put("+2", plusTwoReviewers);
		return reviewers;
	}

	private List<AlgorithmInputObject> readChangeListArray(JsonReader jsonReader) throws IOException {
		List<AlgorithmInputObject> dataObjects = new ArrayList<>();

		jsonReader.beginArray();
		while (jsonReader.hasNext()) {
			dataObjects.add(readEachChangeList(jsonReader));
		}
		jsonReader.endArray();

		return dataObjects;
	}

	private AlgorithmInputObject readEachChangeList(JsonReader jsonReader) throws IOException {
		String id = null, owner = null;
		int totalqoc = 0;
		List<CLFile> files = null;
		boolean isPassed = true;
		jsonReader.beginObject();
		while (jsonReader.hasNext()) {
			String field = jsonReader.nextName();
			if (field.equalsIgnoreCase("id")) {
				id = jsonReader.nextString();
			} else if (field.equalsIgnoreCase("owner")) {
				owner = readOwnerObject(jsonReader);
			} else if (field.equalsIgnoreCase("revisions")) {
				files = readRevisionsObject(jsonReader);
			} else if (field.equalsIgnoreCase("labels")) {
				isPassed = isPassed && readLabelsObject(jsonReader);
			} else if (field.equalsIgnoreCase("insertions")) {
				totalqoc += jsonReader.nextInt();
			} else if (field.equalsIgnoreCase("deletions")) {
				totalqoc += jsonReader.nextInt();
			} else {
				jsonReader.skipValue();
			}
		}

		jsonReader.endObject();
		// Generate a weight randomly now.
		return new AlgorithmInputObject(id, owner, files, null, isPassed, totalqoc, ((Math.random() * 10) % 6),
				((int) (Math.random() * 10) % 5));
	}

	private boolean readLabelsObject(JsonReader jsonReader) throws IOException {
		jsonReader.beginObject();
		boolean isPassed = true;
		while (jsonReader.hasNext()) {
			String field = jsonReader.nextName();
			if (field.equalsIgnoreCase("Verified")) {
				isPassed = isPassed && readVerifiedObject(jsonReader);
			} else {
				jsonReader.skipValue();
			}
		}

		jsonReader.endObject();
		return isPassed;
	}

	private boolean readVerifiedObject(JsonReader jsonReader) throws IOException {
		jsonReader.beginObject();
		boolean isPassed = true;
		while (jsonReader.hasNext()) {
			String field = jsonReader.nextName();
			if (field.equalsIgnoreCase("approved")) {
				isPassed = true;
				jsonReader.skipValue();
			} else if (field.equalsIgnoreCase("rejected")) {
				isPassed = false;
				jsonReader.skipValue();
			} else {
				jsonReader.skipValue();
			}
		}

		jsonReader.endObject();
		return isPassed;
	}

	private List<CLFile> readRevisionsObject(JsonReader jsonReader) throws IOException {
		List<CLFile> files = null;
		jsonReader.beginObject();
		while (jsonReader.hasNext()) {
			jsonReader.nextName();
			files = readCLObject(jsonReader);
		}
		jsonReader.endObject();
		return files;
	}

	private List<CLFile> readCLObject(JsonReader jsonReader) throws IOException {
		jsonReader.beginObject();
		List<CLFile> files = null;
		while (jsonReader.hasNext()) {
			String field = jsonReader.nextName();
			if (field.equalsIgnoreCase("files")) {
				files = readFileNames(jsonReader);
			} else {
				jsonReader.skipValue();
			}
		}
		jsonReader.endObject();
		return files;
	}

	private List<CLFile> readFileNames(JsonReader jsonReader) throws IOException {
		jsonReader.beginObject();
		List<CLFile> clFiles = new ArrayList<CLFile>();
		String filename = null;
		String field = null;
		while (jsonReader.hasNext()) {
			CLFile file = new CLFile();
			filename = jsonReader.nextName();
			file.setFileName(filename);
			int insertions = 0, deletions = 0;
			jsonReader.beginObject();
			while (jsonReader.hasNext()) {
				field = jsonReader.nextName();
				if (field.equalsIgnoreCase("lines_inserted")) {
					insertions = jsonReader.nextInt();
				} else if (field.equalsIgnoreCase("lines_deleted")) {
					deletions = jsonReader.nextInt();
				} else {
					jsonReader.skipValue();
				}
			}

			file.setInsertions(insertions);
			file.setDeletions(deletions);
			clFiles.add(file);
			jsonReader.endObject();
		}

		jsonReader.endObject();
		return clFiles;
	}

	private String readOwnerObject(JsonReader jsonReader) throws IOException {
		String owner = null;
		jsonReader.beginObject();
		while (jsonReader.hasNext()) {
			String field = jsonReader.nextName();
			if (field.equalsIgnoreCase("_account_id")) {
				owner = jsonReader.nextString();
			}
		}
		jsonReader.endObject();
		return owner;
	}

	private String readApprovalsObject(JsonReader jsonReader) throws IOException {
		String codeReview = null;
		jsonReader.beginObject();
		while (jsonReader.hasNext()) {
			String field = jsonReader.nextName();
			if (field.equalsIgnoreCase("Code-Review")) {
				codeReview = jsonReader.nextString();
			} else {
				jsonReader.skipValue();
			}
		}
		jsonReader.endObject();
		return codeReview;
	}

	public List<AlgorithmInputObject> getGerritData(int limit) {
		try {
			return updateReviewers(this.getChangeLists(limit));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private List<AlgorithmInputObject> updateReviewers(List<AlgorithmInputObject> data) throws IOException {
		System.out.println("Updating reviewers");
		for (int i = 0; i < data.size(); i++) {
			try {
				// System.out.println("Processing : "+data.get(i).getId());
				URL url = new URL("https://gerrit-vra.eng.vmware.com/changes/" + data.get(i).getId() + "/reviewers");
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Accept", "application/json");

				if (conn.getResponseCode() != 200) {
					throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
				}

				InputStreamReader isr = new InputStreamReader((conn.getInputStream()));
				BufferedReader br = new BufferedReader(isr);
				JsonReader reader = new JsonReader(br);
				reader.setLenient(true);
				while ((br.readLine()) != null) {
					data.get(i).setReviewers(getReviewers(reader));
				}

				conn.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				// reader.close();
			}
		}

		System.out.println("Done updating reviwers.");
		return data;
	}

	private List<String> getReviewers(JsonReader reader) throws IOException {
		List<String> reviewers = new ArrayList<>();
		reader.beginArray();
		while (reader.hasNext()) {
			reader.beginObject();
			while (reader.hasNext()) {
				String field = reader.nextName();
				if (field.equalsIgnoreCase("_account_id")) {
					reviewers.add(String.valueOf(reader.nextLong()));
				} else {
					reader.skipValue();
				}
			}
			reader.endObject();
		}
		reader.endArray();
		return reviewers;
	}

	public static void main(String[] args) {
		GerritDataFileReader reader = new GerritDataFileReader();
		List<String> changeSetIDs = new ArrayList<>();
		// changeSetIDs.add("Ic2f2231d2a9e7e0dd77bba07ff3f56503562712e");
		// changeSetIDs.add("I9b67613cda36b82af5232d56e470c3a6cf5be5e4");
		changeSetIDs.add("Idabbc18c637ef1e781052534c03b81a60195e9a3");
		for (int j = 0; j < changeSetIDs.size(); j++) {
			HashMap<String, List<String>> asd = reader.getChangeIDReviewers(changeSetIDs.get(j));
			Iterator asdd = asd.entrySet().iterator();
			while (asdd.hasNext()) {
				Map.Entry pair = (Map.Entry) asdd.next();
				System.out.println("For Key : " + pair.getKey());
				if (null != pair.getValue()) {
					System.out.println("Values are : ");
					List<String> hasrd = (List<String>) pair.getValue();
					for (int i = 0; i < hasrd.size(); i++) {
						System.out.println("Value" + i + ":" + hasrd.get(i));
					}
				}
			}
			HashMap<String, List<CLFile>> files = reader.getChangeIDDetails(changeSetIDs.get(j));
			Iterator filesIter = asd.entrySet().iterator();
			while (filesIter.hasNext()) {
				Map.Entry pair = (Map.Entry) filesIter.next();
				System.out.println("For Key : " + pair.getKey());
				if (null != pair.getValue()) {
					System.out.println("Values are : ");
					List<CLFile> hasrd = (List<CLFile>) pair.getValue();
					for (int i = 0; i < hasrd.size(); i++) {
						System.out.println("Value" + i + ":" + hasrd.get(i));
					}
				}
			}
		}
	}
}

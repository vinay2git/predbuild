package com.vmware.scan.collector.parser;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vmware.scan.collector.main.pojos.CLFile;
import com.vmware.scan.collector.main.pojos.ChangeListDetails;
import com.vmware.scan.collector.main.pojos.StageDetails;
import com.vmware.scan.collector.main.pojos.SuiteDetails;
import com.vmware.scan.collector.main.pojos.TaskDetails;
import com.vmware.scan.collector.vrcs.restcalls.VRCSRestApiCalls;

public class ParseTester {
	private static FileWriter changesetFileWriter;
	private static BufferedWriter changesetBufferedWriter;
	private static String parseOutputFilePath = "CollectorOutput/changesetWithSuiteData1.txt";
	private static HttpClient httpClientPostData;
	private static final String DataDumpURL = "http://10.112.82.108:8080/changes";

	public static void main(String[] args) {

		VRCSRestApiCalls vRCSObj = new VRCSRestApiCalls();
		vRCSObj.getConnectionToken();
		List<ChangeListDetails> changeListDetails = vRCSObj.fetchAllExecutionsWithFilter("vra-on-vrcs",
				"PIPELINE_NAME");
		pushToFile(changeListDetails);
		while (!vRCSObj.getNextLinkHREF().equals("END")) {
			changeListDetails = vRCSObj.fetchExecutionsFromLink();
			pushToFile(changeListDetails);
		}
	}

	public static void writeToFile(List<ChangeListDetails> changeListDetails) {
		String appendString = "";
		try {
			changesetFileWriter = new FileWriter(parseOutputFilePath, true);
			changesetBufferedWriter = new BufferedWriter(changesetFileWriter);
			for (int a = 0; a < changeListDetails.size(); a++) {
				if (changeListDetails.get(a).getChangelistID().length() > 5) {
					appendString = changeListDetails.get(a).getChangelistID() + "&"
							+ changeListDetails.get(a).getOwnerID() + "&" + changeListDetails.get(a).getOwnerName()
							+ "&";
					if (null != changeListDetails.get(a).getPlusOneReviewers()) {
						List<String> plusOneReviewers = changeListDetails.get(a).getPlusOneReviewers();
						if (changeListDetails.get(a).getPlusOneReviewers().size() > 0) {
							appendString += "PlusOne=>";
							for (int b = 0; b < plusOneReviewers.size(); b++) {
								if (b == (plusOneReviewers.size() - 1))
									appendString += plusOneReviewers.get(b) + ";";
								else
									appendString += plusOneReviewers.get(b) + ",";
							}
						}
					}
					if (null != changeListDetails.get(a).getPlusTwoReviewers()) {
						List<String> plusTwoReviewers = changeListDetails.get(a).getPlusTwoReviewers();
						if (changeListDetails.get(a).getPlusTwoReviewers().size() > 0) {
							appendString += "PlusTwo=>";
							for (int b = 0; b < plusTwoReviewers.size(); b++) {
								if (b == (plusTwoReviewers.size() - 1))
									appendString += plusTwoReviewers.get(b) + "&";
								else
									appendString += plusTwoReviewers.get(b) + ",";
							}
						}
					}
					if (null != changeListDetails.get(a).getFiles()) {
						List<CLFile> files = changeListDetails.get(a).getFiles();
						for (int b = 0; b < files.size(); b++) {
							if (b == (files.size() - 1))
								appendString += files.get(b).getFileName() + "=>" + files.get(b).getInsertions() + ":"
										+ files.get(b).getDeletions() + "&";
							else
								appendString += files.get(b).getFileName() + "=>" + files.get(b).getInsertions() + ":"
										+ files.get(b).getDeletions() + ",";
						}
					}
					List<StageDetails> stages = changeListDetails.get(a).getStages();
					for (int b = 0; b < stages.size(); b++) {
						appendString += stages.get(b).getStageName() + "=>";
						List<TaskDetails> tasks = stages.get(b).getTasks();
						for (int c = 0; c < tasks.size(); c++) {
							if (stages.get(b).getStageName().equals("Gerrit") && (c == (tasks.size() - 1)))
								appendString += tasks.get(c).getTaskName() + ":" + tasks.get(c).getTaskStatus() + "&";
							else
								appendString += tasks.get(c).getTaskName() + ":" + tasks.get(c).getTaskStatus() + ",";
							if (null != tasks.get(c).getSuites()) {
								List<SuiteDetails> suites = tasks.get(c).getSuites();
								for (int d = 0; d < suites.size(); d++) {
									if ((d == (suites.size() - 1)) && (c == (tasks.size() - 1)))
										appendString += suites.get(d).getSuiteName() + "="
												+ suites.get(d).getSuiteStatus() + "&";
									else
										appendString += suites.get(d).getSuiteName() + "="
												+ suites.get(d).getSuiteStatus() + ",";
								}
							}
						}
					}
					if (!(appendString.contains("NOT_STARTED") || appendString.contains("IN_PROGRESS")
							|| appendString.contains("null"))) {
						int lastCharAmp = appendString.lastIndexOf("&");
						if (lastCharAmp == (appendString.length() - 1))
							appendString = appendString.substring(0, (lastCharAmp - 1));
						changesetBufferedWriter.write(appendString);
						if (a != (changeListDetails.size() - 1))
							changesetBufferedWriter.newLine();
					}
				}
			}

			changesetBufferedWriter.close();
			changesetFileWriter.close();
		} catch (IOException ex) {
			System.out.println("IO Exception Caught " + ex.getMessage());
		}
	}

	public static void pushToFile(List<ChangeListDetails> changeListDetails) {
		for (int a = 0; a < changeListDetails.size(); a++) {
			if (changeListDetails.get(a).getChangelistID().length() > 5) {
				//Gson changeSetJasonObject = new Gson();

				httpClientPostData = HttpClientBuilder.create().build();
				HttpPost tokenRequest = new HttpPost(DataDumpURL);
				
				JSONObject changeListJsonObj = new JSONObject();
				changeListJsonObj.put("id", changeListDetails.get(a).getChangelistID());
				changeListJsonObj.put("ownerid", changeListDetails.get(a).getOwnerID());
				changeListJsonObj.put("ownername", changeListDetails.get(a).getOwnerName());
				changeListJsonObj.put("reviews", getReviewersFromChangeList(changeListDetails.get(a)));
				changeListJsonObj.put("files", getFilesFromChangeList(changeListDetails.get(a).getFiles()));
				changeListJsonObj.put("gerrit", getStageDataFromChangeList(changeListDetails.get(a).getStages(), "Gerrit"));
				changeListJsonObj.put("bat", getStageDataFromChangeList(changeListDetails.get(a).getStages(), "BAT"));
				changeListJsonObj.put("regression", getStageDataFromChangeList(changeListDetails.get(a).getStages(), "Regression Tests"));
				//changeListJsonObj.put("processed", false);
				
				StringEntity dataDumpEntity = null;
				try {
					//dataDumpEntity = new StringEntity(
					//		changeSetJasonObject.toJson(changeListDetails.get(a), ChangeListDetails.class));
					dataDumpEntity = new StringEntity(changeListJsonObj.toString());
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (dataDumpEntity != null)
					tokenRequest.setEntity(dataDumpEntity);
				tokenRequest.setHeader("content-type", "application/json");
				try {
					HttpResponse dumpResponse = httpClientPostData.execute(tokenRequest);
					if (dumpResponse.getStatusLine().getStatusCode() != 200) {
						System.out.println("Post Failed " + dumpResponse.getStatusLine().getStatusCode() + ":"
								+ dumpResponse.getStatusLine().getReasonPhrase());
					}
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public static JSONArray getReviewersFromChangeList(ChangeListDetails changeListDet) {
		JSONArray reviewers = new JSONArray();
		
		JSONObject reviewPlusOneJsonObj = null;
		if (changeListDet.getPlusOneReviewers().size() > 0) {
			reviewPlusOneJsonObj = new JSONObject();
			for (int b = 0; b < changeListDet.getPlusOneReviewers().size(); b++) {
				reviewPlusOneJsonObj.put("name", "one");
				String[] reviewerDetails = changeListDet.getPlusOneReviewers().get(b).split(":");
				reviewPlusOneJsonObj.put("value", reviewerDetails[0]);
				reviewers.put(reviewPlusOneJsonObj);
			}
		}
		JSONObject reviewPlusTwoJsonObj = null;
		if (changeListDet.getPlusTwoReviewers().size() > 0) {
			reviewPlusTwoJsonObj = new JSONObject();
			for (int b = 0; b < changeListDet.getPlusTwoReviewers().size(); b++) {
				reviewPlusTwoJsonObj.put("name", "two");
				String[] reviewerDetails = changeListDet.getPlusTwoReviewers().get(b).split(":");
				reviewPlusTwoJsonObj.put("value", reviewerDetails[0]);
				reviewers.put(reviewPlusTwoJsonObj);
			}
		}
		return reviewers;
	}
	
	public static JSONArray getFilesFromChangeList(List<CLFile> fileListDet) {
		JSONArray files = new JSONArray();
		
		if (fileListDet.size() > 0) {
			for (int b = 0; b < fileListDet.size(); b++) {
				JSONObject currentFile = new JSONObject();
				currentFile.put("name", fileListDet.get(b).getFileName());
				currentFile.put("value", fileListDet.get(b).getTotalQOC());
				files.put(currentFile);
			}
		}
		return files;
	}
	
	public static JSONArray getGerritDataFromChangeList(List<StageDetails> stageDet) {
		JSONArray gerritData = new JSONArray();
		
		for(StageDetails currentStage : stageDet) {
			if (currentStage.getStageName().equalsIgnoreCase("gerrit")) {
				for (int c = 0; c < currentStage.getTasks().size(); c++) {
					JSONObject currentTask = new JSONObject();
					currentTask.put("name", currentStage.getTasks().get(c).getTaskName());
					currentTask.put("value", currentStage.getTasks().get(c).getTaskStatus());
					gerritData.put(currentTask);
				}
			} else {
				continue;
			}
		}
		return gerritData;
	}
	
	public static JSONArray getStageDataFromChangeList(List<StageDetails> stageDet, String stageName) {
		JSONArray stageData = new JSONArray();
		
		for(StageDetails currentStage : stageDet) {
			if (currentStage.getStageName().equalsIgnoreCase(stageName)) {
				for (int c = 0; c < currentStage.getTasks().size(); c++) {
					JSONObject currentTask = new JSONObject();
					currentTask.put("name", currentStage.getTasks().get(c).getTaskName());
					currentTask.put("value", currentStage.getTasks().get(c).getTaskStatus());
					stageData.put(currentTask);
					if (currentStage.getTasks().get(c).getSuites() != null) {
						for (int d = 0; d < currentStage.getTasks().get(c).getSuites().size(); d++) {
							currentTask = new JSONObject();
							currentTask.put("name", currentStage.getTasks().get(c).getSuites().get(d).getSuiteName());
							currentTask.put("value", currentStage.getTasks().get(c).getSuites().get(d).getSuiteStatus());
							stageData.put(currentTask);
						}
					}
				}
			} else {
				continue;
			}
		}
		return stageData;
	}
}

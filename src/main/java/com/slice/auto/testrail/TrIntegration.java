package com.slice.auto.testrail;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TrIntegration {

    private final APIClient client;

    public TrIntegration(String testRailURL, String testRailUsername, String testRailApiKey) {
        client = new APIClient(testRailURL);
        client.setUser(testRailUsername);
        client.setPassword(testRailApiKey);
    }

    void updateTestStatus(String runId, String caseId, int result, String comment) throws IOException, APIException {
        Map<Object, Object> data = new HashMap<>();
        data.put("status_id", result);
        data.put("comment", comment);
        client.sendPost("add_result_for_case/" + runId + "/" + caseId, data);
    }

    String getTestResultId(String runId, String caseId) throws IOException, APIException {
        JsonArray jsonArray = new Gson().toJsonTree(client.sendGet("get_results_for_case/" + runId + "/" + caseId + "&limit=1")).getAsJsonObject().getAsJsonArray("results");
        JsonObject JsonObject = (JsonObject) jsonArray.get(0);
        return JsonObject.get("id").toString();
    }

    void addAttachmentToResult(String runId, String caseId, String file) throws APIException, IOException {
        client.sendPost("add_attachment_to_result/" + getTestResultId(runId, caseId), file);
    }

    public void resetTestStatus(String runId) throws IOException, APIException {
        String onlyFailedTestsFilter = System.getProperty("isXmlDynamic").equals("true") ? "&status_id=5" : "";
        JsonArray array = new Gson().toJsonTree(client.sendGet("get_tests/" + runId + onlyFailedTestsFilter)).getAsJsonObject().getAsJsonArray("tests");
        array.forEach(item -> {
            JsonObject obj = (JsonObject) item;
            try {
                updateTestStatus(runId, obj.get("case_id").toString(), 4, "Resetting test status for new run.");
                System.out.println("Resetting TestRail status for test ID " + obj.get("case_id").toString() + "...");
            } catch (IOException | APIException e) {
                e.printStackTrace();
            }
        });
    }
}

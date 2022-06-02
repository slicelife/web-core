/**
 * TestRail API binding for Java (API v2, available since TestRail 3.0)
 * Updated for TestRail 5.7
 * Learn more:
 * http://docs.gurock.com/testrail-api2/start
 * http://docs.gurock.com/testrail-api2/accessing
 * Copyright Gurock Software GmbH. See license.md for details.
 */

package com.slice.auto.testrail;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class APIClient {
    private String m_user;
    private String m_password;
    private final String m_url;
    private final Gson gson = new Gson();

    public APIClient(String base_url) {
        if (!base_url.endsWith("/")) {
            base_url += "/";
        }
        this.m_url = base_url + "index.php?/api/v2/";
    }

    /**
     * Get/Set User
     * Returns/sets the user used for authenticating the API requests.
     */

    public String getUser() {
        return this.m_user;
    }

    public void setUser(String user) {
        this.m_user = user;
    }

    /**
     * Get/Set Password
     * Returns/sets the password used for authenticating the API requests.
     */

    public String getPassword() {
        return this.m_password;
    }

    public void setPassword(String password) {
        this.m_password = password;
    }

    /**
     * Send Get
     * Issues a GET request (read) against the API and returns the result
     * (as Object, see below).
     * Arguments:
     * uri                  The API method to call including parameters
     * (e.g. get_case/1)
     * Returns the parsed JSON response as standard object which can
     * either be an instance of JsonObject or JSONArray (depending on the
     * API method). In most cases, this returns a JsonObject instance which
     * is basically the same as java.util.Map.
     * If 'get_attachment/:attachment_id', returns a String
     */
    public Object sendGet(String uri, String data) throws IOException, APIException {
        return this.sendRequest("GET", uri, data);
    }

    public Object sendGet(String uri) throws IOException, APIException {
        return this.sendRequest("GET", uri, null);
    }

    /**
     * Send POST
     * Issues a POST request (write) against the API and returns the result
     * (as Object, see below).
     * Arguments:
     * uri                  The API method to call including parameters
     * (e.g. add_case/1)
     * data                 The data to submit as part of the request (e.g.,
     * a map)
     * If adding an attachment, must be the path
     * to the file
     * Returns the parsed JSON response as standard object which can
     * either be an instance of JsonObject or JSONArray (depending on the
     * API method). In most cases, this returns a JsonObject instance which
     * is basically the same as java.util.Map.
     */
    public Object sendPost(String uri, Object data) throws IOException, APIException {
        return this.sendRequest("POST", uri, data);
    }

    private Object sendRequest(String method, String uri, Object data) throws IOException, APIException {
        URL url = new URL(this.m_url + uri);

        // Create the connection object and set the required HTTP method
        // (GET/POST) and headers (content type and basic auth).

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        String auth = getAuthorization(this.m_user, this.m_password);
        conn.addRequestProperty("Authorization", "Basic " + auth);

        if (method.equals("POST")) {
            conn.setRequestMethod("POST");
            // Add the POST arguments, if any. We just serialize the passed
            // data object (i.e. a dictionary) and then add it to the
            // request body.

            if (data != null) {
                if (uri.startsWith("add_attachment")) {  // add_attachment API requests
                    String boundary = "TestRailAPIAttachmentBoundary"; //Can be any random string
                    File uploadFile = new File((String) data);

                    conn.setDoOutput(true);
                    conn.addRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

                    OutputStream ostreamBody = conn.getOutputStream();
                    BufferedWriter bodyWriter = new BufferedWriter(new OutputStreamWriter(ostreamBody));

                    bodyWriter.write("\n\n--" + boundary + "\r\n");
                    bodyWriter.write("Content-Disposition: form-data; name=\"attachment\"; filename=\""
                            + uploadFile.getName() + "\"");
                    bodyWriter.write("\r\n\r\n");
                    bodyWriter.flush();

                    //Read file into request
                    InputStream istreamFile = new FileInputStream(uploadFile);
                    int bytesRead;
                    byte[] dataBuffer = new byte[1024];
                    while ((bytesRead = istreamFile.read(dataBuffer)) != -1) {
                        ostreamBody.write(dataBuffer, 0, bytesRead);
                    }

                    ostreamBody.flush();

                    //end of attachment, add boundary
                    bodyWriter.write("\r\n--" + boundary + "--\r\n");
                    bodyWriter.flush();

                    //Close streams
                    istreamFile.close();
                    ostreamBody.close();
                    bodyWriter.close();
                } else {   // Not an attachment
                    conn.addRequestProperty("Content-Type", "application/json");
                    byte[] block = gson.toJson(data).getBytes(StandardCharsets.UTF_8);
                    conn.setDoOutput(true);
                    OutputStream ostream = conn.getOutputStream();
                    ostream.write(block);
                    ostream.close();
                }
            }
        } else {   // GET request
            conn.addRequestProperty("Content-Type", "application/json");
        }

        // Execute the actual web request (if it wasn't already initiated
        // by getOutputStream above) and record any occurred errors (we use
        // the error stream in this case).
        int status = conn.getResponseCode();

        InputStream iStream;
        if (status != 200) {
            throw new APIException("TestRail API return HTTP " + status + " (No additional error message received)");
        } else {
            iStream = conn.getInputStream();
        }

        // If 'get_attachment' (not 'get_attachments') returned valid status code, save the file
        if ((iStream != null) && (uri.startsWith("get_attachment/"))) {
            FileOutputStream outputStream = new FileOutputStream((String) data);

            int bytesRead;
            byte[] buffer = new byte[1024];
            while ((bytesRead = iStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            iStream.close();
            return data;
        }

        // Not an attachment received
        // Read the response body, if any, and deserialize it from JSON.

        StringBuilder text = new StringBuilder();
        if (iStream != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(iStream, StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                text.append(line);
                text.append(System.getProperty("line.separator"));
            }
            reader.close();
        }

        Object result;
        if (!text.toString().equals("")) {
            result = gson.fromJson(text.toString(), JsonObject.class);
        } else {
            result = new JsonObject();
        }

        // Check for any occurred errors and add additional details to
        // the exception message, if any (e.g. the error message returned
        // by TestRail).

        if (status != 200) {
            String error = "No additional error message received";
            if (result instanceof JsonObject) {
                JsonObject obj = (JsonObject) result;
                if (obj.has("error")) {
                    error = '"' + obj.get("error").toString() + '"';
                }
            }
            throw new APIException("TestRail API returned HTTP " + status + "(" + error + ")");
        }

        return result;
    }

    private static String getAuthorization(String user, String password) {
        try {
            return new String(Base64.getEncoder().encode((user + ":" + password).getBytes()));
        } catch (IllegalArgumentException e) {
            // Not thrown
        }

        return "";
    }
}

package org.huminlabs.huminlabplugin.Backend;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.bukkit.entity.Player;
import org.huminlabs.huminlabplugin.HuMInLabPlugin;
import org.huminlabs.huminlabplugin.Objective.PlayerPointer;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

/**
 * The BackendRequestHandler class handles requests to the backend server.
 * It provides methods for sending requests, updating user data, and pulling player pointers.
 */

public final class BackendRequestHandler {
    private HuMInLabPlugin plugin;
    private String url;
    private String token;

    public String query = "query MyQuery {\n" +
            "  userData(userID: \"test\", key: \"myUserData\") {\n" +
            "    data\n" +
            "  }\n" +
            "}";


    /**
     * Constructs a BackendRequestHandler object.
     *
     * @param plugin The plugin object.
     * @param url The URL of the backend server.
     * @param authorization The authorization token.
     */
    public BackendRequestHandler(HuMInLabPlugin plugin, String url, String authorization) {
        this.plugin = plugin;
        this.url = url;
        this.token = authorization;
    }



    /**
     * Gets the URL of the backend server.
     *
     * @return The URL of the backend server.
     */
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Gets the authorization token.
     *
     * @return The authorization token.
     */
    public String getToken() {
        return token;
    }



    /**
     * Sends a request to the backend server.
     *
     * @param queryString The query string for the request.
     * @return The response from the server as a JsonObject.
     * @throws URISyntaxException If the URL is invalid.
     * @throws IOException If an I/O error occurs.
     */
    public JsonObject sendRequest(String queryString) throws URISyntaxException, IOException {

            //TODO
            // When changing data, you need to first get the data from the server, edit it, then send it back
            // This is so that you don't overwrite other data that may have been changed since you last got it
            String line;
            CloseableHttpClient client = HttpClientBuilder.create().build();
            CloseableHttpResponse response = null;

            HttpPost post = new HttpPost(url);
            post.addHeader("Content-Type", "application/json");
            post.addHeader("Accept", "*/*");
            post.addHeader("Authorization", token);

        try{
            JsonObject json = new JsonObject();
            json.addProperty("query", queryString);

            post.setEntity(new StringEntity(json.toString()));

            response = client.execute(post);

            BufferedReader buffReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder builder = new StringBuilder();

            while((line = buffReader.readLine()) != null) {
                builder.append(line);
                builder.append(System.lineSeparator());
            }
          //  System.out.println(builder.toString());
            String jsonString = builder.toString();
            JsonObject jsonObject = new Gson().fromJson(jsonString, JsonObject.class);
            return jsonObject;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Updates the backend with the objective ID for a specific user.
     *
     * @param uuid The UUID of the user.
     * @param unit The unit ID.
     * @param objectiveID The objective ID.
     */
    public void objectiveUpdate(String uuid, String unit, String objectiveID) {
        System.out.println("Updating Backend with objectiveID: " + objectiveID);
        String queryString = "mutation MyMutation {" +
                                "\ncreateUserData(userID: \""+ uuid +"\", key: \"myUserData\", data: \"{\\\"minecraft\\\": { \\\"unitID\\\": \\\""+unit+"\\\",\\\"objectiveID\\\": \\\""+objectiveID+"\\\"}}\")" +
                            "\n}";
//        }
        try {
            System.out.println(sendRequest(queryString));
        } catch (URISyntaxException | IOException e) {
            System.out.println("Error sending request to backend");
        }

        //TODO
        // Send notification to Drew-Backend that a user has advanced to a new objective
    }

    /**
     * Updates the backend with user data.
     *
     * @param uuid The UUID of the user.
     * @param name The name of the user.
     */
    public void userBackendUpdate(String uuid, String name){
        System.out.println("Updating Backend with userID: " + uuid);
        String queryString = "mutation MyMutation {" +
                                "\ncreateUser(userID:\""+ uuid +"\", accessLevel: \"STUDENT\", groups: [\"G1\", \"G2\"], email: \""+name+"\") {" +
                                    "\nuserID" +
                                    "\nemail"+
                                    "\ndateCreated" +
                                    "\nlastUpdate" +
                                    "\naccessLevel" +
                                    "\ngeneratedID" +
                                    "\ngroups" +
                                "\n}" +
                            "\n}";
        try {
            System.out.println(sendRequest(queryString));
        } catch (URISyntaxException | IOException e) {
            System.out.println("Error sending request to backend");
        }

        //TODO
        // Send notification to Drew-Backend that a new user has been created/joined
    }

    /**
     * Pulls player pointers from the backend for a specific player.
     *
     * @param player The player object.
     * @return The PlayerPointer object.
     */
    public PlayerPointer pullPlayerPointers(Player player){
        String uuid = player.getUniqueId().toString();
        System.out.println("Pulling: " + player.getName());

        String queryString = "query MyQuery {" +
                "\ngetUserData(userID: \"" + uuid + "\", key: \"myUserData\")\")" +
                "\n}";
        try {
            JsonObject data = sendRequest(queryString);
            if(data.get("data").getAsJsonObject().get("getUserData").isJsonNull()){
                System.out.println("User does not exist in backend");
                String mutationString = "mutation MyMutation {" +
                                "\ncreateUserData(userID: \""+ uuid +"\", key: \"myUserData\", data: \"{\\\"minecraft\\\": { \\\"unitID\\\": \\\"1\\\",\\\"objectiveID\\\": \\\"1.0\\\"}}\")" +
                            "\n}";
                return null;
            }

            String userData = data.getAsJsonObject("data").get("getUserData").getAsString();

            JsonObject getUserData = JsonParser.parseString(userData).getAsJsonObject();

            String unitID = getUserData.getAsJsonObject("data").getAsJsonObject("minecraft").get("unitID").getAsString();
            String objectiveID = getUserData.getAsJsonObject("data").getAsJsonObject("minecraft").get("objectiveID").getAsString();
            PlayerPointer playerPointer = new PlayerPointer(uuid, unitID, objectiveID);

            System.out.println("PlayerPointer: " + playerPointer.getUnit() + " " + playerPointer.getObjectiveID());

            return playerPointer;
        } catch (URISyntaxException | IOException e) {
            System.out.println("Error sending request to backend");
        }
        System.out.println("Returning null");
        return null;
    }

}
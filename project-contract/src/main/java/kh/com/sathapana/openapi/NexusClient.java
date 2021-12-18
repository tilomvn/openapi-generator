package kh.com.sathapana.openapi;

import okhttp3.*;

import java.io.File;
import java.io.IOException;

public class NexusClient {

    private static final String CONTRACT_URI = "/contract/merchant";
    private final String user;
    private final String password;
    private final String url;

    private final OkHttpClient client;

    public NexusClient(String user, String password, String url) {
        this.user = user;
        this.password = password;
        this.url = url + CONTRACT_URI;
        client = initClient();
    }

    /**
     * Upload to nexus.
     *
     * @param filePath  local file path
     * @param targetURI target URI
     * @throws IOException exception when uploading.
     */
    public void upload(String filePath, String targetURI) throws Exception {
        var file = new File(filePath);
        var mediaType = MediaType.parse("text/yaml");
        var body = RequestBody.create(file, mediaType);
        var finalUrl = this.url + "/" + targetURI;
        System.out.println("Uploading to : " + finalUrl);
        var request = new Request.Builder()
                .url(finalUrl)
                .put(body)
                .build();
        var response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new Exception("Could not upload to nexus " + targetURI);
        }
    }

    private OkHttpClient initClient() {
        return new OkHttpClient().newBuilder().authenticator((route, response) -> {
                    return response.request().newBuilder()
                            .header( "Authorization", Credentials.basic(user, password)).build();
                }).build();
    }
}

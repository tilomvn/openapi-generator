package kh.com.sathapana.openapi;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import org.apache.commons.io.IOUtils;
import org.gradle.api.GradleException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class NexusClient {

    private final WebResource service;
    private String user = "admin";
    private String password = "123456x@X";
    private String url = "http://localhost:8081/repository";

    public NexusClient() {
        service = getService();
    }

    public NexusClient(String user, String password, String url) {
        this.user = user;
        this.password = password;
        System.out.println("nexus host: " + url);
        this.url = url;
        service = getService();
    }

    /**
     * Download from nexus.
     *
     * @param path   URI to download.
     * @param target target dir to store file
     * @throws IOException exception when downloading.
     */
    public void download(String path, String target) throws IOException {
        System.out.println("Downloading from " + path + " and store to " + target);
        var clientResponse = service.path(path).accept(MediaType.WILDCARD).get(ClientResponse.class);
        if (clientResponse.getStatus() != 200) {
            System.out.println(path);
            System.out.println("Could not download from nexus " + path + " status code: " + clientResponse.getStatus());
            throw new GradleException("Could not download from nexus " + path);
        }
        var targetFile = new File(target);
        targetFile.deleteOnExit();
        targetFile.getParentFile().mkdirs();
        targetFile.createNewFile();
        Files.copy(
                clientResponse.getEntityInputStream(),
                targetFile.toPath(),
                StandardCopyOption.REPLACE_EXISTING);
        IOUtils.closeQuietly(clientResponse.getEntityInputStream());
    }

    private WebResource getService() {
        var config = new DefaultClientConfig();
        Client client = Client.create(config);
        client.addFilter(new HTTPBasicAuthFilter(user, password));
        return client.resource(getBaseURI());
    }

    private URI getBaseURI() {
        return UriBuilder.fromUri(url).build();
    }
}

package kh.com.sathapana.openapi;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.nio.file.Path;

public class Main {

    private static final String[] EXTENSIONS = {"yaml"};

    private static final File schemaDir = new File(System.getProperty("user.dir") + "/contract");

    public static void main(String[] args) throws Exception {
        var client = init();
        if (schemaDir.exists()) {
            var files = FileUtils.listFiles(schemaDir, EXTENSIONS, true);
            if (CollectionUtils.isNotEmpty(files)) {
                for (File file : files) {
                    client.upload(file.getAbsolutePath(), getTargetURI(file.toPath()));
                }
            }
        }
    }

    private static NexusClient init() throws Exception {
        var nexusUser = System.getenv("NEXUS_USER");
        var nexusPassword = System.getenv("NEXUS_PASSWORD");
        var nexusHost = System.getenv("NEXUS_HOST");
        if (StringUtils.isNotEmpty(nexusUser) && StringUtils.isNotEmpty(nexusPassword) && StringUtils.isNotEmpty(nexusHost)) {
            return new NexusClient(nexusUser, nexusPassword, nexusHost);
        }
        throw new Exception("Nexus information is not valid");
    }

    private static String getTargetURI(Path filePath) {
        return schemaDir.toPath().relativize(filePath).toString();
    }
}

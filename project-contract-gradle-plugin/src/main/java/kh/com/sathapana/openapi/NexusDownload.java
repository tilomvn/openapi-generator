package kh.com.sathapana.openapi;

import org.apache.commons.lang3.StringUtils;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

abstract public class NexusDownload extends DefaultTask {

    public List<String> paths = new ArrayList<>();

    public File outputDir;
    private NexusClient client;

    {
        var downloadDir = "/openApi/nexus-download";
        outputDir = getProject().file(getProject().getBuildDir() + downloadDir);
    }

    private void init() {
        var nexusUser = System.getenv("NEXUS_USER");
        if (nexusUser == null) {
            nexusUser = (String) getProject().getProperties().get("nexusUser");
        }

        String nexusPassword = System.getenv("NEXUS_PASSWORD");
        if (nexusPassword == null) {
            nexusPassword = (String) getProject().getProperties().get("nexusPassword");
        }

        String nexusHost = System.getenv("NEXUS_HOST");
        if (nexusHost == null) {
            nexusHost = (String) getProject().getProperties().get("nexusHost");
        }
        if (StringUtils.isNotEmpty(nexusUser) && StringUtils.isNotEmpty(nexusPassword)
                && StringUtils.isNotEmpty(nexusHost)) {
            client = new NexusClient(nexusUser, nexusPassword, nexusHost);
        } else {
            throw new GradleException("Nexus username/password are not correct");
        }
    }

    @TaskAction
    public void download() throws IOException {
        if (client == null) {
            init();
        }
        System.out.println(paths);
        for (String path : paths) {
            var split = path.split(":");
            var split1 = split[0].split("\\.");
            var exactPath =
                    split[0].replaceAll("\\.", "\\/") +
                            "/" +
                            split1[split1.length - 1] +
                            "-" +
                            split[split.length - 1] +
                            ".yaml";
            client.download(exactPath, outputDir.getAbsolutePath() + "/" + exactPath);
        }
    }
}

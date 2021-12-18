package kh.com.sathapana.openapi;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class OpenApiPlugin implements Plugin<Project> {

    private static final String DOWNLOAD_TASK = "downloadTask";
    private static final String OPENAPI_EXTENSION = "openApi";

    @Override
    public void apply(Project project) {
        var extension = project.getExtensions().create(OPENAPI_EXTENSION, OpenApiExtension.class);
        extension.setProject(project);
        project.getTasks().register(DOWNLOAD_TASK,
                NexusDownload.class,
                nexusDownload -> {
                    if (extension.getConsumers() != null) {
                        extension.getConsumers().forEach(c -> nexusDownload.paths.add(c));
                    }
                    if (extension.getProducers() != null) {
                        extension.getProducers().forEach(p -> nexusDownload.paths.add(p));
                    }
                });
        project.getTasks().getByName("compileJava").dependsOn(DOWNLOAD_TASK);
        project.afterEvaluate(action -> extension.registerGenerateTasks());
    }
}

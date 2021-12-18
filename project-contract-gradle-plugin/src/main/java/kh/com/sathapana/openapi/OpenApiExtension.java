package kh.com.sathapana.openapi;

import org.apache.commons.collections.CollectionUtils;
import org.gradle.api.Project;
import org.jetbrains.annotations.NotNull;
import org.openapitools.generator.gradle.plugin.tasks.GenerateTask;

import java.io.File;
import java.util.List;

public class OpenApiExtension {

    private static final String BASE_PACKAGE = "kh.com.sathapana.";

    private List<String> consumers;
    private List<String> producers;
    private Project project;

    public List<String> getProducers() {
        return producers;
    }

    public void setProducers(List<String> producers) {
        this.producers = producers;
    }

    public List<String> getConsumers() {
        return consumers;
    }

    public void setConsumers(List<String> consumers) {
        this.consumers = consumers;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void registerGenerateTasks() {
        var baseDir = project.getBuildDir().getAbsolutePath() + "/openApi";
        var downloadDir = baseDir + "/nexus-download/";
        System.out.println(producers);
        if (CollectionUtils.isNotEmpty(producers)) {
            for (String producer : producers) {
                String[] strings = producer.split(":");
                if (strings.length == 2) {
                    String[] strings1 = strings[0].split("\\.");
                    if (strings1.length >= 2) {
                        String filePath = getFilePath(downloadDir, strings, strings1[strings1.length - 1]);
                        String taskName = "javaSpring" + strings1[strings1.length - 1];
                        GenerateTask task = project.getTasks().create(taskName, GenerateTask.class);
                        task.getInputSpec().set(filePath);
                        task.getGeneratorName().set("spring");
                        setCommonConfigs(task, strings1[strings1.length - 1], baseDir);
                        task.getConfigOptions().put("delegatePattern", "true");
                        task.getConfigOptions().put("interfaceOnly", "true");
                        task.getConfigOptions().put("enumPropertyNaming", "original");
                        task.getConfigOptions().put("useTags", "true");
                    }
                }
            }
        }
        if (CollectionUtils.isNotEmpty(consumers)) {
            for (String consumer : consumers) {
                String[] strings = consumer.split(":");
                if (strings.length == 2) {
                    String[] strings1 = strings[0].split("\\.");
                    if (strings1.length >= 2) {
                        String taskName = "javaClient" + strings1[strings1.length - 1];
                        GenerateTask task = project.getTasks().create(taskName, GenerateTask.class);
                        task.getInputSpec()
                                .set(getFilePath(downloadDir, strings, strings1[strings1.length - 1]));
                        task.getGlobalProperties().put("modelDocs", "false");
                        task.getGeneratorName().set("java");
                        setCommonConfigs(task, strings1[strings1.length - 1], baseDir);
                        task.getInvokerPackage().set(BASE_PACKAGE + strings1[strings1.length - 1] + ".invoker");
                        task.getLibrary().set("resttemplate");
                        task.getTypeMappings().put("OffsetDateTime", "java.time.Instant");
                        task.getTypeMappings().put("java.time.OffsetDateTime", "java.time.Instant");
                        task.getConfigOptions().put("useTags", "true");
                    }
                }
            }
        }
    }

    private void setCommonConfigs(GenerateTask task, String service, String baseDir) {
        task.getApiPackage().set(BASE_PACKAGE + service + ".api");
        task.getModelPackage().set(BASE_PACKAGE + service + ".model");
        task.getSkipOperationExample().set(true);
        task.getGenerateApiTests().set(false);
        task.getGenerateModelTests().set(false);
        task.getOutputDir().set(baseDir + "/generated");
        task.getConfigOptions().put("dateLibrary", "java8");
        task.doLast(action -> project.fileTree(new File(baseDir + "/generated"))
                .filter(f -> f.isFile() && !f.getName().endsWith(".java"))
                .forEach(File::delete));
        project.getTasks().getByName("compileJava").dependsOn(task);
    }

    @NotNull
    private String getFilePath(String downloadDir, String[] strings, String s) {
        return downloadDir + strings[0].replaceAll("\\.", "\\/") + "/" + s + "-" +
                strings[strings.length - 1] + ".yaml";
    }
}
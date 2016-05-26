package com.klp

import org.gradle.api.Plugin
import org.gradle.api.Project;

class GreetingPlugin implements Plugin<Project> {
    void apply(Project project) {
        project.afterEvaluate {
            project.android.applicationVariants.each { variant ->
                def dexTask = project.tasks.findByName("transformClassesWithDexFor${variant.name.capitalize()}")
                if (dexTask) {
                    project.logger.error "dex=>${variant.name.capitalize()}"

                    dexTask.inputs.files.files.each { File file ->
                        project.logger.error "file inputs=>${file.absolutePath}"
                        project.logger.error "hello world"
                    }

                    dexTask.outputs.files.files.each { File file ->
                        project.logger.error "file outputs=>${file.absolutePath}"
                    }
                }
            }
        }
    }
}





package com.klp

import org.gradle.api.Plugin
import org.gradle.api.Project

class GreetingPlugin implements Plugin<Project> {
    GreetingPluginExtension klpExt;
    File originalOutputFile;

    void apply(Project project) {
        //演示添加外部属性
        klpExt = project.extensions.create("klp", GreetingPluginExtension, project);
        project.task('hello') << {
            println "${project.klp.message} from ${project.klp.greeter}"
        }


        project.afterEvaluate {//gradle建立完task的有向图
            //清除自定义的输出文件夹
            def cleanCustomOutputFileTask = project.task("cleanCustomOutputFile") << {
                def dir = klpExt.outPutFile;
                if (dir && dir.listFiles()) {
                    dir.listFiles().sort().each { File file ->
                        if (file.isFile()) {
                            file.delete()
                        } else {
                            file.deleteDir()
                        }
                    }
                }
            }
            //建立依赖
            project.getTasksByName("clean", true) << {
                it.dependsOn cleanCustomOutputFileTask
                println "clean custom out put file"
            }

//
            project.android.applicationVariants.all { variant ->

                originalOutputFile = variant.outputs[0].outputFile;// 原路径
                println originalOutputFile.absolutePath
                variant.outputs.each { output ->
                    output.outputFile = new File(klpExt.outPutFile, "custom.apk")
                }
            }
        }
    }
}

class GreetingPluginExtension {

    String message
    String greeter
    File outPutFile;

    GreetingPluginExtension(Project project) {
        this.outPutFile = new File(project.rootProject.buildDir, "customOutPut");
    }
}





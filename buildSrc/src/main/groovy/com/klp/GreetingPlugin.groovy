package com.klp

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy;

class GreetingPlugin implements Plugin<Project> {
    GreetingPluginExtension klpExt;

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
            project.getTasksByName("clean", true).each {
                it.dependsOn cleanCustomOutputFileTask
                println "clean custom out put file"
            }

//
            project.android.applicationVariants.all { variant ->
                def File originalOutputFile = variant.outputs[0].outputFile;// 原路径
                def File customOutputFile = klpExt.outPutFile;//自定义的路径
                println originalOutputFile.absoluteFile
                println customOutputFile.absoluteFile

                def copyChangeTask = project.task("copyChange${variant.name.capitalize()}", type: Copy) << {
                    from originalOutputFile.absoluteFile
                    into customOutputFile.absoluteFile
                    println "copyChangeTask is runing"
                    println originalOutputFile.absoluteFile.exists()
                }

                project.tasks.getByName("assemble"){
                    it.doLast{
                        println "do last"
                        copyChangeTask
                    }
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





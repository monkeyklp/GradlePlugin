package com.monkey.gradle

import org.gradle.api.Project
import java.io.File

class PluginExtenstion {
    var message = null
    var greeter = null
    var outPutFile : File

    constructor(project : Project) {
        this.outPutFile = File(project.rootProject.buildDir, "kotlinOutPut")
    }

}
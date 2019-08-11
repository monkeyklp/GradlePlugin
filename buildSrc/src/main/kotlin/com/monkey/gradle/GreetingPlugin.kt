package com.monkey.gradle

import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.api.BaseVariant
import com.android.build.gradle.internal.scope.VariantScope
import com.android.build.gradle.internal.variant.BaseVariantData
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File

class GreetingPlugin : Plugin<Project> {
    inline fun <reified T : BaseExtension> Project.getAndroid(): T = extensions.getByName("android") as T
    val com.android.build.gradle.api.BaseVariant.variantData: BaseVariantData
        get() = javaClass.getDeclaredMethod("getVariantData").invoke(this) as BaseVariantData
    val BaseVariant.scope: VariantScope
        get() = variantData.scope
    override fun apply(project: Project) {
        var extenstion = project.extensions.create("kotlinPlugin", PluginExtenstion::class.java, project)

        project.afterEvaluate {
            val cleanPluginTask = project.task("cleanKotlinPulgin").doLast {
                var dir = extenstion.outPutFile
                if (dir.exists()) {
                    dir.deleteRecursively()
                }
            }


            when {
                project.plugins.hasPlugin("com.android.application") -> project.getAndroid<AppExtension>().let {android ->
                    android.applicationVariants.all { variant ->
                        variant.outputs.all {
                        var output: File = variant.packageApplicationProvider.get().javaClass.getDeclaredField("outputDirectory") as File
                            output = extenstion.outPutFile
                        }

                    }

                }
            }

        }
    }
}


package net.bjonnh.intellij.filepermissionsplugin.template.services

import com.intellij.openapi.project.Project
import net.bjonnh.intellij.filepermissionsplugin.template.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}

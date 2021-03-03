package com.github.bjonnh.filepermissionsplugin.services

import com.github.bjonnh.filepermissionsplugin.MyBundle
import com.intellij.openapi.project.Project

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}

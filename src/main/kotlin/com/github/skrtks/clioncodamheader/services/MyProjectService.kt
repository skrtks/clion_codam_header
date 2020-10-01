package com.github.skrtks.clioncodamheader.services

import com.intellij.openapi.project.Project
import com.github.skrtks.clioncodamheader.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}

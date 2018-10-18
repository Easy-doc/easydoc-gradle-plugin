package com.stalary

import org.gradle.api.Plugin
import org.gradle.api.Project

class SavePlugin implements Plugin<Project> {


    @Override
    void apply(Project project) {
        // 插件入口
        project.extensions.create('easydoc', MyExtension)
        // 创建任务
        project.task('easydoc', type: SaveTask)
    }


}
package net.bjonnh.intellij.filepermissionsplugin.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import java.io.IOException
import java.nio.file.Files
import java.nio.file.attribute.PosixFilePermission


class MakeExecutableAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val currentProject: Project = event.project ?: return

        val dlgTitle: String = event.presentation.description

        val path = event.getData(CommonDataKeys.VIRTUAL_FILE)?.canonicalFile?.toNioPath()
        if (path != null) {
            try {
                val permissions = Files.getPosixFilePermissions(path)
                permissions.add(PosixFilePermission.OWNER_EXECUTE)
                Files.setPosixFilePermissions(path, permissions)
            } catch (ex: IOException) {
                Messages.showMessageDialog(
                    currentProject,
                    "File is not accessible.",
                    dlgTitle,
                    Messages.getInformationIcon()
                )
            }
        }
    }
}

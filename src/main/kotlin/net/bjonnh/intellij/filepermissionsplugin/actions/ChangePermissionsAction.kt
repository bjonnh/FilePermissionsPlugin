package net.bjonnh.intellij.filepermissionsplugin.actions

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import net.bjonnh.intellij.filepermissionsplugin.dialogs.PermissionDialog
import java.io.IOException
import java.nio.file.Files

/**
 * An action that allows a user to change the permissions of the currently selected file
 */
class ChangePermissionsAction : AnAction() {

    /**
     * Internal function called by IntelliJ when the users run this action
     */
    override fun actionPerformed(e: AnActionEvent) {
        val currentProject: Project = e.project ?: return

        val path = e.getData(CommonDataKeys.VIRTUAL_FILE)?.canonicalFile?.toNioPath()
        if (path != null) {

            try {
                val permissions = Files.getPosixFilePermissions(path)
                val fileName = path.fileName.toString()
                val dialog = PermissionDialog(fileName, permissions) { newPermissions ->
                    Files.setPosixFilePermissions(path, newPermissions)
                }
                dialog.show()
            } catch (ex: IOException) {
                Messages.showMessageDialog(
                    currentProject,
                    "${path.fileName} is not accessible.\n${ex.localizedMessage}",
                    "File Access Error",
                    Messages.getInformationIcon()
                )
            }
        }
    }

    /**
     * This action will only be visible in a project and if a file is selected
     */
    override fun update(e: AnActionEvent) {
        val project = e.project
        try {
            val path = e.getData(CommonDataKeys.VIRTUAL_FILE)?.canonicalFile?.toNioPath()
            e.presentation.isEnabledAndVisible = (project != null) && (path != null)
        } catch (_: UnsupportedOperationException) { // Sometimes we see that from toNioPath
            e.presentation.isEnabledAndVisible = false
        }
    }

    /**
     * Specifies that this action update will be executed on the background
     * thread.
     */
    override fun getActionUpdateThread() = ActionUpdateThread.BGT
}

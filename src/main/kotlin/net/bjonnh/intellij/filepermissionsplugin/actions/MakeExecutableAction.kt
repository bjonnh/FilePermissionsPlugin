/**
 * SPDX-License-Identifier: GPL-3.0-or-later
 *
 * Copyright (c) 2021 Jonathan Bisson
 */

package net.bjonnh.intellij.filepermissionsplugin.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import java.io.IOException
import java.nio.file.Files
import java.nio.file.attribute.PosixFilePermission

/**
 * An action that allows a user to make the currently selected file executable
 */
class MakeExecutableAction : AnAction() {

    /**
     * Internal function called by IntelliJ when the users run this action
     */
    override fun actionPerformed(e: AnActionEvent) {
        val currentProject: Project = e.project ?: return

        val path = e.getData(CommonDataKeys.VIRTUAL_FILE)?.canonicalFile?.toNioPath()
        if (path != null) {
            try {
                val permissions = Files.getPosixFilePermissions(path)
                Files.setPosixFilePermissions(path, permissions + PosixFilePermission.OWNER_EXECUTE)
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
     * and it will only be enabled if the file is not executable already
     */
    @Suppress("SwallowedException") // On purpose, if we fail we fail silently and don't display the options
    override fun update(e: AnActionEvent) {
        val project = e.project
        val path = e.getData(CommonDataKeys.VIRTUAL_FILE)?.canonicalFile?.toNioPath()
        e.presentation.isEnabled = try {
            if (path == null) {
                false
            } else {
                PosixFilePermission.OWNER_EXECUTE !in Files.getPosixFilePermissions(path)
            }
        } catch (ex: IOException) {
            false
        }
        e.presentation.isVisible = (project != null) && (path != null)
    }
}

/**
 * SPDX-License-Identifier: GPL-3.0-or-later
 *
 * Copyright (c) 2021 Jonathan Bisson
 */

package net.bjonnh.intellij.filepermissionsplugin.dialogs

import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.layout.PropertyBinding
import com.intellij.ui.layout.Row
import com.intellij.ui.layout.panel
import net.bjonnh.intellij.filepermissionsplugin.models.PermissionManager
import java.nio.file.attribute.PosixFilePermission

/**
 * Create a dialog for the action
 *
 * @param fileName name of the file (only used for the title of the window)
 * @param initialPermissions set of initial permissions
 * @param okFunction function that is executed when the user clicks OK, take the new permissions as a parameter
 */
class PermissionDialog(
    fileName: String,
    initialPermissions: Set<PosixFilePermission>,
    val okFunction: (Set<PosixFilePermission>) -> Unit
) :
    DialogWrapper(null) {
    private val permissionManager = PermissionManager(initialPermissions)

    init {
        init()
        title = "Permissions Of $fileName"
    }

    /**
     * Create the dialog for the action
     */
    override fun createCenterPanel(): DialogPanel = panel {
        row("User") {
            checkBox("Read", permissionManager.userRead)
            checkBox("Write", permissionManager.userWrite)
            checkBox("eXecute", permissionManager.userExecute)
        }
        row("Group") {
            checkBox("Read", permissionManager.groupRead)
            checkBox("Write", permissionManager.groupWrite)
            checkBox("eXecute", permissionManager.groupExecute)
        }
        row("Others") {
            checkBox("Read", permissionManager.othersRead)
            checkBox("Write", permissionManager.othersWrite)
            checkBox("eXecute", permissionManager.othersExecute)
        }
    }

    /**
     * Call okFunction with the current permissions from the dialog
     */
    override fun doOKAction() {
        super.doOKAction()
        okFunction(permissionManager.permissions)
    }
}

/**
 * Create a checkbox working directly on a PropertyBinding
 */
private fun Row.checkBox(s: String, property: PropertyBinding<Boolean>) = checkBox(s, property.get, property.set)

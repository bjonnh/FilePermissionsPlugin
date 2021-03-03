package net.bjonnh.intellij.filepermissionsplugin.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.Messages
import com.intellij.ui.layout.PropertyBinding
import com.intellij.ui.layout.Row
import com.intellij.ui.layout.panel
import java.io.IOException
import java.nio.file.Files
import java.nio.file.attribute.PosixFilePermission

/**
 * The model for the permissions
 */
class PermissionManager(initialPermissions: Set<PosixFilePermission>) {
    private val _permissions: MutableSet<PosixFilePermission> = initialPermissions.toMutableSet()

    val permissions: Set<PosixFilePermission>
        get() = _permissions.toSet()

    val userRead = permissionBuilder(PosixFilePermission.OWNER_READ)
    val userWrite = permissionBuilder(PosixFilePermission.OWNER_WRITE)
    val userExecute = permissionBuilder(PosixFilePermission.OWNER_EXECUTE)

    val groupRead = permissionBuilder(PosixFilePermission.GROUP_READ)
    val groupWrite = permissionBuilder(PosixFilePermission.GROUP_WRITE)
    val groupExecute = permissionBuilder(PosixFilePermission.GROUP_EXECUTE)

    val othersRead = permissionBuilder(PosixFilePermission.OTHERS_READ)
    val othersWrite = permissionBuilder(PosixFilePermission.OTHERS_WRITE)
    val othersExecute = permissionBuilder(PosixFilePermission.OTHERS_EXECUTE)

    fun permissionBuilder(permission: PosixFilePermission): PropertyBinding<Boolean> =
        PropertyBinding<Boolean>(
            { permission in _permissions },
            { value ->
                if (value) _permissions.add(permission)
                else _permissions.remove(permission)
            }
        )
}

/**
 * Create a dialog for the action
 *
 * @param fileName name of the file (only used for the title of the window)
 * @param initialPermissions set of initial permissions
 * @param okFunction function that is executed when the user clicks OK, take the new permissions as a parameter
 */
class Dialog(
    fileName: String,
    initialPermissions: Set<PosixFilePermission>,
    val okFunction: (Set<PosixFilePermission>) -> Unit
) :
    DialogWrapper(null) {
    private val permissionManager = PermissionManager(initialPermissions)

    init {
        init()
        title = "Permissions of $fileName"
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

/**
 * An action that allows a user to change the permissions of the currently selected file
 */
class ChangePermissionsAction : AnAction() {

    /**
     * Internal function called by IntelliJ when the users run this action
     */
    override fun actionPerformed(e: AnActionEvent) {
        val currentProject: Project = e.project ?: return

        val dlgTitle: String = e.presentation.description

        val path = e.getData(CommonDataKeys.VIRTUAL_FILE)?.canonicalFile?.toNioPath()
        if (path != null) {

            try {
                val permissions = Files.getPosixFilePermissions(path)
                val fileName = path.fileName.toString()
                val dialog = Dialog(fileName, permissions) { newPermissions ->
                    Files.setPosixFilePermissions(path, newPermissions)
                }
                dialog.show()
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

    /**
     * This action will only be visible in a project and if a file is selected
     */
    override fun update(e: AnActionEvent) {
        val project = e.project
        val path = e.getData(CommonDataKeys.VIRTUAL_FILE)?.canonicalFile?.toNioPath()
        e.presentation.isEnabledAndVisible = (project != null) && (path != null)
    }
}

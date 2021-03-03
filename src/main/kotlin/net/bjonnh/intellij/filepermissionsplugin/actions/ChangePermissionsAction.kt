package net.bjonnh.intellij.filepermissionsplugin.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.Project
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

    val u_r = permissionBuilder(PosixFilePermission.OWNER_READ)
    val u_w = permissionBuilder(PosixFilePermission.OWNER_WRITE)
    val u_x = permissionBuilder(PosixFilePermission.OWNER_EXECUTE)

    val g_r = permissionBuilder(PosixFilePermission.GROUP_READ)
    val g_w = permissionBuilder(PosixFilePermission.GROUP_WRITE)
    val g_x = permissionBuilder(PosixFilePermission.GROUP_EXECUTE)

    val o_r = permissionBuilder(PosixFilePermission.OTHERS_READ)
    val o_w = permissionBuilder(PosixFilePermission.OTHERS_WRITE)
    val o_x = permissionBuilder(PosixFilePermission.OTHERS_EXECUTE)


    fun permissionBuilder(permission: PosixFilePermission): PropertyBinding<Boolean> =
        PropertyBinding<Boolean>(
            { permission in _permissions },
            { value ->
                if (value) _permissions.add(permission)
                else _permissions.remove(permission)
                println("Hello la: $permission")
            }
        )
}

class Dialog(
    fileName: String,
    permissions: Set<PosixFilePermission>,
    val okFunction: (Set<PosixFilePermission>) -> Unit
) :
    DialogWrapper(null) {
    private val permissionManager = PermissionManager(permissions)

    init {
        init()
        title = "Change permissions of $fileName"
        println("Hello ici")
    }

    override fun createCenterPanel() = panel {
        row("User") {
            checkBox("Read", permissionManager.u_r)
            checkBox("Write", permissionManager.u_w)
            checkBox("eXecute", permissionManager.u_x)
        }
        row("Group") {
            checkBox("Read", permissionManager.g_r)
            checkBox("Write", permissionManager.g_w)
            checkBox("eXecute", permissionManager.g_x)
        }
        row("Others") {
            checkBox("Read", permissionManager.o_r)
            checkBox("Write", permissionManager.o_w)
            checkBox("eXecute", permissionManager.o_x)
        }
    }

    override fun doOKAction() {
        super.doOKAction()
        println(permissionManager.permissions)
        okFunction(permissionManager.permissions)
    }
}

/**
 * Create a checkbox working directly on a PropertyBinding
 */
private fun Row.checkBox(s: String, property: PropertyBinding<Boolean>) = checkBox(s, property.get, property.set)


class ChangePermissionsAction : AnAction() {
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
                //permissions.add(PosixFilePermission.OWNER_EXECUTE)
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

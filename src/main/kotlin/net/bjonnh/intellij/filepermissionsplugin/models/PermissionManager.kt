package net.bjonnh.intellij.filepermissionsplugin.models

import com.intellij.ui.layout.PropertyBinding
import java.nio.file.attribute.PosixFilePermission

/**
 * The model for the permissions
 */
class PermissionManager(initialPermissions: Set<PosixFilePermission>) {
    private val _permissions: MutableSet<PosixFilePermission> = initialPermissions.toMutableSet()

    /**
     * The currently set permissions
     */
    val permissions: Set<PosixFilePermission>
        get() = _permissions.toSet()

    val userRead: PropertyBinding<Boolean> = permissionBuilder(PosixFilePermission.OWNER_READ)
    val userWrite: PropertyBinding<Boolean> = permissionBuilder(PosixFilePermission.OWNER_WRITE)
    val userExecute: PropertyBinding<Boolean> = permissionBuilder(PosixFilePermission.OWNER_EXECUTE)

    val groupRead: PropertyBinding<Boolean> = permissionBuilder(PosixFilePermission.GROUP_READ)
    val groupWrite: PropertyBinding<Boolean> = permissionBuilder(PosixFilePermission.GROUP_WRITE)
    val groupExecute: PropertyBinding<Boolean> = permissionBuilder(PosixFilePermission.GROUP_EXECUTE)

    val othersRead: PropertyBinding<Boolean> = permissionBuilder(PosixFilePermission.OTHERS_READ)
    val othersWrite: PropertyBinding<Boolean> = permissionBuilder(PosixFilePermission.OTHERS_WRITE)
    val othersExecute: PropertyBinding<Boolean> = permissionBuilder(PosixFilePermission.OTHERS_EXECUTE)

    /**
     * Helper function to create a dynamic set of permissions
     *
     * @param permission The permission that can be made present or not (using set) and read from permissions (with get)
     */
    private fun permissionBuilder(permission: PosixFilePermission): PropertyBinding<Boolean> =
        PropertyBinding(
            { permission in _permissions },
            { value ->
                if (value) _permissions.add(permission)
                else _permissions.remove(permission)
            }
        )
}

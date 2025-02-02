/**
 * SPDX-License-Identifier: GPL-3.0-or-later
 *
 * Copyright (c) 2021 Jonathan Bisson
 */

package net.bjonnh.intellij.filepermissionsplugin.models

import com.intellij.ui.dsl.builder.MutableProperty
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

    val userRead: MutableProperty<Boolean> = permissionBuilder(PosixFilePermission.OWNER_READ)
    val userWrite: MutableProperty<Boolean> = permissionBuilder(PosixFilePermission.OWNER_WRITE)
    val userExecute: MutableProperty<Boolean> = permissionBuilder(PosixFilePermission.OWNER_EXECUTE)

    val groupRead: MutableProperty<Boolean> = permissionBuilder(PosixFilePermission.GROUP_READ)
    val groupWrite: MutableProperty<Boolean> = permissionBuilder(PosixFilePermission.GROUP_WRITE)
    val groupExecute: MutableProperty<Boolean> = permissionBuilder(PosixFilePermission.GROUP_EXECUTE)

    val othersRead: MutableProperty<Boolean> = permissionBuilder(PosixFilePermission.OTHERS_READ)
    val othersWrite: MutableProperty<Boolean> = permissionBuilder(PosixFilePermission.OTHERS_WRITE)
    val othersExecute: MutableProperty<Boolean> = permissionBuilder(PosixFilePermission.OTHERS_EXECUTE)

    /**
     * Helper function to create a dynamic set of permissions
     *
     * @param permission The permission that can be made present or not (using set) and read from permissions (with get)
     */
    private fun permissionBuilder(permission: PosixFilePermission): MutableProperty<Boolean> =
        MutableProperty(
            { permission in _permissions },
            { value ->
                if (value) _permissions.add(permission)
                else _permissions.remove(permission)
            }
        )
}

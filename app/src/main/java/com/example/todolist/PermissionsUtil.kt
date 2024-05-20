package com.example.todolist

import android.content.pm.PackageManager
import com.example.todolist.ApplicationsUtil.getNonSystemInstalledApplications

object PermissionsUtil {

    // Gets a list of all permissions of all non-system packages installed on device
    fun getNonSystemPackagesPermissions(packageManager: PackageManager) : HashMap<String, MutableList<String>> {
        val nonSystemInstalledApps = getNonSystemInstalledApplications(packageManager)
        val permissionsMap = HashMap<String, MutableList<String>>()
        val packagePermissions = packageManager.getInstalledPackages(PackageManager.GET_PERMISSIONS)
        for (pi in packagePermissions) {
            val piStr = pi.packageName.toString()
            // Checks if the package name string is in the list of non-system installed pacakages first
            if (piStr in nonSystemInstalledApps) {
                permissionsMap[piStr] = mutableListOf()
                if (pi.requestedPermissions == null) {
                    continue
                } else {
                    for (perm in pi.requestedPermissions) {
                        permissionsMap[piStr]?.add(perm.toString())
                    }
                }
            }
        }

        return permissionsMap
    }

    // Takes in a list of permissions that are deemed bad and a hash map of packages and their respective permissions.
    // Checks if packages' permissions contains bad permissions.
    // Returns a list of packages that had bad permissions
    fun filterBadPermissions(packagePermissions:  HashMap<String, MutableList<String>>, badPermissions: ArrayList<String>) : ArrayList<String> {
        val appsWithBadPermission = ArrayList<String>()
        for (badPermission in badPermissions) {
            for ((key, value) in packagePermissions) {
                if (badPermission in value) {
                    appsWithBadPermission.add("$key $badPermission")
                }
            }
        }
        return appsWithBadPermission
    }
}
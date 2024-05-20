package com.example.todolist

import android.content.pm.PackageManager
import android.os.Build
import com.example.todolist.ApplicationsUtil.getNonSystemInstalledApplications

object InstallationSourceUtil {
    // Get installer source information of non-system packages installed on device
    fun getInstallerSources(packageManager: PackageManager): HashMap<String, String> {
        var installerInfo: String
        val installerInfoMap = HashMap<String,String>()
        // Get the list of installed non-system packages on device first
        val installedAppsList = getNonSystemInstalledApplications(packageManager)

        try {
            for (installedApp in installedAppsList) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    installerInfo = packageManager.getInstallSourceInfo(installedApp).installingPackageName.toString()
                } else {
                    installerInfo = packageManager.getInstallerPackageName(installedApp).toString()
                }
                installerInfoMap[installedApp] = installerInfo
            }
        } catch (e: Exception) {
            e.printStackTrace();
        }

        return installerInfoMap
    }

    // Filters a hashmap of installed apps with the list of approved installer sources and returns an arraylist
    // of packages not from the approved list
    fun filterNotFromApprSources(installedSources:  HashMap<String, String>, approvedInstallerSources: ArrayList<String>) : ArrayList<String> {
        val appsWithUnapprovedInstallSource = ArrayList<String>()
        for ((key, value) in installedSources) {
            if (value !in approvedInstallerSources) {
                appsWithUnapprovedInstallSource.add("$key $value")
            }
        }
        return appsWithUnapprovedInstallSource
    }
}
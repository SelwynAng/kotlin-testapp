package com.example.todolist

import android.app.ActivityManager
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi

object ApplicationsUtil {
    // Function to retrieve the signature of an app
    @RequiresApi(Build.VERSION_CODES.P)
    fun getSignature(packageManager: PackageManager, packageName: String): String? {
        try {
            val packageInfo: PackageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
            val signatures = packageInfo.signingInfo.signingCertificateHistory
            return signatures.toString()
        } catch (e: Exception) {
            Log.e("Error", e.message ?: "Error retrieving signature")
        }
        return null
    }

    // Function to retrieve all installed applications in the device
    fun getInstalledApplications(packageManager: PackageManager): ArrayList<String> {
        val packageNameList = ArrayList<String>()
        try {
            val pm : PackageManager = packageManager
            val packages = pm.getInstalledPackages(PackageManager.GET_META_DATA)
            val packageNames = packages.map { packageInfo -> packageInfo.packageName }

            packageNames.forEach {packageName ->
                packageNameList.add(packageName.toString())
            }

        } catch (e: Exception) {
            Log.e("Error", e.message ?: "Error retrieving installed applications")
        }

        return packageNameList
    }

    // Gets all non-system installed packages in the device
    fun getNonSystemInstalledApplications(packageManager: PackageManager): List<String> {
        val packageList = mutableListOf<String>()
        try {
            val packages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
            for (packageInfo in packages) {
                if (!isSystemApp(packageInfo)) {
                    packageList.add(packageInfo.packageName)
                }
            }
        } catch (e: Exception) {
            Log.e("Error", e.message ?: "Error retrieving installed applications")
        }

        return packageList.toList()
    }

    // Helper function to check if app is a system app
    private fun isSystemApp(packageInfo: ApplicationInfo): Boolean {
        return (packageInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0
    }

    // Function to retrieve all currently running applications in the device
    // Does not work as of now, does not fetch background applications due to Android in-built privacy
    fun getRunningApplications(activityManager: ActivityManager): ArrayList<String> {
        val runningAppList = ArrayList<String>()
        try {
            val runningProcesses = activityManager.runningAppProcesses
            if (runningProcesses != null) {
                for (processInfo in runningProcesses) {
                    // Add the package name of each running process to the list
                    runningAppList.add(processInfo.processName)
                }
            }

        } catch (e: Exception) {
            Log.e("Error", e.message ?: "Error retrieving installed applications")
        }
        return runningAppList
    }
}
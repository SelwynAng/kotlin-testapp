package com.example.todolist

import android.app.ActivityManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.todolist.ui.theme.TodoListTheme
import kotlin.math.log

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoListTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                            name = "Signature Retriever",
                            modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

        // Retrieve all installed applications in the device and log their signatures
        val packageNameList = getInstalledApplications(packageManager)
        Log.d("Installed Package List", packageNameList.toString())

        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningAppList = getRunningApplications(activityManager)
        Log.d("Running Apps List", runningAppList.toString())

        for (packageName in packageNameList) {
            val signature = getSignature(packageManager, packageName)
            Log.d("Signature", (packageName + ", " + signature) ?: "Signature not found")
        }



    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
            text = "Hello $name!",
            modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TodoListTheme {
        Greeting("Signature Retriever")
    }
}

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

// Function to retrieve all currently running applications in the device
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
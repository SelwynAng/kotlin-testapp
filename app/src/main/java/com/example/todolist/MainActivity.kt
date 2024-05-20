package com.example.todolist

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todolist.InstallationSourceUtil.filterNotFromApprSources
import com.example.todolist.PermissionsUtil.filterBadPermissions
import com.example.todolist.PermissionsUtil.getNonSystemPackagesPermissions
import com.example.todolist.ui.theme.TodoListTheme
import com.example.todolist.MaliciousAppPopUpUtil.showMaliciousAppPopUp
import com.example.todolist.InstallationSourceUtil.getInstallerSources

class MainActivity : ComponentActivity() {
    @SuppressLint("ShowToast")
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val packageManager = packageManager

        // Make a static list of "bad permissions" and then filter for packages that has these bad permissions
        // If such packages are present, show pop up window to notify user about presence of malicious apps
        val BAD_PERMISSIONS = arrayListOf("android.permission.SEND_SMS", "android.permission.VIBRATE", "android.permission.ACCESS_NOTIFICATION_POLICY")
        val appsWithBadPermissions = filterBadPermissions(getNonSystemPackagesPermissions(packageManager), BAD_PERMISSIONS)
        if (appsWithBadPermissions.size > 0) {
            showMaliciousAppPopUp(this)
        }

        // Make a static list of approved installer sources and then filter for packages that do not come from these approved sources
        // If such packages are present, display it in the app (can give user option to close the application)
        val APPROVED_INSTALLER_SOURCE = arrayListOf("com.android.vending", "com.samsung.android.app.omcagent", "com.sec.android.app.samsungapps", "com.huawei.appmarket", "com.xiaomi.miinstaller")
        val appsNotFromApprSources = filterNotFromApprSources(getInstallerSources(packageManager), APPROVED_INSTALLER_SOURCE)

        setContent {
            TodoListTheme {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item {
                        Text(
                            text = "Apps with Bad Permissions",
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                    items(appsWithBadPermissions) { packageName ->
                        Text(
                            text = packageName,
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    item {
                        Text(
                            text = "Apps not from Approved Sources",
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                    items(appsNotFromApprSources) { packageName ->
                        Text(
                            text = packageName,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AppListItem(packageName: String) {
    Surface(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Text(
            text = packageName,
            modifier = Modifier.clickable {
                // Handle click on app item (optional)
                // You could show more details about the app or its permissions
            },
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AppListItemPreview() {
    TodoListTheme {
        AppListItem("com.example.app")
    }
}
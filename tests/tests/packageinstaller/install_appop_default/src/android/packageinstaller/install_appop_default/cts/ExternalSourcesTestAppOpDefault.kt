/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.packageinstaller.install_appop_default.cts

import android.app.AppOpsManager.MODE_DEFAULT
import android.platform.test.annotations.AppModeFull
import androidx.test.InstrumentationRegistry
import androidx.test.filters.MediumTest
import androidx.test.runner.AndroidJUnit4
import android.support.test.uiautomator.By
import android.support.test.uiautomator.BySelector
import android.support.test.uiautomator.UiDevice
import android.support.test.uiautomator.Until
import com.android.compatibility.common.util.AppOpsUtils
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith

private const val INSTALL_CONFIRM_TEXT_ID = "install_confirm_question"
private const val ALERT_DIALOG_TITLE_ID = "android:id/alertTitle"

@RunWith(AndroidJUnit4::class)
@MediumTest
@AppModeFull
class ExternalSourcesTest : PackageInstallerTestBase() {
    private val context = InstrumentationRegistry.getTargetContext()
    private val pm = context.packageManager
    private val packageName = context.packageName
    private val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    private fun assertUiObject(errorMessage: String, selector: BySelector) {
        assertNotNull(errorMessage, uiDevice.wait(Until.findObject(selector), TIMEOUT))
    }

    private fun assertInstallBlocked(errorMessage: String) {
        assertUiObject(errorMessage, By.res(ALERT_DIALOG_TITLE_ID))
        uiDevice.pressBack()
    }

    @Before
    fun verifyAppOpDefault() {
        assertThat(AppOpsUtils.getOpMode(packageName, APP_OP_STR))
                .isEqualTo(MODE_DEFAULT)
    }

    private fun defaultSourceTest(startInstallation: () -> Unit) {
        assertFalse("Package $packageName with default app ops state allowed to install packages",
                pm.canRequestPackageInstalls())

        startInstallation()
        assertInstallBlocked("Install blocking dialog not shown when app op set to default")

        assertTrue("Operation not logged", AppOpsUtils.rejectedOperationLogged(packageName,
                APP_OP_STR))
    }

    @Test
    @Ignore // TODO(b/150819885) this crashes because we restart the app when they request install perm
    fun defaultSourceTestViaIntent() {
        defaultSourceTest { startInstallationViaIntent() }
    }

    @Test
    @Ignore // TODO(b/150819885) this crashes because we restart the app when they request install perm
    fun defaultSourceTestViaSession() {
        defaultSourceTest { startInstallationViaSession() }
    }

    @Test
    fun defaultSourceTest() {
        assertFalse("Package $packageName with default app ops state allowed to install packages",
                pm.canRequestPackageInstalls())
    }
}

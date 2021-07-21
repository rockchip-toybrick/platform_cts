/*
 * Copyright (C) 2016 The Android Open Source Project
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
package com.android.cts.deviceowner;

import android.app.PendingIntent;
import android.content.ContentResolver;
import android.os.Process;
import android.provider.Settings;

import java.io.ByteArrayInputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class AdminActionBookkeepingTest extends BaseDeviceOwnerTest {
    /*
     * The CA cert below is the content of cacert.pem as generated by:
     *
     * openssl req -new -x509 -days 3650 -extensions v3_ca -keyout cakey.pem -out cacert.pem
     */
    private static final String TEST_CA =
            "-----BEGIN CERTIFICATE-----\n" +
            "MIIDXTCCAkWgAwIBAgIJAK9Tl/F9V8kSMA0GCSqGSIb3DQEBCwUAMEUxCzAJBgNV\n" +
            "BAYTAkFVMRMwEQYDVQQIDApTb21lLVN0YXRlMSEwHwYDVQQKDBhJbnRlcm5ldCBX\n" +
            "aWRnaXRzIFB0eSBMdGQwHhcNMTUwMzA2MTczMjExWhcNMjUwMzAzMTczMjExWjBF\n" +
            "MQswCQYDVQQGEwJBVTETMBEGA1UECAwKU29tZS1TdGF0ZTEhMB8GA1UECgwYSW50\n" +
            "ZXJuZXQgV2lkZ2l0cyBQdHkgTHRkMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIB\n" +
            "CgKCAQEAvItOutsE75WBTgTyNAHt4JXQ3JoseaGqcC3WQij6vhrleWi5KJ0jh1/M\n" +
            "Rpry7Fajtwwb4t8VZa0NuM2h2YALv52w1xivql88zce/HU1y7XzbXhxis9o6SCI+\n" +
            "oVQSbPeXRgBPppFzBEh3ZqYTVhAqw451XhwdA4Aqs3wts7ddjwlUzyMdU44osCUg\n" +
            "kVg7lfPf9sTm5IoHVcfLSCWH5n6Nr9sH3o2ksyTwxuOAvsN11F/a0mmUoPciYPp+\n" +
            "q7DzQzdi7akRG601DZ4YVOwo6UITGvDyuAAdxl5isovUXqe6Jmz2/myTSpAKxGFs\n" +
            "jk9oRoG6WXWB1kni490GIPjJ1OceyQIDAQABo1AwTjAdBgNVHQ4EFgQUH1QIlPKL\n" +
            "p2OQ/AoLOjKvBW4zK3AwHwYDVR0jBBgwFoAUH1QIlPKLp2OQ/AoLOjKvBW4zK3Aw\n" +
            "DAYDVR0TBAUwAwEB/zANBgkqhkiG9w0BAQsFAAOCAQEAcMi4voMMJHeQLjtq8Oky\n" +
            "Azpyk8moDwgCd4llcGj7izOkIIFqq/lyqKdtykVKUWz2bSHO5cLrtaOCiBWVlaCV\n" +
            "DYAnnVLM8aqaA6hJDIfaGs4zmwz0dY8hVMFCuCBiLWuPfiYtbEmjHGSmpQTG6Qxn\n" +
            "ZJlaK5CZyt5pgh5EdNdvQmDEbKGmu0wpCq9qjZImwdyAul1t/B0DrsWApZMgZpeI\n" +
            "d2od0VBrCICB1K4p+C51D93xyQiva7xQcCne+TAnGNy9+gjQ/MyR8MRpwRLv5ikD\n" +
            "u0anJCN8pXo6IMglfMAsoton1J6o5/ae5uhC6caQU8bNUsCK570gpNfjkzo6rbP0\n" +
            "wQ==\n" +
            "-----END CERTIFICATE-----";

    @Override
    protected void tearDown() throws Exception {
        mDevicePolicyManager.setSecurityLoggingEnabled(getWho(), false);
        mDevicePolicyManager.setNetworkLoggingEnabled(getWho(), false);
        mDevicePolicyManager.uninstallCaCert(getWho(), TEST_CA.getBytes());

        super.tearDown();
    }

    /**
     * Test: Retrieving security logs should update the corresponding timestamp.
     */
    public void testRetrieveSecurityLogs() throws Exception {
        Thread.sleep(1);
        final long previousTimestamp = mDevicePolicyManager.getLastSecurityLogRetrievalTime();

        mDevicePolicyManager.setSecurityLoggingEnabled(getWho(), true);

        long timeBefore = System.currentTimeMillis();
        mDevicePolicyManager.retrieveSecurityLogs(getWho());
        long timeAfter = System.currentTimeMillis();

        final long firstTimestamp = mDevicePolicyManager.getLastSecurityLogRetrievalTime();
        assertTrue(firstTimestamp > previousTimestamp);
        assertTrue(firstTimestamp >= timeBefore);
        assertTrue(firstTimestamp <= timeAfter);

        Thread.sleep(2);
        timeBefore = System.currentTimeMillis();
        final boolean preBootSecurityLogsRetrieved =
                mDevicePolicyManager.retrievePreRebootSecurityLogs(getWho()) != null;
        timeAfter = System.currentTimeMillis();

        final long secondTimestamp = mDevicePolicyManager.getLastSecurityLogRetrievalTime();
        if (preBootSecurityLogsRetrieved) {
            // If the device supports pre-boot security logs, verify that retrieving them updates
            // the timestamp.
            assertTrue(secondTimestamp > firstTimestamp);
            assertTrue(secondTimestamp >= timeBefore);
            assertTrue(secondTimestamp <= timeAfter);
        } else {
            // If the device does not support pre-boot security logs, verify that the attempt to
            // retrieve them does not update the timestamp.
            assertEquals(firstTimestamp, secondTimestamp);
        }
    }

    /**
     * Test: Requesting a bug report should update the corresponding timestamp.
     */
    public void testRequestBugreport() throws Exception {
        // This test leaves a notification which will block future tests that request bug reports
        // to fix this - we dismiss the bug report before returning
        CountDownLatch notificationDismissedLatch = initTestRequestBugreport();

        Thread.sleep(1);
        final long previousTimestamp = mDevicePolicyManager.getLastBugReportRequestTime();

        final long timeBefore = System.currentTimeMillis();
        mDevicePolicyManager.requestBugreport(getWho());
        final long timeAfter = System.currentTimeMillis();

        final long newTimestamp = mDevicePolicyManager.getLastBugReportRequestTime();
        assertTrue(newTimestamp > previousTimestamp);
        assertTrue(newTimestamp >= timeBefore);
        assertTrue(newTimestamp <= timeAfter);

        cleanupTestRequestBugreport(notificationDismissedLatch);
    }

    private CountDownLatch initTestRequestBugreport() {
        CountDownLatch notificationDismissedLatch = new CountDownLatch(1);
        NotificationListener.getInstance().addListener((sbt) -> {
            // The notification we are looking for is the one which confirms the bug report is
            // ready and asks for consent to send it
            if (sbt.getPackageName().equals("android") &&
                    sbt.getTag().equals("DevicePolicyManager") &&
                    sbt.getNotification().actions != null) {
                try {
                    // The first action is to decline
                    sbt.getNotification().actions[0].actionIntent.send();
                    notificationDismissedLatch.countDown();
                } catch (PendingIntent.CanceledException e) {
                    fail("Could not dismiss bug report notification");
                }
            }
        });
        return notificationDismissedLatch;
    }

    private void cleanupTestRequestBugreport(CountDownLatch notificationDismissedLatch)
            throws Exception {
        notificationDismissedLatch.await();
        NotificationListener.getInstance().clearListeners();
    }

    /**
     * Test: Retrieving network logs should update the corresponding timestamp.
     */
    public void testGetLastNetworkLogRetrievalTime() throws Exception {
        Thread.sleep(1);
        final long previousTimestamp = mDevicePolicyManager.getLastSecurityLogRetrievalTime();

        mDevicePolicyManager.setNetworkLoggingEnabled(getWho(), true);

        long timeBefore = System.currentTimeMillis();
        mDevicePolicyManager.retrieveNetworkLogs(getWho(), 0 /* batchToken */);
        long timeAfter = System.currentTimeMillis();

        final long newTimestamp = mDevicePolicyManager.getLastNetworkLogRetrievalTime();
        assertTrue(newTimestamp > previousTimestamp);
        assertTrue(newTimestamp >= timeBefore);
        assertTrue(newTimestamp <= timeAfter);
    }

    /**
     * Test: The Device Owner should be able to set and retrieve the name of the organization
     * managing the device.
     */
    public void testDeviceOwnerOrganizationName() throws Exception {
        mDevicePolicyManager.setOrganizationName(getWho(), null);
        assertNull(mDevicePolicyManager.getDeviceOwnerOrganizationName());

        mDevicePolicyManager.setOrganizationName(getWho(), "organization");
        assertEquals("organization", mDevicePolicyManager.getDeviceOwnerOrganizationName());

        mDevicePolicyManager.setOrganizationName(getWho(), null);
        assertNull(mDevicePolicyManager.getDeviceOwnerOrganizationName());
    }

    /**
     * Test: When a Device Owner is set, isDeviceManaged() should return true.
     */
    public void testIsDeviceManaged() throws Exception {
        assertTrue(mDevicePolicyManager.isDeviceManaged());
    }

    /**
     * Test: It should be recored whether the Device Owner or the user set the current IME.
     */
    public void testIsDefaultInputMethodSet() throws Exception {
        final String setting = Settings.Secure.DEFAULT_INPUT_METHOD;
        final ContentResolver resolver = getContext().getContentResolver();
        final String ime = Settings.Secure.getString(resolver, setting);

        Settings.Secure.putString(resolver, setting, "com.test.1");
        Thread.sleep(500);
        assertFalse(mDevicePolicyManager.isCurrentInputMethodSetByOwner());

        mDevicePolicyManager.setSecureSetting(getWho(), setting, "com.test.2");
        Thread.sleep(500);
        assertTrue(mDevicePolicyManager.isCurrentInputMethodSetByOwner());

        Settings.Secure.putString(resolver, setting, ime);
        Thread.sleep(500);
        assertFalse(mDevicePolicyManager.isCurrentInputMethodSetByOwner());
    }

    /**
     * Test: It should be recored whether the Device Owner or the user installed a CA cert.
     */
    public void testGetPolicyInstalledCaCerts() throws Exception {
        final byte[] rawCert = TEST_CA.getBytes();
        final Certificate cert = CertificateFactory.getInstance("X.509")
                .generateCertificate(new ByteArrayInputStream(rawCert));

        // Install a CA cert.
        KeyStore keyStore = KeyStore.getInstance("AndroidCAStore");
        keyStore.load(null, null);
        assertNull(keyStore.getCertificateAlias(cert));
        assertTrue(mDevicePolicyManager.installCaCert(getWho(), rawCert));
        final String alias = keyStore.getCertificateAlias(cert);
        assertNotNull(alias);

        // Verify that the CA cert was marked as installed by the Device Owner.
        verifyOwnerInstalledStatus(alias, true);

        // Uninstall the CA cert.
        mDevicePolicyManager.uninstallCaCert(getWho(), rawCert);

        // Verify that the CA cert is no longer marked as installed by the Device Owner.
        verifyOwnerInstalledStatus(alias, false);
    }

    private void verifyOwnerInstalledStatus(String alias, boolean expectOwnerInstalled) {
        final List<String> ownerInstalledCerts =
                mDevicePolicyManager.getOwnerInstalledCaCerts(Process.myUserHandle());
        assertNotNull(ownerInstalledCerts);
        assertEquals(expectOwnerInstalled, ownerInstalledCerts.contains(alias));
    }
}
/**
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

package android.security.cts;

import android.platform.test.annotations.SecurityTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.android.tradefed.testtype.DeviceJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(DeviceJUnit4ClassRunner.class)
public class Poc18_04 extends SecurityTestCase {
    /**
     * b/69683251
     * Does not require root but must be a hostside test to avoid
     * a race condition
     */
    @Test
    @SecurityTest(minPatchLevel = "2018-04")
    public void testPocCVE_2017_13286() throws Exception {
        getOomCatcher().setHighMemoryTest();
        LaunchSomeWhere.launchSomeWhere("CVE_2017_13286", getDevice());
    }

    /**
     * b/69634768
     * Does not require root but must be a hostside test to avoid a race condition
     */
    @Test
    @SecurityTest(minPatchLevel = "2018-04")
    public void testPocCVE_2017_13288() throws Exception {
        getOomCatcher().setHighMemoryTest();
        LaunchSomeWhere.launchSomeWhere("CVE_2017_13288", getDevice());
    }

    /**
     * b/70398564
     * Does not require root but must be a hostside test to avoid a race condition
     */
    @Test
    @SecurityTest(minPatchLevel = "2018-04")
    public void testPocCVE_2017_13289() throws Exception {
        getOomCatcher().setHighMemoryTest();
        LaunchSomeWhere.launchSomeWhere("CVE_2017_13289", getDevice());
    }
}

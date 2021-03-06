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

package android.cts.tagging.disabled;

import android.os.Build;
import androidx.test.filters.SmallTest;
import androidx.test.runner.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import android.cts.tagging.Utils;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class TaggingTest {

    @Test
    public void testHeapTaggingEnabled() {
      // Skip the test if not Arm64.
      if (Build.CPU_ABI.startsWith("arm64")) {
        int tag = Utils.nativeHeapPointerTag();
        if (Utils.kernelSupportsTaggedPointers()) {
          assertNotEquals(0, tag);
        } else {
          assertEquals(0, tag);
        }
      }
    }

    @Test
    public void testHeapTaggingDisabled() {
      assertEquals(0, Utils.nativeHeapPointerTag());
    }
}

/*
 * Copyright (C) 2019 The Android Open Source Project
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

package android.view.cts.sdk28;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

import androidx.test.InstrumentationRegistry;
import androidx.test.annotation.UiThreadTest;
import androidx.test.filters.MediumTest;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@MediumTest
@RunWith(AndroidJUnit4.class)
public class ViewGroupTest {

    private Context mContext;
    private MockViewGroup mViewGroup;

    @UiThreadTest
    @Before
    public void setup() {
        mContext = InstrumentationRegistry.getTargetContext();
        mViewGroup = new MockViewGroup(mContext);
    }

    @UiThreadTest
    @Test
    public void testFitSystemWindows() {
        Rect rect = new Rect(1, 1, 100, 100);
        assertFalse(mViewGroup.fitSystemWindows(rect));

        mViewGroup = new MockViewGroup(mContext);
        MockView mv = new MockView(mContext);
        mViewGroup.addView(mv);
        assertTrue(mViewGroup.fitSystemWindows(rect));
    }

    private class MockViewGroup extends ViewGroup {

        public MockViewGroup(Context context) {
            super(context);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
        }

        @Override
        protected boolean fitSystemWindows(Rect insets) {
            return super.fitSystemWindows(insets);
        }
    }

    private class MockView extends View {

        public MockView(Context context) {
            super(context);
        }

        @Override
        protected boolean fitSystemWindows(Rect insets) {
            return true;
        }
    }

}

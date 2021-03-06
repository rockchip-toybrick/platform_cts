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

package android.view.cts;

import android.app.Activity;
import android.view.KeyEvent;


public class KeyEventInjectionActivity extends Activity {
    private static final String TAG = "KeyEventInjectionActivity";

    private KeyEvent.Callback mCallback;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // This is needed to activate onKeyLongPress behaviour
        event.startTracking();
        return mCallback.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return mCallback.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return mCallback.onKeyLongPress(keyCode, event);
    }

    @Override
    public boolean onKeyMultiple(int keyCode, int count, KeyEvent event) {
        // Do not expect this to be called. ACTION_MULTIPLE is deprecated.
        return mCallback.onKeyMultiple(keyCode, count, event);
    }

    void setCallback(KeyEvent.Callback callback) {
        mCallback = callback;
    }
}

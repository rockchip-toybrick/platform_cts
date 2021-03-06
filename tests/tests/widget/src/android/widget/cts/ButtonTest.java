/*
 * Copyright (C) 2008 The Android Open Source Project
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

package android.widget.cts;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Xml;
import android.widget.Button;

import androidx.test.InstrumentationRegistry;
import androidx.test.filters.SmallTest;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.xmlpull.v1.XmlPullParser;

@SmallTest
@RunWith(AndroidJUnit4.class)
public class ButtonTest {
    @Test
    public void testConstructor() {
        Context context = InstrumentationRegistry.getTargetContext();
        XmlPullParser parser = context.getResources().getXml(R.layout.togglebutton_layout);
        AttributeSet attrs = Xml.asAttributeSet(parser);

        new Button(context, attrs, 0);
        new Button(context, attrs);
        new Button(context);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorWithNullContext1() {
        new Button(null);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorWithNullContext2() {
        new Button(null, null);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorWithNullContext3() {
        new Button(null, null, -1);
    }
}

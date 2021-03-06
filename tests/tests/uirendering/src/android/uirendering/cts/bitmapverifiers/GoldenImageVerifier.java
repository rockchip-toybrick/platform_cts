/*
 * Copyright (C) 2014 The Android Open Source Project
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
package android.uirendering.cts.bitmapverifiers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.uirendering.cts.bitmapcomparers.BitmapComparer;
import android.uirendering.cts.differencevisualizers.PassFailVisualizer;

public class GoldenImageVerifier extends BitmapVerifier {
    private final BitmapComparer mBitmapComparer;
    private final int[] mGoldenBitmapArray;
    private final int mWidth;
    private final int mHeight;

    public GoldenImageVerifier(Bitmap goldenBitmap, BitmapComparer bitmapComparer) {
        mWidth = goldenBitmap.getWidth();
        mHeight = goldenBitmap.getHeight();
        mGoldenBitmapArray = new int[mWidth * mHeight];
        goldenBitmap.getPixels(mGoldenBitmapArray, 0, mWidth, 0, 0, mWidth, mHeight);
        mBitmapComparer = bitmapComparer;
    }

    public GoldenImageVerifier(Context context, int goldenResId, BitmapComparer bitmapComparer) {
        this(BitmapFactory.decodeResource(context.getResources(), goldenResId), bitmapComparer);
    }

    @Override
    public boolean verify(Bitmap bitmap) {
        // Clip to the size of the golden image.
        if (bitmap.getWidth() > mWidth || bitmap.getHeight() > mHeight) {
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, mWidth, mHeight);
        }
        return super.verify(bitmap);
    }

    @Override
    public boolean verify(int[] bitmap, int offset, int stride, int width, int height) {
        boolean success = mBitmapComparer.verifySame(mGoldenBitmapArray, bitmap, offset, stride,
                width, height);
        if (!success) {
            mDifferenceBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            int[] differences = new PassFailVisualizer().getDifferences(mGoldenBitmapArray, bitmap);
            mDifferenceBitmap.setPixels(differences, 0, width, 0, 0, width, height);
        }
        return success;
    }
}

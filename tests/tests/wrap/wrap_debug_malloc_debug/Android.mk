# Copyright (C) 2017 The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MULTILIB := both
LOCAL_MODULE_TAGS := tests
LOCAL_MODULE_PATH := $(TARGET_OUT_DATA_APPS)
LOCAL_DEX_PREOPT := false
LOCAL_PROGUARD_ENABLED := disabled
LOCAL_STATIC_JAVA_LIBRARIES := \
	compatibility-device-util-axt \
	androidx.test.rules
LOCAL_JAVA_LIBRARIES := android.test.runner.stubs android.test.base.stubs
LOCAL_SRC_FILES := $(call all-java-files-under, ../src)
LOCAL_COMPATIBILITY_SUITE := cts vts10 general-tests
LOCAL_SDK_VERSION := current

LOCAL_PREBUILT_JNI_LIBS_arm := wrap.sh
LOCAL_PREBUILT_JNI_LIBS_arm64 := wrap.sh
LOCAL_PREBUILT_JNI_LIBS_mips := wrap.sh
LOCAL_PREBUILT_JNI_LIBS_mips64 := wrap.sh
LOCAL_PREBUILT_JNI_LIBS_x86 := wrap.sh
LOCAL_PREBUILT_JNI_LIBS_x86_64 := wrap.sh

LOCAL_PACKAGE_NAME := CtsWrapWrapDebugMallocDebugTestCases
LOCAL_MANIFEST_FILE := AndroidManifest.xml

# Jarjar to make WrapTest unique.
LOCAL_JARJAR_RULES := $(LOCAL_PATH)/jarjar-rules.txt

LOCAL_USE_EMBEDDED_NATIVE_LIBS := false

include $(BUILD_PACKAGE)

include $(CLEAR_VARS)

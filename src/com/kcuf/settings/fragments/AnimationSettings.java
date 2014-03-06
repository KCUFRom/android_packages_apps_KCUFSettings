/*
 * Copyright (C) 2014-2015 The CyanogenMod Project
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

package com.kcuf.settings.fragments;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.UserInfo;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.UserManager;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v14.preference.SwitchPreference;
import android.provider.Settings;
import android.widget.Toast;
import com.android.settings.R;
import android.support.annotation.NonNull;

import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.SettingsPreferenceFragment;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;


public class AnimationSettings extends SettingsPreferenceFragment
                implements Preference.OnPreferenceChangeListener {

    private static final String KEY_TOAST_ANIMATION = "toast_animation";

    private ListPreference mToastAnimation;

    Toast mToast;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        addPreferencesFromResource(R.xml.kcuf_settings_animations);

        final ContentResolver resolver = getActivity().getContentResolver();

        mToastAnimation = (ListPreference) findPreference(KEY_TOAST_ANIMATION);
        int toastanimation = Settings.System.getIntForUser(resolver,
                Settings.System.TOAST_ANIMATION, 1,
                UserHandle.USER_CURRENT);
        mToastAnimation.setValue(String.valueOf(toastanimation));
        mToastAnimation.setSummary(mToastAnimation.getEntry());
        int CurrentToastAnimation = Settings.System.getInt(getContentResolver(), Settings.System.TOAST_ANIMATION, 1);
        mToastAnimation.setValueIndex(CurrentToastAnimation); //set to index of default value
        mToastAnimation.setSummary(mToastAnimation.getEntries()[CurrentToastAnimation]);
        mToastAnimation.setOnPreferenceChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();
        if (preference == mToastAnimation) {
            int value = Integer.parseInt((String) newValue);
            int index = mToastAnimation.findIndexOfValue((String) newValue);
            Settings.System.putIntForUser(resolver,
                    Settings.System.TOAST_ANIMATION, value, UserHandle.USER_CURRENT);
            mToastAnimation.setSummary(mToastAnimation.getEntries()[index]);
            if (mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(getActivity(), R.string.toast_animation_test,
                    Toast.LENGTH_SHORT);
            mToast.show();
            return true;

        }

        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.KCUF_SETTINGS;
    }
}

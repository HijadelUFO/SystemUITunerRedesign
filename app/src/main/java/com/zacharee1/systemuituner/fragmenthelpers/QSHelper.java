package com.zacharee1.systemuituner.fragmenthelpers;

import android.os.Build;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.SwitchPreference;
import android.provider.Settings;

import com.zacharee1.systemuituner.fragments.ItemDetailFragment;
import com.zacharee1.systemuituner.prefs.SliderPreference;
import com.zacharee1.systemuituner.misc.SettingsUtils;

public class QSHelper
{
    private ItemDetailFragment mFragment;

    public QSHelper(ItemDetailFragment fragment) {
        mFragment = fragment;

        setSwitchStates();
        setSwitchListeners();
        setSliderState();
    }

    private void setSwitchStates() {
        for (int i = 0; i < mFragment.getPreferenceScreen().getRootAdapter().getCount(); i++) { //loop through every preference
            Object o = mFragment.getPreferenceScreen().getRootAdapter().getItem(i);

            if (o instanceof SwitchPreference) { //if current preference is a SwitchPreference
                SwitchPreference pref = (SwitchPreference) o;

                pref.setChecked(Settings.Secure.getInt(mFragment.getContext().getContentResolver(), pref.getKey(), 1) == 1);
            }
        }
    }

    private void setSwitchListeners() {
        for (int i = 0; i < mFragment.getPreferenceScreen().getRootAdapter().getCount(); i++) { //loop through every preference
            Object o = mFragment.getPreferenceScreen().getRootAdapter().getItem(i);

            if (o instanceof SwitchPreference) { //if current preference is a SwitchPreference
                final SwitchPreference pref = (SwitchPreference) o;

                pref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
                {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object o)
                    {
                        if (Boolean.valueOf(o.toString())) {
                            SettingsUtils.writeSecure(mFragment.getContext(), preference.getKey(), "1");
                        } else {
                            SettingsUtils.writeSecure(mFragment.getContext(), preference.getKey(), "0");
                        }
                        return true;
                    }
                });
            }
        }
    }

    private void setSliderState() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {

            SliderPreference pref = (SliderPreference) mFragment.findPreference("sysui_qqs_count"); //find the SliderPreference

            pref.setMaxProgess(20);
            pref.setProgressState(Settings.Secure.getInt(mFragment.getContext().getContentResolver(), "sysui_qqs_count", 5)); //set the progress/value from Settings
            pref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
            {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o)
                {
                    SettingsUtils.writeSecure(mFragment.getContext(), "sysui_qqs_count", o.toString()); //write new value to Settings if user presses OK
                    return true;
                }
            });
        } else {
            PreferenceCategory category = (PreferenceCategory) mFragment.findPreference("qqs_count_category");
            mFragment.getPreferenceScreen().removePreference(category);
        }

    }
}

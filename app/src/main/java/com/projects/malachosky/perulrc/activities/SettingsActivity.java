package com.projects.malachosky.perulrc.activities;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.SwitchPreference;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;

import com.projects.malachosky.perulrc.R;
import com.projects.malachosky.perulrc.model.Note;
import com.projects.malachosky.perulrc.tasks.DataTask;

import java.util.List;

public class SettingsActivity extends AppCompatPreferenceActivity {

    private final String mTag = SettingsActivity.class.getSimpleName();

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(this, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName);
    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {

        private final String mGeneralTag = GeneralPreferenceFragment.class.getSimpleName();

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);

            initializePreferences();
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        private void initializePreferences() {
            Preference preference = findPreference("clear_notes");
            preference.setOnPreferenceClickListener(pref -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.SettingsTheme);
                builder.setMessage("Are you sure you want to delete all of your notes?")
                        .setTitle("Clear All Notes");
                builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                    Log.v(mGeneralTag, "User decided to delete all their notes");
                    /* Sending a blank note will Delete All Notes in the file system */
                    new DataTask().execute(new Note());
                });
                builder.setNegativeButton("No", (dialogInterface, i) -> {
                    Log.v(mGeneralTag, "User kept their notes!");
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            });

            SwitchPreference nightModePref = (SwitchPreference)findPreference("night_mode");
            //TODO: Add a switch preference listener to listen for change and set the night theme or day theme
        }
    }
}
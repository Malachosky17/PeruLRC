package com.projects.malachosky.perulrc.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.projects.malachosky.perulrc.R;
import com.projects.malachosky.perulrc.model.Constants;

public class MainActivity extends AppCompatActivity {

    private final String LOGTAG = MainActivity.class.getSimpleName();

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private int viewPageNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //Adding one to position to resemble the day number
                viewPageNumber = position + 1;

            }

            @Override
            public void onPageSelected(int position) {
                //Not implemented
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //Not implemented
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        } else if(id == R.id.action_notes) {
            Intent intent = new Intent(this, NotesContainerActivity.class);
            startActivity(intent);
        } else if(id == R.id.action_full_view) {
            Intent intent = new Intent(this, SelectionDetailedActivity.class);
            intent.putExtra("view_page_number", viewPageNumber);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */

        private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET};

        private final String LOGTAG = PlaceholderFragment.class.getSimpleName();
        private static final String ARG_SECTION_NUMBER = "section_number";
        private final int PERMISSION_CODE = 333;

        private ImageView imageView;
        private TextView textView_sectionNumber;
        private WebView sectionWebView;
        private RatingBar ratingBar;
        private SharedPreferences sharedPreferences;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            sharedPreferences = getContext().getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            int sectionNum = getArguments().getInt(ARG_SECTION_NUMBER);

            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            textView_sectionNumber = (TextView) rootView.findViewById(R.id.section_label);
            imageView = (ImageView) rootView.findViewById(R.id.section_image);
            sectionWebView = (WebView) rootView.findViewById(R.id.section_webview);
            ratingBar = (RatingBar) rootView.findViewById(R.id.ratingBar);
            customizeNodes(sectionNum);

            if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        permissions, PERMISSION_CODE);
            } else {
                sharedPreferences.edit().putBoolean("permissions", true).apply();
            }
            return rootView;
        }

        @Override
        public void onResume() {
            super.onResume();
        }


        /**
         * TODO: Check for all permissions needed to make permission requests simple.
         * @param requestCode
         * @param permissions
         * @param grantResults
         */
        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            switch(requestCode) {
                case PERMISSION_CODE:
                    boolean externalStoragePermission = grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    boolean internalStoragePermission = grantResults[1]==PackageManager.PERMISSION_GRANTED;
                    boolean internetPermission = grantResults[2]==PackageManager.PERMISSION_GRANTED;
                    if(externalStoragePermission && internalStoragePermission && internetPermission) {
                        Log.v(LOGTAG, "All permissions were granted by the user.");
                    }
                    break;
            }
        }

        /**
         * Customize each fragment to represent the day in Peru
         * @param sectionNum -> pass in the day
         */
        private void customizeNodes(int sectionNum) {
            //TODO; Fix delay bug with the rating stars.
            String webData = "";
            int rating = 0;
            switch(sectionNum) {
                case 1:
                    webData = getString(R.string.short_description_one);
                    imageView.setImageResource(R.drawable.lima_city);
                    ratingBar.setRating(sharedPreferences.getInt("day_1_rating", rating));
                    break;
                case 2:
                    webData = getString(R.string.short_description_two);
                    imageView.setImageResource(R.drawable.bohemian_district);
                    ratingBar.setRating(sharedPreferences.getInt("day_2_rating", rating));
                    break;
                case 3:
                    webData = getString(R.string.short_description_three);
                    imageView.setImageResource(R.drawable.agro_ecological_produce);
                    ratingBar.setRating(sharedPreferences.getInt("day_3_rating", rating));
                    break;
                case 4:
                    webData = getString(R.string.short_description_four);
                    imageView.setImageResource(R.drawable.university_students);
                    ratingBar.setRating(sharedPreferences.getInt("day_4_rating", rating));
                    break;
                case 5:
                    webData = getString(R.string.short_description_five);
                    imageView.setImageResource(R.drawable.puno_city);
                    ratingBar.setRating(sharedPreferences.getInt("day_5_rating", rating));
                    break;
                case 6:
                    webData = getString(R.string.short_description_six);
                    imageView.setImageResource(R.drawable.peru_background);
                    ratingBar.setRating(sharedPreferences.getInt("day_6_rating", rating));
                    break;
                case 7:
                    webData = getString(R.string.short_description_seven);
                    imageView.setImageResource(R.drawable.puno_adventure);
                    ratingBar.setRating(sharedPreferences.getInt("day_7_rating", rating));
                    break;
                case 8:
                    webData = getString(R.string.short_description_eight);
                    imageView.setImageResource(R.drawable.lake_titicaca);
                    ratingBar.setRating(sharedPreferences.getInt("day_8_rating", rating));
                    break;
                case 9:
                    webData = getString(R.string.short_description_nine);
                    imageView.setImageResource(R.drawable.faux_taquile_island);
                    ratingBar.setRating(sharedPreferences.getInt("day_9_rating", rating));
                    break;
                case 10:
                    webData = getString(R.string.short_description_ten);
                    imageView.setImageResource(R.drawable.lima_last_day);
                    ratingBar.setRating(sharedPreferences.getInt("day_10_rating", rating));
                    break;
                default:
                    break;
            }
            textView_sectionNumber.setText(getString(R.string.section_format, sectionNum));
            sectionWebView.loadData("<html>" +
                    "<style>" +
                    "body {background-color: #272828;}" +
                    "h2 {color: #ffffff}" +
                    "</style>" +
                    "<h2>" +
                    webData +
                    "</h2>" +
                    "</html>", "text/html", null);
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            viewPageNumber = position;
            switch (position) {
                case 0:
                    return "Day 1";
                case 1:
                    return "Day 2";
                case 2:
                    return "Day 3";
                case 3:
                    return "Day 4";
                case 4:
                    return "Day 5";
                case 5:
                    return "Day 6";
                case 6:
                    return "Day 7";
                case 7:
                    return "Day 8";
                case 8:
                    return "Day 9";
                case 9:
                    return "Day 10";
            }
            return null;
        }
    }
}

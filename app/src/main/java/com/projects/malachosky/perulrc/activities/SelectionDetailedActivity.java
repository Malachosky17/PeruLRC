package com.projects.malachosky.perulrc.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.projects.malachosky.perulrc.R;
import com.projects.malachosky.perulrc.model.Constants;

public class SelectionDetailedActivity extends AppCompatActivity {

    private final String LOGTAG = SelectionDetailedActivity.class.getSimpleName();
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_detailed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        int dayNumber = getIntent().getIntExtra("view_page_number", 0);
        Log.v(LOGTAG, "View page launched from: " + dayNumber);
        webView = (WebView)findViewById(R.id.detailed_webview);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RatingDialogFragment ratingDialogFragment = new RatingDialogFragment();
                ratingDialogFragment.setDay(dayNumber);
                FragmentManager fm = getSupportFragmentManager();
                ratingDialogFragment.show(fm, "rating_dialog");
            }
        });
        viewPageToWebData(dayNumber);
    }

    private void viewPageToWebData(int dayNumber) {
        String title;
        String subTitle;
        String webData = getString(R.string.big_title_day);
        switch(dayNumber) {
            case 1:
                title = getString(R.string.big_title_day) + " " + dayNumber;
                subTitle = getString(R.string.subtitle_day_one);
                webData = getString(R.string.description_day_one);
                break;
            case 2:
                title = getString(R.string.big_title_day) + " " + dayNumber;
                subTitle = getString(R.string.subtitle_day_two);
                webData = getString(R.string.description_day_two);
                break;
            case 3:
                title = getString(R.string.big_title_day) + " " + dayNumber;
                subTitle = getString(R.string.subtitle_day_three);
                webData = getString(R.string.description_day_three);
                break;
            case 4:
                title = getString(R.string.big_title_day) + " " + dayNumber;
                subTitle = getString(R.string.subtitle_day_four);
                webData = getString(R.string.description_day_four);
                break;
            case 5:
                title = getString(R.string.big_title_day) + " " + dayNumber;
                subTitle = getString(R.string.subtitle_day_five);
                webData = getString(R.string.description_day_five);
                break;
            case 6:
                title = getString(R.string.big_title_day) + " " + dayNumber;
                subTitle = getString(R.string.subtitle_day_six);
                webData = getString(R.string.description_day_six);
                break;
            case 7:
                title = getString(R.string.big_title_day) + " " + dayNumber;
                subTitle = getString(R.string.subtitle_day_seven);
                webData = getString(R.string.description_day_seven);
                break;
            case 8:
                title = getString(R.string.big_title_day) + " " + dayNumber;
                subTitle = getString(R.string.subtitle_day_eight);
                webData = getString(R.string.description_day_eight);
                break;
            case 9:
                title = getString(R.string.big_title_day) + " " + dayNumber;
                subTitle = getString(R.string.subtitle_day_nine);
                webData = getString(R.string.description_day_nine);
                break;
            case 10:
                title = getString(R.string.big_title_day) + " " + dayNumber;
                subTitle = getString(R.string.subtitle_day_ten);
                webData = getString(R.string.description_day_ten);
                break;
            default:
                title = getString(R.string.big_title_day) + " " + 1;
                subTitle = getString(R.string.subtitle_day_one);
                break;
        }
        setTitle(title);
        webView.loadData("<html>" +
                "<h3>" + subTitle + "</h3>" +
                "<body>" +
                webData +
                "</body>" +
                "</html>", "text/html", null);
    }

    public static class RatingDialogFragment extends DialogFragment {

        private RatingBar ratingBar;
        private TextView textView_title;
        private Button btn_ok;
        private SharedPreferences sharedPreferences;
        private int mDay;

        public RatingDialogFragment() {

        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_rating, container, false);
            ratingBar = (RatingBar) rootView.findViewById(R.id.dialog_rating_bar);
            textView_title = (TextView) rootView.findViewById(R.id.dialog_title);
            btn_ok = (Button) rootView.findViewById(R.id.dialog_ok_btn);
            sharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE);
            return rootView;
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            ratingBar.setRating(sharedPreferences.getInt("day_" + mDay + "_rating", 0));
            getDialog().setTitle("Rate your adventure");
            this.btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int rating = Math.round(ratingBar.getRating());
                    sharedPreferences.edit().putInt("day_" + mDay + "_rating", rating).apply();
                    dismiss();
                }
            });
        }

        public void setDay(int pDay) {
            this.mDay = pDay;
        }
    }
}

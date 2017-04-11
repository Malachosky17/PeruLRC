package com.projects.malachosky.perulrc.model;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * @author Malac
 * @since 3/26/2017
 */

public class NoteCardView extends CardView {

    private AppCompatImageView mImgView;
    private Uri mImgURI;
    private TextView mTitleView;
    private TextView mBodyView;
    private TextView mDateView;

    public NoteCardView(Context context) {
        super(context);
    }

    /**
     * Full constructor of NoteCardView
     * @param context - Context of calling activity
     * @param title - Title of the card
     * @param body - A short body sneak peek of the full journal entry
     * @param imgURI - The first image of the associate Note
     */
    public NoteCardView(Context context, String title, String body, Uri imgURI) {
        super(context);
        setImgView(imgURI);

    }

    private String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        return format.format(calendar.getTime());
    }

    public void setImgView(Uri imageUri) {
        if(imageUri != null) {
            mImgView.setImageURI(imageUri);
            mImgURI = imageUri;
        } else {
            //TODO: Set image to some default blank image.
        }
    }

    public Uri getImgViewURI() {
        return mImgURI;
    }
}

package com.projects.malachosky.perulrc.model;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Malac on 3/26/2017.
 *
 * @author: Malac
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

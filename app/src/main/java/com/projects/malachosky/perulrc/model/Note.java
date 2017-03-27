package com.projects.malachosky.perulrc.model;

import android.net.Uri;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Malac on 3/26/2017.
 *
 * @author: Malac
 */

public class Note {

    private Uri mImgURI;
    private String mTitle;
    private String mBody;
    private String mCreatedDate;

    public Note(String pTitle, String pBody) {
        this.mTitle = pTitle;
        this.mBody = pBody;
        setCurrentDate();
    }

    public String getTitle() {
        return mTitle;
    }
    public void setTitle(String title) {
        this.mTitle = title;
    }
    public String getBody() {
        return mBody;
    }
    public void setBody(String body) {
        this.mBody = body;
    }
    public Uri getImgURI() {
        return mImgURI;
    }
    public void setImgURI(Uri imgURI) {
        this.mImgURI = imgURI;
    }
    public String getCurrentDate() {
        return mCreatedDate;
    }
    private void setCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        this.mCreatedDate = format.format(calendar.getTime());
    }
    public void updateCurrentDate() {
        setCurrentDate();
    }
}

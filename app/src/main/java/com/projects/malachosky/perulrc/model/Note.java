package com.projects.malachosky.perulrc.model;

import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * @author Malac
 * @since 3/26/2017
 */

public class Note implements Parcelable{

    private Note replacedNote;
    private Constants.DataAction action = Constants.DataAction.SaveNote;
    private Uri mImgURI;
    private String mTitle;
    private String mBody;
    private String mCreatedDate;
    private CountDownTimer timer;

    /**
     * Constructing new Note
     * @param pTitle - Title of the Note
     * @param pBody - Journal entry of the Note
     */
    public Note(String pTitle, String pBody) {
        this.mTitle = pTitle;
        this.mBody = pBody;
        setCurrentDate();
    }

    /**
     * Used to show an undo filler in the NotesContainerActivity
     * @param pTitle
     */
    public Note(String pTitle) {
        this.mTitle = pTitle;
    }

    /**
     * Used to DeleteAll of the notes in the file system
     */
    public Note() {
        this.action = Constants.DataAction.DeleteAll;
    }

    public String getTitle() {
        return mTitle;
    }
    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getFileName() {
        return mTitle + ".txt";
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
    public void setAction(Constants.DataAction action) {
        this.action = action;
    }
    public Constants.DataAction getAction() {
        return action;
    }
    public CountDownTimer getTimer() {
        return timer;
    }
    public void setTimer(CountDownTimer timer) {
        this.timer = timer;
    }
    public Note getReplacedNote() {
        return replacedNote;
    }
    public void setReplacedNote(Note replacedNote) {
        this.replacedNote = replacedNote;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mBody);
        dest.writeString(mCreatedDate);
        dest.writeValue(mImgURI);
    }

    public static final Parcelable.Creator<Note> CREATOR = new Parcelable.Creator<Note>() {
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    /**
     * Private constructor used to make a parcelable Note
     * @param in - Parcel object to set the member variables
     */
    private Note(Parcel in) {
        mTitle = in.readString();
        mBody = in.readString();
        mCreatedDate = in.readString();
        mImgURI = (Uri) in.readValue(Uri.class.getClassLoader());
    }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "Title: %s\n" +
                                             "URI: %s", mTitle, getFileName());
    }
}
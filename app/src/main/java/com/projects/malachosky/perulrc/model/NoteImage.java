package com.projects.malachosky.perulrc.model;

import android.net.Uri;

/**
 * Created by Malac on 3/31/2017.
 *
 * @author: Malac
 */

public class NoteImage {

    private Uri mImgResource;

    public NoteImage(Uri imgResource) {
        this.mImgResource = imgResource;
    }

    public Uri getImgURI() {
        return mImgResource;
    }
}

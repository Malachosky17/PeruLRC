package com.projects.malachosky.perulrc.model;

import android.os.Environment;

import java.io.File;

/**
 * @author Malac
 * @since 3/28/2017
 */

public final class Constants {

    public enum DataAction {
        DeleteAll, SaveNote, DeleteNote
    }

    public static final String sdCard = Environment.getExternalStorageDirectory() + File.separator;
    public static final String mainFolder = sdCard + "PeruNotes";

    public static final String SHARED_PREF = "private_prefs";
}

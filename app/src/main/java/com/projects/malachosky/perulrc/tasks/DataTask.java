package com.projects.malachosky.perulrc.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.projects.malachosky.perulrc.model.Constants;
import com.projects.malachosky.perulrc.model.Note;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * @author Malac
 * @since 3/28/2017
 */

public class DataTask extends AsyncTask<Note, Void, Void> {

    private String TAG = DataTask.class.getSimpleName();
    private File mMainDirectory;

    public DataTask() {

    }

    /**
     * This method is a safeguard to make sure that the file system exists. If it does not exist, it is created.
     */
    @Override
    protected void onPreExecute() {
        mMainDirectory = new File(Constants.mainFolder);
        if(!mMainDirectory.exists()) {
            if(mMainDirectory.mkdirs()) {
                Log.v(TAG, "Created " + mMainDirectory.getName());
            }
        } else {
            Log.v(TAG, mMainDirectory.getName() + " exists!");
        }
        super.onPreExecute();
    }

    /**
     * This method is to either save a note, delete a note or delete every note in the file system.
     * @param param - Single Note param possible carrying DataAction
     * @return - Returns nothing
     */
    @Override
    protected Void doInBackground(Note... param) {
        if(param.length == 0) {
            return null;
        }
        if(param[0].getAction() == Constants.DataAction.SaveNote) {
            //TODO: Create the note that was just saved.
            saveAssociatedNote(param[0]);
        } else if(param[0].getAction() == Constants.DataAction.DeleteAll) {
            deleteAllNotes();
        } else if(param[0].getAction() == Constants.DataAction.DeleteNote) {
            deleteAssociatedFile(param[0]);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
    }

    private void deleteAllNotes() {
        if(mMainDirectory.exists()) {
            for(File file : mMainDirectory.listFiles()) {
                boolean success = file.delete();
                if(success) {
                    Log.v(TAG, "Successfully deleted: " + file.getName());
                }
            }
        }
    }

    /**
     * @param note - Data passed here will be written to disk.
     */
    private void noteToFile(Note note) {
        File file = new File(Constants.mainFolder, note.getTitle() + ".txt");
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file));
            outputStreamWriter.write(note.getBody());
            outputStreamWriter.close();
            Log.v(TAG, "Created: " + file.getName());
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * @param note - Deletes the file with name note.getFileName()
     */
    private void deleteAssociatedFile(Note note) {
        File file = new File(Constants.mainFolder, note.getTitle() + ".txt");
        if(file.exists()) {
            boolean success = file.delete();
            if(success) {
                Log.v(TAG, "Successfully deleted: " + file.getName());
            } else {
                Log.v(TAG, "Could not delete: " + file.getName());
            }
        } else {
            Log.e(TAG, "Could not find: " + file.getName());
        }
    }

    /**
     * Overwrites the current file if it exists or creates a new file to represent the note
     * @param note - Carries data needed to be written
     */
    private void saveAssociatedNote(Note note) {
        File file = new File(Constants.mainFolder, note.getTitle() + ".txt");
        if(!file.exists()) {
            noteToFile(note);
            return;
        }
        try {
            boolean deleted = file.delete();
            if(deleted) {
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file));
                outputStreamWriter.write(note.getBody());
                outputStreamWriter.close();
                Log.v(TAG, "Edited: " + file.getName());
            } else {
                Log.v(TAG, "Did not edit: " + file.getName());
            }
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
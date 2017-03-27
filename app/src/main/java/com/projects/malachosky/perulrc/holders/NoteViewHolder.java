package com.projects.malachosky.perulrc.holders;

import android.net.Uri;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.projects.malachosky.perulrc.model.Note;

/**
 * Created by Malac on 3/26/2017.
 *
 * @author: Malac
 */

public class NoteViewHolder extends RecyclerView.ViewHolder {

    private AppCompatImageView mImgView;
    private TextView mTitleView;
    private TextView mBodyView;
    private TextView mDateView;

    public NoteViewHolder(View itemView) {
        super(itemView);
    }

    public void updateUI(Note note) {
        Uri imgURI = note.getImgURI();
        mImgView.setImageURI(imgURI);
        mTitleView.setText(note.getTitle());
        mBodyView.setText(note.getBody());
        mDateView.setText(note.getCurrentDate());
    }
}

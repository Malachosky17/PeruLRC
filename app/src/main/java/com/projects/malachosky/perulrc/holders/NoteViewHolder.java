package com.projects.malachosky.perulrc.holders;

import android.net.Uri;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.projects.malachosky.perulrc.R;
import com.projects.malachosky.perulrc.model.Note;

/**
 * Created by Malac on 3/26/2017.
 *
 * @author: Malac
 */

public class NoteViewHolder extends RecyclerView.ViewHolder {

    private final String LOGTAG = NoteViewHolder.class.getSimpleName();

    private ImageView mAttachmentImage;
    private TextView mTitleView;
    private TextView mBodyView;
    private TextView mTimeView;

    public NoteViewHolder(View itemView) {
        super(itemView);
        this.mAttachmentImage = (ImageView)itemView.findViewById(R.id.note_card_attach_status);
        this.mTitleView = (TextView)itemView.findViewById(R.id.note_card_title);
        this.mBodyView = (TextView)itemView.findViewById(R.id.note_card_body);
        this.mTimeView = (TextView)itemView.findViewById(R.id.note_card_time);
    }

    public void updateUI(Note note) {
        if(note.getImgURI() != null) {
            Log.v(LOGTAG, "URI exists: " + note.getImgURI().toString());
            mAttachmentImage.setVisibility(View.VISIBLE);
        }
        mTitleView.setText(note.getTitle());
        mBodyView.setText(note.getBody());
        mTimeView.setText(note.getCurrentDate());
    }
}

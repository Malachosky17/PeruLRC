package com.projects.malachosky.perulrc.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projects.malachosky.perulrc.R;
import com.projects.malachosky.perulrc.activities.NoteViewActivity;
import com.projects.malachosky.perulrc.holders.NoteViewHolder;
import com.projects.malachosky.perulrc.model.Note;

import java.util.ArrayList;

/**
 * Created by Malac on 3/26/2017.
 *
 * @author: Malac
 */

public class NotesAdapter extends RecyclerView.Adapter<NoteViewHolder> {

    private ArrayList<Note> notesList;
    public NotesAdapter(ArrayList<Note> notes) {
        this.notesList = notes;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View noteCard = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_card, parent, false);
        return new NoteViewHolder(noteCard);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        final Note note = notesList.get(position);
        holder.updateUI(note);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
                //TODO: Start the activity of which handles editing a note.
               Intent intent = new Intent(view.getContext(), NoteViewActivity.class);
               intent.putExtra(Note.class.getCanonicalName(), note);
               view.getContext().startActivity(intent);
           }
       });
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }
}

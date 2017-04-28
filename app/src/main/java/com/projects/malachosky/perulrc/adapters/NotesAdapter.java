package com.projects.malachosky.perulrc.adapters;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projects.malachosky.perulrc.R;
import com.projects.malachosky.perulrc.activities.NoteViewActivity;
import com.projects.malachosky.perulrc.holders.NoteViewHolder;
import com.projects.malachosky.perulrc.model.Note;
import com.projects.malachosky.perulrc.tasks.DataTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Malac on 4/11/2017.
 *
 * TODO: Implement the pending undo feature from:
 *      https://github.com/hdpavan/RecyclerViewItemTouchHelperSwipe/blob/master/app/src/main/java/com/tutorialsbuzz/recyclerviewitemleftswipe/
 *      http://tutorialsbuzz.com/2016/12/android-swipe-recyclerview-items-using-itemtouchhelper.html
 *
 * @author Malac
 */
public class NotesAdapter extends RecyclerView.Adapter<NoteViewHolder> {

    private ArrayList<Note> notesList;

    private static final int PENDING_REMOVAL_TIMER = 3000; //3 secs

    /**
     * Handler for running delayed runnables
     */
    private Handler handler = new Handler();

    /**
     * Map of notes to pending runnables, allows cancel
     */
    private HashMap<Note, Runnable> pendingRunnables = new HashMap<>();

    public NotesAdapter(ArrayList<Note> notesList) {
        this.notesList = notesList;
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

        holder.itemView.setOnClickListener(view -> {
            if(note.getTitle().contentEquals("Undo") && note.getCurrentDate() == null) {
                note.getTimer().cancel();
                holder.updateUI(note.getReplacedNote());
            } else {
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

    /**
     * @param position - indicated position to remove note from list
     */
    public void remove(int position) {
        if(position < 0 || position >= notesList.size()) {
            return;
        }
        notesList.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * Inserting the note into a specified index within the list.
     * @param note - Desired note to insert
     * @param index - Desired index spot of which the note will be inserted
     */
    public void insert(Note note, int index) {
        notesList.add(index, note);
        notifyItemInserted(index);
    }

    public Note getItem(int position) {
        return notesList.get(position);
    }
}
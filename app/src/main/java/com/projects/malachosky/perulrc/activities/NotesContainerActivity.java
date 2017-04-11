package com.projects.malachosky.perulrc.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.projects.malachosky.perulrc.R;
import com.projects.malachosky.perulrc.holders.NoteViewHolder;
import com.projects.malachosky.perulrc.model.Constants;
import com.projects.malachosky.perulrc.model.Note;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class NotesContainerActivity extends AppCompatActivity {

    private final String LOGTAG = NotesContainerActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_container);
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(listener -> {
            Note note = new Note("testing!","This is the body text.");
            Intent intent = new Intent(this, NoteViewActivity.class);
            intent.putExtra(Note.class.getCanonicalName(), note);
            startActivity(intent);
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.container_recycler_view);
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE);
        if(!sharedPreferences.getBoolean("permissions", false)) {
            Log.v(LOGTAG, "The user did not accept the permissions. Hiding the notes.");
            mRecyclerView.setVisibility(View.GONE);
            TextView textView = (TextView)findViewById(R.id.error_permissions);
            textView.setVisibility(View.VISIBLE);
            return;
        }
        try {
            ArrayList<Note> noteList = new NoteFinder().execute().get();
            if(noteList != null) {
                NotesAdapter adapter = new NotesAdapter(noteList);
                mRecyclerView.setAdapter(adapter);
                mRecyclerView.addItemDecoration(new VerticalSpaceItemDecorator(30));
                LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                mRecyclerView.setLayoutManager(layoutManager);
                setUpItemTouchHelper();
            }
        } catch(InterruptedException | ExecutionException ie) {
            Log.v(LOGTAG, "Can't read files because of: " + ie.getCause());
        }
    }

    private void setUpItemTouchHelper() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int swipedPosition = viewHolder.getAdapterPosition();
                NotesAdapter notesAdapter = (NotesAdapter)mRecyclerView.getAdapter();
                notesAdapter.remove(swipedPosition);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    class NotesAdapter extends RecyclerView.Adapter<NoteViewHolder> {

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

            holder.itemView.setOnClickListener(view -> {
                Intent intent = new Intent(view.getContext(), NoteViewActivity.class);
                intent.putExtra(Note.class.getCanonicalName(), note);
                view.getContext().startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return notesList.size();
        }

        public void remove(int position) {
            if(position < 0 || position >= notesList.size()) {
                return;
            }
            notesList.remove(position);
            notifyItemRemoved(position);
        }
    }

    private class NoteFinder extends AsyncTask<Void, Void, ArrayList<Note>> {

        private final String LOGTAG = NoteFinder.class.getSimpleName();

        @Override
        protected ArrayList<Note> doInBackground(Void... voids) {
            File file = new File(Constants.mainFolder);
            if(file.exists()) {
                if(file.isDirectory()) {
                    Log.v(LOGTAG, file.getName() + " is a directory");
                    ArrayList<Note> noteList = new ArrayList<>();
                    for(File noteFile : file.listFiles()) {
                        noteList.add(fileToNote(noteFile));
                    }
                    return noteList;
                }
            }
            return null;
        }

        private Note fileToNote(File noteFile) {
            String bodyText = null;
            String title = null;
            try {
                bodyText = readFile(noteFile);
                title = noteFile.getName().replace(".txt", "");
            } catch(IOException ioe) {
                Log.v(LOGTAG, "Could not read file: " + noteFile.getName());
            }
            return new Note(title, bodyText);
        }

        private String readFile(File file) throws IOException {
            BufferedReader br = new BufferedReader(new FileReader(file));
            try {
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();

                while (line != null) {
                    sb.append(line);
                    sb.append("\n");
                    line = br.readLine();
                }
                return sb.toString();
            } finally {
                br.close();
            }
        }
    }

    private class VerticalSpaceItemDecorator extends RecyclerView.ItemDecoration {
        private final int spacer;

        public VerticalSpaceItemDecorator(int spacer) {
            this.spacer = spacer;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.bottom = spacer;
        }
    }
}
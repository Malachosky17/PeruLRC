package com.projects.malachosky.perulrc.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.projects.malachosky.perulrc.R;
import com.projects.malachosky.perulrc.adapters.NotesAdapter;
import com.projects.malachosky.perulrc.model.Constants;
import com.projects.malachosky.perulrc.model.Note;
import com.projects.malachosky.perulrc.tasks.DataTask;

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
            Note note = new Note("","");
            Intent intent = new Intent(this, NoteViewActivity.class);
            intent.putExtra(Note.class.getCanonicalName(), note);
            startActivity(intent);
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.container_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new VerticalSpaceItemDecorator(30));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        setUpItemTouchHelper();
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
                //TODO: Add the undo note right after the previous note, when swiping multiple they disappear.
                //TODO: If before exists and after exists, insert in the middle. If before no longer exists, add before the next and vice versa.
                NotesAdapter notesAdapter = (NotesAdapter)mRecyclerView.getAdapter();
                Note undoFiller = new Note("Undo");
                undoFiller.setReplacedNote(notesAdapter.getItem(swipedPosition));
                notesAdapter.remove(swipedPosition);
                undoFiller.setTimer(new CountDownTimer(5000, 1000) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        notesAdapter.remove(swipedPosition);
                        undoFiller.getReplacedNote().setAction(Constants.DataAction.DeleteNote);
                        new DataTask().execute(undoFiller.getReplacedNote());
                    }
                });
                undoFiller.getTimer().start();
                notesAdapter.insert(undoFiller, swipedPosition);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
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
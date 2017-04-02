package com.projects.malachosky.perulrc.activities;

import android.content.Intent;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.projects.malachosky.perulrc.R;
import com.projects.malachosky.perulrc.adapters.NotesAdapter;
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
    private RecyclerView recyclerView;


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
        recyclerView = (RecyclerView) findViewById(R.id.container_recycler_view);
        recyclerView.setHasFixedSize(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            ArrayList<Note> noteList = new NoteFinder().execute().get();
            if(noteList != null) {
                NotesAdapter adapter = new NotesAdapter(noteList);
                recyclerView.setAdapter(adapter);
                recyclerView.addItemDecoration(new VerticalSpaceItemDecorator(30));
                LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(layoutManager);
            }
        } catch(InterruptedException | ExecutionException ie) {
            Log.v(LOGTAG, "Can't read files because of: " + ie.getCause());
        }
    }

    class NoteFinder extends AsyncTask<Void, Void, ArrayList<Note>> {

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

    class VerticalSpaceItemDecorator extends RecyclerView.ItemDecoration {
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
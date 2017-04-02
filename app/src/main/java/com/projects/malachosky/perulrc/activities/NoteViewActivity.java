package com.projects.malachosky.perulrc.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.projects.malachosky.perulrc.R;
import com.projects.malachosky.perulrc.model.Constants;
import com.projects.malachosky.perulrc.model.Note;
import com.projects.malachosky.perulrc.tasks.DataTask;

import java.io.File;
import java.util.Locale;

public class NoteViewActivity extends AppCompatActivity implements View.OnClickListener {

    private final String LOGTAG = NoteViewActivity.class.getSimpleName();

    private Note mNote;
    private EditText mEditTextTitle;
    private EditText mBodyText;
    private Button mBtn_Cancel;
    private Button mBtn_Save_Edit;

    private String mInitTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_view);

        Bundle b = getIntent().getExtras();
        mNote = b.getParcelable(Note.class.getCanonicalName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mNote != null) {
            Toast.makeText(this, mNote.getTitle(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "FAILED", Toast.LENGTH_LONG).show();
        }
        mInitTitle = mNote.getFileName();
        initializeTextFields();
        initializeButtons();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btn_cancel:
                    finish();
                break;
            case R.id.btn_edit_save:
                String text = mBtn_Save_Edit.getText().toString();
                boolean edit = false;
                if(text.contentEquals(getString(R.string.btn_save))) {
                    //TODO: Save the Note with DataTask
                    mNote.setTitle(mEditTextTitle.getText().toString());
                    mNote.setBody(mBodyText.getText().toString());
                    mBtn_Save_Edit.setText(getString(R.string.btn_edit));
                    mNote.setAction(Constants.DataAction.SaveNote);
                    renameFile();
                    new DataTask().execute(mNote);
                } else {
                    //TODO: Open fields up for editing
                    mBtn_Cancel.setVisibility(View.VISIBLE);
                    mBtn_Save_Edit.setText(getString(R.string.btn_save));
                    edit = true;
                }
                mBodyText.setEnabled(edit);
                mEditTextTitle.setEnabled(edit);
                break;
        }
    }

    private void renameFile() {
        File from = new File(Constants.mainFolder, mInitTitle);
        if(from.exists()) {
            File to = new File(Constants.mainFolder, mNote.getFileName());
            if(from.renameTo(to)) {
                Log.v(LOGTAG, String.format(Locale.ENGLISH, "Renamed %s to %s", mInitTitle, mNote.getFileName()));
            }
        }
    }

    private void initializeTextFields() {
        mEditTextTitle = (EditText)findViewById(R.id.edit_title);
        mBodyText = (EditText)findViewById(R.id.edit_body);
        mEditTextTitle.setEnabled(false);
        mBodyText.setEnabled(false);
        mEditTextTitle.setText(mNote.getTitle());
        mBodyText.setText(mNote.getBody());
    }

    private void initializeButtons() {
        mBtn_Cancel = (Button)findViewById(R.id.btn_cancel);
        mBtn_Save_Edit = (Button)findViewById(R.id.btn_edit_save);
        mBtn_Cancel.setOnClickListener(this);
        mBtn_Save_Edit.setOnClickListener(this);
    }
}
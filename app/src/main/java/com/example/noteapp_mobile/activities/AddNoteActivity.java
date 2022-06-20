package com.example.noteapp_mobile.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.noteapp_mobile.R;
import com.example.noteapp_mobile.database.MyNoteDatabase;
import com.example.noteapp_mobile.entities.MyNoteEntities;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddNoteActivity extends AppCompatActivity {
    private EditText inputNoteTitle, inputNoteText;
    private TextView textDateTime, saveNote;
    private View indicator1, indicator2;
    String selectedColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        indicator1 = findViewById(R.id.view_indicator);
        indicator2 = findViewById(R.id.view_indicator2);
        saveNote = findViewById(R.id.btn_add_note);

        inputNoteText = findViewById(R.id.txt_note);
        inputNoteTitle = findViewById(R.id.txt_title);
        textDateTime = findViewById(R.id.txt_date_time);

        saveNote.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                saveNotes();
            }
        });

        textDateTime.setText(
                new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault())
                        .format(new Date())
        );

    }
    private void saveNotes() {
        if(inputNoteTitle.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Note title can not be Empty", Toast.LENGTH_SHORT).show();
            return;
        }else
        if(inputNoteText.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Note text can not be Empty", Toast.LENGTH_SHORT).show();
            return;
        }
        final MyNoteEntities myNoteEntities = new MyNoteEntities();
        myNoteEntities.setTitle(inputNoteTitle.getText().toString());
        myNoteEntities.setTitle(inputNoteText.getText().toString());
        myNoteEntities.setTitle(textDateTime.getText().toString());
        myNoteEntities.setTitle(selectedColor);

        class SaveNotes extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids){
                MyNoteDatabase
                        .getMyNoteDatabase(getApplicationContext())
                        .noteDAO()
                        .insertNote(myNoteEntities);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        }
        new SaveNotes().execute();
    }
    private void bottomSheet()
    {
        final LinearLayout linearLayout = findViewById(R.id.bottom_layout);
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(linearLayout);
        linearLayout.findViewById(R.id.bottom_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED)
                {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                else
                {
                    bottomSheetBehavior.setState((BottomSheetBehavior.STATE_EXPANDED));
                }
            }
        });
       // final ImageView
    }
}
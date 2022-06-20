package com.example.noteapp_mobile.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.noteapp_mobile.R;
import com.example.noteapp_mobile.database.MyNoteDatabase;
import com.example.noteapp_mobile.entities.MyNoteEntities;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.InputStream;

public class AddNoteActivity extends AppCompatActivity {

    private EditText inputNoteTitle, inputNoteText;
    private TextView txtDateTime, saveNote;
    private View indicator1, indicator2;
    String selectedColor;

    ImageView addImg;
    private String imagePath;

    public static final int STORAGE_PERMISSION = 1;
    public static final int SELECT_IMG = 1;
    private AlertDialog alertDialog;


    private MyNoteEntities alreadyAvailableNote;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        indicator1 = findViewById(R.id.view_indicator);
        indicator2 = findViewById(R.id.view_indicator2);
//        saveNote = findViewById(R.id.save_note);
//        inputNoteText = findViewById(R.id.input_note_text);
//        inputNoteTitle = findViewById(R.id.input_note_title);
        txtDateTime = findViewById(R.id.txt_date_time);
        addImg = findViewById(R.id.image_note);

        selectedColor = "#FF937B";
        imagePath = "";


        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if(getIntent().getBooleanExtra("updateOrView", false)){
            alreadyAvailableNote = (MyNoteEntities) getIntent().getSerializableExtra("myNotes");
            setViewUpdate();
        }

        //4th-47:00
        findViewById(R.id.img_remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addImg.setImageBitmap(null);
                addImg.setVisibility(View.GONE);
                findViewById(R.id.img_remove).setVisibility(View.GONE);

                imagePath = "";
            }
        });
    }

    private void setViewUpdate() {
        inputNoteTitle.setText(alreadyAvailableNote.getTitle());
        inputNoteText.setText(alreadyAvailableNote.getNoteText());
        //textDateTime.setText(alreadyAvailableNote.getDateTime());
        txtDateTime.setText(alreadyAvailableNote.getDateTime());

        if(alreadyAvailableNote.getImagePath() != null && !alreadyAvailableNote.getImagePath().trim().isEmpty()){
            addImg.setImageBitmap(BitmapFactory.decodeFile(alreadyAvailableNote.getImagePath()));
            addImg.setVisibility(View.VISIBLE);
            findViewById(R.id.img_remove).setVisibility(View.VISIBLE);
            imagePath = alreadyAvailableNote.getImagePath();
        }
    }

    private void bottomSheet(){
        final LinearLayout linearLayout = findViewById(R.id.bottom_layout);
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(linearLayout);
        linearLayout.findViewById(R.id.bottom_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bottomSheetBehavior.getState()!=BottomSheetBehavior.STATE_EXPANDED){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                else{
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });

        final MyNoteEntities myNoteEntities = new MyNoteEntities();
        myNoteEntities.setTitle(inputNoteTitle.getText().toString());
        myNoteEntities.setNoteText(inputNoteText.getText().toString());
        myNoteEntities.setDateTime(txtDateTime.getText().toString());
        myNoteEntities.setColor(selectedColor);
        myNoteEntities.setImagePath(imagePath);

        if(alreadyAvailableNote!=null){
            myNoteEntities.setId(alreadyAvailableNote.getId());
        }

        if(alreadyAvailableNote != null && alreadyAvailableNote.getColor()!=null && !alreadyAvailableNote.getColor().trim().isEmpty() ){
            switch (alreadyAvailableNote.getColor()){
                case "#FFFB7B":
                    linearLayout.findViewById(R.id.view_color2).performClick();
                    break;
                case "#ADFF7B":
                    linearLayout.findViewById(R.id.view_color3).performClick();
                    break;
                case "#96FFEA":
                    linearLayout.findViewById(R.id.view_color4).performClick();
                    break;
                case "#969CFF":
                    linearLayout.findViewById(R.id.view_color5).performClick();
                    break;
                case "#FF96F5":
                    linearLayout.findViewById(R.id.view_color6).performClick();
                    break;
            }
        }

        if(alreadyAvailableNote != null){
            linearLayout.findViewById(R.id.btn_remove).setVisibility(View.VISIBLE);
            linearLayout.findViewById(R.id.btn_remove).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    showDeleteDialog();
                }
            });
        }
    }

    private void showDeleteDialog() {
        if(alertDialog == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(AddNoteActivity.this);
            View view = LayoutInflater.from(this).inflate(R.layout.layout_delete_note,
                    (ViewGroup)findViewById(R.id.layoutDeleteNote_Container));

            builder.setView(view);
            alertDialog = builder.create();

            if(alertDialog.getWindow()!=null){
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

            }

            view.findViewById(R.id.textDeleteNote).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    class DeleteNoteTask extends AsyncTask<Void,Void,Void>{

                        @Override
                        protected Void doInBackground(Void... voids) {
                            MyNoteDatabase.getMyNoteDatabase(getApplicationContext()).noteDAO().deleteNotes(alreadyAvailableNote);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void unused) {
                            super.onPostExecute(unused);

                            Intent intent = new Intent();
                            intent.putExtra("isNoteDeleted", true);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }

                    new DeleteNoteTask().execute();
                }
            });

            view.findViewById(R.id.textCancelNote).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
        }
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==SELECT_IMG && resultCode==RESULT_OK){
            if(data!=null){
                Uri selectImageUri = data.getData();
                if( selectImageUri!=null ){
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(selectImageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        addImg.setImageBitmap(bitmap);
                        addImg.setVisibility(View.VISIBLE);
                        imagePath = getPathFromUri(selectImageUri);
                        findViewById(R.id.img_remove).setVisibility(View.VISIBLE);

                    }
                    catch (Exception exception){
                        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }



    ////////////////////////////

}
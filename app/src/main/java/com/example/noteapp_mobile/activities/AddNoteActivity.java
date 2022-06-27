package com.example.noteapp_mobile.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddNoteActivity extends AppCompatActivity {

    private EditText inputNoteTitle, inputNoteText;
    private TextView txtDateTime,saveNote;
    private View indicator1, indicator2;
    public String selectedColor;
    public ImageView addImg;
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
        saveNote = findViewById(R.id.save_note);
        inputNoteText = findViewById(R.id.txt_note);
        inputNoteTitle = findViewById(R.id.txt_title);
        txtDateTime = findViewById(R.id.txt_date_time);
        addImg = findViewById(R.id.image_note);

        selectedColor = "#FF937B";
        imagePath = "";

        if(getIntent().getBooleanExtra("updateOrView", false)){
            alreadyAvailableNote = (MyNoteEntities) getIntent().getSerializableExtra("myNotes");
            setViewUpdate();
        }

        saveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNotes();
            }
        });

        txtDateTime.setText(
                new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault())
                        .format(new Date())
        );
        bottomSheet();
        setViewColor();
        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



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

    private void setViewColor(){
        GradientDrawable gradientDrawable = (GradientDrawable) indicator1.getBackground();
        gradientDrawable.setColor(Color.parseColor(selectedColor));

        GradientDrawable gradientDrawable2 = (GradientDrawable) indicator2.getBackground();
        gradientDrawable2.setColor(Color.parseColor(selectedColor));
    }

//    private void updateNotes(){
//        if(inputNoteTitle.getText().toString().trim().isEmpty()){
//            Toast.makeText(this, "Note Title Can't Be Empty", Toast.LENGTH_LONG).show();
//            return;
//        }else
//        if(inputNoteText.getText().toString().trim().isEmpty()){
//            Toast.makeText(this, "Note Text Can't Be Empty", Toast.LENGTH_LONG).show();
//            return;
//        }
//
//        final MyNoteEntities myNoteEntities = new MyNoteEntities();
//        myNoteEntities.setTitle(inputNoteTitle.getText().toString());
//        myNoteEntities.setNoteText(inputNoteText.getText().toString());
//        myNoteEntities.setDateTime(txtDateTime.getText().toString());
//        myNoteEntities.setColor(selectedColor);
//
//        class UpdateNotes extends AsyncTask<Void, Void, Void>{
//            @Override
//            protected Void doInBackground(Void... voids){
//                MyNoteDatabase.getMyNoteDatabase(getApplicationContext())
//                        .noteDAO()
//                        .updateNotes(myNoteEntities);
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void avoid)
//            {
//                super.onPostExecute(avoid);
//                Intent intent  = new Intent();
//                setResult(RESULT_OK, intent);
//                finish();
//            }
//        }
//        new UpdateNotes().execute();
//
//
//    }

    private void saveNotes() {
        if(inputNoteTitle.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Note Title Can't Be Empty", Toast.LENGTH_LONG).show();
            return;
        }else
        if(inputNoteText.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Note Text Can't Be Empty", Toast.LENGTH_LONG).show();
            return;
        }

        final MyNoteEntities myNoteEntities = new MyNoteEntities();
        myNoteEntities.setTitle(inputNoteTitle.getText().toString());
        myNoteEntities.setNoteText(inputNoteText.getText().toString());
        myNoteEntities.setDateTime(txtDateTime.getText().toString());
        myNoteEntities.setColor(selectedColor);

        if(alreadyAvailableNote!=null){
            myNoteEntities.setId(alreadyAvailableNote.getId());
        }

        class SaveNotes extends AsyncTask<Void, Void, Void>{
            @Override
            protected Void doInBackground(Void... voids){
                MyNoteDatabase.getMyNoteDatabase(getApplicationContext())
                        .noteDAO()
                        .insertNote(myNoteEntities);
                return null;
            }

            @Override
            protected void onPostExecute(Void avoid)
            {
                super.onPostExecute(avoid);
                Intent intent  = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        }
        new SaveNotes().execute();


    }
    private void setViewUpdate() {
        inputNoteTitle.setText(alreadyAvailableNote.getTitle());
        inputNoteText.setText(alreadyAvailableNote.getNoteText());
        //txtDateTime.setText(alreadyAvailableNote.getDateTime());
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

        final ImageView imgColor1 = linearLayout.findViewById(R.id.img_color1);
        final ImageView imgColor2 = linearLayout.findViewById(R.id.img_color2);
        final ImageView imgColor3 = linearLayout.findViewById(R.id.img_color3);
        final ImageView imgColor4 = linearLayout.findViewById(R.id.img_color4);
        final ImageView imgColor5 = linearLayout.findViewById(R.id.img_color5);
        final ImageView imgColor6 = linearLayout.findViewById(R.id.img_color6);

        final MyNoteEntities myNoteEntities = new MyNoteEntities();
        myNoteEntities.setTitle(inputNoteTitle.getText().toString());
        myNoteEntities.setNoteText(inputNoteText.getText().toString());
        myNoteEntities.setDateTime(txtDateTime.getText().toString());
        myNoteEntities.setColor(selectedColor);
        myNoteEntities.setImagePath(imagePath);

//        if(alreadyAvailableNote!=null){
//            myNoteEntities.setId(alreadyAvailableNote.getId());
//        }

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
               //         imagePath = getPathFromUri(selectImageUri);
                        findViewById(R.id.img_remove).setVisibility(View.VISIBLE);

                    }
                    catch (Exception exception){
                        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
}
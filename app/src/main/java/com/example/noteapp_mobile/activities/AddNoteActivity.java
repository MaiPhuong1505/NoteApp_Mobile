package com.example.noteapp_mobile.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.noteapp_mobile.R;
import com.example.noteapp_mobile.entities.MyNoteEntities;

import java.io.InputStream;
import java.io.InputStreamReader;

public class AddNoteActivity extends AppCompatActivity {

    private EditText inputNoteTitle, inputNoteText;
    private TextView txtDateTime, saveNote;
    private View indicator1, indicator2;
    String selectedColor;

    ImageView addImg;
    private String imagePath;

    public static final int STORAGE_PERMISSION = 1;
    public static final int SELECT_IMG = 1;


    private MyNoteEntities alreadyAvailableNote;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);



        if(getIntent().getBooleanExtra("updateOrView", false)){
            alreadyAvailableNote = (MyNoteEntities) getIntent().getSerializableExtra("myNotes");
            setViewUpdate();
        }

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
        textDateTime.setText(alreadyAvailableNote.getDateTime());

        if(alreadyAvailableNote.getImagePath() != null && !alreadyAvailableNote.getImagePath().trim().isEmpty()){
            addImg.setImageBitmap(BitmapFactory.decodeFile(alreadyAvailableNote.getImagePath()));
            addImg.setVisibility(View.VISIBLE);
            findViewById(R.id.img_remove).setVisibility(View.VISIBLE);
            imagePath = alreadyAvailableNote.getImagePath();
        }
    }

    private void bottomSheet(){
        if(alreadyAvailableNote!=null){
            myNoteEntities.setId(alreadyAvailableNote.getId());
        }

        if(alreadyAvailableNote != null && alreadyAvailableNote.getColor()!=null && !alreadyAvailableNote.getColor().trim().isEmpty() ){
            switch (alreadyAvailableNote.getColor()){
                case "#FFFB7B":
                    linearLayout.findViewById(R.id.view_color2).performCick();
                    break;
                case "#ADFF7B":
                    linearLayout.findViewById(R.id.view_color3).performCick();
                    break;
                case "#96FFEA":
                    linearLayout.findViewById(R.id.view_color4).performCick();
                    break;
                case "#969CFF":
                    linearLayout.findViewById(R.id.view_color5).performCick();
                    break;
                case "#FF96F5":
                    linearLayout.findViewById(R.id.view_color6).performCick();
                    break;
            }
        }
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
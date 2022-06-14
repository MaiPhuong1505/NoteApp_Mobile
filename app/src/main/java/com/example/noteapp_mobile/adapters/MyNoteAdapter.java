package com.example.noteapp_mobile.adapters;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteapp_mobile.R;
import com.example.noteapp_mobile.entities.MyNoteEntities;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;
import com.example.noteapp_mobile.listeners.MyNoteListeners;
public class MyNoteAdapter extends RecyclerView.Adapter<MyNoteAdapter.ViewHolder> {

    List<MyNoteEntities> noteEntitiesList;
    MyNoteListeners myNoteListener;


//    public MyNoteAdapter(List<MyNoteEntities> noteEntitiesList) {
//        this.noteEntitiesList = noteEntitiesList;
//    }

    public MyNoteAdapter(List<MyNoteEntities> noteEntitiesList, MyNoteListeners myNoteListener) {
        this.noteEntitiesList = noteEntitiesList;
        this.myNoteListener = myNoteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setNote(noteEntitiesList.get(position));
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myNoteListener.myNoteClick(noteEntitiesList.get(position), position);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return noteEntitiesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtItemTitle, txtItemNote, txtItemDate;
        private LinearLayout linearLayout;
        RoundedImageView roundedImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtItemTitle = itemView.findViewById(R.id.txt_item_title);
            txtItemNote = itemView.findViewById(R.id.txt_item_note);
            txtItemDate = itemView.findViewById(R.id.txt_item_date);
            linearLayout = itemView.findViewById(R.id.layout_note);
            roundedImageView = itemView.findViewById(R.id.image_note);

        }

        public void setNote(MyNoteEntities myNoteEntities) {
            txtItemTitle.setText(myNoteEntities.getTitle());
            txtItemNote.setText(myNoteEntities.getNoteText());
            txtItemDate.setText(myNoteEntities.getDateTime());

            GradientDrawable gradientDrawable = (GradientDrawable) linearLayout.getBackground();
            if (myNoteEntities.getColor() != null){
                gradientDrawable.setColor(Color.parseColor(myNoteEntities.getColor()));
            }
            else{
                gradientDrawable.setColor(Color.parseColor("#FF937B"));
            }
        }
    }
}

package com.example.noteapp_mobile.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.noteapp_mobile.R;
import com.example.noteapp_mobile.activities.AddNoteActivity;
import com.example.noteapp_mobile.adapters.MyNoteAdapter;
import com.example.noteapp_mobile.database.MyNoteDatabase;
import com.example.noteapp_mobile.entities.MyNoteEntities;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import com.example.noteapp_mobile.listeners.MyNoteListeners;

public class MyNoteFragment extends Fragment implements MyNoteListeners{

    FloatingActionButton addNote;

    public static final int REQUEST_CODE_AND_NOTE = 1;
    public static final int UPDATE_NOTE = 2;
    public static final int SHOW_NOTE = 3;
    private int clickedPosition = -1;

    private RecyclerView noteRec;
    private List<MyNoteEntities> noteEntitiesList;
    private MyNoteAdapter myNoteAdapter;

    public MyNoteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_note, container, false);
        addNote = view.findViewById(R.id.btn_add_note);
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        noteRec = view.findViewById(R.id.note_list);
        noteRec.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        noteEntitiesList = new ArrayList<>();
        myNoteAdapter = new MyNoteAdapter(noteEntitiesList, this);
        noteRec.setAdapter(myNoteAdapter);

        getAllNotes(SHOW_NOTE);

        return view;
    }

    private void getAllNotes(final int requestCode) {
        @SuppressLint("StaticFieldLeak")
        class GetNoteTask extends AsyncTask<Void, Void, List<MyNoteEntities>>{
            @Override
            protected List<MyNoteEntities> doInBackground(Void... voids) {
                return MyNoteDatabase
                        .getMyNoteDatabase(getActivity().getApplicationContext())
                        .noteDAO()
                        .getAllNotes();
            }

            @Override
            protected void onPostExecute(List<MyNoteEntities> myNoteEntities) {
                super.onPostExecute(myNoteEntities);

                if(requestCode == SHOW_NOTE){
                    noteEntitiesList.addAll(myNoteEntities);
                    myNoteAdapter.notifyDataSetChanged();
                }
                else if(requestCode == REQUEST_CODE_AND_NOTE){
                    noteEntitiesList.add(0, myNoteEntities.get(0));
                    myNoteAdapter.notifyItemInserted(0);
                    noteRec.smoothScrollToPosition(0);
                }
                else if(requestCode == UPDATE_NOTE){
                    noteEntitiesList.remove(clickedPosition);
                    noteEntitiesList.add(clickedPosition, myNoteEntities.get(clickedPosition));
                    myNoteAdapter.notifyItemChanged(clickedPosition);
                }
            }
        }
        new GetNoteTask().execute();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_AND_NOTE && resultCode == RESULT_OK){
            getAllNotes(REQUEST_CODE_AND_NOTE);
        }
        else if(requestCode == UPDATE_NOTE && resultCode == RESULT_OK){
            if(data!=null){
                getAllNotes(UPDATE_NOTE);
            }
        }
    }

    @Override
    public void myNoteClick(MyNoteEntities myNoteEntities, int position) {
        clickedPosition = position;
        Intent intent = new Intent(getContext().getApplicationContext(), AddNoteActivity.class);
        intent.putExtra("updateOrView", true);
        intent.putExtra("myNotes", myNoteEntities);
        startActivityForResult(intent, UPDATE_NOTE);
    }
}
package com.example.noteapp_mobile.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

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

public class MyNoteFragment extends Fragment {

    FloatingActionButton addNote;
    public static final int REQUEST_CODE_ADD_NOTE =1;
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
                startActivityForResult(new Intent(getContext(), AddNoteActivity.class),REQUEST_CODE_ADD_NOTE);
            }
        });

       /* noteRec = view.findViewById(R.id.note_list);
        noteRec.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        noteEntitiesList = new ArrayList<>();
        myNoteAdapter = new MyNoteAdapter(noteEntitiesList);
        noteRec.setAdapter(myNoteAdapter);*/

      //  getAllNotes();

        return view;
    }

    /*private void getAllNotes() {
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
                if (noteEntitiesList.size() == 0){
                    noteEntitiesList.addAll(myNoteEntities);
                    myNoteAdapter.notifyDataSetChanged();
                }else{
                    noteEntitiesList.add(0, myNoteEntities.get(0));
                    myNoteAdapter.notifyItemChanged(0);
                }
                noteRec.smoothScrollToPosition(0);
            }
        }
        new GetNoteTask().execute();
    }*/
}
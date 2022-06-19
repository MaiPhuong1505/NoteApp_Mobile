package com.example.noteapp_mobile.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.noteapp_mobile.entities.MyNoteEntities;

import java.util.List;

@Dao
public interface MyNoteDAO {

    @Query("SELECT * FROM note_db ORDER BY id DESC")
    List<MyNoteEntities> getAllNotes();


}

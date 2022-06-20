package com.example.noteapp_mobile.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.noteapp_mobile.entities.MyNoteEntities;

import java.util.List;

@Dao
public interface MyNoteDAO {

    @Query("SELECT * FROM note ORDER BY id DESC")
    List<MyNoteEntities> getAllNotes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNote(MyNoteEntities noteEntities);


}

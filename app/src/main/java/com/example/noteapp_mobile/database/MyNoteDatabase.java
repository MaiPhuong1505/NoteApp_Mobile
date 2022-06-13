package com.example.noteapp_mobile.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.noteapp_mobile.dao.MyNoteDAO;
import com.example.noteapp_mobile.entities.MyNoteEntities;

@Database(entities = MyNoteEntities.class, version = 1, exportSchema = false)
public abstract class MyNoteDatabase extends RoomDatabase {
    private static MyNoteDatabase myNoteDatabase;
    public static synchronized MyNoteDatabase getMyNoteDatabase(Context context){
        if (myNoteDatabase == null){
            myNoteDatabase = Room.databaseBuilder(
                    context,
                    MyNoteDatabase.class,
                    "note_db"
            ).build();
        }
        return myNoteDatabase;
    }
    public abstract MyNoteDAO noteDAO();
}

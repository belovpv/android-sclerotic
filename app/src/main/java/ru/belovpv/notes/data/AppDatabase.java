package ru.belovpv.notes.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by pavel-adm on 16.08.2018.
 */

@Database(entities = {Category.class, Note.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
  public abstract CategoryDao categoryDao();
  public abstract NoteDao noteDao();
}
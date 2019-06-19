package ru.belovpv.notes.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface NoteDao {

  @Insert
  long insert(Note note);

  @Update
  void update(Note[] notes);

  @Delete
  void delete(Note note);

  @Query("SELECT * FROM note WHERE categoryId = :categoryId ORDER BY id ASC")
  LiveData<List<Note>> getNotesByCategory(int categoryId);
}

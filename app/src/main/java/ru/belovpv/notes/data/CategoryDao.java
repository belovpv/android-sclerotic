package ru.belovpv.notes.data;

import java.util.List;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

@Dao
public interface CategoryDao {

  @Insert
  long insert(Category category);

  @Delete
  void delete(Category category);

  @Query("SELECT *, (select count(1) from note where note.categoryId=category.id and note.isActive) as activeNotes FROM category ORDER BY name ASC")
  LiveData<List<Category>> getAllCategories();
}

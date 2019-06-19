package ru.belovpv.notes.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;

/**
 * Created by pavel-adm on 16.08.2018.
 */
@Entity(tableName = "note",
    foreignKeys = @ForeignKey(entity = Category.class,
    parentColumns = "id",
    childColumns = "categoryId",
    onDelete = ForeignKey.CASCADE),
    indices = {@Index(value={"categoryId", "text"},unique=true)})
public class Note {

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "id")
  private long _id;

  @ColumnInfo(name = "text")
  private String _text;

  @ColumnInfo(name = "categoryId")
  private long _categoryId;

  @ColumnInfo(name = "isActive")
  private boolean _isActive = true;

  public long getId() {
    return _id;
  }
  public void setId(long id) {
    _id = id;
  }

  public String getText() {
    return _text;
  }
  public void setText(String text) {
    if (_text != null && !_text.equals(text)) {
      _isDirty = true;
    }
    _text = text;
  }

  public long getCategoryId() {
    return _categoryId;
  }
  public void setCategoryId(long categoryId) {
    if (_categoryId != categoryId) {
      _isDirty = true;
    }
    _categoryId = categoryId;
  }

  public boolean isActive() {
    return _isActive;
  }
  public void setIsActive(boolean isActive) {
    if (_isActive != isActive) {
      _isDirty = true;
    }
    _isActive = isActive;
  }

  @Ignore
  private boolean _isDirty = false;
  public boolean isDirty() {
    return _isDirty;
  }
  public void reset() {
    _isDirty = false;
  }

  @Override
  public String toString() {
    return _text;
  }
}

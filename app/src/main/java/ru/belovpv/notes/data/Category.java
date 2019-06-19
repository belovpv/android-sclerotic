package ru.belovpv.notes.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.Nullable;

import java.io.Serializable;

/**
 * Created by pavel-adm on 16.08.2018.
 */
@Entity(tableName = "category", indices={@Index(value={"name"}, unique=true)})
public class Category implements Serializable {

  @PrimaryKey(autoGenerate = true)
  public int id;

  public String name;

  @Nullable
  public int activeNotes;

  @Override
  public String toString() {
    return name;
  }
}

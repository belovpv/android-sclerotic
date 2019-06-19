package ru.belovpv.notes;

import android.app.Application;
import android.arch.persistence.room.Room;

import ru.belovpv.notes.data.AppDatabase;

public class NotesApplication extends Application {

  private static AppDatabase _db;

  @Override
  public void onCreate() {
    super.onCreate();
    _db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, Config.dbName).build();
  }

  public static AppDatabase getDb(){
    return _db;
  }
}

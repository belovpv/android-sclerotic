package ru.belovpv.notes;

import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ru.belovpv.notes.Utility.Task;
import ru.belovpv.notes.data.AppDatabase;
import ru.belovpv.notes.data.Category;
import ru.belovpv.notes.data.Note;
import ru.belovpv.notes.view.NotesAdapter;

public class NotesActivity extends AppCompatActivity {

  Category category;
  ListView listNotes;
  NotesAdapter adpNotes;
  private final static String CATEGORY_PARAM = "category";
  final static int REQUEST_INSERT = 1;

  public static Intent getIntent(Context context, Category category){
    Intent intent = new Intent(context, NotesActivity.class);
    Bundle b = new Bundle();
    b.putSerializable(CATEGORY_PARAM, category);
    intent.putExtras(b);
    return intent;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_notes);

    FloatingActionButton fab = findViewById(R.id.cmdAddNote);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        addNote();
      }
    });

    adpNotes = new NotesAdapter(this);
    listNotes = findViewById(R.id.listNotes);
    listNotes.setAdapter(adpNotes);

    Bundle b = getIntent().getExtras();
    category =  null;
    if(b != null)
      category = (Category)b.getSerializable(CATEGORY_PARAM);

    updateNotes(category.id);
  }

  @Override
  protected void onPause() {
    super.onPause();
    ArrayList<Note> notes = new ArrayList<>();
    for(int i=0; i < adpNotes.getCount(); i++) {
      Note note = adpNotes.getItem(i);
      if (note.isDirty()) {
        notes.add(note);
      }
    }
    if (!notes.isEmpty()) {
      final Note[] noteArray = new Note[notes.size()];
      notes.toArray(noteArray);
      new Task(new Task.OperationCallback() {
          @Override
          public Long execute() {
            NotesApplication.getDb().noteDao().update(noteArray);
            return new Long(noteArray.length);
          }
        },
        new Task.CompleteCallback() {
          @Override
          public void success(long id) {
          }

          @Override
          public void error(String msg) {
          }
        }
      ).execute();
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch(requestCode) {
      case REQUEST_INSERT:
        updateNotes(category.id);
        break;
    }
  }

  void addNote() {
    Intent intent = AddNoteActivity.getIntent(this, category);
    startActivityForResult(intent, REQUEST_INSERT);
  }

  void updateNotes(int categoryId){
    NotesApplication.getDb().noteDao().getNotesByCategory(categoryId).observe(this, new Observer<List<Note>>() {
      @Override
      public void onChanged(@Nullable List<Note> notes) {
        adpNotes.clear();
        adpNotes.addAll(notes);
      }
    });
  }
}

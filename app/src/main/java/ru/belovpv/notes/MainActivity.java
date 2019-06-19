package ru.belovpv.notes;

import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.List;

import ru.belovpv.notes.Utility.Task;
import ru.belovpv.notes.data.AppDatabase;
import ru.belovpv.notes.data.Category;
import ru.belovpv.notes.view.CategoriesAdapter;

public class MainActivity extends AppCompatActivity {

  AppDatabase db;
  ListView listCategory;
  CategoriesAdapter adpCategory;
  final static int REQUEST_INSERT = 1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    FloatingActionButton fab = findViewById(R.id.cmdAddNote);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        MainActivity.this.addNote();
      }
    });

    adpCategory = new CategoriesAdapter(this, new CategoriesAdapter.OnListListener() {
      @Override
      public void onItemClick(Category category) {
        onSelectCategory(category);
      }
      @Override
      public void onItemDeleteClick(Category category) {
        MainActivity.this.onCategoryDeleteRequest(category);
      }
    });
    listCategory = findViewById(R.id.listCategory);
    listCategory.setAdapter(adpCategory);

    db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, Config.dbName).build();
    updateCategories();
  }

  private void onSelectCategory(Category category) {
    startActivity(NotesActivity.getIntent(this, category));
  }

  void deleteCategory(final Category category){
    new Task(new Task.OperationCallback() {
      @Override
      public Long execute() {
        NotesApplication.getDb().categoryDao().delete(category);
        return 1L;
      }
    }, new Task.CompleteCallback() {
      @Override
      public void success(long id) {
        ((CategoriesAdapter)listCategory.getAdapter()).notifyDataSetChanged();
      }

      @Override
      public void error(String msg) {
      }
    }).execute();
  }

  void updateCategories(){
    NotesApplication.getDb().categoryDao().getAllCategories().observe(this, new Observer<List<Category>>() {
      @Override
      public void onChanged(@Nullable List<Category> categories) {
        adpCategory.clear();
        adpCategory.addAll(categories);
      }
    });
  }

  void onCategoryDeleteRequest(final Category category){
    AlertDialog dialog = new AlertDialog.Builder(this).create();
    dialog.setTitle(getResources().getString(R.string.deleting));
    dialog.setMessage(getResources().getString(R.string.ensure_deleting));
    dialog.setCancelable(false);
    dialog.setButton(DialogInterface.BUTTON_POSITIVE,  getResources().getString(R.string.ensure),
        new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int buttonId) {
            MainActivity.this.deleteCategory(category);
          }
        });
    dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.cancel),
        new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int buttonId) {
          }
        });
    dialog.setIcon(android.R.drawable.ic_dialog_alert);
    dialog.show();
  }

  void addNote() {
    Intent intent = AddNoteActivity.getIntent(this, null);
    startActivityForResult(intent, REQUEST_INSERT);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch(requestCode) {
      case REQUEST_INSERT:
        updateCategories();
        break;
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }
}

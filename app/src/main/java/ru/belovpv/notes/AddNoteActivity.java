package ru.belovpv.notes;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import java.util.List;

import ru.belovpv.notes.Utility.Task;
import ru.belovpv.notes.data.Category;
import ru.belovpv.notes.data.Note;

public class AddNoteActivity extends AppCompatActivity {

  Category category;
  TextView txtError;
  AutoCompleteTextView tvCategory;
  TextInputEditText txt;
  ArrayAdapter<Category> adpCategory;
  private final static String CATEGORY_PARAM = "category";

  public static Intent getIntent(Context context, Category category){
    Intent intent = new Intent(context, AddNoteActivity.class);
    Bundle b = new Bundle();
    b.putSerializable(CATEGORY_PARAM, category);
    intent.putExtras(b);
    return intent;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_note);

    Bundle b = getIntent().getExtras();
    category =  null;
    if(b != null) {
      category = (Category) b.getSerializable(CATEGORY_PARAM);
    }

    adpCategory = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
    tvCategory = findViewById(R.id.tvCategory);
    if (category != null) {
      tvCategory.setText(category.name);
      tvCategory.dismissDropDown();
      tvCategory.setEnabled(false);
    }

    txtError = findViewById(R.id.txtError);
    txt = findViewById(R.id.txt);
    tvCategory.setAdapter(adpCategory);
    tvCategory.setOnDismissListener(new AutoCompleteTextView.OnDismissListener() {
      @Override
      public void onDismiss() {
        //AddNoteActivity.this.addNewCategory(tvCategory.getText().toString());
      }
    });

    NotesApplication.getDb().categoryDao().getAllCategories().observe(this, new Observer<List<Category>>() {
      @Override
      public void onChanged(@Nullable List<Category> categories) {
        adpCategory.clear();
        adpCategory.addAll(categories);
      }
    });
  }

  void addNewCategory(final String categoryTitle, final Task.CompleteCallback callback) {
    final Category cat = new Category();
    cat.name = categoryTitle;
    new Task(new Task.OperationCallback() {
      @Override
      public Long execute() {
        return NotesApplication.getDb().categoryDao().insert(cat);
      }
    }, new Task.CompleteCallback() {
      @Override
      public void success(long id) {
        callback.success(id);
      }

      @Override
      public void error(String msg) {
        setError(msg);
      }
    }).execute();
  }

  void addNote(long categoryId, String text) {
    final Note note = new Note();
    note.setCategoryId(categoryId);
    note.setText(text);

    new Task(new Task.OperationCallback() {
      @Override
      public Long execute() {
        return NotesApplication.getDb().noteDao().insert(note);
      }
    }, new Task.CompleteCallback() {
      @Override
      public void success(long id) {
        AddNoteActivity.this.finish();
      }

      @Override
      public void error(String msg) {
        setError(msg);
      }
    }).execute();
  }

  boolean save() {
    long categoryId;
    if (category != null) {
      //this activity was called from category activity
      addNote(category.id, txt.getText().toString());
    }
    else if (tvCategory.isPerformingCompletion()){
      //Существующая категория
      int pos = tvCategory.getListSelection();
      categoryId = adpCategory.getItem(pos).id;
      addNote(categoryId, txt.getText().toString());
    } else {
      //Новая категория
      addNewCategory(tvCategory.getText().toString(), new Task.CompleteCallback() {
        @Override
        public void success(long categotyId) {
          addNote(categotyId, txt.getText().toString());
        }

        @Override
        public void error(String msg) {
          setError(msg);
        }
      });
    }
    return true;
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu_add_note, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle presses on the action bar items
    switch (item.getItemId()) {
      case R.id.action_save:
        return save();
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  void setError(String msg) {
    txtError.setText(msg);
  }
}

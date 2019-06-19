package ru.belovpv.notes.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import ru.belovpv.notes.NotesApplication;
import ru.belovpv.notes.R;
import ru.belovpv.notes.Utility.Task;
import ru.belovpv.notes.data.Category;

public class CategoriesAdapter extends ArrayAdapter<Category> {
  public interface OnListListener {
    void onItemClick(Category category);
    void onItemDeleteClick(Category category);
  }

  OnListListener listener;

  public CategoriesAdapter(Context context, OnListListener listener) {
    super(context, 0);
    this.listener = listener;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    final Category category = getItem(position);
    // Check if an existing view is being reused, otherwise inflate the view
    if (convertView == null) {
      convertView = LayoutInflater.from(getContext()).inflate(R.layout.note_row, parent, false);
    }

    final TextView txtNoteText = convertView.findViewById(R.id.txtNoteText);
    final CheckBox chkNoteActive = convertView.findViewById(R.id.chkNoteActive);
    final ImageButton cmdDelete = convertView.findViewById(R.id.cmdDelete);
    final View vRow = convertView.findViewById(R.id.row);
    vRow.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        CategoriesAdapter.this.listener.onItemClick(category);
      }
    });

    txtNoteText.setText(category.name);
    chkNoteActive.setChecked(category.activeNotes > 0);
    cmdDelete.setTag(position);
    cmdDelete.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        deleteItem(v);
      }
    });
    return convertView;
  }

  private void deleteItem(View v){
    int position= (Integer)v.getTag();
    final Category item = getItem(position);
    this.listener.onItemDeleteClick(item);
  }
}

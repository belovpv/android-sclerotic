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
import ru.belovpv.notes.Utility.Task;
import ru.belovpv.notes.R;
import ru.belovpv.notes.data.Note;

public class NotesAdapter extends ArrayAdapter<Note> implements View.OnClickListener {

  public NotesAdapter(Context context) {
    super(context, 0);
  }

  @Override
  public void onClick(View v) {

    int position= (Integer)v.getTag();
    Note note = getItem(position);

    switch (v.getId())
    {
      case R.id.cmdDelete:
        deleteNote(note);
        break;
    }
  }

  public void reset() {
    for (int i = 0; i < this.getCount(); i++) {
      Note note = getItem(i);
      note.reset();
    }
  }

  private void deleteNote(final Note note) {
    new Task(new Task.OperationCallback() {
      @Override
      public Long execute() {
        NotesApplication.getDb().noteDao().delete(note);
        return 1L;
      }
    }, new Task.CompleteCallback() {
      @Override
      public void success(long id) {
        NotesAdapter.this.notifyDataSetChanged();
      }

      @Override
      public void error(String msg) {
      }
    }).execute();
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    final Note note = getItem(position);
    // Check if an existing view is being reused, otherwise inflate the view
    if (convertView == null) {
      convertView = LayoutInflater.from(getContext()).inflate(R.layout.note_row, parent, false);
    }

    TextView txtNoteText = convertView.findViewById(R.id.txtNoteText);
    final CheckBox chkNoteActive = convertView.findViewById(R.id.chkNoteActive);
    ImageButton cmdDelete = convertView.findViewById(R.id.cmdDelete);

    txtNoteText.setText(note.getText());
    chkNoteActive.setChecked(note.isActive());
    cmdDelete.setOnClickListener(this);
    cmdDelete.setTag(position);
    chkNoteActive.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        note.setIsActive(chkNoteActive.isChecked());
      }
    });
    note.reset();
    return convertView;
  }
}

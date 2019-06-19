package ru.belovpv.notes.Utility;

import android.os.AsyncTask;

public class Task extends AsyncTask<Void, Void, Long> {

  //Calls on complete task
  public interface CompleteCallback {
    void success(long id);
    void error(String msg);
  }

  //Task body
  public interface OperationCallback {
    Long execute();
  }

  private CompleteCallback completeCallback;
  private OperationCallback doCallback;

  public Task(OperationCallback doCallback, CompleteCallback completeCallback) {
    this.completeCallback = completeCallback;
    this.doCallback = doCallback;
  }

  @Override
  protected Long doInBackground(Void... voids) {
    return this.doCallback.execute();
  }

  @Override
  protected void onPostExecute(Long id){
    completeCallback.success(id);
  }
}

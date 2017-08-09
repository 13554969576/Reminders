package com.yanxit.reminders;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class RemindersActivity extends AppCompatActivity {

    private ListView mListView;
    private DbAdapter dbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);
        mListView = (ListView)findViewById(R.id.reminders_list_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.deleteDatabase("reminders");
        dbAdapter = new DbAdapter(this);
        dbAdapter.open();
        mListView.setAdapter(dbAdapter.getRemidersAsAdapter());
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemView, int position, long id) {
                //Toast.makeText(RemindersActivity.this,String.format("Clicked %d", position ),Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(RemindersActivity.this);
                ListView modelViews = new ListView(RemindersActivity.this);
                String[] models = new String[]{"编辑","删除"};
                ArrayAdapter<String> modelAdapter = new ArrayAdapter<>(RemindersActivity.this, android.R.layout.simple_list_item_1,android.R.id.text1,models);
                modelViews.setAdapter(modelAdapter);
                builder.setView(modelViews);
                final Reminder reminder = (Reminder)itemView.getTag();
                final Dialog dialog = builder.create();
                dialog.show();
                modelViews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if(position==0){
                            openEditor(reminder);
                        } else if(position==1){
                            dbAdapter.deleteReminder(reminder.getId());
                        }
                        dialog.dismiss();
                    }
                });
            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        });
        setSupportActionBar(toolbar);
    }



    private void openEditor(final Reminder reminder){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.reminders_edtitor);
        TextView titleView = (TextView)dialog.findViewById(R.id.editor_title);
        final EditText contentText = (EditText)dialog.findViewById(R.id.edtor_content);
        Button submitBtn = (Button)dialog.findViewById(R.id.editor_submit);
        final CheckBox importantChk = (CheckBox)dialog.findViewById(R.id.edtor_important);
        LinearLayout root = (LinearLayout)dialog.findViewById(R.id.reminder_editor);
        final boolean isEdit = reminder!=null;
        if(isEdit){
            titleView.setText("修改备注");
            importantChk.setChecked(reminder.getImportant()==1);
            contentText.setText(reminder.getContent());
            root.setBackgroundColor(getResources().getColor(R.color.blue,this.getTheme()));
        }
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = contentText.getText().toString();
                if(isEdit){
                    Reminder tmpReminder = new Reminder(reminder.getId(),content,importantChk.isChecked()?1:0);
                    dbAdapter.updateReminder(tmpReminder);
                } else {
                    dbAdapter.createReminder(content,importantChk.isChecked());
                }
                ((RemindersCursorAdapter) mListView.getAdapter()).changeCursor(dbAdapter.findAllAsCursor());
                dialog.dismiss();
            }
        });
        Button cancelBtn = (Button)dialog.findViewById(R.id.editor_cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        if(dbAdapter!=null)dbAdapter.close();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_reminders, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_new) {
            Log.i(getLocalClassName(),"new reminder");
            openEditor(null);
            return true;
        } else if(id == R.id.action_exit) {
            finish();
            return true;
        } else {
            return false;
        }
    }
}

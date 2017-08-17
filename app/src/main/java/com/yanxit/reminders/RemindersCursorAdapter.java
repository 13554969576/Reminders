package com.yanxit.reminders;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * Created by yanxit on 8/8/2017.
 */

public class RemindersCursorAdapter extends SimpleCursorAdapter {


    public RemindersCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return super.newView(context, cursor, parent);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);
        View listTab = view.findViewById(R.id.row_tab);
        Reminder reminder = (Reminder)view.getTag();
        int id = cursor.getInt(DbAdapter.INDEX_ID);
        if(reminder==null || id!=reminder.getId()){
            reminder = new Reminder(id,cursor.getString(DbAdapter.INDEX_CONTENT),cursor.getInt(DbAdapter.INDEX_IMPORTANT));
            view.setTag(reminder);
            Log.i(this.getClass().getName(),String.format("the reminder %s is bind to the view %s ",
                    reminder.toString(),view.toString()));
        }

        if(reminder.getImportant()==1){
            listTab.setBackgroundColor(context.getResources().getColor(R.color.orange,context.getTheme()));
        } else {
            listTab.setBackgroundColor(context.getResources().getColor(R.color.green,context.getTheme()));
        }
    }



}

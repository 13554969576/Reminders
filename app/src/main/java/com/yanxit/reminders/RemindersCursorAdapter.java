package com.yanxit.reminders;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;

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
        if(reminder==null){
            reminder = new Reminder(cursor.getInt(DbAdapter.INDEX_ID),cursor.getString(DbAdapter.INDEX_CONTENT),cursor.getInt(DbAdapter.INDEX_IMPORTANT));
            view.setTag(reminder);
        }

        if(reminder.getImportant()==1){
            listTab.setBackgroundColor(context.getResources().getColor(R.color.orange,context.getTheme()));
        } else {
            listTab.setBackgroundColor(context.getResources().getColor(R.color.green,context.getTheme()));
        }
    }
}

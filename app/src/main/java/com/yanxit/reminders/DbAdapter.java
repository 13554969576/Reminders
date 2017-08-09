package com.yanxit.reminders;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by yanxit on 8/8/2017.
 */

public class DbAdapter {

    public static final String COL_ID = "_id";
    public static final String COL_CONTENT = "content";
    public static final String COL_IMPORTANT = "important";

    public static final int INDEX_ID = 0;
    public static final int INDEX_CONTENT = INDEX_ID + 1;;
    public static final int INDEX_IMPORTANT = INDEX_ID + 2;

    private static final String TAG = "DbAdapter";
    private DbHelper dbHelper;

    private SQLiteDatabase db;

    private static final String DATABASE_NAME = "reminders";
    private static final String TABLE_NAME = "reminder";
    private static final int DATABASE_VERSION = 1;

    private final Context context;

    private static final String DATABASE_CREATE = String.format("CREATE TABLE if not exists %s (%s INTEGER PRIMARY KEY autoincrement, " +
        "%s TEXT, %s INTEGER);",TABLE_NAME,COL_ID,COL_CONTENT,COL_IMPORTANT) ;

    public DbAdapter(Context context) {
        this.context = context;
    }

    public void open(){
        dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public void close(){
        if(dbHelper != null){
            dbHelper.close();
        }
    }

    public long createReminder(String content, boolean important){
        ContentValues values = new ContentValues();
        values.put(COL_CONTENT,content);
        values.put(COL_IMPORTANT,important?1:0);
        return db.insert(TABLE_NAME,null,values);
    }

    public long createReminder(Reminder reminder){
       return createReminder(reminder.getContent(),reminder.getImportant()==1);
    }

    public Reminder getReminder(int id){
        Cursor cursor = db.query(TABLE_NAME,new String[]{COL_ID,COL_CONTENT,COL_IMPORTANT},COL_ID + "=?",new String[]{String.valueOf(id)},null,null,null,null);
        if(cursor != null){
            cursor.moveToFirst();
            return new Reminder(cursor.getInt(INDEX_ID),cursor.getString(INDEX_CONTENT),cursor.getInt(INDEX_IMPORTANT));
        } else {
            return null;
        }
    }

    public Cursor findAllAsCursor(){
        Cursor cursor = db.query(TABLE_NAME,new String[]{COL_ID,COL_CONTENT,COL_IMPORTANT},null,null,null,null,null );
        return cursor;
//        if(cursor==null) return Collections.emptyList();
//        List<Reminder> ret = new ArrayList<>();
//        while(cursor.moveToNext()){
//            Reminder reminder = new Reminder(cursor.getInt(INDEX_ID),cursor.getString(INDEX_CONTENT),cursor.getInt(INDEX_IMPORTANT));
//            ret.add(reminder);
//        }
//        return ret;
    }

    public boolean updateReminder(Reminder reminder){
        ContentValues values = new ContentValues();
        values.put(COL_CONTENT,reminder.getContent());
        values.put(COL_IMPORTANT,reminder.getImportant());
        return db.update(TABLE_NAME,values,COL_ID + "=?",new String[]{String.valueOf(reminder.getId())})>0;
    }

    public boolean deleteReminder(int id){
        return db.delete(TABLE_NAME,COL_ID + "=?",new String[]{String.valueOf(id)})>0;
    }

    public void deleteAll(){
        db.delete(TABLE_NAME,null,null);
    }

    public RemindersCursorAdapter getRemidersAsAdapter(){
        Cursor c = findAllAsCursor();
        String[] from = new String[] {COL_CONTENT};
        int[] to = new int[]{R.id.row_text};
        return new RemindersCursorAdapter(context,R.layout.reminders_row,c,from,to,0);
    }

    class DbHelper extends SQLiteOpenHelper{
        public DbHelper(Context context) {
            super( context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i(TAG,DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i(TAG,String.format("Upgrading database from version %d to %d, which will destroy all old data", oldVersion,newVersion));
            String sql = String.format("DROP TABLE IF EXISTS %s",TABLE_NAME);
            Log.i(TAG,sql);
            db.execSQL(sql);
            onCreate(db);
        }
    }
}

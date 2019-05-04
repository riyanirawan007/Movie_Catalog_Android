package app.android.rynz.imoviecatalog.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import static app.android.rynz.imoviecatalog.data.db.MainDBContract.*;

public class MainDBHelper extends SQLiteOpenHelper
{
    public MainDBHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL(TableFavorite.SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
        sqLiteDatabase.execSQL(TableFavorite.SQL_DROP);
        onCreate(sqLiteDatabase);
    }
}

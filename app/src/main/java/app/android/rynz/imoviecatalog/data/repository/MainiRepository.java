package app.android.rynz.imoviecatalog.data.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import app.android.rynz.imoviecatalog.helper.MainDBHelper;
import app.android.rynz.imoviecatalog.view.service.MainService;

import static app.android.rynz.imoviecatalog.data.db.MainDBContract.TableFavorite;

public class MainiRepository implements MainService.FavoriteProvider
{
    private MainDBHelper helper;
    private SQLiteDatabase database;

    public MainiRepository(@NonNull Context context)
    {
        helper = new MainDBHelper(context);
    }

    @Override
    public long insertFavProvider(ContentValues item)
    {
        database=helper.getWritableDatabase();
        return database.insert(TableFavorite.TABLE_NAME,null,item);
    }

    @Override
    public int deleteFavProvider(String id)
    {
        database=helper.getWritableDatabase();
        return database.delete(TableFavorite.TABLE_NAME,TableFavorite._ID+" = ?",new String[]{id});
    }

    @Override
    public int updateFavProvider(String id, ContentValues item)
    {
        database=helper.getWritableDatabase();
        return database.update(TableFavorite.TABLE_NAME,item,TableFavorite._ID+" = ?",new String[]{id});
    }

    @Nullable
    @Override
    public Cursor getFavByIDProvider(String id)
    {
        database=helper.getReadableDatabase();
        return database.query(TableFavorite.TABLE_NAME
        ,null
        ,TableFavorite._ID+" = ? "
        ,new String[]{id}
        ,null
        ,null
        ,null);
    }

    @Nullable
    @Override
    public Cursor getFavByMovieIDProvider(String MovieID)
    {
        database=helper.getReadableDatabase();
        return database.query(TableFavorite.TABLE_NAME
                ,null
                ,TableFavorite.COLUMN_MOVIE_ID+" = ? "
                ,new String[]{MovieID}
                ,null
                ,null
                ,null);
    }

    @Nullable
    @Override
    public Cursor getFavProvider()
    {
        database=helper.getReadableDatabase();
        return database.query(TableFavorite.TABLE_NAME
            ,null
            ,null
            ,null
            ,null
            ,null
            ,TableFavorite._ID+" ASC ");
    }
}

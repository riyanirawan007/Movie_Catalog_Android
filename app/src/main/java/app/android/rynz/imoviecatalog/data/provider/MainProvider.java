package app.android.rynz.imoviecatalog.data.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import app.android.rynz.imoviecatalog.widget.FavoriteWidget;
import app.android.rynz.imoviecatalog.data.repository.MainiRepository;

public class MainProvider extends ContentProvider
{
    public static final String AUTHORITY="app.android.rynz.imoviecatalog";
    public static final String BASE_PATH="favorite";
    public static final String PATH_BY_MOVIEID="by_movie_id";
    public static final Uri CONTENT_URI=new Uri.Builder().scheme("content")
            .authority(AUTHORITY)
            .appendPath(BASE_PATH)
            .build();
    public static final int TAG_FAVORITE =1;
    public static final int TAG_FAVORITE_ID =2;
    public static final int TAG_FAVORITE_MOVIE_ID =3;

    private static final UriMatcher uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH, TAG_FAVORITE);
        uriMatcher.addURI(AUTHORITY, BASE_PATH+"/#", TAG_FAVORITE_ID);
        uriMatcher.addURI(AUTHORITY, BASE_PATH+"/"+PATH_BY_MOVIEID+"/#", TAG_FAVORITE_MOVIE_ID);
    }

    private MainiRepository repository;

    @Override
    public boolean onCreate()
    {
        if(getContext()!=null)
            repository=new MainiRepository(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1)
    {
        Cursor cursor;
        switch (uriMatcher.match(uri))
        {
            case TAG_FAVORITE:{
                cursor=repository.getFavProvider();
                break;
            }
            case TAG_FAVORITE_ID:{
                cursor=repository.getFavByIDProvider(uri.getLastPathSegment());
                break;
            }
            case TAG_FAVORITE_MOVIE_ID:{
                cursor=repository.getFavByMovieIDProvider(uri.getLastPathSegment());
                break;
            }
            default:
            {
                cursor=null;
                break;
            }
        }

        if(cursor!=null)
        {
            if(getContext()!=null)
                cursor.setNotificationUri(getContext().getContentResolver(),uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri)
    {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues)
    {
        long added;
        switch (uriMatcher.match(uri))
        {
            case TAG_FAVORITE:
            {
                added=repository.insertFavProvider(contentValues);
                break;
            }
            default:{
                added=0;
                break;
            }
        }
        if(added>0)
        {
            if(getContext()!=null)
            {
                getContext().getContentResolver().notifyChange(uri,null);
                FavoriteWidget.notifyWidgetFavoriteChanged(getContext());
            }
        }
        return Uri.parse(CONTENT_URI+"/"+added);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings)
    {
        int deleted;
        switch (uriMatcher.match(uri))
        {
            case TAG_FAVORITE_ID:
            {
                deleted=repository.deleteFavProvider(uri.getLastPathSegment());
                break;
            }
            default:
            {
                deleted=0;
                break;
            }
        }

        if(deleted>0)
        {
            if(getContext()!=null)
            {
                getContext().getContentResolver().notifyChange(uri,null);
                FavoriteWidget.notifyWidgetFavoriteChanged(getContext());
            }
        }
        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings)
    {
        int updated;
        switch (uriMatcher.match(uri))
        {
            case TAG_FAVORITE_ID:
            {
                updated=repository.updateFavProvider(uri.getLastPathSegment(),contentValues);
                break;
            }
            default:
            {
                updated=0;
                break;
            }
        }

        if(updated>0)
        {
            if(getContext()!=null)
            {
                getContext().getContentResolver().notifyChange(uri,null);
                FavoriteWidget.notifyWidgetFavoriteChanged(getContext());
            }
        }
        return updated;
    }
}

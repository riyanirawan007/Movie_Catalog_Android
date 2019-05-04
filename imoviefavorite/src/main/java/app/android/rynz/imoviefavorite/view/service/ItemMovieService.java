package app.android.rynz.imoviefavorite.view.service;

import android.support.annotation.NonNull;
import android.view.View;

import app.android.rynz.imoviefavorite.data.model.FavoriteMovie;

public interface ItemMovieService
{
    interface OnClickItemListener{
        void onClickItemListener(@NonNull FavoriteMovie movie, @NonNull View v, int position);
    }

    interface OnLongClickItemListener{
        void onLongClickItemListener(@NonNull FavoriteMovie movie, @NonNull View v, int position);
    }
}

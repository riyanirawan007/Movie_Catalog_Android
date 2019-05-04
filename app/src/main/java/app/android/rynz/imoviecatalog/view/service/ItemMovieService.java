package app.android.rynz.imoviecatalog.view.service;

import android.support.annotation.NonNull;
import android.view.View;

import app.android.rynz.imoviecatalog.data.model.results.ResultMovie;

public interface ItemMovieService
{
    interface OnClickItemListener{
        void onClickItemListener(@NonNull ResultMovie movie, @NonNull View v, int position);
    }

    interface OnLongClickItemListener{
        void onLongClickItemListener(@NonNull ResultMovie movie, @NonNull View v, int position);
    }
}

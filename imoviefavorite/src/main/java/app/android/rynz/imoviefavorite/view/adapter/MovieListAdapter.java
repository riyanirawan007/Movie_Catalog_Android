package app.android.rynz.imoviefavorite.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.Locale;

import app.android.rynz.imoviefavorite.R;
import app.android.rynz.imoviefavorite.data.model.FavoriteMovie;
import app.android.rynz.imoviefavorite.data.repository.TMDBApiReference;
import app.android.rynz.imoviefavorite.util.DateFormatConverter;
import app.android.rynz.imoviefavorite.view.service.ItemMovieService;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.FavoriteViewHolder>
{

    private ArrayList<FavoriteMovie> list;
    private Context context;
    private ItemMovieService.OnClickItemListener onClickItemListener;

    public void setOnClickItemListener(ItemMovieService.OnClickItemListener onClickItemListener)
    {
        this.onClickItemListener = onClickItemListener;
    }

    public MovieListAdapter(@NonNull Context context, ArrayList<FavoriteMovie> list)
    {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        return new FavoriteViewHolder(
                LayoutInflater.from(context)
                        .inflate(R.layout.rv_movie_list_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final FavoriteViewHolder holder, final int position)
    {
        final FavoriteMovie movie = list.get(holder.getAdapterPosition());

        Glide.with(context)
                .load(TMDBApiReference.TMDB_POSTER_342px + movie.getPosterPath())
                .thumbnail(0.9f)
                .apply(new RequestOptions().placeholder(R.drawable.no_images)
                        .error(R.drawable.no_images))
                .into(holder.poster);
        String titleSnipset = movie.getTitle();
        if (titleSnipset.length() > 30)
        {
            titleSnipset = titleSnipset.substring(0, 30) + "...";
        }
        holder.title.setText(titleSnipset);

        String overviewSnipset = "";
        if (movie.getOverview() != null) overviewSnipset = movie.getOverview();
        if (overviewSnipset.length() > 120)
        {
            overviewSnipset = overviewSnipset.substring(0, 120) + "...";
        }
        holder.desc.setText(overviewSnipset);
        holder.releaseDate.setText(
                new DateFormatConverter()
                        .withDate(movie.getReleaseDate())
                        .withPatternConvert(DateFormatConverter.PATTERN_DATE_SQL, DateFormatConverter.PATTERN_DATE_SPELL_COMMON, Locale.getDefault())
                        .doConvert()
        );
        holder.share.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_caption_watch) + " " + movie.getTitle() + "\n" + context.getResources().getString(R.string.label_release_date)
                        + " " + new DateFormatConverter()
                        .withDate(movie.getReleaseDate())
                        .withPatternConvert(DateFormatConverter.PATTERN_DATE_SQL, DateFormatConverter.PATTERN_DATE_SPELL_COMMON, Locale.getDefault())
                        .doConvert() + "\n" + context.getResources().getString(R.string.label_overview) + " : " + movie.getOverview());
                shareIntent.setType("text/plain");
                context.startActivity(shareIntent);
            }
        });

        holder.getView().setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (onClickItemListener != null)
                    onClickItemListener.onClickItemListener(movie, holder.getView(), holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    class FavoriteViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.lv_item_poster)
        ImageView poster;
        @BindView(R.id.lv_item_title)
        TextView title;
        @BindView(R.id.lv_item_desc)
        TextView desc;
        @BindView(R.id.lv_item_release_date)
        TextView releaseDate;
        @BindView(R.id.iv_item_share)
        ImageView share;
        private View v;

        FavoriteViewHolder(@NonNull View v)
        {
            super(v);
            ButterKnife.bind(this, v);
            this.v = v;
        }

        public View getView()
        {
            return v;
        }
    }
}

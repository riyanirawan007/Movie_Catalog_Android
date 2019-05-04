package app.android.rynz.imoviecatalog.view.adapter;

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

import app.android.rynz.imoviecatalog.R;
import app.android.rynz.imoviecatalog.data.model.FavoriteMovie;
import app.android.rynz.imoviecatalog.data.model.NowPlaying;
import app.android.rynz.imoviecatalog.data.model.SearchMovie;
import app.android.rynz.imoviecatalog.data.model.TopRated;
import app.android.rynz.imoviecatalog.data.model.UpComing;
import app.android.rynz.imoviecatalog.data.model.results.ResultMovie;
import app.android.rynz.imoviecatalog.data.repository.TMDBApiReference;
import app.android.rynz.imoviecatalog.util.lib.DateFormatConverter;
import app.android.rynz.imoviecatalog.view.service.ItemMovieService;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieListViewHolder>
{
    private Context context;
    private SearchMovie searchMovie;
    private TopRated topRatedModel;
    private NowPlaying nowPlaying;
    private UpComing upComingMovieModel;
    private ArrayList<FavoriteMovie> favoriteMovieMovieModel;
    private ResultMovie movie;
    
    private ItemMovieService.OnClickItemListener onClickItemListener;

    public MovieListAdapter(@NonNull Context context)
    {
        this.context = context;
    }

    public MovieListAdapter withSearchMovie(@NonNull SearchMovie searchMovie)
    {
        this.searchMovie = searchMovie;
        return this;
    }

    public MovieListAdapter withTopRatedMovie(@NonNull TopRated topRatedModel)
    {
        this.topRatedModel = topRatedModel;
        return this;
    }

    public MovieListAdapter withNowPlayingMovie(@NonNull NowPlaying nowPlaying)
    {
        this.nowPlaying = nowPlaying;
        return this;
    }

    public MovieListAdapter withUpComingMovie(@NonNull UpComing upComingMovieModel)
    {
        this.upComingMovieModel=upComingMovieModel;
        return this;
    }

    public MovieListAdapter withFavMovie(@NonNull ArrayList<FavoriteMovie> favMovie)
    {
        favoriteMovieMovieModel =favMovie;
        return this;
    }

    @NonNull
    @Override
    public MovieListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position)
    {
        View v = LayoutInflater.from(context).inflate(R.layout.rv_movie_list_item,viewGroup,false);
        return new MovieListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieListViewHolder holder, int i)
    {
        int position = holder.getAdapterPosition();
        if(searchMovie !=null)
        {
            movie= searchMovie.getMovieList().get(position);
        }
        else if(nowPlaying !=null)
        {
            movie= nowPlaying.getMovieList().get(position);
        }
        else if(topRatedModel !=null)
        {
            movie= topRatedModel.getMovieList().get(position);
        }
        else if(upComingMovieModel!=null)
        {
            movie=upComingMovieModel.getMovieList().get(position);
        }
        else if(favoriteMovieMovieModel !=null)
        {
            FavoriteMovie model= favoriteMovieMovieModel.get(position);
            movie=new ResultMovie(model.getVoteCount(),Integer.valueOf(model.getMovieID())
            ,model.getVoteAverage(),model.getPopularity(),model.isVideo()
            ,model.isAdult(),model.getTitle(),model.getPosterPath()
            ,model.getOriLanguage(),model.getOriTitle()
            ,model.getBackdropPath(),model.getOverview()
            ,model.getReleaseDate()
            ,null);
        }

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

        String overviewSnipset = movie.getOverview();
        if (overviewSnipset.length() > 120)
        {
            overviewSnipset = overviewSnipset.substring(0, 120) + "...";
        }
        holder.desc.setText(overviewSnipset);
        holder.releaseDate.setText(
                new DateFormatConverter()
                        .withDate(movie.getRealeseDate())
                        .withPatternConvert(DateFormatConverter.PATTERN_DATE_SQL, DateFormatConverter.PATTERN_DATE_SPELL_COMMON, Locale.getDefault())
                        .doConvert()
        );
        holder.share.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(searchMovie !=null)
                {
                    movie= searchMovie.getMovieList().get(holder.getAdapterPosition());
                }
                else if(nowPlaying !=null)
                {
                    movie= nowPlaying.getMovieList().get(holder.getAdapterPosition());
                }
                else if(topRatedModel !=null)
                {
                    movie= topRatedModel.getMovieList().get(holder.getAdapterPosition());
                }
                else if(upComingMovieModel!=null)
                {
                    movie=upComingMovieModel.getMovieList().get(holder.getAdapterPosition());
                }
                else if(favoriteMovieMovieModel !=null)
                {
                    FavoriteMovie model= favoriteMovieMovieModel.get(holder.getAdapterPosition());
                    movie=new ResultMovie(model.getVoteCount(),movie.getIdMovie()
                            ,model.getVoteAverage(),model.getPopularity(),model.isVideo()
                            ,model.isAdult(),model.getTitle(),model.getPosterPath()
                            ,model.getOriLanguage(),model.getOriTitle()
                            ,model.getBackdropPath(),model.getOverview()
                            ,model.getReleaseDate()
                            ,null);
                }
                Intent shareIntent=new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT,context.getString(R.string.share_caption_watch)+" "+movie.getTitle()+"\n"+context.getResources().getString(R.string.label_release_date)
                        +" "+new DateFormatConverter()
                        .withDate(movie.getRealeseDate())
                        .withPatternConvert(DateFormatConverter.PATTERN_DATE_SQL, DateFormatConverter.PATTERN_DATE_SPELL_COMMON, Locale.getDefault())
                        .doConvert()+"\n"+context.getResources().getString(R.string.label_overview)+" : "+movie.getOverview());
                shareIntent.setType("text/plain");
                context.startActivity(shareIntent);
            }
        });
        holder.getView().setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(searchMovie !=null)
                {
                    movie= searchMovie.getMovieList().get(holder.getAdapterPosition());
                }
                else if(nowPlaying !=null)
                {
                    movie= nowPlaying.getMovieList().get(holder.getAdapterPosition());
                }
                else if(topRatedModel !=null)
                {
                    movie= topRatedModel.getMovieList().get(holder.getAdapterPosition());
                }
                else if(upComingMovieModel!=null)
                {
                    movie=upComingMovieModel.getMovieList().get(holder.getAdapterPosition());
                }else if(favoriteMovieMovieModel !=null)
                {
                    FavoriteMovie model= favoriteMovieMovieModel.get(holder.getAdapterPosition());
                    movie=new ResultMovie(model.getVoteCount(),movie.getIdMovie()
                            ,model.getVoteAverage(),model.getPopularity(),model.isVideo()
                            ,model.isAdult(),model.getTitle(),model.getPosterPath()
                            ,model.getOriLanguage(),model.getOriTitle()
                            ,model.getBackdropPath(),model.getOverview()
                            ,model.getReleaseDate()
                            ,null);
                }
                if(onClickItemListener!=null) onClickItemListener.onClickItemListener(movie,holder.getView(),holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount()
    {
        int size;
        if(searchMovie !=null)
        {
            size= searchMovie.getMovieList().size();
        }
        else if(nowPlaying !=null)
        {
            size= nowPlaying.getMovieList().size();
        }
        else if(topRatedModel !=null)
        {
            size= topRatedModel.getMovieList().size();
        }
        else if(upComingMovieModel!=null)
        {
            size=upComingMovieModel.getMovieList().size();
        }
        else if(favoriteMovieMovieModel !=null)
        {
            size= favoriteMovieMovieModel.size();
        }
        else
        {
            size=0;
        }
        return size;
    }

    class MovieListViewHolder extends RecyclerView.ViewHolder{

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

        MovieListViewHolder(@NonNull View v)
        {
            super(v);
            ButterKnife.bind(this, v);
            this.v=v;
        }

        public View getView()
        {
            return v;
        }
    }

    public void setOnClickItemListener(ItemMovieService.OnClickItemListener onClickItemListener)
    {
        this.onClickItemListener = onClickItemListener;
    }
}

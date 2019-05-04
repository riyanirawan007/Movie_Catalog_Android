package app.android.rynz.imoviecatalog.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import app.android.rynz.imoviecatalog.R;
import app.android.rynz.imoviecatalog.data.model.NowPlaying;
import app.android.rynz.imoviecatalog.data.model.UpComing;
import app.android.rynz.imoviecatalog.data.model.results.ResultMovie;
import app.android.rynz.imoviecatalog.data.repository.TMDBApiReference;
import app.android.rynz.imoviecatalog.view.service.ItemMovieService;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HomePosterAdapter extends RecyclerView.Adapter<HomePosterAdapter.HomeMovieListViewHolder>
{
    private Context context;
    private NowPlaying nowPlayingMovie;
    private UpComing upComingMovie;
    private ItemMovieService.OnClickItemListener onClickItemListener;

    public HomePosterAdapter(@NonNull Context context)
    {
        this.context=context;
    }
    public HomePosterAdapter withNowPlayingMovie(@NonNull NowPlaying nowPlayingMovie)
    {
        this.nowPlayingMovie=nowPlayingMovie;
        return this;
    }
    public HomePosterAdapter withUpComingMovie(@NonNull UpComing upComingMovie)
    {
        this.upComingMovie=upComingMovie;
        return this;
    }

    @NonNull
    @Override
    public HomeMovieListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View v= LayoutInflater.from(context).inflate(R.layout.rv_home_movie_list,viewGroup,false);
        return new HomeMovieListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final HomeMovieListViewHolder holder, int i)
    {
        ResultMovie movie;
        if(nowPlayingMovie!=null)
        {
            movie=nowPlayingMovie.getMovieList().get(i);
        }
        else
        {
            movie=upComingMovie.getMovieList().get(i);
        }

        String snipTitle=movie.getTitle();
        int limitTitle=25;
        if(snipTitle.length()>limitTitle)
        {
            snipTitle=snipTitle.substring(0,limitTitle).concat("...");
        }
        holder.title.setText(snipTitle);
        Glide.with(context)
                .asDrawable()
                .thumbnail(0.9f)
                .load(TMDBApiReference.TMDB_POSTER_500px.concat(movie.getPosterPath()))
                .apply(new RequestOptions().placeholder(R.drawable.no_images).error(R.drawable.no_images))
                .into(holder.photo);
        holder.getView().setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ResultMovie movieModel;
                if(nowPlayingMovie!=null)
                {
                    movieModel=nowPlayingMovie.getMovieList().get(holder.getAdapterPosition());
                }
                else
                {
                    movieModel=upComingMovie.getMovieList().get(holder.getAdapterPosition());
                }
                if(onClickItemListener!=null) onClickItemListener.onClickItemListener(movieModel,holder.getView(),holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount()
    {
        if(nowPlayingMovie!=null)
        {
            return nowPlayingMovie.getMovieList().size();
        }
        else
        {
            return upComingMovie.getMovieList().size();
        }
    }

    class HomeMovieListViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tv_home_movie_list)
        TextView title;
        @BindView(R.id.iv_home_movie_list)
        ImageView photo;
        private View itemView;
        public HomeMovieListViewHolder(@NonNull View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);
            this.itemView=itemView;
        }

        public View getView()
        {
            return itemView;
        }
    }

    public void setOnClickItemListener(ItemMovieService.OnClickItemListener onClickItemListener)
    {
        this.onClickItemListener = onClickItemListener;
    }
}

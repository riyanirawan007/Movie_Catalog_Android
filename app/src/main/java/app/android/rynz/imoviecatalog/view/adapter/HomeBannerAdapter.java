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
import app.android.rynz.imoviecatalog.data.model.TopRated;
import app.android.rynz.imoviecatalog.data.model.results.ResultMovie;
import app.android.rynz.imoviecatalog.data.repository.TMDBApiReference;
import app.android.rynz.imoviecatalog.view.service.ItemMovieService;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeBannerAdapter extends RecyclerView.Adapter<HomeBannerAdapter.HomeSliderViewHolder>
{
    private Context context;
    private TopRated topRated;
    private ItemMovieService.OnClickItemListener onClickItemListener;

    public HomeBannerAdapter(@NonNull Context context, @NonNull TopRated topRated)
    {
        this.context=context;
        this.topRated = topRated;
    }
    @NonNull
    @Override
    public HomeSliderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View v= LayoutInflater.from(context).inflate(R.layout.rv_home_movie_slider,viewGroup,false);
        return new HomeSliderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final HomeSliderViewHolder holder, int i)
    {
        ResultMovie movie= topRated.getMovieList().get(i);
        String snipTitle=movie.getTitle();
        int limitTitle=30;
        if(snipTitle.length()>limitTitle)
        {
            snipTitle=snipTitle.substring(0,limitTitle).concat("...");
        }
        holder.title.setText(snipTitle);

        Glide.with(context)
                .asDrawable()
                .thumbnail(0.9f)
                .load(TMDBApiReference.TMDB_POSTER_500px.concat(movie.getBackDropPath()))
                .apply(new RequestOptions().placeholder(R.drawable.no_images).error(R.drawable.no_images))
                .into(holder.photo);
        holder.getView().setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ResultMovie movieModel= topRated.getMovieList().get(holder.getAdapterPosition());
                if(onClickItemListener!=null) onClickItemListener.onClickItemListener(movieModel,holder.getView(),holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return topRated.getMovieList().size();
    }

    class HomeSliderViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.iv_home_top_rated)
        ImageView photo;
        @BindView(R.id.tv_home_top_rated)
        TextView title;
        private View v;
        public HomeSliderViewHolder(@NonNull View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);
            v=itemView;
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

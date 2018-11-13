package com.example.sakshi.popularmoviesstage1.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.sakshi.popularmoviesstage1.R;
import com.example.sakshi.popularmoviesstage1.activity.MovieDetailsActivity;
import com.example.sakshi.popularmoviesstage1.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> moviesList;
    private Context mContext;
    public class MovieViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImgPoster;


        MovieViewHolder(View view) {
            super(view);
            mImgPoster= view.findViewById(R.id.ivposter);
        }
    }


    public MovieAdapter(List<Movie> moviesList, Context context) {
        this.moviesList = moviesList;
        this.mContext=context;
    }




    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_poster_list, parent, false);

        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        final Movie movie = moviesList.get(position);
        holder.mImgPoster.setImageResource(R.mipmap.ic_launcher_round);
        Picasso.with(mContext)
                .load("http://image.tmdb.org/t/p/w185/"+movie.getPosterPath())
                .resize(185,278)
                .placeholder(R.drawable.movie_poster)
                .into(holder.mImgPoster);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, MovieDetailsActivity.class);
                intent.putExtra("title",movie.getOriginalTitle());
                intent.putExtra("date",movie.getReleaseDate());
                intent.putExtra("review",movie.getVoteAverage());
                intent.putExtra("posterpath",movie.getPosterPath());
                intent.putExtra("overview",movie.getOverview());
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
package com.example.sakshi.popularmoviesstage1.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sakshi.popularmoviesstage1.R;
import com.squareup.picasso.Picasso;

import java.util.Locale;

public class MovieDetailsActivity extends AppCompatActivity {

    private String mMovieTitle,mReleaseDate,mMovieOverview,mMoviePosterPath;
    private TextView mTxtMovieTitle,mTxtReleaseDate,mTxtMovieRating,mTxtOverview,mTxtDateHeading;
    private ImageView mImgPoster;
    private Double mMovieRating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        initUI();
        getData();
        setData();
    }

    private void initUI() {
        mTxtMovieTitle=(TextView)findViewById(R.id.textview_original_title);
        mTxtMovieRating=(TextView)findViewById(R.id.textview_vote_average);
        mTxtReleaseDate=(TextView)findViewById(R.id.textview_release_date);
        mTxtOverview=(TextView)findViewById(R.id.textview_overview);
        mTxtDateHeading=(TextView)findViewById(R.id.textview_release_date_title);
        mImgPoster=(ImageView)findViewById(R.id.imageview_poster);

    }

    private void setData() {
        mTxtMovieTitle.setText(mMovieTitle);
        if(!TextUtils.isEmpty(mReleaseDate))
            mTxtReleaseDate.setText(mReleaseDate);
        else {
            mTxtReleaseDate.setVisibility(View.GONE);
            mTxtDateHeading.setVisibility(View.GONE);
        }
        mTxtOverview.setText(mMovieOverview);

        mTxtMovieRating.setText(String.format(Locale.getDefault(),"%f", mMovieRating));
        Picasso.with(this)
                .load("http://image.tmdb.org/t/p/w185/"+mMoviePosterPath)
                .resize(185,278)
                .placeholder(R.drawable.movie_poster)
                .into(mImgPoster);

    }


    private void getData() {
        Intent intent=getIntent();
        mMovieTitle=intent.getStringExtra("title");
        mReleaseDate=intent.getStringExtra("date");
        mMovieRating=intent.getDoubleExtra("review",1.1);
        mMovieOverview=intent.getStringExtra("overview");
        mMoviePosterPath=intent.getStringExtra("posterpath");

    }
}

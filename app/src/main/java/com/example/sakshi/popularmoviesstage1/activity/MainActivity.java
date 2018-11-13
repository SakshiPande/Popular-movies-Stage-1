package com.example.sakshi.popularmoviesstage1.activity;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.sakshi.popularmoviesstage1.R;
import com.example.sakshi.popularmoviesstage1.adapter.MovieAdapter;
import com.example.sakshi.popularmoviesstage1.model.Movie;
import com.example.sakshi.popularmoviesstage1.utils.Constant;
import com.example.sakshi.popularmoviesstage1.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView mRvMovies;
    private String API_KEY,mSortType;
    private SharedPreferences sharedPreferences;
    private  SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        API_KEY = this.getResources().getString(R.string.api_key);

        sharedPreferences = getSharedPreferences(Constant.SHARED_PREF_NAME,MODE_PRIVATE);
        mSortType = sharedPreferences.getString("sort_type", "popular");

        if(mSortType.equals("popular"))
            getSupportActionBar().setTitle(R.string.most_popular_menu_title);
        else if(mSortType.equals("top_rated"))
            getSupportActionBar().setTitle(R.string.top_rated_menu_title);

        if(Utils.isNetworkAvailable(this)) {
            new FetchMovieDB().execute(getURL(mSortType));
        }
        else {
            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }

    }

    private void initUI() {

        mRvMovies = findViewById(R.id.rvmovies);
    }
    private String getURL(String sortByParam){
        if(!TextUtils.isEmpty(sortByParam)) {
            Uri builtUri = Uri.parse(Constant.TMDB_BASE_URL).buildUpon()
                    .appendPath(sortByParam)
                    .appendQueryParameter(Constant.API_KEY_PARAM, API_KEY)
                    .build();

            return builtUri.toString();
        }
        else
        {
            Uri builtUri = Uri.parse(Constant.TMDB_BASE_URL).buildUpon()
                    .appendQueryParameter(Constant.API_KEY_PARAM, API_KEY)
                    .build();

            return builtUri.toString();
        }
    }

    private void setMovieList(List<Movie> movieList) {
    }

    private class FetchMovieDB extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader bufferedReader = null;
            String result;
            try {


                URL url = new URL(params[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();


                int statusCode = urlConnection.getResponseCode();

                if (statusCode == 200) {

                    bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));


                    StringBuilder response = new StringBuilder();
                    String inputLine;
                    while ((inputLine = bufferedReader.readLine()) != null) {
                        response.append(inputLine);
                    }

                    result = response.toString();
                    return result;
                } else {
                    Log.d(TAG, "API response incorrect status code:" + statusCode);
                }


            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            final String TAG_RESULTS = "results";
            final String TAG_ORIGINAL_TITLE = "original_title";
            final String TAG_POSTER_PATH = "poster_path";
            final String TAG_OVERVIEW = "overview";
            final String TAG_VOTE_AVERAGE = "vote_average";
            final String TAG_RELEASE_DATE = "release_date";

            try {
                if(!TextUtils.isEmpty(s)) {
                    JSONObject movieJsonObj = new JSONObject(s);

                    JSONArray results = movieJsonObj.getJSONArray(TAG_RESULTS);
                    if (results != null) {
                        List<Movie> movieList = new ArrayList<>();

                        for (int i = 0; i < results.length(); i++) {
                            Movie movie = new Movie();
                            JSONObject movieDetails = results.getJSONObject(i);
                            movie.setOriginalTitle(movieDetails.getString(TAG_ORIGINAL_TITLE));
                            movie.setPosterPath(movieDetails.getString(TAG_POSTER_PATH));
                            movie.setOverview(movieDetails.getString(TAG_OVERVIEW));
                            movie.setVoteAverage(movieDetails.getDouble(TAG_VOTE_AVERAGE));
                            movie.setReleaseDate(movieDetails.getString(TAG_RELEASE_DATE));
                            movieList.add(movie);
                        }
                        setMovieList(movieList);
                        if (mRvMovies != null) {
                            mRvMovies.setHasFixedSize(true);
                            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL);
                            mRvMovies.setLayoutManager(staggeredGridLayoutManager);
                            mRvMovies.setAdapter(new MovieAdapter(movieList, MainActivity.this));
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sortby_menu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.most_popular:
                item.setChecked(!item.isChecked());
                if(Utils.isNetworkAvailable(MainActivity.this)) {
                    editor=sharedPreferences.edit();
                    editor.putString(Constant.SHARED_PREF_NAME,"popular");
                    editor.apply();
                    getSupportActionBar().setTitle(R.string.most_popular_menu_title);
                    Toast.makeText(this, "Sorting movies by popularity", Toast.LENGTH_SHORT).show();
                    new FetchMovieDB().execute(getURL("popular"));
                }else
                    Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.top_rated:
                item.setChecked(!item.isChecked());
                if(Utils.isNetworkAvailable(MainActivity.this)) {
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString(Constant.SHARED_PREF_NAME,"top_rated");
                    editor.apply();
                    getSupportActionBar().setTitle(R.string.top_rated_menu_title);
                    Toast.makeText(this, "Sorting movies by top rated", Toast.LENGTH_SHORT).show();
                    new FetchMovieDB().execute(getURL("top_rated"));
                }
                else
                    Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}




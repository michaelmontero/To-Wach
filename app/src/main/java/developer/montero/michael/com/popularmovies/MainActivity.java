package developer.montero.michael.com.popularmovies;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import developer.montero.michael.com.popularmovies.adapter.MovieAdapter;
import developer.montero.michael.com.popularmovies.interfaces.MovieClickListener;
import developer.montero.michael.com.popularmovies.model.Movie;
import developer.montero.michael.com.popularmovies.util.NetworkUtil;

public class MainActivity extends AppCompatActivity implements MovieClickListener {
    final static String MOVIE = "movie";
    private MovieAdapter movieAdapter;
    private static final String TAG = MainActivity.class.getName();
    private RecyclerView movieRecyclerView;
    private ArrayList<Movie> movieArrayList = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movieAdapter = new MovieAdapter(this, null, this);

        movieRecyclerView = (RecyclerView)findViewById(R.id.main_movie_recyclerview);
        GridLayoutManager layoutManager;
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            layoutManager = new GridLayoutManager(this,2);
        }else{
            layoutManager = new GridLayoutManager(this,3);
        }
     //   layoutManager.set
        movieRecyclerView.setLayoutManager(layoutManager);
        movieRecyclerView.setHasFixedSize(true);

        movieRecyclerView.setAdapter(movieAdapter);

        URL url = NetworkUtil.createUrl("popularity");
        new NetworkTask().execute(url);
    }


    private class NetworkTask extends AsyncTask<URL,Void, ArrayList<Movie>>{

        @Override
        protected void onPreExecute() {
            Log.i(TAG,"onPreExecute()");
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Movie> doInBackground(URL... params) {
            Log.i(TAG,"doInBackground()");
            try {
                String movies = NetworkUtil.getMovies(params[0]);
                if(movies != null){
                    movieArrayList = NetworkUtil.convertJsonToMovieList(movies);
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return movieArrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            super.onPostExecute(movies);
            movieAdapter.swapMovies(movies);
        }
    }

    @Override
    public void onMovieClick(int moviePosition) {
        Intent intent = new Intent(this, DetailActivity.class);
        Movie movie = movieArrayList.get(moviePosition);
        intent.putExtra(MOVIE, movie);
        startActivity(intent);
    }
}

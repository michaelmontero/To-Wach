package developer.montero.michael.com.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import developer.montero.michael.com.popularmovies.adapter.MovieAdapter;
import developer.montero.michael.com.popularmovies.interfaces.MovieClickListener;
import developer.montero.michael.com.popularmovies.model.Movie;
import developer.montero.michael.com.popularmovies.util.Commons;
import developer.montero.michael.com.popularmovies.util.NetworkUtil;

public class MainActivity extends AppCompatActivity implements MovieClickListener {
    final static String MOVIE = "movie";
    private MovieAdapter movieAdapter;
    private static final String TAG = MainActivity.class.getName();
    private RecyclerView movieRecyclerView;
    private ArrayList<Movie> movieArrayList = null;
    private ProgressBar progressBar;
    private TextView tvErrorMessage;
    private SharedPreferences preferece;
    private String DEFAULT_FILTRER = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DEFAULT_FILTRER = getString(R.string.filter_popularity);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        tvErrorMessage = (TextView)findViewById(R.id.tv_errorMessage);

        Toolbar toolbar = (Toolbar)findViewById(R.id.mToolbar);
        setSupportActionBar(toolbar);

        movieAdapter = new MovieAdapter(this, null, this);

        movieRecyclerView = (RecyclerView)findViewById(R.id.main_movie_recyclerview);
        GridLayoutManager layoutManager;
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            layoutManager = new GridLayoutManager(this,2);
        }else{
            layoutManager = new GridLayoutManager(this,3);
        }
        movieRecyclerView.setLayoutManager(layoutManager);
        movieRecyclerView.setHasFixedSize(true);

        movieRecyclerView.setAdapter(movieAdapter);

        if(Commons.isConnected(this)){
           preferece= PreferenceManager.getDefaultSharedPreferences(this);
            String filter = preferece.getString("preferences_filter", DEFAULT_FILTRER);
            URL url = NetworkUtil.createUrl(filter);
            new NetworkTask().execute(url);
        }else{
            showError(getString(R.string.errorMessage_noInternetConnection));
        }
    }

    private void showProgress(){
        progressBar.setVisibility(View.VISIBLE);
        movieRecyclerView.setVisibility(View.INVISIBLE);
    }
    private void showData(){
        progressBar.setVisibility(View.INVISIBLE);
        movieRecyclerView.setVisibility(View.VISIBLE);
    }
    private void showError(String text){
        progressBar.setVisibility(View.INVISIBLE);
        movieRecyclerView.setVisibility(View.INVISIBLE);
        tvErrorMessage.setVisibility(View.VISIBLE);
        tvErrorMessage.setText(text);
    }

    private class NetworkTask extends AsyncTask<URL,Void, ArrayList<Movie>>{

        @Override
        protected void onPreExecute() {
            showProgress();
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Movie> doInBackground(URL... params) {
            try {
                String movies = NetworkUtil.getMovies(params[0]);
                if(movies != null){
                    movieArrayList = NetworkUtil.convertJsonToMovieList(movies);
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                showError(getString(R.string.errorMessage_unespexted));
            }
            return movieArrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            super.onPostExecute(movies);
            showData();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_setting:
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                break;
        }

        return true;
    }
}

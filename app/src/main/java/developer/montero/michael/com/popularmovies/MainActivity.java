package developer.montero.michael.com.popularmovies;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

public class MainActivity extends AppCompatActivity implements MovieClickListener, LoaderManager.LoaderCallbacks<ArrayList<Movie>> {
    final static String MOVIE = "movie";
    private MovieAdapter movieAdapter;
    private static final String TAG = MainActivity.class.getName();
    private RecyclerView movieRecyclerView;
    private ArrayList<Movie> movieArrayList = null;
    public ProgressBar progressBar;
    private TextView tvErrorMessage;
    private SharedPreferences preferece;
    private String DEFAULT_FILTRER = null;
    private LoaderManager loaderManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DEFAULT_FILTRER = getString(R.string.filter_popularity);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        tvErrorMessage = (TextView)findViewById(R.id.tv_errorMessage);

        Toolbar toolbar = (Toolbar)findViewById(R.id.mToolbar);
        setSupportActionBar(toolbar);

        loaderManager = getLoaderManager();

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
        preferece= PreferenceManager.getDefaultSharedPreferences(this);

        if(Commons.isConnected(this)){
          loaderManager.initLoader(0,null, this);
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

    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int id, Bundle args) {
        String filter = preferece.getString("preferences_filter", DEFAULT_FILTRER);
        URL url = NetworkUtil.createUrl(filter);
        try{
               return new MovieLoader(this, url) {
                    @Override
                    public ArrayList<Movie> loadInBackground() {
                        return super.loadInBackground();
                    }
                };
        }catch (Exception e){
            showError(getString(R.string.errorMessage_unespexted));
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> data) {
        showData();
        movieArrayList = data;
        movieAdapter.swapMovies(data);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Movie>> loader) {
        movieAdapter.swapMovies(null);
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

    public abstract class MovieLoader extends AsyncTaskLoader<ArrayList<Movie>>{

        private URL param;
        private ArrayList<Movie> movieArrayList;
        public MovieLoader(Context context,URL param) {
            super(context);
            this.param = param;
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            showProgress();
            forceLoad();
        }

        @Override
        public ArrayList<Movie> loadInBackground() {
            try {
                String movies = NetworkUtil.getMovies(param);
                if(movies != null){
                    movieArrayList = NetworkUtil.convertJsonToMovieList(movies);
                }
            } catch (IOException | JSONException e) {
                movieArrayList = null;
                e.printStackTrace();
            }
            return movieArrayList;
        }
    }

}


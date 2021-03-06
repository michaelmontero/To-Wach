package developer.montero.michael.com.popularmovies;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONException;

import java.net.URL;
import java.util.ArrayList;

import developer.montero.michael.com.popularmovies.adapter.MovieAdapter;
import developer.montero.michael.com.popularmovies.interfaces.LoadMoreListener;
import developer.montero.michael.com.popularmovies.interfaces.MovieClickListener;
import developer.montero.michael.com.popularmovies.loader.DataLoader;
import developer.montero.michael.com.popularmovies.model.Movie;
import developer.montero.michael.com.popularmovies.util.Commons;
import developer.montero.michael.com.popularmovies.util.NetworkUtil;

public class MainActivity extends AppCompatActivity implements MovieClickListener, LoaderManager.LoaderCallbacks<String> {
    private static final String TAG = MainActivity.class.getName();
    final static String MOVIE = "movie";
    private static final String MOVIE_URL = "MOVIE_URL";
    private MovieAdapter movieAdapter;
    private RecyclerView movieRecyclerView;
    private ArrayList<Movie> movieArrayList = null;
    public ProgressBar progressBar;
    private TextView tvErrorMessage;
    private SharedPreferences preferece;
    private String DEFAULT_FILTER;
    private LoaderManager loaderManager;
    private AlertDialog alertDialog;
    private static final String SORT_BY = "SORT_BY";
    private static final int MOVIE_LOADER = 22;
    private Button refreshButton;
    private AdView mAdView;
    static int pageNumber = 0;
    private static final String SELECTED_FILTER = "selectedFilter";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DEFAULT_FILTER = getString(R.string.filter_popularity);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        tvErrorMessage = (TextView)findViewById(R.id.tv_errorMessage);

        Toolbar toolbar = (Toolbar)findViewById(R.id.mToolbar);
        setSupportActionBar(toolbar);

        loaderManager = getLoaderManager();

        refreshButton = (Button) findViewById(R.id.refreshButton);



        movieRecyclerView = (RecyclerView)findViewById(R.id.main_movie_recyclerview);
        GridLayoutManager layoutManager;
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            layoutManager = new GridLayoutManager(this,2);
        }else{
            layoutManager = new GridLayoutManager(this,3);
        }
        movieRecyclerView.setLayoutManager(layoutManager);
        movieRecyclerView.setHasFixedSize(true);

        movieAdapter = new MovieAdapter(this,movieRecyclerView, null, this);

        movieRecyclerView.setAdapter(movieAdapter);
        preferece= PreferenceManager.getDefaultSharedPreferences(this);

        showProgress();
        initLoader();

        movieAdapter.setLoadMoreListener(new LoadMoreListener() {
            @Override
            public void onLoadMoreListener() {
                if(movieArrayList != null){
                    int size = movieArrayList.size();
                    if(size % 20 == 0){
                        Log.i(TAG, "Loading more data..");
                        initLoader();
                    }
                }
            }
        });


        //Todo: Replace the test case
        MobileAds.initialize(this, "ca-app-pub-3940256099942544/6300978111");
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }


    private void initLoader(){
        if(Commons.isConnected(this)){
            pageNumber += 1;
            Loader<Object> searchLoader = loaderManager.getLoader(MOVIE_LOADER);

            String filter = preferece.getString(SORT_BY, DEFAULT_FILTER);
            String languaje = getString(R.string.languaje);
            URL url = NetworkUtil.createUrl(0,filter,languaje,pageNumber);

            Bundle bundle = new Bundle();
            bundle.putString(MOVIE_URL, url.toString());

            if(searchLoader ==null){
                loaderManager.initLoader(MOVIE_LOADER,bundle, this);
            }else{
                loaderManager.restartLoader(MOVIE_LOADER,bundle, this);
            }
        }else {
            showError(getString(R.string.errorMessage_noInternetConnection));
        }
    }
    private void showProgress(){
        progressBar.setVisibility(View.VISIBLE);
        tvErrorMessage.setVisibility(View.INVISIBLE);
        movieRecyclerView.setVisibility(View.INVISIBLE);
        refreshButton.setVisibility(View.INVISIBLE);
    }
    private void showData(){
        progressBar.setVisibility(View.INVISIBLE);
        refreshButton.setVisibility(View.INVISIBLE);
        tvErrorMessage.setVisibility(View.INVISIBLE);
        movieRecyclerView.setVisibility(View.VISIBLE);
    }
    private void showError(String text){
        progressBar.setVisibility(View.INVISIBLE);
        refreshButton.setVisibility(View.VISIBLE);
        movieRecyclerView.setVisibility(View.INVISIBLE);
        tvErrorMessage.setVisibility(View.VISIBLE);
        tvErrorMessage.setText(text);
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        try{
               URL url =new URL(args.getString(MOVIE_URL));
               return new DataLoader(this, url) {
                    @Override
                    public String loadInBackground() {
                        return super.loadInBackground();
                    }
               };
        }catch (Exception e){
            showError(getString(R.string.errorMessage_unespexted));
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        showData();
        try {
            ArrayList<Movie> mData = NetworkUtil.convertJsonToMovieList(data);
            if(movieArrayList != null && movieArrayList.size() > 0 && mData != null){
                movieArrayList.addAll(mData);
            }else{
                movieArrayList = mData;
            }
            Log.i(TAG, movieArrayList.size()+"");
            movieAdapter.swapMovies(movieArrayList);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
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
                int selected = preferece.getInt(SELECTED_FILTER, -1);
                showSortDialog(selected);
                break;
        }
        return true;
    }

    private void showSortDialog(final int selected){
        String [] filterOptions = getResources().getStringArray(R.array.key);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.preference_filter_title));
        builder.setSingleChoiceItems(filterOptions,selected, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int index) {
                String [] filterValues = getResources().getStringArray(R.array.valor);

                SharedPreferences.Editor editor = preferece.edit();
                editor.putString(SORT_BY, filterValues[index]);
                editor.putInt(SELECTED_FILTER, index);
                editor.apply();

                int oldFilterIndex = preferece.getInt(SELECTED_FILTER, 1);

                //If select new filter choise then reset the page number and the data
                if(index == oldFilterIndex){
                    pageNumber = 0;
                    movieArrayList.clear();
                    movieAdapter.swapMovies(null);
                }

                showProgress();
                initLoader();
                alertDialog.cancel();
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }

    public void reload(View view) {
        showProgress();
        initLoader();
    }


}
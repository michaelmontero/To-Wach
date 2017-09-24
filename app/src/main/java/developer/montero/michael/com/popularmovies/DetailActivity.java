package developer.montero.michael.com.popularmovies;

import android.app.LoaderManager;
import android.content.Loader;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.net.URL;
import java.util.ArrayList;

import developer.montero.michael.com.popularmovies.adapter.CommentAdapter;
import developer.montero.michael.com.popularmovies.loader.DataLoader;
import developer.montero.michael.com.popularmovies.model.Comments;
import developer.montero.michael.com.popularmovies.model.Movie;
import developer.montero.michael.com.popularmovies.util.NetworkUtil;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{
    private ImageView movieImage;
    private Movie movie;
    private Toolbar toolbar;
    private RatingBar rating;
    private TextView releaseDate, synopsis, title;
    private static final String MOVIE_ID = "id_movie";
    private static final int COMMENT_LOADER = 10;
    private LoaderManager loaderManager;
    private static final String TAG = DetailActivity.class.getName();
    private ArrayList<Comments> comments;
    private RecyclerView recyclerView;
    private CommentAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

         toolbar = (Toolbar)findViewById(R.id.mToolbar);
         setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
        }

        recyclerView = (RecyclerView)findViewById(R.id.comments_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new CommentAdapter(this,null);
        recyclerView.setAdapter(adapter);


        Bundle bundle = getIntent().getExtras();
        if(bundle.containsKey(MainActivity.MOVIE)){
            movie = (Movie)bundle.getSerializable(MainActivity.MOVIE);
        }
        initViews();
        collpaseToolBar();

        loaderManager = getLoaderManager();
        Loader<Object> searchLoader = loaderManager.getLoader(COMMENT_LOADER);

        bundle.putInt(MOVIE_ID, movie.getId());
        if(searchLoader == null){
            loaderManager.initLoader(COMMENT_LOADER, bundle,this);
        }else{
            loaderManager.restartLoader(COMMENT_LOADER,bundle,this);
        }
    }

    private void collpaseToolBar(){
            final CollapsingToolbarLayout collapsingToolbarLayout =
                    (CollapsingToolbarLayout)findViewById(R.id.collpasingToolBar);
            collapsingToolbarLayout.setTitle("");

        AppBarLayout appBarLayout =
                (AppBarLayout)findViewById(R.id.appBarLayout);
        appBarLayout.setExpanded(true);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                toolbar.setBackgroundColor(Color.TRANSPARENT);
                if (scrollRange == -1){
                    scrollRange=appBarLayout.getTotalScrollRange();
                }
                if((scrollRange + verticalOffset) == 0){
                    collapsingToolbarLayout.setTitle(movie.getTitle());
                    toolbar.setBackgroundColor(Color.parseColor("#1a1a1a"));
                    isShow = true;
                }else if(isShow){
                    collapsingToolbarLayout.setTitle("");
                    isShow = false;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }

    private void initViews(){
        movieImage = (ImageView)findViewById(R.id.detail_movie_image);
        releaseDate = (TextView)findViewById(R.id.detail_release_date);
        synopsis = (TextView)findViewById(R.id.detail_synopsis);
        rating = (RatingBar) findViewById(R.id.detail_rating);
       title = (TextView)findViewById(R.id.detail_movie_title);

        releaseDate.setText(movie.getReleaseDate());
        synopsis.setText(movie.getSynopsis());
        title.setText(movie.getTitle());
        Integer mRating= movie.getRating().intValue();
        rating.setRating(mRating);
        showImage();
    }

    private void showImage(){
        URL uri = NetworkUtil.createImageUrl(movie.getImage());
        Picasso.with(this).load(uri.toString()).fit().into(movieImage);
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        int movieId= args.getInt(MOVIE_ID,0);
        URL url = NetworkUtil.createUrl(movieId,"reviews","en",1);
        return new DataLoader(this,url) {
            @Override
            public String loadInBackground() {
                return super.loadInBackground();
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        try {
            comments=NetworkUtil.convertJsonToCommentList(data);
            adapter.refreshData(comments);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e(TAG,data);
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}

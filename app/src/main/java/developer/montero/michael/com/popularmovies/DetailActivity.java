package developer.montero.michael.com.popularmovies;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URL;

import developer.montero.michael.com.popularmovies.model.Movie;
import developer.montero.michael.com.popularmovies.util.NetworkUtil;

public class DetailActivity extends AppCompatActivity {
    private ImageView movieImage;
    private Movie movie;
    private Toolbar toolbar;
    private RatingBar rating;
    private TextView releaseDate, synopsis, title;
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

        Bundle bundle = getIntent().getExtras();
        if(bundle.containsKey(MainActivity.MOVIE)){
            movie = (Movie)bundle.getSerializable(MainActivity.MOVIE);
        }
        initViews();
        collpaseToolBar();
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
        finish();
    }

    private void initViews(){
        movieImage = (ImageView)findViewById(R.id.detail_movie_image);
        releaseDate = (TextView)findViewById(R.id.detail_release_date);
        synopsis = (TextView)findViewById(R.id.detail_synopsis);
        rating = (RatingBar) findViewById(R.id.detail_rating);
       title = (TextView)findViewById(R.id.detail_movie_title);
//
        releaseDate.setText(movie.getReleaseDate());
        synopsis.setText(movie.getSynopsis());
        title.setText(movie.getTitle());
        rating.setRating(6);
      //  rating.setText(String.format(getString(R.string.detail_movieRating),movie.getRating()));
        showImage();
    }

    private void showImage(){
        URL uri = NetworkUtil.createImageUrl(movie.getImage());
        Picasso.with(this).load(uri.toString()).fit().into(movieImage);
    }
}

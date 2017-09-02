package developer.montero.michael.com.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URL;

import developer.montero.michael.com.popularmovies.model.Movie;
import developer.montero.michael.com.popularmovies.util.NetworkUtil;

public class DetailActivity extends AppCompatActivity {
    private ImageView movieImage;
    private Movie movie;
    private TextView releaseDate, synopsis, title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = (Toolbar)findViewById(R.id.mToolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Bundle bundle = getIntent().getExtras();
        if(bundle.containsKey(MainActivity.MOVIE)){
            movie = (Movie)bundle.getSerializable(MainActivity.MOVIE);
        }
        initViews();
    }

    private void initViews(){
        movieImage = (ImageView)findViewById(R.id.detail_movie_image);
        releaseDate = (TextView)findViewById(R.id.detail_release_date);
        synopsis = (TextView)findViewById(R.id.detail_synopsis);
        title = (TextView)findViewById(R.id.detail_movie_title);

        releaseDate.setText(movie.getReleaseDate());
        synopsis.setText(movie.getSynopsis());
        title.setText(movie.getTitle());
        showImage();
    }
    private void showImage(){
        URL uri = NetworkUtil.createImageUrl(movie.getImage());
        Picasso.with(this).load(uri.toString()).fit().into(movieImage);
    }
}

package developer.montero.michael.com.popularmovies;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.net.URL;

import developer.montero.michael.com.popularmovies.model.Movie;
import developer.montero.michael.com.popularmovies.util.NetworkUtil;

public class DetailActivity extends AppCompatActivity {
    private ImageView movieImage;
    private Movie movie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = (Toolbar)findViewById(R.id.mToolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        movieImage = (ImageView)findViewById(R.id.detail_movie_image);

        Bundle bundle = getIntent().getExtras();

        if(bundle.containsKey(MainActivity.MOVIE)){
            movie = (Movie)bundle.getSerializable(MainActivity.MOVIE);
        }

        toolbar.setTitle(movie.getTitle());
        URL uri = NetworkUtil.createImageUrl(movie.getImage());
        Picasso.with(this).load(uri.toString()).into(movieImage);
    }
}

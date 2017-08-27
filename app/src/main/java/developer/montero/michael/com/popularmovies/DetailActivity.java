package developer.montero.michael.com.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import developer.montero.michael.com.popularmovies.model.Movie;

public class DetailActivity extends AppCompatActivity {

    private Movie movie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle bundle = getIntent().getExtras();

        if(bundle.containsKey(MainActivity.MOVIE)){
            movie = (Movie)bundle.getSerializable(MainActivity.MOVIE);
        }
    }
}

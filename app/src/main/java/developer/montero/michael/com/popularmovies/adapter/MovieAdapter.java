package developer.montero.michael.com.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import developer.montero.michael.com.popularmovies.R;
import developer.montero.michael.com.popularmovies.interfaces.MovieClickListener;
import developer.montero.michael.com.popularmovies.model.Movie;
import developer.montero.michael.com.popularmovies.util.NetworkUtil;

/**
 * Created by Michael A. Montero on 25-Aug-17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieHolder> {
    private MovieClickListener movieClickListener;
    private final static String TAG = MovieAdapter.class.getName();
    private ArrayList<Movie> movies;
    private Context context;

    public MovieAdapter(Context context, ArrayList<Movie> movies, MovieClickListener movieClickListener) {
        this.context = context;
        this.movies = movies;
        this.movieClickListener = movieClickListener;
    }

    @Override
    public MovieAdapter.MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.movie_list, parent, false);
        return new MovieHolder(view);
    }


    @Override
    public void onBindViewHolder(MovieAdapter.MovieHolder holder, int position) {
        ImageView view = holder.movieImage;
        if (view == null) {
            view = new ImageView(context);
        }
        Movie movie = movies.get(position);
        String url = NetworkUtil.createImageUrl(movie.getImage()).toString();
        Log.i(TAG, url);
        Picasso.with(context)
                .load(url).into(view);
    }
    @Override
    public int getItemCount() {
        int count = (movies == null) ? 0 : movies.size();
        return count;
    }

    public void swapMovies(ArrayList<Movie> movies){
        this.movies = movies;
        notifyDataSetChanged();
    }

    public class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView movieImage;
        public MovieHolder(View itemView) {
            super(itemView);
            movieImage = (ImageView)itemView.findViewById(R.id.img_movie);
        }

        @Override
        public void onClick(View v) {
            movieClickListener.onMovieClick(getLayoutPosition());
        }
    }
}

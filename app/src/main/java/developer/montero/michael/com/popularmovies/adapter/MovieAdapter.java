package developer.montero.michael.com.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import developer.montero.michael.com.popularmovies.R;
import developer.montero.michael.com.popularmovies.interfaces.LoadMoreListener;
import developer.montero.michael.com.popularmovies.interfaces.MovieClickListener;
import developer.montero.michael.com.popularmovies.model.Movie;
import developer.montero.michael.com.popularmovies.util.NetworkUtil;

/**
 * Created by Michael A. Montero on 25-Aug-17.
 */

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private MovieClickListener movieClickListener;
    private final static String TAG = MovieAdapter.class.getName();
    private ArrayList<Movie> movies;
    private Context context;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private LoadMoreListener loadMoreListener;
    private RecyclerView recyclerView;
    private int totalItemCount = 0;
    private int lastVisibleItem = 0;
    private boolean isLoading;
    private int visibleThreshold = 5;
    public MovieAdapter(Context context,RecyclerView recyclerView, ArrayList<Movie> movies, MovieClickListener movieClickListener) {
        this.context = context;
        this.movies = movies;
        this.recyclerView = recyclerView;
        this.movieClickListener = movieClickListener;


        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (loadMoreListener != null) {
                        loadMoreListener.onLoadMoreListener();
                    }
                    isLoading = true;
                }
            }
        });

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if(viewType == VIEW_TYPE_ITEM){
            View view = inflater.inflate(R.layout.movie_list, parent, false);
            return new MovieHolder(view);
        }else{
            View view = inflater.inflate(R.layout.loading, parent, false);
            return new LoadViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return movies.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MovieHolder){
            MovieHolder movieHolder = (MovieHolder)holder;
            ImageView view = movieHolder.movieImage;
            if (view == null) {
                view = new ImageView(context);
            }
            Movie movie = movies.get(position);
            String url = NetworkUtil.createImageUrl(movie.getImage()).toString();
            Log.i(TAG, url);
            Picasso.with(context)
                    .load(url).into(view);

            movieHolder.movieTitle.setText(movie.getTitle());
        }else if(holder instanceof LoadViewHolder){
            LoadViewHolder loadingViewHolder = (LoadViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    public void setLoadMoreListener(LoadMoreListener loadMoreListener){
        this.loadMoreListener = loadMoreListener;
    }
    public void setLoaded() {
        isLoading = false;
    }

    @Override
    public int getItemCount() {
        return (movies == null) ? 0 : movies.size();
    }

    public void swapMovies(ArrayList<Movie> mMovies){
        if(movies != null && movies.size() >0){
            movies.addAll(mMovies);
        }else{
            movies = mMovies;
        }
        notifyDataSetChanged();
    }

    public class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView movieImage;
        TextView movieTitle;
        public MovieHolder(View itemView) {
            super(itemView);
            movieImage = (ImageView)itemView.findViewById(R.id.img_movie);
            movieTitle = (TextView)itemView.findViewById(R.id.movie_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            movieClickListener.onMovieClick(getLayoutPosition());
        }
    }


    public class LoadViewHolder extends RecyclerView.ViewHolder{
        ProgressBar progressBar;
        public LoadViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.loading_progressBar);
        }

    }
}

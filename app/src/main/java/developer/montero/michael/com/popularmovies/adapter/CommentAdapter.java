package developer.montero.michael.com.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import developer.montero.michael.com.popularmovies.model.Comments;

/**
 * Created by Michael A. Montero on 23/09/2017.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {
    private Context context;
    private ArrayList<Comments> comments;

    public CommentAdapter(Context context, ArrayList<Comments> comments){
        this.context = context;
        this.comments = comments;
    }

    @Override
    public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(CommentHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return (comments == null) ? 0 : comments.size();
    }

    public class CommentHolder extends ViewHolder{
        public CommentHolder(View itemView) {
            super(itemView);
        }
    }
}

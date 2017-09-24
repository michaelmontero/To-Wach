package developer.montero.michael.com.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import developer.montero.michael.com.popularmovies.R;
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
        LayoutInflater inflater = LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.comment_adapter, parent,false);
        return new CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentHolder holder, int position) {
        Comments comment = comments.get(position);
        holder.author.setText(comment.getAuthor());
        holder.comment.setText(comment.getComment());
    }

    public void refreshData(ArrayList<Comments> comments){
        this.comments = comments;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (comments == null) ? 0 : comments.size();
    }

    public class CommentHolder extends ViewHolder{
        TextView author,comment;
        public CommentHolder(View itemView) {
            super(itemView);
            author = (TextView)itemView.findViewById(R.id.author);
            comment = (TextView)itemView.findViewById(R.id.comment);
        }
    }
}

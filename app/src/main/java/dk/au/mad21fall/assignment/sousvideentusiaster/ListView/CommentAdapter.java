package dk.au.mad21fall.assignment.sousvideentusiaster.ListView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import dk.au.mad21fall.assignment.sousvideentusiaster.R;


public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    //interface for handling when a Post item is clicked in various ways
    public interface ICommentItemClickedListener {
        void onPostClicked(int index);
        //...
    }

    //callback interface for user actions on each item
    private ICommentItemClickedListener listener;

    //data in the adapter
    private ArrayList<Comment> commentList;

    //constructor
    public CommentAdapter(ICommentItemClickedListener listener){
        this.listener = listener;
    }

    public void updateCommentList(ArrayList<Comment> lists){
        commentList = lists;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        CommentViewHolder pvh = new CommentViewHolder(v, listener);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        holder.userName.setText(commentList.get(position).publisher);
        holder.comment.setText(commentList.get(position).comment);

    }

    //override this to return size of list
    @Override
    public int getItemCount(){
        if(commentList == null ){
            return 0;
        }else{
            return commentList.size();
        }

    }

    //The ViewHolder class for holding information about each list item in the RecycleView
    public class CommentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        //viewHolder ui widget references
        TextView userName, comment;
        ImageView image_profile;

        //custom callback interface for user actions done to the view holder item
        ICommentItemClickedListener listener;

        //constructor
        public CommentViewHolder(@NonNull View itemView, ICommentItemClickedListener postItemClickedListener){
            super(itemView);

            listener = postItemClickedListener;

            //get references from layout file
            image_profile = itemView.findViewById(R.id.image_profile);
            userName = itemView.findViewById(R.id.username);
            comment = itemView.findViewById(R.id.comment);


            //set click listener for whole list item
            itemView.setOnClickListener(this);
        }

        //react to user clicking the listItem (implements OnClickListener)
        @Override
        public void onClick(View view){
            listener.onPostClicked(getAdapterPosition());
        }
    }

}

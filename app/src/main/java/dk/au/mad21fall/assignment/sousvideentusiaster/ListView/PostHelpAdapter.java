package dk.au.mad21fall.assignment.sousvideentusiaster.ListView;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import dk.au.mad21fall.assignment.sousvideentusiaster.R;


public class PostHelpAdapter extends RecyclerView.Adapter<PostHelpAdapter.PostViewHolder> {

    //interface for handling when a Post item is clicked in various ways
    public interface IPostItemClickedListener{
        void onPostClicked(int index);
        //...
    }

    //callback interface for user actions on each item
    private IPostItemClickedListener listener;

    //data in the adapter
    private ArrayList<HelpPost> helpPostList;

    //constructor
    public PostHelpAdapter(IPostItemClickedListener listener){
        this.listener = listener;
    }

    public void updateHelpPostList(ArrayList<HelpPost> lists){
        helpPostList = lists;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_help, parent, false);
        PostViewHolder pvh = new PostViewHolder(v, listener);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        holder.userName.setText(helpPostList.get(position).userName);
        holder.timePosted.setText(helpPostList.get(position).timePosted);
        holder.chipText01.setText(helpPostList.get(position).chipText01);
        holder.chipText02.setText(helpPostList.get(position).chipText02);
        holder.chipStatus.setText(helpPostList.get(position).chipStatus);
        holder.postedImage.setImageResource(R.drawable.flex_icon);
        holder.profileImage.setImageResource(R.drawable.flex_icon);
        holder.postText.setText(helpPostList.get(position).postText);
        holder.numberOfComments.setText(String.valueOf(helpPostList.get(position).numberOfComments) + " Comment(s).");
        //holder.rating.setRating(String.valueOf(flexPostList.get(position).rating));
    }

    //override this to return size of list
    @Override
    public int getItemCount(){
        if(helpPostList == null ){
            return 0;
        }else{
            return helpPostList.size();
        }

    }

    //The ViewHolder class for holding information about each list item in the RecycleView
    public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        //viewHolder ui widget references
        TextView userName;
        TextView timePosted;
        TextView chipText01;
        TextView chipText02;
        TextView chipStatus;
        TextView postText;
        TextView numberOfComments;
        ImageView postedImage;
        ImageView profileImage;

        //custom callback interface for user actions done to the view holder item
        IPostItemClickedListener listener;

        //constructor
        public PostViewHolder(@NonNull View itemView, IPostItemClickedListener postItemClickedListener){
            super(itemView);

            //get references from layout file
            userName = itemView.findViewById(R.id.list_item_usernameTxt);
            timePosted = itemView.findViewById(R.id.list_item_timePostedTxt);
            postedImage = itemView.findViewById(R.id.list_item_image);
            profileImage = itemView.findViewById(R.id.list_item_profilePic);
            chipText01 = itemView.findViewById(R.id.list_item_chip01);
            chipText02 = itemView.findViewById(R.id.list_item_chip02);
            chipStatus = itemView.findViewById(R.id.list_item_help_chipStatus);
            postText = itemView.findViewById(R.id.list_item_userTextTxt);
            numberOfComments = itemView.findViewById(R.id.list_item_commentsTxt);

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

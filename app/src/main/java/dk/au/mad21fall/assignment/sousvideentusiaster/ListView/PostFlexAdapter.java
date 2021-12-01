package dk.au.mad21fall.assignment.sousvideentusiaster.ListView;

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


public class PostFlexAdapter extends RecyclerView.Adapter<PostFlexAdapter.PostViewHolder> {

    //interface for handling when a Post item is clicked in various ways
    public interface IPostItemClickedListener{
        void onPostClicked(int index);
        //...
    }

    //callback interface for user actions on each item
    private IPostItemClickedListener listener;

    //data in the adapter
    private ArrayList<FlexPost> flexPostList;

    //constructor
    public PostFlexAdapter(IPostItemClickedListener listener){
        this.listener = listener;
    }

    public void updateFlexPostList(ArrayList<FlexPost> lists){
        flexPostList = lists;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_flex, parent, false);
        PostViewHolder pvh = new PostViewHolder(v, listener);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        holder.userName.setText(flexPostList.get(position).userName);
        holder.timePosted.setText(flexPostList.get(position).timePosted);
        holder.chipText01.setText(flexPostList.get(position).chipText01);
        holder.chipText02.setText(flexPostList.get(position).chipText02);
        holder.postedImage.setImageResource(R.drawable.flex_icon);
        holder.profileImage.setImageResource(R.drawable.flex_icon);
        holder.postText.setText(flexPostList.get(position).postText);
        holder.numberOfComments.setText(String.valueOf(flexPostList.get(position).numberOfComments) + " Comment(s).");
        //holder.rating.setRating(String.valueOf(flexPostList.get(position).rating));
        holder.hoursCooked.setText(String.valueOf(flexPostList.get(position).hoursCooked));
        holder.degrees.setText(String.valueOf(flexPostList.get(position).degrees));
    }

    //override this to return size of list
    @Override
    public int getItemCount(){
        if(flexPostList == null ){
            return 0;
        }else{
            return flexPostList.size();
        }

    }

    //The ViewHolder class for holding information about each list item in the RecycleView
    public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        //viewHolder ui widget references
        TextView userName;
        TextView timePosted;
        TextView chipText01;
        TextView chipText02;
        TextView postText;
        TextView numberOfComments;
        ImageView postedImage;
        ImageView profileImage;
        RatingBar rating;
        TextView hoursCooked;
        TextView degrees;

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
            postText = itemView.findViewById(R.id.list_item_userTextTxt);
            numberOfComments = itemView.findViewById(R.id.list_item_commentsTxt);
            rating = itemView.findViewById(R.id.list_item_ratingbar);
            hoursCooked = itemView.findViewById(R.id.list_item_hoursCookedTxt);
            degrees = itemView.findViewById(R.id.list_item_degreesTxt);

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

package dk.au.mad21fall.assignment.sousvideentusiaster.Post;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import dk.au.mad21fall.assignment.sousvideentusiaster.R;

public class PostFlex extends Fragment {
    public static PostFlex newInstance(){return new PostFlex();}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        return inflater.inflate(R.layout.post_flex_fragment, container, false);
    }
}


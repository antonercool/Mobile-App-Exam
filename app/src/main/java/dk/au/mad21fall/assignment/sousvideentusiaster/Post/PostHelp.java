package dk.au.mad21fall.assignment.sousvideentusiaster.Post;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import dk.au.mad21fall.assignment.sousvideentusiaster.MasterNavigator.INavigator;
import dk.au.mad21fall.assignment.sousvideentusiaster.R;
import dk.au.mad21fall.assignment.sousvideentusiaster.Utils.Permissions;

public class PostHelp extends Fragment  {

    private static int RESULT_LOAD_IMAGE = 1;
    private static int GALLERY_ID = 999;
    private int upload_counter = 0;
    private ImageView []imageArray = new ImageView[5];

    Button postBttn, cancelBttn, uploadBttn;
    TextView title, content, uploadCounter, coockedMeat;
    ImageView img01, img02, img03, img04, img05;

    public static PostHelp newInstance(){return new PostHelp();}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.post_help_fragment, container, false);
        InitUIElements(view);



        uploadBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Permissions.verifyStoragePermissions(getActivity());
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, GALLERY_ID);
            }
        });

        postBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(title.getText())){
                    title.setError("Title is required!");
                }
                if(TextUtils.isEmpty(content.getText())){
                    content.setError("Please provide some description!");
                }
                if(TextUtils.isEmpty(coockedMeat.getText())){
                    coockedMeat.setError("Please type your cooked meat");
                }
            }
        });

        cancelBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((INavigator)getActivity()).onHelpPostCancelClicked();
            }
        });


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK && requestCode == GALLERY_ID) {
            upload_counter++;
            uploadCounter.setText(String.valueOf(upload_counter) + "/5");
            imageArray[upload_counter - 1].setVisibility(View.VISIBLE);
            if(upload_counter == 5){
                uploadBttn.setEnabled(false);
            }
        }
    }



    public void InitUIElements(View view){

        postBttn = view.findViewById(R.id.post_help_postBttn);
        cancelBttn = view.findViewById(R.id.post_help_cancelBttn);
        uploadBttn = view.findViewById(R.id.post_flex_uploadBttn);

        title = view.findViewById(R.id.post_help_title);
        content = view.findViewById(R.id.post_help_content_text);
        coockedMeat = view.findViewById(R.id.post_help_meatCooked);
        uploadCounter = view.findViewById(R.id.post_flex_imageUploadCounter);

        img01 = view.findViewById(R.id.post_flex_image01);
        img02 = view.findViewById(R.id.post_flex_image02);
        img03 = view.findViewById(R.id.post_flex_image03);
        img04 = view.findViewById(R.id.post_flex_image04);
        img05 = view.findViewById(R.id.post_flex_image05);

        img01.setVisibility(View.INVISIBLE);
        img02.setVisibility(View.INVISIBLE);
        img03.setVisibility(View.INVISIBLE);
        img04.setVisibility(View.INVISIBLE);
        img05.setVisibility(View.INVISIBLE);

        imageArray[0] = img01;
        imageArray[1] = img02;
        imageArray[2] = img03;
        imageArray[3] = img04;
        imageArray[4] = img05;
    }



}


package dk.au.mad21fall.assignment.sousvideentusiaster.MasterNavigator.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;

import dk.au.mad21fall.assignment.sousvideentusiaster.Login.LoginActivity;
import dk.au.mad21fall.assignment.sousvideentusiaster.R;

public class Profile extends Fragment {

    Button logoutBttn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.profile_fragment, container, false);
        logoutBttn = view.findViewById(R.id.logoutBttn);

        logoutBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                getActivity().finish();
            }
        });

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(getContext());

        return view;
    }


}

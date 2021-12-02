package dk.au.mad21fall.assignment.sousvideentusiaster.MasterNavigator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import dk.au.mad21fall.assignment.sousvideentusiaster.DetailView.DetailFlex;
import dk.au.mad21fall.assignment.sousvideentusiaster.MasterNavigator.Fragments.Flex;
import dk.au.mad21fall.assignment.sousvideentusiaster.MasterNavigator.Fragments.News;
import dk.au.mad21fall.assignment.sousvideentusiaster.MasterNavigator.Fragments.Profile;
import dk.au.mad21fall.assignment.sousvideentusiaster.MasterNavigator.Fragments.Question;
import dk.au.mad21fall.assignment.sousvideentusiaster.Post.PostFlex;
import dk.au.mad21fall.assignment.sousvideentusiaster.Post.PostHelp;
import dk.au.mad21fall.assignment.sousvideentusiaster.R;

public class MasterNavigatorActivity extends AppCompatActivity implements INavigator {

    BottomNavigationView menuNavigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_navigator);

        if (savedInstanceState == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, News.newInstance(), null
                    )
                    .commitNow();
        }

        setUpUiElements();
    }

    private void setUpUiElements(){
        menuNavigator = findViewById(R.id.menu_navigator);

        menuNavigator.setOnNavigationItemSelectedListener(menu_listener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener menu_listener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()){
                        case R.id.flex_item:
                            selectedFragment =  new Flex();
                            break;
                        case R.id.news_item:
                            selectedFragment =  new News();
                            break;
                        case R.id.profile_item:
                            selectedFragment =  new Profile();
                            break;
                        case R.id.question_item:
                            selectedFragment =  new Question();
                            break;
                        default:
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commitNow();

                    // return true to indicate we want to clicked item to be selected
                    return true;
                }
            };

    @Override
    public void onFlexPostClicked() {
        Fragment postFlex = new PostFlex();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                postFlex).commitNow();
    }

    @Override
    public void onFlexPostCancelClicked() {
        Fragment Flex = new Flex();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                Flex).commitNow();
    }

    @Override
    public void onHelpPostClicked() {
        Fragment helpPost = new PostHelp();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                helpPost).commitNow();
    }

    @Override
    public void onHelpPostCancelClicked() {
        Fragment Question = new Question();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                Question).commitNow();
    }

    //TODO send ID of clicked item with itent
    @Override
    public void onDetailClicked() {
        Intent i = new Intent(this, DetailFlex.class);
        startActivity(i);
    }
}
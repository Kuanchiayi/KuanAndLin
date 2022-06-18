package com.example.kuanandlin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    NavigationBarView navigationBarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        openFragment(new CalculateFragment());

        /*      ButtonNavigationBar     */
        navigationBarView.getMenu().setGroupCheckable(0, false, false);
        navigationBarView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.calculate:
                        setTitle("統計");
                        openFragment(new CalculateFragment());
                        break;
                    case R.id.show:
                        setTitle("");
//                        openFragment(bulletinFragment);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    private void initView(){
        navigationBarView = findViewById(R.id.navigation);
    }

    private void openFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container_main, fragment);
        fragmentTransaction.commit();
    }
}
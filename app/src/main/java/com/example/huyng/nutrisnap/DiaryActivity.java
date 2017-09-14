package com.example.huyng.nutrisnap;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

public class DiaryActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FrameLayout diaryLayout = (FrameLayout) findViewById(R.id.diary);
            FrameLayout progressLayout = (FrameLayout) findViewById(R.id.progress);
            switch (item.getItemId()) {
                case R.id.navigation_diary:
                    diaryLayout.setVisibility(View.VISIBLE);
                    progressLayout.setVisibility(View.GONE);
                    setTitle("Diary");
                    return true;
                case R.id.navigation_progress:
                    diaryLayout.setVisibility(View.GONE);
                    progressLayout.setVisibility(View.VISIBLE);
                    setTitle("Progress");
                    return true;
            }

            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Load Diary Fragment by default
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.diary, DiaryFragment.newInstance());
        transaction.replace(R.id.progress, ProgressFragment.newInstance());
        transaction.commit();
    }

}

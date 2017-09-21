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
    private final int DIARY_SELECTED = 0;
    private final int PROGRESS_SELECTED = 1;
    int selectedFragment;
    DiaryFragment diaryFragment;
    ProgressFragment progressFragment;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FrameLayout diaryLayout = (FrameLayout) findViewById(R.id.diary);
            FrameLayout progressLayout = (FrameLayout) findViewById(R.id.progress);
            switch (item.getItemId()) {
                case R.id.navigation_diary:
                    selectedFragment = DIARY_SELECTED;
                    diaryLayout.setVisibility(View.VISIBLE);
                    progressLayout.setVisibility(View.GONE);
                    setSupportActionBar(diaryFragment.getToolbar());
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                    return true;
                case R.id.navigation_progress:
                    selectedFragment = PROGRESS_SELECTED;
                    diaryLayout.setVisibility(View.GONE);
                    progressLayout.setVisibility(View.VISIBLE);
                    setSupportActionBar(progressFragment.getToolbar());
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
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
        diaryFragment = DiaryFragment.newInstance();
        progressFragment = ProgressFragment.newInstance();
        transaction.replace(R.id.diary, diaryFragment );
        transaction.replace(R.id.progress, progressFragment);
        transaction.commit();
        selectedFragment = DIARY_SELECTED;

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (selectedFragment == DIARY_SELECTED) {
            setSupportActionBar(diaryFragment.getToolbar());
        }
        else
            setSupportActionBar(progressFragment.getToolbar());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle(" ");

    }
    public void toggleCalendar(View view) {
        if (selectedFragment == DIARY_SELECTED) {
            diaryFragment.toggleCalendar(view);
        }

    }
}

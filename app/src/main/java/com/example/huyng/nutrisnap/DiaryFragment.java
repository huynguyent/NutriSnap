package com.example.huyng.nutrisnap;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;

import com.example.huyng.nutrisnap.database.Entry;
import com.example.huyng.nutrisnap.database.EntryRepository;
import com.example.huyng.nutrisnap.database.FoodRepository;

import java.util.List;

public class DiaryFragment extends Fragment {
    private Activity mContext;
    private Toolbar toolbar;
    EntryRepository entryRepository;
    FoodRepository foodRepository;
    private CalendarView calendarView;
    private List<Entry> entryList;
    private LinearLayoutManager llm;
    private  DiaryRecyclerAdapter adapter;
    private boolean expanded;

    public DiaryFragment() {
        // Required empty public constructor
    }

    public static DiaryFragment newInstance() {
        DiaryFragment fragment = new DiaryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        entryRepository = new EntryRepository(mContext);
        foodRepository = new FoodRepository(mContext);
        entryList = entryRepository.getAllEntry();
        adapter = new DiaryRecyclerAdapter(mContext, foodRepository, entryList);
        llm = new LinearLayoutManager(mContext);
        expanded = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diary, container, false);


        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Set up tool bar
        toolbar = (Toolbar) getView().findViewById(R.id.toolbar);

        //Disable appbar scrolling
        AppBarLayout appBar = (AppBarLayout) getView().findViewById(R.id.app_bar);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) appBar.getLayoutParams();
        AppBarLayout.Behavior appBarLayoutBehaviour = new AppBarLayout.Behavior();
        appBarLayoutBehaviour.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
            @Override
            public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                return false;
            }
        });
        layoutParams.setBehavior(appBarLayoutBehaviour);
        /*
        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                RecyclerView rv = (RecyclerView) getView().findViewById(R.id.rv);
                ImageView arrow = (ImageView) getView().findViewById(R.id.arrow);
                Log.d("AAA", String.valueOf(verticalOffset));
                if (Math.abs(verticalOffset) == Math.abs(appBarLayout.getTotalScrollRange())) {
                    expanded = false;
                    ViewCompat.animate(arrow).rotation(0).start();
                    rv.setNestedScrollingEnabled(false);
                }
                else if (verticalOffset == 0) {
                    expanded = true;
                    rv.setNestedScrollingEnabled(true);
                    ViewCompat.animate(arrow).rotation(180).start();
                }
            }
        });
        */

        // Set up RecyclerView to display results
        RecyclerView rv = (RecyclerView) getView().findViewById(R.id.rv);
        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void toggleCalendar(View view) {
        AppBarLayout appBar = (AppBarLayout) getView().findViewById(R.id.app_bar);
        RecyclerView rv = (RecyclerView) getView().findViewById(R.id.rv);
        ImageView arrow = (ImageView) getView().findViewById(R.id.arrow);
        expanded = !expanded;
        if (expanded) {
            ViewCompat.animate(arrow).rotation(180).start();
        } else {
            ViewCompat.animate(arrow).rotation(0).start();
        }
        appBar.setExpanded(expanded,true);
    }
}

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.huyng.nutrisnap.database.Entry;
import com.example.huyng.nutrisnap.database.EntryRepository;
import com.example.huyng.nutrisnap.database.FoodRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
        expanded = false;

        // Initialize database
        entryRepository = new EntryRepository(mContext);
        foodRepository = new FoodRepository(mContext);

        // Initialize recycler view
        Date today = Calendar.getInstance().getTime();
        entryList = new ArrayList<Entry>();
        entryList.addAll(entryRepository.findEntryByDate(today));
        adapter = new DiaryRecyclerAdapter(mContext, foodRepository, entryList);
        llm = new LinearLayoutManager(mContext);

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

        // Set up RecyclerView to display results
        final RecyclerView rv = (RecyclerView) getView().findViewById(R.id.rv);
        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);

        // Listener for Calendar View
        CalendarView calendarView = (CalendarView) getView().findViewById(R.id.calendar);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView,
                                            int year, int month, int day) {
                // Get date from CalendarView
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.DAY_OF_MONTH, day);
                Date date = cal.getTime();
                Log.d("TAG", date.toString());

                // Update data for recyclerview
                entryList.clear();
                entryList.addAll(entryRepository.findEntryByDate(date));
                adapter.notifyDataSetChanged();
                ((AppBarLayout) getView().findViewById(R.id.app_bar)).setExpanded(false);
                expanded = false;

                // Update title to Today, Yesterday, or otherwise dd/MM/YYYY
                Calendar todayCal = Calendar.getInstance();

                TextView textView = (TextView) getView().findViewById(R.id.title_text);
                if (cal.get(Calendar.YEAR) == todayCal.get(Calendar.YEAR)
                        && cal.get(Calendar.DAY_OF_YEAR) == todayCal.get(Calendar.DAY_OF_YEAR)) {
                    textView.setText("Today");
                } else {
                    todayCal.add(Calendar.DAY_OF_YEAR, -1);
                    if (cal.get(Calendar.YEAR) == todayCal.get(Calendar.YEAR)
                            && cal.get(Calendar.DAY_OF_YEAR) == todayCal.get(Calendar.DAY_OF_YEAR)) {
                        textView.setText("Yesterday");
                    }
                    else {
                        SimpleDateFormat df = new SimpleDateFormat("dd/MM/YYYY");
                        textView.setText(df.format(date));
                    }
                }


            }
        });
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

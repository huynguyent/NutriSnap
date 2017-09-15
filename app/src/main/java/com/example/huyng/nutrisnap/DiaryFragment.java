package com.example.huyng.nutrisnap;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.huyng.nutrisnap.database.Entry;
import com.example.huyng.nutrisnap.database.EntryRepository;
import com.example.huyng.nutrisnap.database.FoodRepository;

import java.util.List;

public class DiaryFragment extends Fragment {
    private Activity mContext;
    private Toolbar toolbar;
    EntryRepository entryRepository;
    FoodRepository foodRepository;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diary, container, false);
        // Set up tool bar
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("Diary");
        // Set up RecyclerView to display results
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        rv.setLayoutManager(llm);
        // Initialize RecyclerAdapter
        List<Entry> entryList = entryRepository.getAllEntry();
        DiaryRecyclerAdapter adapter = new DiaryRecyclerAdapter(mContext, foodRepository, entryList);
        rv.setAdapter(adapter);

        // Inflate the layout for this fragment
        return view;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    public Toolbar getToolbar() {
        return toolbar;
    }
}

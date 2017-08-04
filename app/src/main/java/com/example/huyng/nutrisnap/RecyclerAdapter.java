package com.example.huyng.nutrisnap;

import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import static android.content.ContentValues.TAG;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int HEADER_VIEW = 1;
    private static final int LOADING_VIEW = 2;

    Bitmap headerBitmap;
    Boolean showLoading;
    List<FoodInfo> foodInfos;

    RecyclerAdapter (Bitmap headerBitmap, Boolean showLoading, List<FoodInfo> foodInfos) {
        this.headerBitmap = headerBitmap;
        this.showLoading = showLoading;
        this.foodInfos = foodInfos;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if (viewType == HEADER_VIEW) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_header, parent, false);
            HeaderHolder holder = new HeaderHolder(v);
            return holder;
        }
        else if (viewType == LOADING_VIEW) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_loading, parent, false);
            LoadingHolder holder = new LoadingHolder(v);
            return holder;
        }
        else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_row, parent, false);
            FoodInfoHolder holder = new FoodInfoHolder(v);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {
        try {
            if (holder instanceof HeaderHolder) {
                HeaderHolder vh = (HeaderHolder) holder;
                Log.i(TAG, headerBitmap.toString());
                vh.headerPhoto.setImageBitmap(headerBitmap);

            }
            else if (holder instanceof LoadingHolder) {
                LoadingHolder vh = (LoadingHolder) holder;
                //vh.loadingPanel.setVisibility();
            }
            else {
                FoodInfoHolder vh = (FoodInfoHolder) holder;
                FoodInfo foodInfo = foodInfos.get(i - 2);
                vh.foodName.setText(foodInfo.name);
                vh.foodCal.setText("Calories: " + foodInfo.cal);
                vh.foodProtein.setText("Protein: " + foodInfo.protein + " g");
                vh.foodFat.setText("Fat: " + foodInfo.fat + " g");
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }

        //holder.foodCal.setText(foodInfos.get(i).cal);
    }

    @Override
    public int getItemCount() {
        if (foodInfos == null)
            return 0;
        return foodInfos.size() + 2;
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEADER_VIEW;
        }
        else if (position == 1) {
            return LOADING_VIEW;
        }
        return super.getItemViewType(position);
    }



    /*
     ******************************************************************
     *  SUB CLASSES
     ******************************************************************
     */

    /* HeadeHolder that display food image */
    public static class HeaderHolder extends RecyclerView.ViewHolder {
        CardView cv;
        ImageView headerPhoto;

        public HeaderHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            headerPhoto = (ImageView)itemView.findViewById(R.id.header_photo);
        }
    }

    /* LoadingHolder that display loading panel */
    public static class LoadingHolder extends RecyclerView.ViewHolder {
        CardView cv;
        View loadingPanel;
        public LoadingHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            loadingPanel = (View) itemView.findViewById(R.id.loading_panel);
        }
    }

    /* FoodInfoHolder that displays food information */
    public static class FoodInfoHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView foodName;
        TextView foodCal;
        TextView foodProtein;
        TextView foodFat;

        FoodInfoHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            foodName = (TextView)itemView.findViewById(R.id.food_name);
            foodCal = (TextView)itemView.findViewById(R.id.food_cal);
            foodProtein = (TextView)itemView.findViewById(R.id.food_protein);
            foodFat = (TextView)itemView.findViewById(R.id.food_fat);

        }
    }

}

package com.example.huyng.nutrisnap;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.huyng.nutrisnap.database.Food;

import java.util.List;

import static android.content.ContentValues.TAG;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int HEADER_VIEW = 1;
    private static final int LOADING_VIEW = 2;

    private static Activity context;
    private AddFoodDialogFragment dialog;
    Bitmap headerBitmap;
    Boolean showLoading;
    List<Food> foodInfos;

    RecyclerAdapter (Activity context, Bitmap headerBitmap, Boolean showLoading, List<Food> foodInfos) {
        this.context = context;
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
                final Food foodInfo = foodInfos.get(i - 2);

                // Display food information
                vh.foodName.setText(foodInfo.getName());
                vh.foodCal.setText("Calories: " + foodInfo.getCalories());
                vh.foodCarb.setText("Carb: " + foodInfo.getCarb() + " g");
                vh.foodProtein.setText("Protein: " + foodInfo.getProtein() + " g");
                vh.foodFat.setText("Fat: " + foodInfo.getFat() + " g");
                String serving = "Serving: " + foodInfo.getServing() + " g";
                if (!foodInfo.getUnit().equals(""))
                    serving += " (per " + foodInfo.getUnit() +")";
                vh.foodServing.setText(serving);

                // Set on click listener for add button
                vh.addBtn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v)
                    {
                        dialog = new AddFoodDialogFragment();
                        Bundle args = new Bundle();
                        args.putString("code", foodInfo.getFoodCode());
                        args.putString("name", foodInfo.getName());
                        args.putString("unit", foodInfo.getUnit());
                        args.putInt("serving", foodInfo.getServing());
                        args.putInt("cal", foodInfo.getCalories());
                        args.putDouble("carb", foodInfo.getCarb());
                        args.putDouble("protein", foodInfo.getProtein());
                        args.putDouble("fat", foodInfo.getFat());
                        dialog.setArguments(args);
                        dialog.show(context.getFragmentManager(), "add_food");
                    }
                });

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
        TextView foodCarb;
        TextView foodProtein;
        TextView foodFat;
        TextView foodServing;
        ImageView addBtn;

        FoodInfoHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            foodName = (TextView)itemView.findViewById(R.id.food_name);
            foodServing = (TextView)itemView.findViewById(R.id.food_serving);
            foodCal = (TextView)itemView.findViewById(R.id.food_cal);
            foodCarb = (TextView)itemView.findViewById(R.id.food_carb);
            foodProtein = (TextView)itemView.findViewById(R.id.food_protein);
            foodFat = (TextView)itemView.findViewById(R.id.food_fat);
            addBtn = (ImageView)itemView.findViewById(R.id.add_btn);
        }
    }

}

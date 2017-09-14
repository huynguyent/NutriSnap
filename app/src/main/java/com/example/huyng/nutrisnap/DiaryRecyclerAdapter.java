package com.example.huyng.nutrisnap;


import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.huyng.nutrisnap.database.Entry;
import com.example.huyng.nutrisnap.database.Food;
import com.example.huyng.nutrisnap.database.FoodRepository;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class DiaryRecyclerAdapter extends RecyclerView.Adapter<DiaryRecyclerAdapter.EntryViewHolder> {
    private List<Entry> entryList;
    private Context context;
    private FoodRepository foodRepository;

    public DiaryRecyclerAdapter(Context context, FoodRepository foodRepository, List<Entry> entryList) {
        this.entryList = entryList;
        this.context = context;
        this.foodRepository = foodRepository;
    }

    @Override
    public DiaryRecyclerAdapter.EntryViewHolder onCreateViewHolder(ViewGroup parent,
                                                                   int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_entry, parent, false);

        return new EntryViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(EntryViewHolder holder, int position) {
        Entry entry = entryList.get(position);
        int amount = entry.getAmount();
        Food food = foodRepository.findFoodByCode(entry.getFoodCode());
        holder.foodName.setText(food.getName());
        holder.foodCal.setText(String.valueOf(amount * food.getCalories()));
        // Display food amount
        String amountText;
        if (food.getUnit() == null)
            amountText = String.valueOf(amount * 100) + "g";
        else {
            amountText = String.valueOf(amount) + " " + food.getUnit();
            if (amount > 1) // Change to plural if more than 1
                amountText += "s";
        }
        holder.foodAmount.setText(amountText);
        // Display food info
        NumberFormat nf = new DecimalFormat("##.#");
        String infoText = String.format(Locale.getDefault(),
                                        "%sg fat , %sg carbs , %sg protein",
                                        nf.format(food.getFat() * amount),
                                        nf.format(food.getCarb() * amount),
                                        nf.format(food.getProtein() * amount));
        holder.foodInfo.setText(infoText);
        // Display image
        ContextWrapper cw = new ContextWrapper(context);
        Uri uri = Uri.parse(cw.getDir("imageDir", Context.MODE_PRIVATE).getAbsolutePath()
                + "/" + entry.getImage());
        holder.foodImage.setImageURI(uri);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return entryList.size();
    }


    public static class EntryViewHolder extends RecyclerView.ViewHolder {
        TextView foodName;
        TextView foodCal;
        TextView entryTime;
        TextView foodInfo;
        TextView foodAmount;
        ImageView foodImage;

        public EntryViewHolder(View view) {
            super(view);
            foodName = (TextView) view.findViewById(R.id.food_name);
            foodAmount = (TextView) view.findViewById(R.id.food_amount);
            foodCal = (TextView) view.findViewById(R.id.food_cal);
            foodInfo = (TextView) view.findViewById(R.id.food_info);
            foodImage = (ImageView) view.findViewById(R.id.entry_image);
        }
    }
}

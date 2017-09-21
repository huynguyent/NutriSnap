package com.example.huyng.nutrisnap;


import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.huyng.nutrisnap.database.Entry;
import com.example.huyng.nutrisnap.database.Food;
import com.example.huyng.nutrisnap.database.FoodRepository;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DiaryRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int CHART_VIEW = 1;

    private List<Entry> entryList;
    private Context context;
    private FoodRepository foodRepository;

    public DiaryRecyclerAdapter(Context context, FoodRepository foodRepository, List<Entry> entryList) {
        this.entryList = entryList;
        this.context = context;
        this.foodRepository = foodRepository;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                   int viewType) {
        View v;
        if (viewType == CHART_VIEW) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_piechart, parent, false);
            return new ChartViewHolder(v);
        }
        else {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cardview_entry, parent, false);
            return new EntryViewHolder(v);
        }


    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ChartViewHolder) {
            ChartViewHolder vh = (ChartViewHolder) holder;
            if (entryList.size() == 0) {
                vh.messageText.setVisibility(View.VISIBLE);
                vh.chart.setVisibility(View.GONE);
            }
            else {
                vh.messageText.setVisibility(View.GONE);
                vh.chart.setVisibility(View.VISIBLE);
                // Calculate chart values
                float fat = 0f;
                float carbs = 0f;
                float protein = 0f;
                for (Entry entry : entryList) {
                    Food food = foodRepository.findFoodByCode(entry.getFoodCode());
                    fat += food.getFat() * entry.getAmount();
                    carbs += food.getCarb() * entry.getAmount();
                    protein += food.getProtein() * entry.getAmount();
                }
                float total = fat + carbs + protein;

                // Set up Pie Chart
                List<PieEntry> entries = new ArrayList<>();
                entries.add(new PieEntry(fat, "Fat"));
                entries.add(new PieEntry(carbs, "Carbs"));
                entries.add(new PieEntry(protein, "Protein"));
                PieDataSet set = new PieDataSet(entries, "");
                set.setColors(ColorTemplate.MATERIAL_COLORS);
                set.setValueTextSize(15f);
                PieData data = new PieData(set);
                vh.chart.setData(data);
                vh.chart.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                vh.chart.getDescription().setEnabled(false);
                vh.chart.getLegend().setEnabled(false);
                vh.chart.animateX(700);
            }
        }
        else {
            EntryViewHolder vh = (EntryViewHolder) holder;
            Entry entry = entryList.get(position - 1);
            int amount = entry.getAmount();
            Food food = foodRepository.findFoodByCode(entry.getFoodCode());
            vh.foodName.setText(food.getName());
            vh.foodCal.setText(String.valueOf(amount * food.getCalories()));
            // Display food amount
            String amountText;
            if (food.getUnit() == null)
                amountText = String.valueOf(amount * 100) + "g";
            else {
                amountText = String.valueOf(amount) + " " + food.getUnit();
                if (amount > 1) // Change to plural if more than 1
                    amountText += "s";
            }
            vh.foodAmount.setText(amountText);
            // Display food info
            NumberFormat nf = new DecimalFormat("##.#");
            String infoText = String.format(Locale.getDefault(),
                    "%sg fat , %sg carbs , %sg protein",
                    nf.format(food.getFat() * amount),
                    nf.format(food.getCarb() * amount),
                    nf.format(food.getProtein() * amount));
            vh.foodInfo.setText(infoText);
            // Display image
            ContextWrapper cw = new ContextWrapper(context);
            Uri uri = Uri.parse(cw.getDir("imageDir", Context.MODE_PRIVATE).getAbsolutePath()
                    + "/" + entry.getImage());
            vh.foodImage.setImageURI(uri);
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (entryList == null)
            return 0;
        return entryList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return CHART_VIEW;
        }
        return super.getItemViewType(position);
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

    /* LoadingHolder that display loading panel */
    public static class ChartViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        PieChart chart;
        TextView messageText;

        public ChartViewHolder(View itemView) {
            super(itemView);
            //cv = (CardView)itemView.findViewById(R.id.cv);
            chart = (PieChart) itemView.findViewById(R.id.chart);
            messageText = (TextView) itemView.findViewById(R.id.message_text);
        }
    }


}

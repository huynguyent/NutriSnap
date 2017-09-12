package com.example.huyng.nutrisnap;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

import static com.example.huyng.nutrisnap.R.id.fat;

public class AddFoodDialogFragment extends DialogFragment {
    DialogListener mListener;
    private Bundle args;
    private TextView amountTxt;
    private Button addBtn;
    private Button minusBtn;
    private TextView weightTxt;
    private TextView calTxt;
    private TextView carbTxt;
    private TextView proteinTxt;
    private TextView fatTxt;
    private int amount;
    private String code;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View addFoodView = inflater.inflate(R.layout.dialog_addfood, null);
        args = getArguments();
        builder.setTitle("Add to diary - " + args.getString("name"))
                .setView(addFoodView)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        amount = Integer.parseInt(amountTxt.getText().toString());
                        code = args.getString("code");
                        mListener.onDialogPositiveClick(AddFoodDialogFragment.this);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogNegativeClick(AddFoodDialogFragment.this);
                    }
                });
        TextView unitTxt = (TextView) addFoodView.findViewById(R.id.unit);
        amountTxt = (TextView) addFoodView.findViewById(R.id.amount);
        addBtn = (Button) addFoodView.findViewById(R.id.add_btn);
        minusBtn = (Button) addFoodView.findViewById(R.id.minus_btn);
        weightTxt = (TextView) addFoodView.findViewById(R.id.weight);
        calTxt = (TextView) addFoodView.findViewById(R.id.cal);
        carbTxt = (TextView) addFoodView.findViewById(R.id.carb);
        proteinTxt = (TextView) addFoodView.findViewById(R.id.protein);
        fatTxt = (TextView) addFoodView.findViewById(fat);

        // Set on click listeners for add and minus buttons
        addBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int amount = Integer.parseInt(amountTxt.getText().toString());
                amount += 1;
                amountTxt.setText(String.valueOf(amount));
                updateText(amount);
            }
        });
        minusBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int amount = Integer.parseInt(amountTxt.getText().toString());
                if (amount > 1) {
                    amount -= 1;
                    amountTxt.setText(String.valueOf(amount));
                    updateText(amount);
                }
            }
        });
        amountTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String content = amountTxt.getText().toString();
                if (!content.equals("")) {
                    int amount = Integer.parseInt(content);
                    if (amount < 1)
                        amountTxt.setText("1");
                    updateText(amount);
                }
            }
        });
        updateText(1);
        return builder.create();
    }

    @Override
    public void onAttach(Context context ) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (DialogListener) context;
            Log.d("AddDialog", context.toString());
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    private void updateText(int amount) {
        int weight = amount * args.getInt("serving");
        weightTxt.setText(String.format(Locale.getDefault(),"Weight: %d g", weight));
        int cal = amount * args.getInt("cal");
        calTxt.setText(String.format(Locale.getDefault(),"Calories: %d ", cal));
        double protein = amount * args.getDouble("protein");
        proteinTxt.setText(String.format(Locale.getDefault(), "Protein: %.1f g", protein));
        double fat = amount * args.getDouble("fat");
        fatTxt.setText(String.format(Locale.getDefault(), "Fat: %.1f g", fat));
    }

    public interface DialogListener {
        public void onDialogPositiveClick(AddFoodDialogFragment dialog);
        public void onDialogNegativeClick(AddFoodDialogFragment dialog);
    }

    public int getAmount() {
        return amount;
    }

    public String getCode() {
        return code;
    }
}

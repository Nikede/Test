package com.nikede.test.Fragments;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.nikede.test.Data.CreditLab;
import com.nikede.test.Data.DataReceiver;
import com.nikede.test.Data.DebitLab;
import com.nikede.test.Models.CreditByPartner;
import com.nikede.test.Models.DebitByPartner;
import com.nikede.test.R;
import com.nikede.test.database.DebitAndCreditBaseHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {

    PieChart chartCreditByPartner;
    PieChart chartDebitByPartner;
    List<CreditByPartner> creditsByPartner;
    List<DebitByPartner> debitsByPartner;
    private static final String TAG = "MainFragment";
    boolean exception = false;
    String exceptionText = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);

        chartCreditByPartner = view.findViewById(R.id.chartCreditByPartner);
        chartDebitByPartner = view.findViewById(R.id.chartDebitByPartner);
        creditsByPartner = CreditLab.get(getActivity().getApplicationContext()).getCreditsByPartner();
        debitsByPartner = DebitLab.get(getActivity().getApplicationContext()).getDebitsByPartner();


        Button resetButton = (Button) view.findViewById(R.id.Reset);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FetchItemsTask().execute();
            }
        });

        chartCreditByPartner.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Дебит")
                        .setMessage("На на партнёра: " + ((CreditByPartner) e.getData()).getPartner() +
                                "  сумма - " + (Double.toString(((CreditByPartner) e.getData()).getSum())))
                        .setCancelable(false)
                        .setNegativeButton("ОК",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

        chartDebitByPartner.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Дебит")
                        .setMessage("На на партнёра: " + ((DebitByPartner) e.getData()).getPartner() +
                                "  сумма - " + (Double.toString(((DebitByPartner) e.getData()).getSum())))
                        .setCancelable(false)
                        .setNegativeButton("ОК",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

        if (creditsByPartner.size() == 0 && debitsByPartner.size() == 0)
            new FetchItemsTask().execute();

        updateUI(chartCreditByPartner, chartDebitByPartner, creditsByPartner, debitsByPartner);

        return view;
    }

    private void updateUI(PieChart chartCreditByPartner, PieChart chartDebitByPartner, List<CreditByPartner> creditsByPartner, List<DebitByPartner> debitsByPartner) {
        List<PieEntry> pieEntries = new ArrayList<>();
        for (DebitByPartner debitByPartner : debitsByPartner) {
            pieEntries.add(new PieEntry((float) debitByPartner.getSum(), debitByPartner.getPartner(), debitByPartner));
        }
        PieDataSet pieDataSet = new PieDataSet(pieEntries, "Debit by partner");
        pieDataSet.setHighlightEnabled(true);
        pieDataSet.setSliceSpace(3f);
        pieDataSet.setSelectionShift(5f);
        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        PieData pieData = new PieData(pieDataSet);
        chartDebitByPartner.setData(pieData);
        chartDebitByPartner.setUsePercentValues(true);
        chartDebitByPartner.getDescription().setEnabled(false);
        chartDebitByPartner.setDragDecelerationFrictionCoef(0.99f);
        chartDebitByPartner.setDrawHoleEnabled(true);
        chartDebitByPartner.setHoleColor(Color.WHITE);
        chartDebitByPartner.setTransparentCircleRadius(60f);
        chartDebitByPartner.invalidate();

        List<PieEntry> pieEntries1 = new ArrayList<>();
        for (CreditByPartner creditByPartner : creditsByPartner) {
            pieEntries1.add(new PieEntry((float) creditByPartner.getSum(), creditByPartner.getPartner(), creditByPartner));
        }
        PieDataSet pieDataSet1 = new PieDataSet(pieEntries1, "Credit by partner");
        pieDataSet1.setHighlightEnabled(true);
        pieDataSet1.setSliceSpace(3f);
        pieDataSet1.setSelectionShift(5f);
        pieDataSet1.setColors(ColorTemplate.JOYFUL_COLORS);
        PieData pieData1 = new PieData(pieDataSet1);
        chartCreditByPartner.setData(pieData1);
        chartCreditByPartner.setUsePercentValues(true);
        chartCreditByPartner.getDescription().setEnabled(false);
        chartCreditByPartner.setDragDecelerationFrictionCoef(0.99f);
        chartCreditByPartner.setDrawHoleEnabled(true);
        chartCreditByPartner.setHoleColor(Color.WHITE);
        chartCreditByPartner.setTransparentCircleRadius(60f);
        chartCreditByPartner.invalidate();
    }

    private class FetchItemsTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                String result = new DataReceiver()
                        .getUrlString("https://ws-tszh.vdgb-soft.ru/api/mobile/test/test?mode=all");
                Log.i(TAG, "Fetched contents of URL: " + result);
                JSONObject jsonBody = new JSONObject(result);
                new DataReceiver().parseAllItems(DebitLab.get(getActivity().getApplicationContext()), CreditLab.get(getActivity().getApplicationContext()), jsonBody);

            } catch (IOException ioe) {
                Log.e(TAG, "Failed to fetch URL: ", ioe);
                exception = true;
                exceptionText = "Failed to fetch URL";
            } catch (JSONException je){
                Log.e(TAG, "Failed to parse JSON", je);
                exception = true;
                exceptionText = "Failed to parse JSON";
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (exception) {
                Toast.makeText(getActivity().getApplicationContext(), exceptionText, Toast.LENGTH_SHORT).show();
                exception = false;
                return;
            }
            creditsByPartner = CreditLab.get(getActivity().getApplicationContext()).getCreditsByPartner();
            debitsByPartner = DebitLab.get(getActivity().getApplicationContext()).getDebitsByPartner();
            updateUI(chartCreditByPartner, chartDebitByPartner, creditsByPartner, debitsByPartner);
        }
    }
}

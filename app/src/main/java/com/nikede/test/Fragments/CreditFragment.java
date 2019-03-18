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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.nikede.test.Data.CreditLab;
import com.nikede.test.Data.DataReceiver;
import com.nikede.test.Data.DebitLab;
import com.nikede.test.Models.CreditByDate;
import com.nikede.test.Models.CreditByPartner;
import com.nikede.test.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreditFragment extends Fragment {

    TableLayout tableByDate;
    TableLayout tableByPartner;
    LineChart chartByDate;
    PieChart chartByPartner;
    List<CreditByDate> creditsByDate;
    List<CreditByPartner> creditsByPartner;
    private static final String TAG = "CreditFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.debit_and_credit_fragment, container, false);

        tableByDate = (TableLayout) view.findViewById(R.id.tableDebitByDate);
        tableByPartner = (TableLayout) view.findViewById(R.id.tableDebitByPartner);
        chartByDate = (LineChart) view.findViewById(R.id.chartByDate);
        chartByPartner = (PieChart) view.findViewById(R.id.chartByPartner);
        creditsByDate = CreditLab.get(getActivity().getApplicationContext()).getCreditsByDate();
        creditsByPartner = CreditLab.get(getActivity().getApplicationContext()).getCreditsByPartner();

        chartByDate.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Дебит")
                        .setMessage("На дату: " + ((CreditByDate) e.getData()).getDate().toString() +
                                "  сумма - " + (Double.toString(((CreditByDate) e.getData()).getSum())))
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

        chartByPartner.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
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

        final Button drawGraphs = (Button) view.findViewById(R.id.graph);
        drawGraphs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawGraphs(chartByDate, chartByPartner, creditsByDate, creditsByPartner);
            }
        });

        final Button drawTables = (Button) view.findViewById(R.id.table);
        drawTables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawTables(tableByDate, tableByPartner, creditsByDate, creditsByPartner);
            }
        });

        Button resetButton = (Button) view.findViewById(R.id.Reset);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FetchItemsTask().execute();
            }
        });

        drawTables(tableByDate, tableByPartner, creditsByDate, creditsByPartner);

        return view;
    }

    private void drawGraphs(LineChart chartByDate, PieChart chartByPartner, List<CreditByDate> creditsByDate, List<CreditByPartner> creditsByPartner) {
        tableByDate.setVisibility(View.GONE);
        tableByPartner.setVisibility(View.GONE);
        chartByDate.clear();
        chartByPartner.clear();

        List<Entry> entries = new ArrayList<>();
        for (CreditByDate creditByDate : creditsByDate) {
            entries.add(new Entry(creditByDate.getDate().getTime(), (float) creditByDate.getSum(), creditByDate));
        }
        LineDataSet dataSet = new LineDataSet(entries, "Credit by date");
        dataSet.setHighlightEnabled(true);
        dataSet.setDrawHighlightIndicators(true);
        dataSet.setHighLightColor(Color.BLACK);
        LineData lineData = new LineData(dataSet);
        chartByDate.setData(lineData);
        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return new SimpleDateFormat("dd-MM-yyyy").format(new Date((long) value));
            }
        };

        XAxis xAxis = chartByDate.getXAxis();
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(formatter);
        chartByDate.getDescription().setEnabled(false);
        chartByDate.invalidate();

        List<PieEntry> pieEntries = new ArrayList<>();
        for (CreditByPartner creditByPartner : creditsByPartner) {
            pieEntries.add(new PieEntry((float) creditByPartner.getSum(), creditByPartner.getPartner(), creditByPartner));
        }
        PieDataSet pieDataSet = new PieDataSet(pieEntries, "Credit by partner");
        pieDataSet.setHighlightEnabled(true);
        pieDataSet.setSliceSpace(3f);
        pieDataSet.setSelectionShift(5f);
        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        PieData pieData = new PieData(pieDataSet);
        chartByPartner.setData(pieData);
        chartByPartner.setUsePercentValues(true);
        chartByPartner.getDescription().setEnabled(false);
        chartByPartner.setDragDecelerationFrictionCoef(0.99f);
        chartByPartner.setDrawHoleEnabled(true);
        chartByPartner.setHoleColor(Color.WHITE);
        chartByPartner.setTransparentCircleRadius(60f);
        chartByDate.invalidate();

        chartByPartner.setVisibility(View.VISIBLE);
        chartByDate.setVisibility(View.VISIBLE);
    }

    private void drawTables(TableLayout tableByDate, TableLayout tableByPartner, List<CreditByDate> creditsByDate, List<CreditByPartner> creditsByPartner) {
        chartByDate.setVisibility(View.GONE);
        chartByPartner.setVisibility(View.GONE);
        tableByDate.removeAllViewsInLayout();
        tableByPartner.removeAllViewsInLayout();

        TableRow tableRow = new TableRow(getActivity().getApplicationContext());
        TextView sum = new TextView(getActivity().getApplicationContext());
        sum.setText("Сумма");
        sum.setGravity(Gravity.CENTER_HORIZONTAL);
        sum.setTextColor(0xff000000);
        sum.setPadding(5, 5, 5, 5);
        TextView date = new TextView(getActivity().getApplicationContext());
        date.setText("Дата");
        date.setGravity(Gravity.CENTER_HORIZONTAL);
        date.setTextColor(0xff000000);
        date.setPadding(5, 5, 5, 5);
        tableRow.addView(sum);
        tableRow.addView(date);
        tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
        tableByDate.addView(tableRow);

        for (CreditByDate creditByDate : creditsByDate) {
            tableRow = new TableRow(getActivity().getApplicationContext());
            sum = new TextView(getActivity().getApplicationContext());
            sum.setText(Double.toString(Math.round(creditByDate.getSum())));
            sum.setGravity(Gravity.CENTER_HORIZONTAL);
            sum.setTextColor(0xff000000);
            sum.setPadding(5, 5, 5, 5);
            date = new TextView(getActivity().getApplicationContext());
            date.setText(creditByDate.getDate().toString());
            date.setGravity(Gravity.CENTER_HORIZONTAL);
            date.setTextColor(0xff000000);
            date.setPadding(5, 5, 5, 5);
            tableRow.addView(sum);
            tableRow.addView(date);
            tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
            tableByDate.addView(tableRow);
        }

        tableRow = new TableRow(getActivity().getApplicationContext());
        sum = new TextView(getActivity().getApplicationContext());
        sum.setText("Сумма");
        sum.setGravity(Gravity.CENTER_HORIZONTAL);
        sum.setTextColor(0xff000000);
        sum.setPadding(5, 5, 5, 5);
        TextView partner = new TextView(getActivity().getApplicationContext());
        partner.setText("Партнёр");
        partner.setGravity(Gravity.CENTER_HORIZONTAL);
        partner.setTextColor(0xff000000);
        partner.setPadding(5, 5, 5, 5);
        tableRow.addView(sum);
        tableRow.addView(partner);
        tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
        tableByPartner.addView(tableRow);

        for (CreditByPartner creditByPartner : creditsByPartner) {
            tableRow = new TableRow(getActivity().getApplicationContext());
            sum = new TextView(getActivity().getApplicationContext());
            sum.setText(Double.toString(Math.round(creditByPartner.getSum())));
            sum.setGravity(Gravity.CENTER_HORIZONTAL);
            sum.setTextColor(0xff000000);
            sum.setPadding(5, 5, 5, 5);
            partner = new TextView(getActivity().getApplicationContext());
            partner.setText(creditByPartner.getPartner());
            partner.setGravity(Gravity.CENTER_HORIZONTAL);
            partner.setTextColor(0xff000000);
            partner.setPadding(5, 5, 5, 5);
            tableRow.addView(sum);
            tableRow.addView(partner);
            tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
            tableByPartner.addView(tableRow);
        }

        tableByDate.setVisibility(View.VISIBLE);
        tableByPartner.setVisibility(View.VISIBLE);
    }



    private class FetchItemsTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                String result = new DataReceiver()
                        .getUrlString("https://ws-tszh.vdgb-soft.ru/api/mobile/test/test?mode=credit");
                Log.i(TAG, "Fetched contents of URL: " + result);
                JSONObject jsonBody = new JSONObject(result);
                new DataReceiver().parseCreditItems(CreditLab.get(getActivity().getApplicationContext()), jsonBody);

            } catch (IOException ioe) {
                Log.e(TAG, "Failed to fetch URL: ", ioe);
                Toast.makeText(getActivity().getApplicationContext(), "Failed to fetch URL", Toast.LENGTH_SHORT).show();
            } catch (JSONException je){
                Log.e(TAG, "Failed to parse JSON", je);
                Toast.makeText(getActivity().getApplicationContext(), "Failed to parse JSON", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            creditsByDate = CreditLab.get(getContext().getApplicationContext()).getCreditsByDate();
            creditsByPartner = CreditLab.get(getContext().getApplicationContext()).getCreditsByPartner();
            drawTables(tableByDate, tableByPartner, creditsByDate, creditsByPartner);
        }
    }
}

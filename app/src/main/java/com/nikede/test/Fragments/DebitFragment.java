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
import android.view.MenuItem;
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
import com.nikede.test.Models.DebitByDate;
import com.nikede.test.Models.DebitByPartner;
import com.nikede.test.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DebitFragment extends Fragment {

    TableLayout tableByDate;
    TableLayout tableByPartner;
    TableLayout tableSingleEntry;
    LineChart chartByDate;
    PieChart chartByPartner;
    List<DebitByDate> debitsByDate;
    List<DebitByPartner> debitsByPartner;
    private static final String TAG = "DebitFragment";
    boolean exception = false;
    String exceptionText = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.debit_and_credit_fragment, container, false);

        tableByDate = (TableLayout) view.findViewById(R.id.tableDebitByDate);
        tableByPartner = (TableLayout) view.findViewById(R.id.tableDebitByPartner);
        tableSingleEntry = (TableLayout) view.findViewById(R.id.tableSingleEntry);
        chartByDate = (LineChart) view.findViewById(R.id.chartByDate);
        chartByPartner = (PieChart) view.findViewById(R.id.chartByPartner);
        debitsByDate = DebitLab.get(getActivity().getApplicationContext()).getDebitsByDate();
        debitsByPartner = DebitLab.get(getActivity().getApplicationContext()).getDebitsByPartner();

        chartByDate.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                tableSingleEntry.removeAllViewsInLayout();
                TableRow tableRow = new TableRow(getActivity().getApplicationContext());
                TextView sum = new TextView(getActivity().getApplicationContext());
                sum.setText(Double.toString(Math.round(((DebitByDate) e.getData()).getSum())));
                sum.setBackground(getResources().getDrawable(R.drawable.border));
                sum.setGravity(Gravity.CENTER_HORIZONTAL);
                sum.setTextColor(0xff000000);
                sum.setPadding(5, 5, 5, 5);
                TextView date = new TextView(getActivity().getApplicationContext());
                date.setText(new SimpleDateFormat("dd-MM-yyyy").format(((DebitByDate) e.getData()).getDate()));
                date.setBackground(getResources().getDrawable(R.drawable.border));
                date.setGravity(Gravity.CENTER_HORIZONTAL);
                date.setTextColor(0xff000000);
                date.setPadding(5, 5, 5, 5);
                tableRow.addView(sum);
                tableRow.addView(date);
                tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
                tableSingleEntry.addView(tableRow);
                tableSingleEntry.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected() {
                tableSingleEntry.setVisibility(View.GONE);
            }
        });

        chartByPartner.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                tableSingleEntry.removeAllViewsInLayout();
                TableRow tableRow = new TableRow(getActivity().getApplicationContext());
                TextView sum = new TextView(getActivity().getApplicationContext());
                sum.setText(Double.toString(Math.round(((DebitByPartner) e.getData()).getSum())));
                sum.setBackground(getResources().getDrawable(R.drawable.border));
                sum.setGravity(Gravity.CENTER_HORIZONTAL);
                sum.setTextColor(0xff000000);
                sum.setPadding(5, 5, 5, 5);
                TextView partner = new TextView(getActivity().getApplicationContext());
                partner.setText(((DebitByPartner) e.getData()).getPartner());
                partner.setBackground(getResources().getDrawable(R.drawable.border));
                partner.setGravity(Gravity.CENTER_HORIZONTAL);
                partner.setTextColor(0xff000000);
                partner.setPadding(5, 5, 5, 5);
                tableRow.addView(sum);
                tableRow.addView(partner);
                tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
                tableSingleEntry.addView(tableRow);
                tableSingleEntry.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected() {
                tableSingleEntry.setVisibility(View.GONE);
            }
        });

        final Button drawGraphs = (Button) view.findViewById(R.id.graph);
        drawGraphs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawGraphs(chartByDate, chartByPartner, debitsByDate, debitsByPartner);
            }
        });

        final Button drawTables = (Button) view.findViewById(R.id.table);
        drawTables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawTables(tableByDate, tableByPartner, debitsByDate, debitsByPartner);
            }
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        drawTables(tableByDate, tableByPartner, debitsByDate, debitsByPartner);

        return view;
    }

    private void drawGraphs(LineChart chartByDate, PieChart chartByPartner, List<DebitByDate> debitsByDate, List<DebitByPartner> debitsByPartner) {
        tableByDate.setVisibility(View.GONE);
        tableByPartner.setVisibility(View.GONE);
        chartByDate.clear();
        chartByPartner.clear();

        List<Entry> entries = new ArrayList<>();
        for (DebitByDate debitByDate : debitsByDate) {
            entries.add(new Entry(debitByDate.getDate().getTime(), (float) debitByDate.getSum(), debitByDate));
        }
        LineDataSet dataSet = new LineDataSet(entries, "Debit by date");
        dataSet.setHighlightEnabled(true);
        dataSet.setDrawHighlightIndicators(true);
        dataSet.setHighLightColor(Color.BLACK);
        dataSet.setDrawValues(false);
        LineData lineData = new LineData(dataSet);
        chartByDate.setData(lineData);
        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return new SimpleDateFormat("dd-MM").format(new Date((long) value));
            }
        };

        XAxis xAxis = chartByDate.getXAxis();
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(formatter);
        chartByDate.getDescription().setEnabled(false);
        chartByDate.invalidate();

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
        chartByPartner.setData(pieData);
        chartByPartner.setUsePercentValues(true);
        chartByPartner.getDescription().setEnabled(false);
        chartByPartner.setDragDecelerationFrictionCoef(0.99f);
        chartByPartner.setDrawHoleEnabled(true);
        chartByPartner.setHoleColor(Color.WHITE);
        chartByPartner.setTransparentCircleRadius(60f);
        chartByPartner.setEntryLabelColor(Color.BLACK);
        chartByPartner.invalidate();

        chartByPartner.setVisibility(View.VISIBLE);
        chartByDate.setVisibility(View.VISIBLE);
    }

    private void drawTables(TableLayout tableByDate, TableLayout tableByPartner, List<DebitByDate> debitsByDate, List<DebitByPartner> debitsByPartner) {
        chartByDate.setVisibility(View.GONE);
        chartByPartner.setVisibility(View.GONE);
        tableByDate.removeAllViewsInLayout();
        tableByPartner.removeAllViewsInLayout();

        TableRow tableRow = new TableRow(getActivity().getApplicationContext());
        TextView sum = new TextView(getActivity().getApplicationContext());
        sum.setText("Сумма");
        sum.setTextSize(20);
        sum.setBackground(getResources().getDrawable(R.drawable.border));
        sum.setGravity(Gravity.CENTER_HORIZONTAL);
        sum.setTextColor(0xff000000);
        sum.setPadding(5, 5, 5, 5);
        TextView date = new TextView(getActivity().getApplicationContext());
        date.setText("Дата");
        date.setTextSize(20);
        date.setBackground(getResources().getDrawable(R.drawable.border));
        date.setGravity(Gravity.CENTER_HORIZONTAL);
        date.setTextColor(0xff000000);
        date.setPadding(5, 5, 5, 5);
        tableRow.addView(sum);
        tableRow.addView(date);
        tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
        tableByDate.addView(tableRow);

        for (DebitByDate debitByDate : debitsByDate) {
            tableRow = new TableRow(getActivity().getApplicationContext());
            sum = new TextView(getActivity().getApplicationContext());
            sum.setText(Double.toString(Math.round(debitByDate.getSum())));
            sum.setBackground(getResources().getDrawable(R.drawable.border));
            sum.setGravity(Gravity.CENTER_HORIZONTAL);
            sum.setTextColor(0xff000000);
            sum.setPadding(5, 5, 5, 5);
            date = new TextView(getActivity().getApplicationContext());
            date.setText(new SimpleDateFormat("dd-MM-yyyy").format(debitByDate.getDate()));
            date.setBackground(getResources().getDrawable(R.drawable.border));
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
        sum.setTextSize(20);
        sum.setBackground(getResources().getDrawable(R.drawable.border));
        sum.setGravity(Gravity.CENTER_HORIZONTAL);
        sum.setTextColor(0xff000000);
        sum.setPadding(5, 5, 5, 5);
        TextView partner = new TextView(getActivity().getApplicationContext());
        partner.setText("Партнёр");
        partner.setTextSize(20);
        partner.setBackground(getResources().getDrawable(R.drawable.border));
        partner.setGravity(Gravity.CENTER_HORIZONTAL);
        partner.setTextColor(0xff000000);
        partner.setPadding(5, 5, 5, 5);
        tableRow.addView(sum);
        tableRow.addView(partner);
        tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
        tableByPartner.addView(tableRow);

        for (DebitByPartner debitByPartner : debitsByPartner) {
            tableRow = new TableRow(getActivity().getApplicationContext());
            sum = new TextView(getActivity().getApplicationContext());
            sum.setText(Double.toString(Math.round(debitByPartner.getSum())));
            sum.setBackground(getResources().getDrawable(R.drawable.border));
            sum.setGravity(Gravity.CENTER_HORIZONTAL);
            sum.setTextColor(0xff000000);
            sum.setPadding(5, 5, 5, 5);
            partner = new TextView(getActivity().getApplicationContext());
            partner.setText(debitByPartner.getPartner());
            partner.setBackground(getResources().getDrawable(R.drawable.border));
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
                        .getUrlString("https://ws-tszh.vdgb-soft.ru/api/mobile/test/test?mode=debit ");
                Log.i(TAG, "Fetched contents of URL: " + result);
                JSONObject jsonBody = new JSONObject(result);
                new DataReceiver().parseDebitItems(DebitLab.get(getActivity().getApplicationContext()), jsonBody);

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
            debitsByDate = DebitLab.get(getActivity().getApplicationContext()).getDebitsByDate();
            debitsByPartner = DebitLab.get(getActivity().getApplicationContext()).getDebitsByPartner();
            drawTables(tableByDate, tableByPartner, debitsByDate, debitsByPartner);
            Toast.makeText(getActivity().getApplicationContext(), "Данные обновлены", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Reset: {
                new FetchItemsTask().execute();
                return true;
            }
            default:
                return false;
        }
    }
}

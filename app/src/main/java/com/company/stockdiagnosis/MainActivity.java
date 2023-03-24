package com.company.stockdiagnosis;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    Button candleStickButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        try {  // import the reader
            InputStream inputStream = getResources().openRawResource(R.raw.stockexample);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            List<String[]> rows = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                rows.add(line.split(","));

            }

            List<Date> dates = new ArrayList<>();
            List<Float> openLine = new ArrayList<>();
            List<Float> closeLine = new ArrayList<>();
            List<Float> highLine = new ArrayList<>();
            List<Float> lowLine = new ArrayList<>();


            for (int i = 0; i < rows.size(); i++) {
                String[] rowValue = rows.get(i);

                try {
                    dates.add(new SimpleDateFormat("MM/dd/yy").parse(rowValue[0]));
                    openLine.add(Float.parseFloat(rowValue[1]));
                    closeLine.add(Float.parseFloat(rowValue[4]));
                    highLine.add(Float.parseFloat(rowValue[2]));
                    lowLine.add(Float.parseFloat(rowValue[3]));
                } catch (ParseException e) {
                    Log.e(TAG, "Error parsing date or Value");
                }
            }

            LineChart lineChart = findViewById(R.id.lineChart);
            XAxis xAxis = lineChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

            xAxis.setValueFormatter(new IndexAxisValueFormatter() {

                @Override
                public String getFormattedValue(float value) {
                    return new SimpleDateFormat("MM/dd/yy").format(dates.get((int) value));
                }
            });

            // info for Open Line
            List<Entry> openEntries = new ArrayList<>();
            for (int i = 0; i < openLine.size(); i++) {
                openEntries.add(new Entry(i, openLine.get(i)));
            }
            LineDataSet openLineDataSet = new LineDataSet(openEntries, "Open");
            openLineDataSet.setColor(Color.RED);


            // info for closeLine
            List<Entry> closeEntries = new ArrayList<>();
            for (int i = 0; i < closeLine.size(); i++) {
                closeEntries.add(new Entry(i, closeLine.get(i)));
            }
            LineDataSet closeLineDataSet = new LineDataSet(closeEntries, "Close");
            closeLineDataSet.setColor(Color.GREEN);


            // info for highLine
            List<Entry> highEntries = new ArrayList<>();
            for (int i = 0; i < highLine.size(); i++) {
                highEntries.add(new Entry(i, highLine.get(i)));
            }
            LineDataSet highLineDataSet = new LineDataSet(highEntries, "High");
            highLineDataSet.setColor(Color.YELLOW);

            // info for lowLine
            List<Entry> lowEntries = new ArrayList<>();
            for (int i = 0; i < lowLine.size(); i++) {
                lowEntries.add(new Entry(i, lowLine.get(i)));
            }
            LineDataSet lowLineDataSet = new LineDataSet(lowEntries, "Low");
            lowLineDataSet.setColor(Color.BLUE);

            // Combine all the data into setData.
            LineData lineData = new LineData(openLineDataSet, closeLineDataSet, highLineDataSet, lowLineDataSet);


            lineChart.setData(lineData);
            lineChart.invalidate();


        } catch (IOException e) {
            Log.d(TAG, "error");


        }
        candleStickButton = findViewById(R.id.candleStickButton);
        candleStickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (MainActivity.this, CandleStickActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}


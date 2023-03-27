package com.company.stockdiagnosis;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CandleStickActivity extends AppCompatActivity {

    Button lineGraphButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candle_stick);





        // try to create candleStick for the stock.
        try {

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


                CandleStickChart candleStickChart = findViewById(R.id.candleStickChart);
                XAxis xAxis = candleStickChart.getXAxis();
                xAxis.setValueFormatter(new IndexAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    return new SimpleDateFormat("MM/dd/yy").format(dates.get((int) value));
                  }
                });

               List<CandleEntry> candleEntries = new ArrayList<>();
               for(int k = 0; k < openLine.size(); k++) {
                   candleEntries.add(new CandleEntry(k, highLine.get(k), lowLine.get(k), openLine.get(k), closeLine.get(k)));
               }

               CandleDataSet candleDataSet = new CandleDataSet(candleEntries,"CANDLE STICK");
               candleDataSet.setDecreasingColor(Color.RED);
               candleDataSet.setIncreasingColor(Color.GREEN);
               candleDataSet.setDecreasingPaintStyle(Paint.Style.FILL);
               candleDataSet.setIncreasingPaintStyle(Paint.Style.FILL);
               candleDataSet.setNeutralColor(Color.BLUE);

               candleDataSet.setShadowColor(Color.BLACK);
               candleDataSet.setShadowWidth(1f);



                XAxis xAxisCandle = candleStickChart.getXAxis();
                xAxisCandle.setPosition(XAxis.XAxisPosition.BOTTOM);

                CandleData candleData = new CandleData(candleDataSet);
                candleStickChart.setData(candleData);
                candleStickChart.invalidate();
            }

        } catch (IOException e) {
            Log.d(TAG,"ERROR");
        }


        lineGraphButton = findViewById(R.id.lineChartButton);
        lineGraphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (CandleStickActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

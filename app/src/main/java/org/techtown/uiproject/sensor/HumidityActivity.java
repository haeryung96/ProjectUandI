package org.techtown.uiproject.sensor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import org.techtown.uiproject.R;

public class HumidityActivity extends AppCompatActivity {

    private LineChart lineChart;
    ArrayList<Entry> yData;
    DatabaseReference mPostReference;
    ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_humidity);

        lineChart = (LineChart) findViewById(R.id.chart);

        mPostReference = FirebaseDatabase.getInstance().getReference("sensor");
        mPostReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                yData = new ArrayList<>();
                float i = 0;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    i=i+1;
                    String SV = ds.child("humidity").getValue().toString();
                    Float SensorValue = Float.parseFloat(SV);
                    yData.add(new Entry(i,SensorValue));
                }
                final LineDataSet lineDataSet = new LineDataSet(yData,"Humidity");
                //lineDataSet.setColor(R.color.colorPrimaryDark);
                lineDataSet.setLineWidth(3);
                lineDataSet.setCircleRadius(4);
                LineData data = new LineData(lineDataSet);
                lineChart.setData(data);
                lineChart.notifyDataSetChanged();
                lineChart.invalidate();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}

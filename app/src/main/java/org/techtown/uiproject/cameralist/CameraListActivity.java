package org.techtown.uiproject.cameralist;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import org.techtown.uiproject.R;
import org.techtown.uiproject.mypage.Register;
import org.techtown.uiproject.mypage.mypageActivity;
import org.techtown.uiproject.sensor.HumidityActivity;
import org.techtown.uiproject.sensor.TemperatureActivity;


public class CameraListActivity extends AppCompatActivity {

    Button cameraBtn, humBtn, tempBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_list);

        cameraBtn = findViewById(R.id.cameraBtn);
        humBtn = findViewById(R.id.humBtn);
        tempBtn = findViewById(R.id.tempBtn);

        //카메라 버튼 누를시 액티비티 추가하기

        //습도 버튼 클릭시
        humBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CameraListActivity.this, HumidityActivity.class));
            }
        });

        //온도 버튼 클릭시
        tempBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CameraListActivity.this, TemperatureActivity.class));
            }
        });
    }








}

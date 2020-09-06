package org.techtown.uiproject.calendar;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;

import org.techtown.uiproject.R;

public class CalendarActivity extends AppCompatActivity {
    //캘린더 탭 연결
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        ViewPager vp2 = findViewById(R.id.calendar_view);
        ViewAdapter_Calendar adapter2 = new ViewAdapter_Calendar(getSupportFragmentManager());
        vp2.setAdapter(adapter2);
        //viewpager 와 adapter 생성후 연결

        TabLayout tab = findViewById(R.id.tab);
        tab.setupWithViewPager(vp2);
        //tab을 viewpager 와 연결
    }
}
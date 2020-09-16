package org.techtown.uiproject.mypage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.techtown.uiproject.R;

public class DashBoard extends AppCompatActivity {
    String EmailHolder;
    TextView Email;
    Button LogOUT ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        Email = (TextView)findViewById(R.id.textView1);
        LogOUT = (Button)findViewById(R.id.button1);

        Intent intent = getIntent();

        // Receiving User Email Send By mypage.
        //EmailHolder = intent.getStringExtra(mypageActivity.UserEmail);

        // Setting up received email to TextView.
        //Email.setText(Email.getText().toString()+ EmailHolder);

        // Adding click listener to Log Out button.
        /*LogOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Finishing current DashBoard activity on button click.
                finish();

                Toast.makeText(DashBoard.this,"로그아웃 성공!", Toast.LENGTH_LONG).show();

            }
        });*/
    }
}

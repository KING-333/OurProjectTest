package com.example.ourprojecttest;

import androidx.appcompat.app.AppCompatActivity;



import android.os.Bundle;
import android.os.SystemClock;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


hahahahahhahahah
    ///
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView show = (TextView) this.findViewById(R.id.time);
        /*for (int i = 99;i>=0;i--)
        {
            show.setText(i);
           // SystemClock.sleep(1000);

        }*/
        show.setText("发送验证码到邮箱");
    }



}

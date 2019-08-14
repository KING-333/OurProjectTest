package com.example.ourprojecttest;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FindPass extends AppCompatActivity {

    private EditText find_pass_user_name;
    private EditText input_verification_box;
    private Button get_verification_code;
    private Button find_pass_confirm;
    Handler mHandler = new Handler();
    int num=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pass);
        getSupportActionBar().hide();
        find_pass_user_name=findViewById(R.id.find_pass_user_name);
        get_verification_code=findViewById(R.id.get_verification_code);
        find_pass_confirm=findViewById(R.id.find_pass_confirm);
        input_verification_box=findViewById(R.id.input_verification_box);

        get_verification_code.setOnClickListener(new View.OnClickListener(){//设置发送验证码的点击事件
          @Override
          public void onClick(View view) {
              //随机生成6位的验证码
              num = (int)(Math.random()*1000000+1);
                  //耗时操作要起子线程
              new Thread(new Runnable() {
                  @Override
                  public void run() {
               MyEamil myEamil=new MyEamil();

               myEamil.sendMail(find_pass_user_name.getText().toString().trim(),String.valueOf(num));//发送邮箱和验证码

                  }
              }).start();
              CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(get_verification_code, 60000, 1000); //倒计时1分钟
              mCountDownTimerUtils.start();

          }
      });
               //设置找回密码的确认点击事件
        find_pass_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              if(input_verification_box.getText().toString().trim().equals(String.valueOf(num))){//如果验证码正确
                  //////登录更新数据库
              }
              else{
                  Toast.makeText(FindPass.this,"您输入的验证码或邮箱不正确，请重新输入！",Toast.LENGTH_SHORT).show();
              }
            }
        });

    }





}

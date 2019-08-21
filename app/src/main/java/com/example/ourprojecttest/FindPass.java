package com.example.ourprojecttest;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FindPass extends AppCompatActivity {
    private EditText find_pass_input_new_pass;
    private EditText find_pass_user_name;
    private EditText input_verification_box;
    private Button get_verification_code;
    private Button find_pass_confirm;
    int num=-1;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 0:
                    Toast.makeText(FindPass.this, "密码修改成功，请重新登录！", Toast.LENGTH_SHORT).show();
                    break;
                case -1:
                    Toast.makeText(FindPass.this,"密码修改失败",Toast.LENGTH_SHORT).show();
                default:
                    break;
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pass);
        ImmersiveStatusbar.getInstance().Immersive(getWindow(),getActionBar());//状态栏透明
        //getSupportActionBar().hide();
        find_pass_user_name=findViewById(R.id.find_pass_user_name);
        get_verification_code=findViewById(R.id.get_verification_code);
        find_pass_confirm=findViewById(R.id.find_pass_confirm);
        find_pass_input_new_pass=findViewById(R.id.find_pass_input_new_pass);
        input_verification_box=findViewById(R.id.input_verification_box);

        get_verification_code.setOnClickListener(new View.OnClickListener(){//设置发送验证码的点击事件
          @Override
          public void onClick(View view) {
              //随机生成6位的验证码
              num = (int)(Math.random()*1000000+1);
                  //耗时操作要起子线程1
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

                  new Thread(new Runnable() {
                      @Override
                      public void run() {
                          String url="http://139.196.103.219:8080/IM1/servlet/PasswordModify?no="+find_pass_user_name.getText().toString().trim()+"&pwd="+find_pass_input_new_pass.getText().toString().trim();
                          OkHttpClient client = new OkHttpClient();
                          Request request = new Request.Builder()
                                  .url(url)
                                  .build();

                          try {

                              Response response = client.newCall(request).execute();

                              String responseData = response.body().string();

                              parseJSONWithJSONObject(responseData);
                          } catch (Exception e) {
                              e.printStackTrace();
                          }
                      }
                  }).start();
              }
              else{
                  Toast.makeText(FindPass.this,"您输入的验证码或邮箱不正确，请重新输入！",Toast.LENGTH_SHORT).show();
              }
            }
        });

    }


    private void parseJSONWithJSONObject(String jsonData){
        try{
            String code="0000";
            JSONObject jsonObject=new JSONObject(jsonData);
            code=jsonObject.getString("code");
            Message msg = Message.obtain();

            Log.d("FindPass_tiaoshi",code);
            if(code.equals("0")||code.equals("-1")){
                msg.what = 0;

            }
            else{
                msg.what =-1;
            }
            handler.sendMessage(msg);

        }catch (Exception e){
            e.printStackTrace();
        }
    }



}

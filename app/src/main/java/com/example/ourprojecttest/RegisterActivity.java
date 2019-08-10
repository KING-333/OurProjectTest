package com.example.ourprojecttest;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 0:
                    new AlertDialog.Builder(RegisterActivity.this).setTitle("跳转").setMessage("注册成功过,准备好登陆了吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(RegisterActivity.this,MailboxActivity.class);
                            startActivity(intent);
                        }
                    }).setNegativeButton("取消",null).show();
                    break;
                case -1:
                    new AlertDialog.Builder(RegisterActivity.this).setTitle("错误").setMessage("邮箱已被使用").setNegativeButton("确定",null).show();
                    default:
                        break;
            }
        }
    };


    int mYear, mMonth, mDay;
    Button btn;
    TextView dateDisplay;
    final int DATE_DIALOG = 1;
    private TextView stuName;
    private TextView stuNo;
    private TextView stuPwd;
    private TextView stuPwd_two;
    private TextView stuSex;
    private TextView stuHei;
    private TextView stuWei;
    private TextView stuBir;
    private Button btnLook;
    private Button btnRegister;
    private boolean mbDisplayFlg = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnLook = (Button) findViewById(R.id.look);
        stuPwd_two = (TextView) findViewById(R.id.stuPwd_two);
        btnLook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mbDisplayFlg) {
                    // display password text, for example "123456"
                    stuPwd_two.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    // hide password, display "."
                    stuPwd_two.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                mbDisplayFlg = !mbDisplayFlg;
                stuPwd_two.postInvalidate();
            }
        });

        btn = (Button) findViewById(R.id.dateChoose);
        dateDisplay = (TextView) findViewById(R.id.dateDisplay);

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG);
            }
        });

        final Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);

        initView();
        initListener();

    }
    private void initListener(){
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = stuName.getText().toString();
                String userNo = stuNo.getText().toString();
                String userPwd = stuPwd.getText().toString();
                String userPwd_two = stuPwd_two.getText().toString();
                String userSex = stuSex.getText().toString();
                String userHei = stuHei.getText().toString();
                String userWei = stuWei.getText().toString();
                String userBir = stuBir.getText().toString();

                boolean flag = true;
                if (TextUtils.isEmpty(userName)||TextUtils.isEmpty(userBir)||TextUtils.isEmpty(userHei)||TextUtils.isEmpty(userNo)||TextUtils.isEmpty(userPwd)||TextUtils.isEmpty(userSex)||TextUtils.isEmpty(userWei)||TextUtils.isEmpty(userPwd_two)){
                    new AlertDialog.Builder(RegisterActivity.this).setTitle("错误").setMessage("值不能为空").setNegativeButton("确定",null).show();
                    flag = false;
                }
                else if (!checkNo(userNo)&&flag){
                    new AlertDialog.Builder(RegisterActivity.this).setTitle("错误").setMessage("请输入正确邮箱").setNegativeButton("确定",null).show();
                    flag = false;
                }
                else if (!checkPwd(userPwd)&&flag){
                    new AlertDialog.Builder(RegisterActivity.this).setTitle("错误").setMessage("密码长度为6到16位").setNegativeButton("确定",null).show();
                    flag = false;
                }
                else if (!checkPwd_repitition(userPwd,userPwd_two)&&flag){
                    new AlertDialog.Builder(RegisterActivity.this).setTitle("错误").setMessage("两次密码不一致").setNegativeButton("确定",null).show();
                    flag = false;
                }
                else if (!checkSex(userSex)&&flag){
                    new AlertDialog.Builder(RegisterActivity.this).setTitle("错误").setMessage("请合理选择性别").setNegativeButton("确定",null).show();
                    flag = false;
                }
                else if (!checkHei(userHei)&&flag){
                    new AlertDialog.Builder(RegisterActivity.this).setTitle("错误").setMessage("请合理输入身高").setNegativeButton("确定",null).show();
                    flag = false;
                }
                else if (!checkWei(userWei)&&flag){
                    new AlertDialog.Builder(RegisterActivity.this).setTitle("错误").setMessage("请输入合理体重").setNegativeButton("确定",null).show();
                    flag = false;
                }
                else if (flag)
                {
                    interData();
                }



            }
        });
    }
    //验证密码是否一致
    private boolean checkPwd_repitition(String pwd,String pwd_two){
        if (pwd.equals(pwd_two))
        {
            return true;
        }
        else
        {
            return false;
        }

    }
    //验证密码
    private boolean checkPwd(String pwd){
        if (pwd.length()<=16 && pwd.length()>=6){
            return true;
        }
        else {
            return false;
        }
    }
    //检验邮箱
    private boolean checkNo(String no){
        boolean tag = true;
        final String pattern1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        final Pattern pattern = Pattern.compile(pattern1);
        final Matcher mat = pattern.matcher(no);
        if (!mat.find()) {
            tag = false;
        }
        return tag;
    }
    //检验性别
    private boolean checkSex(String no){
        if (no.equals("men")||no.equals("women"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    //检验身高
    private boolean checkHei(String hei){
        try {
            if (Integer.parseInt(hei)<=300&&Integer.parseInt(hei)>=100)
            {
                 return true;
             }
            else
            {
                 return false;
            }

        }catch (NumberFormatException e){
            //new AlertDialog.Builder(RegisterActivity.this).setTitle("错误").setMessage("请输入数字").setNegativeButton("确定",null).show();
            return false;
        }

    }
    //检验体重
    private boolean checkWei(String wei){
        try {
            if (Integer.parseInt(wei)<=150&&Integer.parseInt(wei)>=30)
            {
                return true;
            }
            else
            {
                return false;
            }
        }catch (NumberFormatException e){
            return false;
        }

    }
    //向数据库中插入数据
    private void interData(){




        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String userName = stuName.getText().toString().trim();
                    String userNo = stuNo.getText().toString().trim();
                    String userPwd = stuPwd.getText().toString().trim();
                    String userSex = stuSex.getText().toString().trim();
                    String userHei = stuHei.getText().toString().trim();
                    String userWei = stuWei.getText().toString().trim();
                    String userBir = stuBir.getText().toString().trim();
                    String url = " http://139.196.103.219:8080/IM1/servlet/LoginDataServlet?no="+userNo+"&name="+userName+"&pwd="+userPwd+"&sex="+userSex+"&birth="+userBir+"&height="+userHei+"&weight="+userWei+"&sno=9999";
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(url).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseJSONWithJSONObject(responseData);
                } catch (Exception e)
                {
                   e.printStackTrace();
                }
            }
        }).start();


    }
    private void parseJSONWithJSONObject(String jsonData){
        try{
            JSONObject jsonObject=new JSONObject(jsonData);
            String code=jsonObject.getString("code");
            Message msg = Message.obtain();

                if (code.equals("0"))
                {
                    msg.what = 0 ;
                }
                else
                {
                    msg.what = -1;
                }
                handler.sendMessage(msg);

        } catch ( Exception e)
        {
            e.printStackTrace();
        }
    }
    //初始化数据
    private void initView(){
        stuNo = (TextView) findViewById(R.id.stuNo);
        stuName = (TextView) findViewById(R.id.stuName);
        stuPwd = (TextView) findViewById(R.id.stuPwd);
        stuSex = (TextView) findViewById(R.id.stuSex);
        stuHei = (TextView) findViewById(R.id.stuHei);
        stuWei = (TextView) findViewById(R.id.stuWei);
        stuBir = (TextView) findViewById(R.id.dateDisplay);
        btnRegister = (Button) findViewById(R.id.stuReg);
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG:
                return new DatePickerDialog(this, mdateListener, mYear, mMonth, mDay);
        }
        return null;
    }

    /**
     * 设置日期 利用StringBuffer追加
     */
    public void display() {
        dateDisplay.setText(new StringBuffer().append(mYear).append("-").append(mMonth).append("-").append(mDay).append(" "));
    }

    private DatePickerDialog.OnDateSetListener mdateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            display();
        }
    };



}
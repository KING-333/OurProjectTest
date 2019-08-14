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
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {
    String code="";
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 0:
                    new AlertDialog.Builder(RegisterActivity.this).setTitle("跳转").setMessage("注册成功,准备好登陆了吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
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
    private RadioGroup radioGroup;
    private RadioButton radioMen,radioWomen;
    int mYear, mMonth, mDay;
    Button btn;
    TextView dateDisplay;
    final int DATE_DIALOG = 1;
    private TextView stuName;
    private TextView stuNo;
    private TextView stuPwd;
    private TextView stuPwd_two;
    private TextView stuHei;
    private TextView stuWei;
    private TextView stuBir;
    private TextView stuMsg;
    private Button btnLook;
    private Button btnRegister;
    private Button getmsg;
    private boolean mbDisplayFlg = false;
    private TimeCount time;



    private boolean isHide=true;
    Drawable drawableEyeOpen,drawableEyeClose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ImmersiveStatusbar.getInstance().Immersive(getWindow(),getActionBar());//状态栏透明

        //获取验证码
        time = new TimeCount(60000, 1000);
        getmsg = (Button) this.findViewById(R.id.getmsg);
        getmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                code = getConde();
                sendCode();
                //60秒倒计时
                //time.start();


            }
        });

        //密码可见不可见

        stuPwd_two = (TextView) findViewById(R.id.stuPwd_two);
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
        drawableEyeClose = getResources().getDrawable(R.drawable.biyan);
        drawableEyeOpen = getResources().getDrawable(R.drawable.zhengyan);

        stuPwd.setOnTouchListener(new View.OnTouchListener() {

            final Drawable[] drawables = stuPwd.getCompoundDrawables();//获取密码框的drawable数组
            final int eyeWidth = drawables[2].getBounds().width();// 眼睛图标的宽度

            Drawable drawable = stuPwd.getCompoundDrawables()[2];

            public boolean onTouch(View view, MotionEvent event) {
                if (event.getX() > stuPwd.getWidth() - stuPwd.getPaddingRight() - drawable.getIntrinsicWidth()) {
                    if (event.getAction() != MotionEvent.ACTION_UP)
                        return false;

                    //如果当前密码框是密文
                    if (isHide) {
                        drawableEyeOpen.setBounds(drawables[2].getBounds());//设置睁开眼睛的界限

                        stuPwd.setCompoundDrawables(drawables[0], null, drawableEyeOpen, null);
                        Log.d("loginfalse", String.valueOf(isHide));
                        stuPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        isHide = false;
                    }
                    //如果当前密码框是明文
                    else {
                        drawableEyeClose.setBounds(drawables[2].getBounds());//设置闭眼的界限
                        stuPwd.setCompoundDrawables(drawables[0], null, drawableEyeClose, null);

                        Log.d("logintrue", String.valueOf(isHide));
                        stuPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        isHide = true;
                    }

                }
                return false;
            }
        });
        stuPwd_two.setOnTouchListener(new View.OnTouchListener() {

            final Drawable[] drawables = stuPwd_two.getCompoundDrawables();//获取密码框的drawable数组
            final int eyeWidth = drawables[2].getBounds().width();// 眼睛图标的宽度

            Drawable drawable = stuPwd_two.getCompoundDrawables()[2];

            public boolean onTouch(View view, MotionEvent event) {
                if (event.getX() > stuPwd_two.getWidth() - stuPwd_two.getPaddingRight() - drawable.getIntrinsicWidth()) {
                    if (event.getAction() != MotionEvent.ACTION_UP)
                        return false;

                    //如果当前密码框是密文
                    if (isHide) {
                        drawableEyeOpen.setBounds(drawables[2].getBounds());//设置睁开眼睛的界限

                        stuPwd_two.setCompoundDrawables(drawables[0], null, drawableEyeOpen, null);
                        Log.d("loginfalse", String.valueOf(isHide));
                        stuPwd_two.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        isHide = false;
                    }
                    //如果当前密码框是明文
                    else {
                        drawableEyeClose.setBounds(drawables[2].getBounds());//设置闭眼的界限
                        stuPwd_two.setCompoundDrawables(drawables[0], null, drawableEyeClose, null);

                        Log.d("logintrue", String.valueOf(isHide));
                        stuPwd_two.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        isHide = true;
                    }

                }
                return false;
            }
        });

    }

    //验证码60秒倒计时
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onTick(long millisUntilFinished) {
            getmsg.setBackgroundColor(Color.parseColor("#B6B6D8"));
            getmsg.setClickable(false);
            getmsg.setText("("+millisUntilFinished / 1000 +") 秒后可重新发送");
        }
        @Override
        public void onFinish() {
            getmsg.setText("重新获取验证码");
            getmsg.setClickable(true);
            getmsg.setBackgroundColor(Color.parseColor("#4EB84A"));
        }

    }
    //初始化数据
    private void initView(){
        stuNo = (TextView) findViewById(R.id.stuNo);
        stuName = (TextView) findViewById(R.id.stuName);
        stuPwd = (TextView) findViewById(R.id.stuPwd);
        stuHei = (TextView) findViewById(R.id.stuHei);
        stuWei = (TextView) findViewById(R.id.stuWei);
        stuBir = (TextView) findViewById(R.id.dateDisplay);
        stuMsg = (TextView) findViewById(R.id.msg);
        btnRegister = (Button) findViewById(R.id.stuReg);
        radioMen = (RadioButton) findViewById(R.id.radioMen);
        radioWomen = (RadioButton) findViewById(R.id.radioWomen);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
    }
    //初始化方法
    private void initListener(){
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = stuName.getText().toString();
                String userNo = stuNo.getText().toString();
                String userPwd = stuPwd.getText().toString();
                String userPwd_two = stuPwd_two.getText().toString();
                String userHei = stuHei.getText().toString();
                String userWei = stuWei.getText().toString();
                String userBir = stuBir.getText().toString();
                String userMsg = stuMsg.getText().toString();

                boolean flag = true;
                if (!radioWomen.isChecked()&&!radioMen.isChecked()){
                    new AlertDialog.Builder(RegisterActivity.this).setTitle("错误").setMessage("请选择性别").setNegativeButton("确定",null).show();
                    flag = false;
                }
                else if (TextUtils.isEmpty(userName)||TextUtils.isEmpty(userBir)||TextUtils.isEmpty(userHei)||TextUtils.isEmpty(userNo)||TextUtils.isEmpty(userPwd)||TextUtils.isEmpty(userWei)||TextUtils.isEmpty(userPwd_two)){
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
                else if (!checkHei(userHei)&&flag){
                    new AlertDialog.Builder(RegisterActivity.this).setTitle("错误").setMessage("请合理输入身高").setNegativeButton("确定",null).show();
                    flag = false;
                }
                else if (!checkWei(userWei)&&flag){
                    new AlertDialog.Builder(RegisterActivity.this).setTitle("错误").setMessage("请输入合理体重").setNegativeButton("确定",null).show();
                    flag = false;
                }
                else if (!checkCode(userMsg)&&flag)
                {
                    new AlertDialog.Builder(RegisterActivity.this).setTitle("错误").setMessage("验证码错误").setNegativeButton("确定",null).show();
                    flag = false;
                }
                else if (flag)
                {
                    interData();
                }



            }
        });
    }
    //发送验证码
    private void sendCode()
    {
        stuNo = (TextView) findViewById(R.id.stuNo);
        String userNo = stuNo.getText().toString().trim();
        if (checkNo(userNo)&&!userNo.isEmpty())
            EamilUtil.sendMail(userNo,code);
        else
            new AlertDialog.Builder(RegisterActivity.this).setTitle("错误").setMessage("邮箱格式错误").setNegativeButton("确定",null).show();
    }
    //验证验证码是否一致
    private boolean checkCode(String userCode)
    {
        if (userCode.equals(code))
            return true;
        else
            return false;
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
                    String userHei = stuHei.getText().toString().trim();
                    String userWei = stuWei.getText().toString().trim();
                    String userBir = stuBir.getText().toString().trim();
                    String url = "";
                    if (radioMen.isChecked())
                    {
                        url = " http://139.196.103.219:8080/IM1/servlet/LoginDataServlet?no="+userNo+"&name="+userName+"&pwd="+userPwd+"&sex=男&birth="+userBir+"&height="+userHei+"&weight="+userWei+"&sno=9999";
                    }
                    else
                    {
                        url = " http://139.196.103.219:8080/IM1/servlet/LoginDataServlet?no="+userNo+"&name="+userName+"&pwd="+userPwd+"&sex=女&birth="+userBir+"&height="+userHei+"&weight="+userWei+"&sno=9999";
                    }

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
    //获取验证码
    private String getConde(){
        String code1 ="";
        for (int i =0;i<4;i++)
        {
            code1 = code1 + (int)(Math.random() * 10);
        }
        return code1;
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
        dateDisplay.setText(new StringBuffer().append(mYear).append("-").append(mMonth+1).append("-").append(mDay).append(" "));
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
package com.example.ourprojecttest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DocRegisterActivity extends AppCompatActivity {

    private TextView docNo;
    private TextView docName;
    private TextView docPwd;
    private Button docBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_register);

    }
    private void initView(){
        docNo = (TextView) findViewById(R.id.docNo);
        docName = (TextView) findViewById(R.id.docName);
        docPwd = (TextView) findViewById(R.id.docPwd);
        docBtn = (Button) findViewById(R.id.docReg);
    }
    private void initListener() {
        docBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = docName.getText().toString();
                String no = docNo.getText().toString();
                String pwd = docPwd.getText().toString();
                boolean flag = true;
                if (TextUtils.isEmpty(name)||TextUtils.isEmpty(no)||TextUtils.isEmpty(pwd)){
                    new AlertDialog.Builder(DocRegisterActivity.this).setTitle("错误").setMessage("值不能为空").setNegativeButton("确定",null).show();
                    flag = false;
                }
                else if (!checkNo(no)&&flag){
                    new AlertDialog.Builder(DocRegisterActivity.this).setTitle("错误").setMessage("请输入正确邮箱").setNegativeButton("确定",null).show();
                    flag = false;
                }
                else if (!checkNo_repetition(no)&&flag){
                    new AlertDialog.Builder(DocRegisterActivity.this).setTitle("错误").setMessage("该邮箱已经被注册").setNegativeButton("确定",null).show();
                    flag = false;
                }
                else if (!checkPwd(pwd)&&false){
                    new AlertDialog.Builder(DocRegisterActivity.this).setTitle("错误").setMessage("密码长度为6到16位").setNegativeButton("确定",null).show();
                    flag = false;
                }
                else {
                    interData(name,no,pwd);
                    new AlertDialog.Builder(DocRegisterActivity.this).setTitle("跳转").setMessage("注册成功,准备好登陆了吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(DocRegisterActivity.this,MainActivity.class);
                            startActivity(intent);
                        }
                    }).setNegativeButton("取消",null).show();
                }
            }
        });
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
    //验证邮箱格式
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
    //验证邮箱是否存在
    private boolean checkNo_repetition(String no){
        return true;
    }
    //添加数据到数据库
    private boolean interData(String name,String no,String pwd)
    {
        return true;
    }
}

package com.android.smsmessages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private DBHelper mHelper;
    public static final int REQ_CODE_CONTACT = 1;
    //    private ListView mListView;
//    private SimpleAdapter sa;
//    private Button bt;
    private EditText dingTokenEdit;
    private Button startButton;
    private List<Map<String, Object>> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        checkSMSPermission();
        mHelper = new DBHelper(this);
        mHelper.initData();
        Intent SMSForwardServiceIntent = new Intent(getApplicationContext(), SMSForwardService.class);
        stopService(SMSForwardServiceIntent);
        startService(SMSForwardServiceIntent);
    }

    /**
     * 检查申请短信权限
     */
    private void checkSMSPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            //向系统申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, REQ_CODE_CONTACT);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //判断用户是否，同意 获取短信授权
        if (requestCode == REQ_CODE_CONTACT && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //获取到读取短信权限
        } else {
            Toast.makeText(this, "未获取到短信权限", Toast.LENGTH_SHORT).show();
            System.exit(0);
        }
    }

    private void initView() {

        startButton = (Button) findViewById(R.id.start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dingTokenEdit = (EditText) findViewById(R.id.dingToken);
                String dingToken = dingTokenEdit.getText().toString();
                if (dingToken.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Used default dingToken", Toast.LENGTH_SHORT).show();
                } else {
                    MyApplication application = (MyApplication) getApplication();
                    application.setDingToken(dingToken);
                    mHelper.updateData(dingToken);
                    Intent SMSForwardServiceIntent = new Intent(getApplicationContext(), SMSForwardService.class);
                    stopService(SMSForwardServiceIntent);
                    startService(SMSForwardServiceIntent);
                }
            }
        });
    }
}
package com.example.administrator.mypermission;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_id)
    Button mBtnId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);


    }

    @OnClick(R.id.btn_id)
    public void onViewClicked() {
        //跳转到系统相机；
     // camear();
        //由于6.0以后版本在调用系统设置的时候如果没有配置权限，
        // 6.0以后版本是无法判断出有没有配置权限的，我们需要手动判断有没有添加权限；
        //判断是否添加了权限；ActivityCompat.checkSelfPermission()
//        PackageManager.PERMISSION_GRANTED:有权限
//        PackageManager.PERMISSION_DENIED：拒绝权限
        //如果当前应用没有权限则请求权限；
     if(ActivityCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){

         //权限发生了改变 true  //  false 小米
         if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA)) {

             new AlertDialog.Builder(this).setTitle("title")
                     .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {
                             // 请求授权
                             ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA},1);

                         }
                     }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                 @Override
                 public void onClick(DialogInterface dialog, int which) {

                 }
             }).create().show();



         }else{
             //请求权限；第二个参数为一个权限数组,会有一个请求权限的回调；
             ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},1);

     }
     }else{
           camear();
     }
    }


    /**
     *   //请求权限回调的方法；
     * @param requestCode:
     * @param permissions:请求的权限
     * @param grantResults：请求权限返回的结果；
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
         //参数介绍：第一个是请求码是与请求权限的一一对应的，用来识别是哪个权限的请求；
        if(requestCode==1){
            //camear权限的回调；
            //如果返回的请求结果数组有数据，并且已经配置相机权限；
         if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
             // 表示用户授权
             Toast.makeText(this, " user Permission" , Toast.LENGTH_SHORT).show();
             camear();
         }else{
             //用户拒绝权限；
             Toast.makeText(this, " 没有权限" , Toast.LENGTH_SHORT).show();
         }

        }
    }

    //打开摄像机的方法；
    public void camear(){
     /*
         MediaStore:媒体仓库
         ACTION_IMAGE_CAPTURE：捕获相机
       */
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,1);

    }
}

package com.example.administrator.mypermission;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

import static android.os.Build.VERSION_CODES.M;

/**
 * date:2017/6/23 0023
 * authom:贾雪茹
 * function:
 */
@RuntimePermissions//需要使用运行时权限的Activity和Fragment
public class Main2 extends AppCompatActivity {
    @BindView(R.id.btns_id)
    Button mBtnsId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main2);

        ButterKnife.bind(this);

    }
  //按钮点击事件；
    @OnClick(R.id.btns_id)
    public void onViewClicked() {
    Main2PermissionsDispatcher.camearWithCheck(this);
    }
    //权限被拒绝时；
    @OnPermissionDenied(Manifest.permission.CAMERA)
    public void dnied(){
        Toast.makeText(this, "dnied", Toast.LENGTH_SHORT).show();
    }

    //当用户给予权限，调用权限时；
    @NeedsPermission(Manifest.permission.CAMERA)
    public void camear(){
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivity(intent);
    }

    //当用户点击不再提示并拒绝了权限时调用一般用来向用户解释为何调用此权限；
    @OnNeverAskAgain(Manifest.permission.CAMERA)
    public void noHint(){
        Toast.makeText(this, "onNeverAskAgain", Toast.LENGTH_SHORT).show();

        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        startActivity(localIntent);

    }
    //向用户解释为什么使用该权限，只有在第一次请求被拒绝，再次请求之前调用；
    @OnShowRationale(Manifest.permission.CAMERA)//此注解需要一个参数PermissionRequest request权限的请求
    public void show(final PermissionRequest request){
        //创建对话框用于提示用户权限的作用；
        new AlertDialog.Builder(this).setTitle("title")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 请求授权
                       request.proceed();
                    }
                }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
             //取消授权；
                request.cancel();
            }
        }).create().show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        Main2PermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}

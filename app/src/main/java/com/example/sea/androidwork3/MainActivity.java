package com.example.sea.androidwork3;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.*;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    private Location position;//位置信息

    //定位方式
    String provider = LocationManager.GPS_PROVIDER;

    TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //定位方式
        String provider = LocationManager.GPS_PROVIDER;
        //是否正在定位--GPS是否正在工作?
        boolean isLocating;

        info = (TextView) findViewById(R.id.info);


        //获取位置服务管理对象
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null) {
            Toast.makeText(this, "获取系统服务失败!", Toast.LENGTH_SHORT).show();
            return;
        }

        //如果没有打开GPS,则转至GPS设置页面
        if (!locationManager.isProviderEnabled(provider)) {
            (new AlertDialog.Builder(this))
                    .setTitle("提示")
                    .setMessage("需要启用相应的定位设备")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {    //点击确定后打开设置对话框
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            //打开设置页面
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            try {
                                startActivity(intent);
                            } catch (ActivityNotFoundException ex) {
                                // The Android SDK doc says that the location settings activity
                                // may not be found. In that case show the general settings.
                                // General settings activity
                                intent.setAction(Settings.ACTION_SETTINGS);
                                try {
                                    startActivity(intent);
                                } catch (Exception e) {
                                }
                            }
                        }

                    })
                    .create().show();
        }
        //获取定位信息

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
       // position = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


        position=getBestLocation(locationManager);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 200, 1, locationListener);


        info.setText("未显示位置");


        //显示位置信息
        ShowLocationInfo(position);






    }

    //显示位置信息
    private void ShowLocationInfo(Location position) {
        String s = "";

        if (position == null) {

            info.setText("没有可用的位置信息!");
            return;
        }

        //获取位置信息
        s+="经度: ";
        s+=position.getLongitude()+"\n";
        s+="纬度: ";
        s+=position.getLatitude()+"\n";
        s+="高度: ";
        s+=position.getAltitude()+"\n";
        s+="加速度: ";
        s+=position.getAccuracy()+"\n";
        s+="方向: ";
        s+=position.getBearing();

        //解析经纬度对应的地址信息
        List<Address> address = null;
        Geocoder gc = new Geocoder(this, Locale.CHINA);
        try {
            address = gc.getFromLocation(position.getLatitude(), position.getLongitude(), 2);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String t = "";
        if (address != null && address.size() > 0) {
            for (int i = 0; i < address.size(); i++) {
                t += address.get(i).toString() + "\n";
            }
        } else
            t = "没有解析出地址...\n";

        //显示位置信息
        //  ((TextView)findViewById(R.id.info)).setText(s+"\n"+t);
        info.setText(s + "\n" + t);

    }


    LocationListener locationListener = new LocationListener() {


        @Override
        public void onLocationChanged(Location location) {

            ShowLocationInfo(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }


    };


    /**
     * 获取location对象，优先以GPS_PROVIDER获取location对象，当以GPS_PROVIDER获取到的locaiton为null时
     * ，则以NETWORK_PROVIDER获取location对象，这样可保证在室内开启网络连接的状态下获取到的location对象不为空
     *
     * @param locationManager
     * @return
     */
    private Location getBestLocation(LocationManager locationManager) {
        Location result = null;
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null;
            }

            result = locationManager
                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (result != null) {
                return result;
            } else {
                result = locationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                return result;
            }
        }
        return result;
    }


}

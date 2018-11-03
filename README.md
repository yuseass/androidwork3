# androidwork3
大三下android第三次实验
实验内容	
1. 实现定位操作
（1）检测若系统未打开GPS定位，打开GPS设置功能，由用户打开GPS定位；
（2）通过LocationManager获取位置信息；
2. 实现跟踪操作
（1）利用LocationListener添加监听器；
（2）在监听器中获取位置信息，并显示到界面上；

算法描述及实验步骤	
1.	创建界面布局
2.	使用LocationManager获取系统服务，若没有打开GPS，跳转到设置界面，打开GPS
3.	获取location对象，优先以GPS_PROVIDER获取location对象，当以GPS_PROVIDER获取到的locaiton为null时，则以NETWORK_PROVIDER获取location对象，这样可保证在室内开启网络连接的状态下获取到的location对象不为空
4.	创建方法解析获得的Location，在布局显示
5.	用requestLocationUpdates给locationManager添加监听器，在onLocationChanged()中更新位置信息

package com.at.shield
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.net.VpnService
import android.os.Build
import android.os.ParcelFileDescriptor
class DnsVpnService: VpnService() {
 companion object { @Volatile var running=false; const val ACTION_STOP="com.at.shield.STOP" }
 private var tunnel: ParcelFileDescriptor?=null
 override fun onStartCommand(intent: Intent?, flags:Int, startId:Int):Int {
  if(intent?.action==ACTION_STOP){ stopTunnel(); stopSelf(); return START_NOT_STICKY }
  val nm=getSystemService(NOTIFICATION_SERVICE) as NotificationManager
  if(Build.VERSION.SDK_INT>=26) nm.createNotificationChannel(NotificationChannel("atshield","AT Shield",NotificationManager.IMPORTANCE_LOW))
  val n=if(Build.VERSION.SDK_INT>=26) Notification.Builder(this,"atshield") else Notification.Builder(this)
  startForeground(1,n.setSmallIcon(android.R.drawable.ic_lock_lock).setContentTitle("AT Shield").setContentText("حماية DNS مفعّلة").build())
  try { if(tunnel==null) { tunnel=Builder().setSession("AT Shield").addAddress("10.8.0.2",32).addDnsServer("185.228.168.168").addDnsServer("1.1.1.3").addRoute("0.0.0.0",0).establish(); running=tunnel!=null } } catch(_:Exception){ running=false }
  return START_STICKY
 }
 private fun stopTunnel(){ try{tunnel?.close()}catch(_:Exception){}; tunnel=null; running=false; stopForeground(STOP_FOREGROUND_REMOVE) }
 override fun onDestroy(){stopTunnel();super.onDestroy()}
}

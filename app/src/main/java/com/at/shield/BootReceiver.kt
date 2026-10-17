package com.at.shield
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
class BootReceiver: BroadcastReceiver(){ override fun onReceive(context:Context,intent:Intent?){ if(intent?.action==Intent.ACTION_BOOT_COMPLETED) { /* User can re-enable protection from the app after reboot. */ } } }

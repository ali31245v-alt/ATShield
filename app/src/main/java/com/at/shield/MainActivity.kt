package com.at.shield
import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
class MainActivity: ComponentActivity() {
 private val vpnRequest = registerForActivityResult(androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()) { if(it.resultCode == RESULT_OK) startShield() }
 override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState); setContent { ShieldTheme { var active by remember { mutableStateOf(DnsVpnService.running) }; Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement=Arrangement.spacedBy(16.dp)) { Text("AT Shield", style=MaterialTheme.typography.headlineLarge); Text("حماية وتصفية DNS على الجهاز"); Text(if(active) "الحالة: الحماية مفعّلة" else "الحالة: الحماية متوقفة"); Button(onClick={ if(active) { stopService(Intent(this@MainActivity,DnsVpnService::class.java).setAction(DnsVpnService.ACTION_STOP)); active=false } else { val i=VpnService.prepare(this@MainActivity); if(i!=null) vpnRequest.launch(i) else { startShield(); active=true } } }, modifier=Modifier.fillMaxWidth()) { Text(if(active) "إيقاف الحماية" else "تفعيل الحماية") }; Text("ملاحظة: قد تحتاج إلى تعطيل DNS الخاص في إعدادات أندرويد حتى تعمل التصفية.") } } }
 private fun startShield() { ContextCompat.startForegroundService(this,Intent(this,DnsVpnService::class.java)); DnsVpnService.running=true }
}

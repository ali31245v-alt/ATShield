package com.at.shield

import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {
    private val vpnRequest = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            startShield()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShieldTheme {
                var active by remember { mutableStateOf(DnsVpnService.running) }
                Column(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("AT Shield", style = MaterialTheme.typography.headlineLarge)
                    Text("حماية وتصفية DNS على الجهاز")
                    Text(if (active) "الحالة: الحماية مفعّلة" else "الحالة: الحماية متوقفة")
                    Button(
                        onClick = {
                            if (active) {
                                stopService(
                                    Intent(this@MainActivity, DnsVpnService::class.java)
                                        .setAction(DnsVpnService.ACTION_STOP)
                                )
                                active = false
                            } else {
                                val prepareIntent = VpnService.prepare(this@MainActivity)
                                if (prepareIntent != null) {
                                    vpnRequest.launch(prepareIntent)
                                } else {
                                    startShield()
                                    active = true
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (active) "إيقاف الحماية" else "تفعيل الحماية")
                    }
                    Text("ملاحظة: قد تحتاج إلى تعطيل DNS الخاص في إعدادات أندرويد حتى تعمل التصفية.")
                }
            }
        }
    }

    private fun startShield() {
        ContextCompat.startForegroundService(
            this,
            Intent(this, DnsVpnService::class.java)
        )
        DnsVpnService.running = true
    }
}

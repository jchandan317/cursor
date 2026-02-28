package com.pgsc.tracko

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.pgsc.tracko.navigation.TrackoNavGraph
import com.pgsc.tracko.ui.theme.TrackoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TrackoTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    TrackoNavGraph()
                }
            }
        }
    }
}

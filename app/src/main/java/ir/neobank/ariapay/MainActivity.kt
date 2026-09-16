package ir.neobank.ariapay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import ir.neobank.ariapay.feature.home.HomeRoute
import ir.neobank.ariapay.ui.theme.AriaPayTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AriaPayTheme {
                HomeRoute()
            }
        }
    }
}

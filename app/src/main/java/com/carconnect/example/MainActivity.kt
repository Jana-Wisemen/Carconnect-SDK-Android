package com.carconnect.example

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.carconnect.android_sdk.CarConnect
import com.carconnect.android_sdk.models.authentication.AuthenticationOptions
import com.carconnect.android_sdk.models.Environment
import com.carconnect.android_sdk.models.authentication.Authentication
import com.carconnect.android_sdk.networking.CarConnectResult

class MainActivity : AppCompatActivity() {

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            val tokens = intent?.getStringExtra(Authentication.RESULT_TOKENS)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CarConnect.register("CLIENT_ID", Environment.development)

        CarConnect.getInstance().brands { result ->
            when(result){
                is CarConnectResult.Success -> Log.d("CarConnectExample", result.value.toString())
                is CarConnectResult.Failure -> Log.e("CarConnectExample", result.message ?: "-", result.throwable)
            }
        }

        CarConnect.getInstance().totalConnections(email = "EMAIL") { result ->
            when(result){
                is CarConnectResult.Success -> Log.d("CarConnectExample", result.value.toString())
                is CarConnectResult.Failure -> Log.e("CarConnectExample", result.message ?: "-", result.throwable)
            }
        }

        findViewById<Button>(R.id.btnButton).setOnClickListener {
            val intent = CarConnect.getInstance().authenticationIntent(this, AuthenticationOptions(
                username = "EMAIL",
                brand = null
            ))

            startForResult.launch(intent)
        }

    }
}
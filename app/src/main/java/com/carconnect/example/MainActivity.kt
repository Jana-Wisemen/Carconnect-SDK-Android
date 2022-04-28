package com.carconnect.example

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.carconnect.android_sdk.CarConnect
import com.carconnect.android_sdk.models.AuthenticationOptions
import com.carconnect.android_sdk.models.Environment
import com.carconnect.android_sdk.networking.CarConnectResult

class MainActivity : AppCompatActivity() {

    val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            // Handle the Intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CarConnect.register("5e1fb9d4-3bcd-48dc-9943-337c24fe0e74", Environment.development)

        CarConnect.getInstance().brands { result ->
            when(result){
                is CarConnectResult.Success -> Log.d("CarConnectExample", result.value.toString())
                is CarConnectResult.Failure -> Log.e("CarConnectExample", result.message ?: "-", result.throwable)
            }
        }

        findViewById<Button>(R.id.btnButton).setOnClickListener {
            val intent = CarConnect.getInstance().authenticationIntent(this, AuthenticationOptions("Jesse", null))

            startForResult.launch(intent)
        }

    }
}
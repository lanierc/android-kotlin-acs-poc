package com.example.azurecallingpoc

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.azure.android.communication.common.CommunicationTokenCredential
import com.azure.android.communication.common.CommunicationUserIdentifier
import com.azure.communication.calling.CallAgent
import com.azure.communication.calling.CallClient
import com.azure.communication.calling.StartCallOptions

class MainActivity : AppCompatActivity() {

    private lateinit var callAgent: CallAgent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getAllPermissions()
        createAgent()

        val callButton: Button = findViewById(R.id.call_button)
        callButton.setOnClickListener {
            startCall()
        }
    }

    private fun getAllPermissions() {
        val requiredPermissions: Array<String> = arrayOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
        )

        val permissionsToAskFor: ArrayList<String> = arrayListOf()

        for (permission in requiredPermissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToAskFor.add(permission)
            }
        }
        if (permissionsToAskFor.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToAskFor.toArray(arrayOfNulls<String>(0)),
                1
            )
        }
    }

    private fun createAgent() {
        val userToken: String = BuildConfig.ACS_KEY
        try {
            val credential = CommunicationTokenCredential(userToken)
            callAgent = CallClient().createCallAgent(applicationContext, credential).get()
        } catch (ex: Exception) {
            Toast.makeText(
                    applicationContext,
                    "Failed to create call agent.",
                    Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun startCall() {
        val calleeIdView: EditText = findViewById(R.id.callee_id)
        val calleeId = calleeIdView.text.toString()
        val options = StartCallOptions()
        callAgent.call(
                applicationContext,
                arrayOf(CommunicationUserIdentifier(calleeId)),
                options
        )
    }
}
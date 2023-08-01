package com.example.flashlightapp

import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.widget.ToggleButton

class MainActivity : AppCompatActivity() {
    private lateinit var cameraManager: CameraManager
    private lateinit var cameraId : String
    private var isFlashLightOn: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toggleButton = findViewById<ToggleButton>(R.id.toggleButton)
        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager

        try{
            val cameraIds = cameraManager.cameraIdList
            for(id in cameraIds){
                val characteristics = cameraManager.getCameraCharacteristics(id);
                val flashAvailable = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)
                val lensFacing = characteristics.get(CameraCharacteristics.LENS_FACING)
                if (flashAvailable != null && flashAvailable && lensFacing != null &&
                    lensFacing == CameraCharacteristics.LENS_FACING_BACK
                ) {
                    cameraId = id
                    break
                }
            }
        }
        catch (e: CameraAccessException) {
            e.printStackTrace()
        }
        toggleButton.setOnClickListener {
            toggleFlashlight()
        }
    }

    private fun toggleFlashlight() {
        try{
            isFlashLightOn =!isFlashLightOn
            cameraManager.setTorchMode(cameraId,isFlashLightOn)
        }catch (e: CameraAccessException){
            e.printStackTrace()
            Toast.makeText(this,"Error accessing the Camera!",Toast.LENGTH_SHORT).show()
        }
    }
}
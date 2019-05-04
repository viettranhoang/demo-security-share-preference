package com.vit.demoandroidkeystore

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.security.KeyPairGeneratorSpec
import android.util.Log
import java.math.BigInteger
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PublicKey
import java.util.*
import javax.security.auth.x500.X500Principal

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prefUtils = PrefUtils(this)

        prefUtils.set("a", "Viet")

        Log.i("bbbb", prefUtils.get("a", "adu"))
    }
}

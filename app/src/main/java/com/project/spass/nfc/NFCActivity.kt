package com.project.spass.nfc


import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.project.spass.R

class NFCActivity: AppCompatActivity() {

    private var mNfcAdapter: NfcAdapter? = null

    // Check if device supports NFC
    private fun checkNFC(): Boolean {
        val nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        return if (nfcAdapter != null && nfcAdapter.isEnabled) {
            true
        } else {
            Toast.makeText(this, "Please enable NFC.", Toast.LENGTH_SHORT).show()
            false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfc)
        checkNFC()
    }

    fun setAdapter(adapter: NfcAdapter) {
        mNfcAdapter = adapter
    }

    override fun onResume() {
        super.onResume()
        mNfcAdapter?.let {
            NFCUtil.enableNFCInForeground(it, this, javaClass)
        }
    }

    override fun onPause() {
        super.onPause()
        mNfcAdapter?.let {
            NFCUtil.disableNFCInForeground(it, this)
        }
    }

/*    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val messageWrittenSuccessfully = NFCUtil.createNFCMessage("userId", intent)
        if (messageWrittenSuccessfully) {
            Toast.makeText(this, "Message written to the NFC tag successfully!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Message could not be written to the NFC tag!", Toast.LENGTH_SHORT).show()
        }
    }*/
}


package com.project.spass.presentation.screens.write_to_nfc_screen.component

import android.content.Context
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.project.spass.presentation.MainActivity
import com.project.spass.presentation.common.component.DefaultBackArrow
import com.project.spass.presentation.ui.theme.TextColor

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun WriteToNFC(
    navController: NavController
) {
    val context = LocalContext.current
    val nfcAdapter: NfcAdapter? by lazy {
        NfcAdapter.getDefaultAdapter(context)
    }
    var resultText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(0.5f)) {
                DefaultBackArrow {
                     navController.popBackStack()
                }
            }
            Box(modifier = Modifier.weight(0.7f)) {
                Text(
                    text = "NFC",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }



        Spacer(modifier = Modifier.padding(16.dp))

        Text("1. Hold the NFC tag near the back of your phone.", modifier = Modifier.padding(16.dp))
        Text("2. Tap the button below to write the message to the tag.", modifier = Modifier.padding(16.dp))
        Text("3. Wait for the message to be written to the tag.", modifier = Modifier.padding(16.dp))

        Box(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            // Text to display the result
            ElevatedButton(
                onClick = {
                    writeNfcTag(context, nfcAdapter, onResult = {
                        resultText = it
                    })
                },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = Color(0xFF111827),
                    contentColor = Color.White
                )
            ) {
                Text("Write to NFC")
            }
        }

        Text(resultText, fontSize = 16.sp, modifier = Modifier.padding(16.dp))
    }
}

@ExperimentalAnimationApi
@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
private fun writeNfcTag(
    context: Context,
    nfcAdapter: NfcAdapter?,
    onResult: (String) -> Unit
){
    if (nfcAdapter == null) {
        // Go back to the previous screen
        onResult("NFC is not enabled on this device.")
        return
    }

    val userId = FirebaseAuth.getInstance().currentUser?.uid
    if (userId != null) {
        val url = "https://citygo-1a359.web.app/users/$userId"
        val ndefMessage = NdefMessage(arrayOf(
            NdefRecord.createUri(url)
        ))

        // Write the message to the tag
        val intent = Intent(context, MainActivity::class.java)
        val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
        if(tag == null){
            onResult("Tag not found")
            return
        }
        val ndef = Ndef.get(tag)
        if (ndef == null) {
            onResult("Tag is not NDEF-formatted.")
            return
        }

        if (!ndef.isWritable) {
            onResult("Tag is not writable.")
            return
        }

        ndef.connect()
        ndef.writeNdefMessage(ndefMessage)
        ndef.close()

        onResult("Message written to tag successfully.")
    }else{
        onResult("User not found")
    }
}

package com.project.spass.presentation

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.tech.Ndef
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@Composable
fun NFCWriteScreen() {
    val context = LocalContext.current
    /*val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            // Write to database
            getUserDataFromDatabase(userId = userId, context = context) {
                val nfcAdapter: NfcAdapter? = NfcAdapter.getDefaultAdapter(context)

                nfcAdapter?.let { adapter ->
                    adapter.enableForegroundDispatch(
                        context as Activity,
                        PendingIntent.getActivity(
                            context,
                            0,
                            Intent(
                                context,
                                context::class.java
                            ).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                            PendingIntent.FLAG_IMMUTABLE
                        ),
                        null,
                        null
                    )

                    val ndef = NdefRecord.createTextRecord(null, userId!!.toString())
                    val ndefMessage = NdefMessage(arrayOf(ndef))

                    val intent = Intent(context, context::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                    val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

                    val tag = adapter.tag

                    tag?.let {
                        val ndefTag = Ndef.get(tag)

                        ndefTag?.use { ndefTag ->
                            ndefTag.connect()
                            ndefTag.writeNdefMessage(ndefMessage)
                            ndefTag.close()
                        }
                    }
                }

            }
        }
    }*/
}
fun getUserDataFromDatabase(userId: String, context: Context, callback: (Any?) -> Unit){
    val database = Firebase.database
    val myRef = database.getReference("users")
    myRef.child(userId).get().addOnSuccessListener {
        val value = it.getValue(UserDetails::class.java)
        if (value != null) {
            // User data found
            callback(value)
        } else {
            // User data not found
            Toast.makeText(context, "User data not found", Toast.LENGTH_SHORT).show()
        }
    }.addOnFailureListener {
        // User data not found
        Toast.makeText(context, "User data not found", Toast.LENGTH_SHORT).show()
    }
}

data class UserDetails(
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val address: String? = null,
)
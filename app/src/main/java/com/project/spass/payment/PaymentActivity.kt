package com.project.spass.payment
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.project.spass.presentation.screens.pass_screen.compnent.CurrentUserData
import com.project.spass.presentation.screens.pass_screen.compnent.PassDataFromDepot
import dev.shreyaspatil.easyupipayment.EasyUpiPayment
import dev.shreyaspatil.easyupipayment.listener.PaymentStatusListener
import dev.shreyaspatil.easyupipayment.model.PaymentApp
import dev.shreyaspatil.easyupipayment.model.TransactionDetails

class PaymentActivity: ComponentActivity(), PaymentStatusListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Get Payment Bundle
        val bundle = intent.extras
        val months = bundle?.getString("months")
        val passId = bundle?.getString("passId")
        val startDate = bundle?.getString("purchasedDate")

        // Get pass details from backend

        setContent {
            PaymentScreen(paymentActivity = this@PaymentActivity, months = months.toString(), passId = passId.toString(),purchasedDate = startDate.toString())
        }
    }

    override fun onTransactionCancelled() {
        Toast.makeText(this, "Transaction canceled by user..", Toast.LENGTH_SHORT).show()
    }

    override fun onTransactionCompleted(transactionDetails: TransactionDetails) {
        Toast.makeText(this, "Transaction completed by user..", Toast.LENGTH_SHORT).show()

        // Update the user pass details in the database
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val database = FirebaseDatabase.getInstance()
        val userPassReference: DatabaseReference = database.getReference("users/$userId/passes")

        // Get the current pass details
        val passId = userPassReference.child("passId").get().toString()
        val expiryDate = userPassReference.child("expiryDate").get().toString()
        val purchaseDate = userPassReference.child("purchaseDate").get().toString()

        println("Pass ID: $passId")
        println("Expiry Date: $expiryDate")
        println("Purchase Date: $purchaseDate")


        /*userPassReference.child("transactionId").setValue(transactionDetails.transactionId)
        userPassReference.child("purchaseDate").setValue(transactionDetails.transactionDate)
        userPassReference.child("expiryDate").setValue(transactionDetails.transactionDate)*/

        finish()
    }
}

data class CurrentUserData(
    val firstName: String? = "",
    val lastName: String? = "",
    val email: String? = "",
    val address: String? = "",
    val phoneNumber: String? = "",
    val profileImage: String? = "",
    val purchaseDate: String? = "",
    val expiryDate: String? = "",
    val price : String? = "",
    val passId: String? = ""
)

@Composable
fun PaymentScreen(paymentActivity: PaymentActivity , months: String , passId: String, purchasedDate : String) {
    val ctx = LocalContext.current
    val activity = (LocalContext.current as? Activity)
    GetPassDetails {
        makePayment(
            (it.price)!!.toDouble().toString(),
            "sucess@upi",
            "Swastik Patil",
            "Payment for $months months pass",
            passId,
            ctx,
            activity!!,
            paymentActivity
        )

    }

}

@Composable
private fun GetPassDetails(callback : (PassDataFromDepot) -> Unit){
    var passId by remember { mutableStateOf<String?>(null) }
    var passDataFromDepot by remember { mutableStateOf<PassDataFromDepot?>(null) }
    var currentUserData by remember { mutableStateOf<CurrentUserData?>(null) }

    fetchPassId { it ->
        println(it)
        currentUserData = it
        // passId = currentUserData!!.passId
        fetchOriginalPassData(passId.toString()){
            passDataFromDepot = it
        }
    }

    passDataFromDepot?.let{
        callback(passDataFromDepot!!)
    }

}

private fun makePayment(
    amount: String,
    upi: String,
    name: String,
    desc: String,
    transcId: String, ctx: Context, activity: Activity, mainActivity: PaymentStatusListener
) {
    try {
        val easyUpiPayment = EasyUpiPayment(activity) {
            this.paymentApp = PaymentApp.ALL
            this.payeeVpa = upi
            this.payeeName = name
            this.transactionId = transcId
            this.transactionRefId = transcId
            this.payeeMerchantCode = transcId
            this.description = desc
            this.amount = amount
        }
        easyUpiPayment.setPaymentStatusListener(mainActivity)
        easyUpiPayment.startPayment()
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(ctx, e.message, Toast.LENGTH_SHORT).show()
    }
}


private fun fetchPassId(callback: (CurrentUserData) -> Unit) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    val database = FirebaseDatabase.getInstance()
    val userPassReference: DatabaseReference = database.getReference("users").child(userId.toString())

    userPassReference.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            var currentUserData = CurrentUserData(
                firstName = dataSnapshot.child("firstName").value.toString(),
                lastName = dataSnapshot.child("lastName").value.toString(),
                email = dataSnapshot.child("email").value.toString(),
                address = dataSnapshot.child("address").value.toString(),
                phoneNumber = dataSnapshot.child("phoneNumber").value.toString(),
                profileImage = dataSnapshot.child("profileImage").value.toString(),
            )
            callback(currentUserData)
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Handle database error
        }
    })
}

private fun fetchOriginalPassData( passId: String, callback: (PassDataFromDepot) -> Unit){
    val database = FirebaseDatabase.getInstance()
    val userPassReference: DatabaseReference = database.getReference("passes").child(passId)
    userPassReference.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val passDataFromDepot = PassDataFromDepot(
                passId = dataSnapshot.child("passId").value.toString(),
                source = dataSnapshot.child("source").value.toString(),
                destination = dataSnapshot.child("destination").value.toString(),
                price = dataSnapshot.child("price").value.toString()
            )
            callback(passDataFromDepot)
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Handle database error
        }
    })
}

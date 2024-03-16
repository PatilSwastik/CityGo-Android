package com.project.spass.payment
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.project.spass.domain.model.PassModel
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

        // Get pass details from backend

        setContent {
            PaymentScreen(paymentActivity = this@PaymentActivity, months = months, passId = passId)
        }
    }

    override fun onTransactionCancelled() {
        Toast.makeText(this, "Transaction canceled by user..", Toast.LENGTH_SHORT).show()
    }

    override fun onTransactionCompleted(transactionDetails: TransactionDetails) {
        Toast.makeText(this, "Transaction completed by user..", Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun PaymentScreen(paymentActivity: PaymentActivity , months: String?, passId: String?) {
    val ctx = LocalContext.current
    val activity = (LocalContext.current as? Activity)

    // Get pass details from backend
    val passDetails : PassModel = getPassDetails(passId)
    //val userDetails : UserModel = getUserDetails(userId)

    // Get UPI ID from user using TextField

    makePayment(
        passDetails.price.toString(),
        "success@razorpay",
        "Test ",
        "Test",
        "test",
        ctx,
        activity!!,
        paymentActivity
    )
}

private fun getPassDetails(passId: String?): PassModel{
    // Get pass details from backend
    return PassModel(
        id = 1,
        source = "Test",
        destination = "Test",
        price = 500.0,
    )
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

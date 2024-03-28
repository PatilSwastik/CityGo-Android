package com.project.spass.presentation.screens.pass_detail_screen.component

import android.app.Activity
import android.content.Context
import android.widget.Toast
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.project.spass.payment.PaymentActivity
import com.project.spass.payment.makePayment
import com.project.spass.presentation.screens.pass_detail_screen.PassDetailViewModel
import com.project.spass.presentation.screens.pass_screen.compnent.PassDataFromDepot
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun PassDetailScreen(
    viewModel: PassDetailViewModel = hiltViewModel(),
    popBack: () -> Unit
) {
    val state = viewModel.state.value
    val context = LocalContext.current
    val view = LocalView.current
    val window = (view.context as Activity).window
    if (state.isLoading) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (state.passDetail != null) {
        val pass = state.passDetail
        var months by remember { mutableIntStateOf(1) }
        var date by remember { mutableStateOf("") }
        val lightBlue = Color(0xFFC8D2E9)
        val configuration = LocalConfiguration.current
        window.statusBarColor = lightBlue.toArgb()
        window.navigationBarColor = lightBlue.toArgb()
        Column(
            modifier = Modifier
                .height(configuration.screenHeightDp.dp)
                .background(lightBlue),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column{
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 15.dp, top = 15.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            popBack()
                        },
                        modifier = Modifier
                            .background(color = Color.White, shape = CircleShape)
                            .clip(CircleShape)

                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = null,
                            tint = Color.Black
                        )
                    }
                    Text(
                        text = "Pass Details",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = "",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }

                Spacer(modifier = Modifier.height(50.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                        .background(lightBlue),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "₹"+(pass.price * months).toString(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(15.dp)
                        ) {
                            Text(
                                text = pass.source,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )

                            Spacer(modifier = Modifier.height(25.dp))

                            Text(
                                text = pass.destination,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                            )
                            Spacer(modifier = Modifier.height(25.dp))
                        }

                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(text="Select Months",fontSize = 16.sp, fontWeight = FontWeight.Bold,)
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = {
                                    if (months > 1) {
                                        months--
                                    }
                                },
                                modifier = Modifier
                                    .background(color = Color.White, shape = CircleShape)
                                    .clip(CircleShape)

                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Remove,
                                    contentDescription = null,
                                    tint = Color.Black
                                )
                            }
                            Text(
                                text = months.toString(),
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .width(35.dp)
                                    .wrapContentHeight(),
                                fontWeight = FontWeight.Bold,
                            )
                            IconButton(
                                onClick = {
                                    if (months < 13) {
                                        months++
                                    }
                                },
                                modifier = Modifier
                                    .background(color = Color.White, shape = CircleShape)
                                    .clip(CircleShape)

                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Add,
                                    contentDescription = null,
                                    tint = Color.Black
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp),
                    ){
                        Row (
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically

                        ) {
                            Text(
                                text = "Valid From ",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                            )
                            MyDatePickerDialog{onDateSelected ->
                                date = onDateSelected
                            }
                        }
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFF111827),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 30.dp, bottom = 30.dp)
                        .height(60.dp),
                    onClick = {
                       // Send data to payment activity
                        payment(PaymentActivity(), months.toString(), pass.id.toString(), date, context)
                    }
                ) {
                    Text(text = "BUY NOW", fontSize = 16.sp)
                }
            }
        }

    }else{
        Toast.makeText(context, state.errorMessage, Toast.LENGTH_SHORT).show()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDatePickerDialog(
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(selectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            return utcTimeMillis >= System.currentTimeMillis()
        }
    })

    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""

    DatePickerDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(onClick = {
                onDateSelected(selectedDate)
                onDismiss()
            }
            ) {
                Text(text = "OK")
            }
        },
        dismissButton = {
            Button(onClick = {
                onDismiss()
            }) {
                Text(text = "Cancel")
            }
        }
    ) {
        DatePicker(
            state = datePickerState
        )
    }
}

@Composable
fun MyDatePickerDialog(onDateSelected: (String) -> Unit) {
    var selectedDate by remember {
        mutableStateOf("Select Starting Date")
    }

    var showDatePicker by remember {
        mutableStateOf(false)
    }

    Box(contentAlignment = Alignment.Center) {
        Button(
            onClick = { showDatePicker = true },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFF111827),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(10.dp),
        ) {
            Text(text = selectedDate)
        }
    }

    if (showDatePicker) {
        MyDatePickerDialog(
            onDateSelected = { selectedDate = it
                onDateSelected(selectedDate)
                             },
            onDismiss = { showDatePicker = false }
        )
    }
}

private fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy")
    return formatter.format(Date(millis))
}

fun payment(
    paymentActivity: PaymentActivity, months: String, passId: String, purchasedDate: String,
    context: Context
) {
    getPassDetails(passId){
        val price = (it.price)!!.toDouble() * months.toDouble()
        makePayment(
            price.toString(),
            "sucess@upi",
            "Swastik Patil",
            "Payment for $months months pass",
            passId,
            context,
            context as Activity,
            paymentActivity
        )
    }
 }

fun getPassDetails(passId: String,callback : (PassDataFromDepot) -> Unit){
    val database = FirebaseDatabase.getInstance()
    val userPassReference: DatabaseReference = database.getReference("passes/$passId")
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
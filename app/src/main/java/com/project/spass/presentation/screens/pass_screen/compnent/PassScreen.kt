package com.project.spass.presentation.screens.pass_screen.compnent

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

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

data class PassDetails(
    val passId: String ?= "",
    val purchaseDate: String? = "",
    val expiryDate: String? = ""
)

data class UserData1(
    val firstName: String? = "",
    val lastName: String? = "",
    val email: String? = "",
    val address: String? = "",
    val phoneNumber: String? = "",
    val source : String? = "",
    val destination : String? = "",
    val price : String? = "",
    var passes: List<PassData>? = null
)

data class PassData(
    val passId: String? = "",
    val purchaseDate: String? = "",
    val expiryDate: String? = ""
)

data class PassDataFromDepot(
    val passId: String ?= "",
    val source: String? = "",
    val destination: String? = "",
    val price: String? = ""
    )

@Composable
fun PassScreen() {
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    var passId by remember { mutableStateOf<String?>(null) }
    var passDataFromDepot by remember { mutableStateOf<PassDataFromDepot?>(null) }
    var currentUserData by remember { mutableStateOf<CurrentUserData?>(null) }
    fetchPassId(){ it ->
        println(it)
        currentUserData = it
        passId = currentUserData!!.passId
        fetchOriginalPassData(passId.toString()){
            passDataFromDepot = it
            println("Pass data from depot: $passDataFromDepot")
        }
    }

    var imageUrl by remember(userId) { mutableStateOf<String?>(null) }
    val storageRef = Firebase.storage.reference

    ElevatedCard(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            pressedElevation = 16.dp
        )

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Spacer(modifier = Modifier.height(16.dp))
            // Display other user data
            currentUserData?.let { pass ->
                if(pass.passId != ""){
                    // Profile Image
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = rememberQrBitmapPainter(
                                content = "http://10.0.2.2/qr_code/${userId}/${pass.passId}",
                                size = 300.dp,
                                padding = 1.dp
                            ),
                            contentDescription = null
                        )
                    }

                    LaunchedEffect(userId) {
                        if (userId != null) {
                            val imageRef = storageRef.child("images/$userId")
                            try {
                                val url = imageRef.downloadUrl.await()
                                // Update the imageUrl state with the downloaded URL
                                imageUrl = url.toString()
                            } catch (e: Exception) {
                                // Handle exceptions
                                e.printStackTrace()
                            }
                        }
                    }


                    Text(
                        text = "Name : ${currentUserData!!.firstName ?: ""} ${currentUserData?.lastName ?: ""}",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                    Text(text = "Purchased on : ${currentUserData!!.purchaseDate}", modifier = Modifier.padding(16.dp), textAlign = TextAlign.Left)
                    Text(text = "Expires on : ${currentUserData!!.expiryDate}", modifier = Modifier.padding(16.dp), textAlign = TextAlign.Left)
                    Text(text = "Address : ${currentUserData!!.address}", modifier = Modifier.padding(16.dp), textAlign = TextAlign.Left)

                    passDataFromDepot?.let {
                        Text(text = "Source : ${passDataFromDepot!!.source}", modifier = Modifier.padding(16.dp), textAlign = TextAlign.Left)
                        Text(text = "Destination : ${passDataFromDepot!!.destination}", modifier = Modifier.padding(16.dp), textAlign = TextAlign.Left)
                        Text(text = "Price : ${passDataFromDepot!!.price}", modifier = Modifier.padding(16.dp), textAlign = TextAlign.Left)
                    }

                }else{
                    Row {
                        // Profile Image
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .clip(RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            currentUserData!!.profileImage?.let { url ->
                                Image(
                                    painter = rememberImagePainter(url),
                                    contentDescription = "Profile Picture",
                                    modifier = Modifier
                                        .size(150.dp)
                                        .clip(CircleShape)
                                )
                            }
                        }
                    }

                    Text(
                        text = "Name : ${currentUserData?.firstName ?: ""} ${currentUserData?.lastName ?: ""}",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                    Text(text = "Email : ${currentUserData!!.email}", modifier = Modifier.padding(16.dp), textAlign = TextAlign.Left)
                    Text(text = "Phone Number : ${currentUserData!!.phoneNumber}", modifier = Modifier.padding(16.dp), textAlign = TextAlign.Left)
                    Text(text = "Address : ${currentUserData!!.address}", modifier = Modifier.padding(16.dp), textAlign = TextAlign.Left)
                // Add other fields as needed
                }
            }
        }
    }
}

fun fetchPassId(callback: (CurrentUserData) -> Unit) {
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
                expiryDate = dataSnapshot.child("passes").child("expiryDate").value.toString(),
                purchaseDate = dataSnapshot.child("passes").child("purchaseDate").value.toString(),
               passId = dataSnapshot.child("passes").child("passId").value.toString(),
              )
            callback(currentUserData)
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Handle database error
        }
    })
}

fun fetchOriginalPassData( passId: String, callback: (PassDataFromDepot) -> Unit){
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

@Composable
fun rememberQrBitmapPainter(
    content: String,
    size: Dp = 150.dp,
    padding: Dp = 0.dp
): BitmapPainter {

    check(content.isNotEmpty()) { "Content must not be empty" }
    check(size >= 0.dp) { "Size must be positive" }
    check(padding >= 0.dp) { "Padding must be positive" }

    val density = LocalDensity.current
    val sizePx = with(density) { size.roundToPx() }
    val paddingPx = with(density) { padding.roundToPx() }

    val bitmapState = remember {
        mutableStateOf<Bitmap?>(null)
    }

    // Use dependency on 'content' to re-trigger the effect when content changes
    LaunchedEffect(content) {
        val bitmap = generateQrBitmap(content, sizePx, paddingPx)
        bitmapState.value = bitmap
    }

    val bitmap = bitmapState.value ?: createDefaultBitmap(sizePx)

    return remember(bitmap) {
        BitmapPainter(bitmap.asImageBitmap())
    }
}


/**
 * Generates a QR code bitmap for the given [content].
 * The [sizePx] parameter defines the size of the QR code in pixels.
 * The [paddingPx] parameter defines the padding of the QR code in pixels.
 * Returns null if the QR code could not be generated.
 * This function is suspendable and should be called from a coroutine is thread-safe.
 */
private suspend fun generateQrBitmap(
    content: String,
    sizePx: Int,
    paddingPx: Int
): Bitmap? = withContext(Dispatchers.IO) {
    val qrCodeWriter = QRCodeWriter()

    // Set the QR code margin to the given padding
    val encodeHints = mutableMapOf<EncodeHintType, Any?>()
        .apply {
            this[EncodeHintType.MARGIN] = paddingPx
        }

    try {
        val bitmapMatrix = qrCodeWriter.encode(
            content, BarcodeFormat.QR_CODE,
            sizePx, sizePx, encodeHints
        )

        val matrixWidth = bitmapMatrix.width
        val matrixHeight = bitmapMatrix.height

        val colors = IntArray(matrixWidth * matrixHeight) { index ->
            val x = index % matrixWidth
            val y = index / matrixWidth
            val shouldColorPixel = bitmapMatrix.get(x, y)
            if (shouldColorPixel) Color.BLACK else Color.WHITE
        }

        Bitmap.createBitmap(colors, matrixWidth, matrixHeight, Bitmap.Config.ARGB_8888)
    } catch (ex: WriterException) {
        null
    }
}

/**
 * Creates a default bitmap with the given [sizePx].
 * The bitmap is transparent.
 * This is used as a fallback if the QR code could not be generated.
 * The bitmap is created on the UI thread.
 */
private fun createDefaultBitmap(sizePx: Int): Bitmap {
    return Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888).apply {
        eraseColor(Color.TRANSPARENT)
    }
}

@Preview
@Composable
fun PassScreenPreview() {
    PassScreen()
}
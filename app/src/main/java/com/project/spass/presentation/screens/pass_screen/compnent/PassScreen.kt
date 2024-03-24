package com.project.spass.presentation.screens.pass_screen.compnent

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.project.spass.presentation.screens.profile_screen.component.ComponentCircle
import com.project.spass.presentation.ui.animations.shimmerLoadingAnimation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class CurrentUserData(
    val firstName: String? = "",
    val lastName: String? = "",
    val email: String? = "",
    val address: String? = "",
    val phoneNumber: String? = "",
    val profileImage: String? = "",
    val passes : List<PassData> = emptyList(),
)

data class PassData(
    val passId: String? = "",
    val expiryDate: String? = "",
    val purchaseDate: String? = "",
)

data class PassDataFromDepot(
    val passId: String? = "",
    val source: String? = "",
    val destination: String? = "",
    val price: String? = ""
)

@Composable
fun PassScreen() {
    var isLoadingCompleted by remember { mutableStateOf(false) }
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    var currentUserData by remember { mutableStateOf<CurrentUserData?>(null) }

    fetchPassId {
        currentUserData = it
        isLoadingCompleted = true
    }

    var imageUrl by remember(userId) { mutableStateOf<String?>(null) }
    val storageRef = Firebase.storage.reference

    if (isLoadingCompleted) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(androidx.compose.ui.graphics.Color.White)
                .padding(16.dp),
        ) {
            Column(
                modifier = Modifier.align(alignment = Alignment.TopCenter)
            ) {

                // Display user data
                currentUserData?.let { data ->
                    if (data.passes[0].passId != "") {
                        // Personal Data
                        Spacer(modifier = Modifier.padding(24.dp))
                        Text(
                            text = "Name : ${currentUserData!!.firstName ?: ""} ${currentUserData?.lastName ?: ""}",
                            textAlign = TextAlign.Left,fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                        Text(
                            text = "Contact : ${currentUserData!!.phoneNumber}",
                            textAlign = TextAlign.Left,fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                        Text(
                            text = "Email : ${currentUserData!!.email}",
                            textAlign = TextAlign.Left,fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                        Text(
                            text = "Address : ${currentUserData!!.address}",
                            textAlign = TextAlign.Left,fontSize = 20.sp
                        )

                        // Pass Data
                        Spacer(modifier = Modifier.padding(16.dp))

                        // Pass Each Pass Data to the Pass Composable
                        currentUserData!!.passes.forEachIndexed { index, pass ->
                            Pass(pass = pass)
                            Spacer(modifier = Modifier.padding(8.dp))
                        }
                    }
                    else {
                        Row {
                            // Profile Image
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(150.dp)
                                    .clip(RoundedCornerShape(10.dp)),
                                contentAlignment = Alignment.Center
                            ) {

                                if(currentUserData!!.profileImage == null) {
                                    ComponentCircle(
                                        isLoadingCompleted = isLoadingCompleted,
                                    )
                                }

                                currentUserData!!.profileImage?.let { url ->
                                    Image(
                                        painter = rememberAsyncImagePainter(url) ,
                                        contentDescription = "Profile Picture",
                                        modifier = Modifier
                                            .size(150.dp)
                                            .clip(CircleShape)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.padding(24.dp))
                        Text(
                            text = "Name : ${currentUserData?.firstName ?: ""} ${currentUserData?.lastName ?: ""}",
                            textAlign = TextAlign.Left, fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                        Text(
                            text = "Email : ${currentUserData!!.email}",
                            textAlign = TextAlign.Left,fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                        Text(
                            text = "Contact : ${currentUserData!!.phoneNumber}",
                            textAlign = TextAlign.Left,fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.padding(8.dp))

                        Text(
                            text = "Address : ${currentUserData!!.address}",
                            textAlign = TextAlign.Left,fontSize = 20.sp
                        )

                        // Add other fields as needed
                    }
                }

            }
        }
    }
    else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(androidx.compose.ui.graphics.Color.White)
                .padding(48.dp),
        ) {
            Column(
                modifier = Modifier.align(alignment = Alignment.TopCenter)
            ) {
                Spacer(modifier = Modifier.padding(8.dp))
                ComponentRectangleLineLong(isLoadingCompleted = isLoadingCompleted)
                Spacer(modifier = Modifier.padding(4.dp))
                ComponentRectangleLineLong( isLoadingCompleted = isLoadingCompleted )
                Spacer(modifier = Modifier.padding(4.dp))
                ComponentRectangleLineLong( isLoadingCompleted = isLoadingCompleted )
                Spacer(modifier = Modifier.padding(4.dp))
                ComponentRectangleLineLong( isLoadingCompleted = isLoadingCompleted )
                Spacer(modifier = Modifier.padding(24.dp))
                ComponentRectangle(isLoadingCompleted = isLoadingCompleted)
                Spacer(modifier = Modifier.padding(24.dp))
                ComponentRectangle(isLoadingCompleted = isLoadingCompleted)
            }
        }
    }


}

private fun fetchPassId(callback: (CurrentUserData) -> Unit) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    val database = FirebaseDatabase.getInstance()
    val userPassReference: DatabaseReference =
        database.getReference("users").child(userId.toString())

    userPassReference.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {

            val passesSnapshot = dataSnapshot.child("passes").children

            val passesList = passesSnapshot.map { passSnapshot ->
                PassData(
                    passId = passSnapshot.child("passId").value.toString(),
                    purchaseDate = passSnapshot.child("purchaseDate").value.toString(),
                    expiryDate = passSnapshot.child("expiryDate").value.toString(),
                )
            }

            val currentUserData = CurrentUserData(
                firstName = dataSnapshot.child("firstName").value.toString(),
                lastName = dataSnapshot.child("lastName").value.toString(),
                email = dataSnapshot.child("email").value.toString(),
                address = dataSnapshot.child("address").value.toString(),
                phoneNumber = dataSnapshot.child("phoneNumber").value.toString(),
                profileImage = dataSnapshot.child("profileImage").value.toString(),
                passes = passesList
            )
            callback(currentUserData)
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Handle database error
            callback(CurrentUserData())
        }
    })
}

private fun fetchOriginalPassData(passId: String, callback: (PassDataFromDepot) -> Unit) {
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

private fun createDefaultBitmap(sizePx: Int): Bitmap {
    return Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888).apply {
        eraseColor(Color.TRANSPARENT)
    }
}

@Composable
fun ComponentRectangle(isLoadingCompleted: Boolean) {
    Box(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(24.dp))
            .background(color = androidx.compose.ui.graphics.Color.LightGray)
            .height(200.dp)
            .fillMaxWidth()
            .shimmerLoadingAnimation(isLoadingCompleted)
    )
}

@Composable
fun ComponentRectangleLineLong(isLoadingCompleted: Boolean) {
    Box(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(8.dp))
            .background(color = androidx.compose.ui.graphics.Color.LightGray)
            .size(height = 30.dp, width = 200.dp)
            .shimmerLoadingAnimation(isLoadingCompleted)
    )
}

@Composable
fun Pass(
    pass: PassData,
) {
    var passDataFromDepot by remember { mutableStateOf<PassDataFromDepot?>(null) }
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    fetchOriginalPassData(pass.passId.toString()) {
        passDataFromDepot = it
    }

    passDataFromDepot?.let {
        Box(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(24.dp))
                .background(color = com.project.spass.presentation.ui.theme.CardColor)
                .height(200.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box {
                        Image(
                            painter = rememberQrBitmapPainter("https://citygo-1a359.web.app/qr_code/$userId/${pass.passId}"),
                            contentDescription = null
                        )
                    }
                    Spacer(modifier = Modifier.padding(8.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = "${it.source} \n${it.destination}",
                            textAlign = TextAlign.Left, fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                        Text(
                            text = "${pass.purchaseDate} \n${pass.expiryDate}",
                            textAlign = TextAlign.Left, fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                        Text(
                            text = "₹ ${it.price}",
                            textAlign = TextAlign.Left, fontSize = 20.sp
                        )
                    }
                }
            }
        }
    }
}


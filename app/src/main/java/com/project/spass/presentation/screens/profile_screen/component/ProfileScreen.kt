package com.project.spass.presentation.screens.profile_screen.component

import android.app.Activity
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.project.spass.presentation.common.component.DefaultBackArrow
import com.project.spass.presentation.ui.theme.TextColor
import com.project.spass.presentation.ui.theme.PrimaryColor
import com.project.spass.R
import com.project.spass.presentation.graphs.auth_graph.AuthScreen
import kotlinx.coroutines.tasks.await

@Composable
fun ProfileScreen(
    navController: NavController
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    var imageUrl by remember(userId) { mutableStateOf<String?>(null) }
    val storageRef = Firebase.storage.reference

    val imageUri = remember { mutableStateOf<String?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            imageUri.value = uri.toString()
            if (uri != null) {
                uploadImageToFirebaseStorage(uri){
                    imageUrl = it
                }
            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp)
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
                    text = "Profile",
                    color = MaterialTheme.colors.TextColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            imageUrl?.let { url ->
                val painter = rememberImagePainter(url)
                Image(
                    painter = painter,
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                )
            }
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

        Spacer(modifier = Modifier.height(60.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)

                .background(Color(0x8DB3B0B0), shape = RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
                .clickable {
                    launcher.launch("image/*")
                }
                .padding(5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically

        ) {
            Icon(
                painter = painterResource(id = R.drawable.user_icon),
                contentDescription = null,
                modifier = Modifier.weight(0.05f), tint = MaterialTheme.colors.PrimaryColor
            )
            Text("Profile Picture", modifier = Modifier.weight(0.2f))
            Icon(
                painter = painterResource(id = R.drawable.arrow_right),
                contentDescription = null,
                modifier = Modifier.weight(0.05f),
                tint = MaterialTheme.colors.TextColor
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)

                .background(Color(0x8DB3B0B0), shape = RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
                .clickable {
                    // Start New Activity to write to NFC
                    val intent = android.content.Intent(context, com.project.spass.nfc.NFCActivity::class.java )
                    context.startActivity(intent)
                }
                .padding(5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.user_icon),
                contentDescription = null,
                modifier = Modifier.weight(0.05f), tint = MaterialTheme.colors.PrimaryColor
            )
            Text("Write To NFC", modifier = Modifier.weight(0.2f))
            Icon(
                painter = painterResource(id = R.drawable.arrow_right),
                contentDescription = null,
                modifier = Modifier.weight(0.05f),
                tint = MaterialTheme.colors.TextColor
            )
        }


        Spacer(modifier = Modifier.height(15.dp))


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)

                .background(Color(0x8DB3B0B0), shape = RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
                .clickable {

                }
                .padding(5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.settings),
                contentDescription = null,
                modifier = Modifier.weight(0.05f), tint = MaterialTheme.colors.PrimaryColor
            )
            Text("Settings", modifier = Modifier.weight(0.2f))
            Icon(
                painter = painterResource(id = R.drawable.arrow_right),
                contentDescription = null,
                modifier = Modifier.weight(0.05f),
                tint = MaterialTheme.colors.TextColor
            )
        }


        Spacer(modifier = Modifier.height(15.dp))


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)

                .background(Color(0x8DB3B0B0), shape = RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
                .clickable {

                }
                .padding(5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.question_mark),
                contentDescription = null,
                modifier = Modifier.weight(0.05f), tint = MaterialTheme.colors.PrimaryColor
            )
            Text("Help Center", modifier = Modifier.weight(0.2f))
            Icon(
                painter = painterResource(id = R.drawable.arrow_right),
                contentDescription = null,
                modifier = Modifier.weight(0.05f),
                tint = MaterialTheme.colors.TextColor
            )
        }

        Spacer(modifier = Modifier.height(15.dp))


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(Color(0x8DB3B0B0), shape = RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
                .clickable {
                    onLogout(navController,context)
                }
                .padding(5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.log_out),
                contentDescription = null,
                modifier = Modifier.weight(0.05f), tint = MaterialTheme.colors.PrimaryColor
            )
            Text("Logout", modifier = Modifier.weight(0.2f))
            Icon(
                painter = painterResource(id = R.drawable.arrow_right),
                contentDescription = null,
                modifier = Modifier.weight(0.05f),
                tint = MaterialTheme.colors.TextColor
            )
        }

    }
}

private fun onLogout(navController: NavController,context : Context){
    // clear shared preferences
    FirebaseAuth.getInstance().signOut()
    navController.navigate(AuthScreen.SignInScreen.route)
}

private fun uploadImageToFirebaseStorage(uri: Uri, imageUrl: (String) -> Unit) {
    val storageRef = Firebase.storage.reference
    val imagesRef = storageRef.child("images/${FirebaseAuth.getInstance().currentUser?.uid}")
    val uploadTask = imagesRef.putFile(uri)

    uploadTask.continueWithTask { task ->
        if (!task.isSuccessful) {
            task.exception?.let {
                throw it
            }
        }
        imagesRef.downloadUrl
    }.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val downloadUri = task.result

            saveImageUriToDatabase(downloadUri.toString())
            // set the imageUri as profileImage
            imageUrl(downloadUri.toString())

        } else {
            // Handle failures
            println("Failed to Save Image URI")
        }
    }
}

private fun saveImageUriToDatabase(imageUri: String) {
    val database = Firebase.database
    val currentUser = FirebaseAuth.getInstance().currentUser
    currentUser?.uid?.let {
        val userRef = database.getReference("users/$it")
        userRef.child("profileImage").setValue(imageUri)
    }
}

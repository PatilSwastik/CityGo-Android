package com.project.spass.presentation.graphs.home_graph


import com.project.spass.presentation.screens.pass_screen.compnent.PassScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.project.spass.domain.model.User
import com.project.spass.presentation.graphs.Graph
import com.project.spass.presentation.graphs.detail_graph.DetailScreen
import com.project.spass.presentation.graphs.detail_graph.detailNavGraph
import com.project.spass.presentation.help_desk.HelpDesk
import com.project.spass.presentation.screens.dashboard_screen.component.DashboardScreen
import com.project.spass.presentation.screens.profile_screen.component.ProfileScreen
import com.project.spass.presentation.screens.write_to_nfc_screen.component.WriteToNFC
import kotlinx.coroutines.flow.StateFlow

@Composable
fun HomeNavGraph(
    navController: NavHostController,
    searchText: String,
    user: StateFlow<User?>
) {

    NavHost(
        navController = navController,
        route = Graph.HOME,
        startDestination = ShopHomeScreen.DashboardScreen.route
    ) {
        composable(ShopHomeScreen.DashboardScreen.route) {
            DashboardScreen(searchText = searchText) { passId ->
                navController.navigate(DetailScreen.PassDetailScreen.route + "/${passId}")
            }
        }
        composable(ShopHomeScreen.PassScreen.route) {
            PassScreen()
        }
        composable(ShopHomeScreen.ProfileScreen.route) {
            ProfileScreen(navController,user)
        }

        composable(ShopHomeScreen.WriteToNFC.route) {
            WriteToNFC(navController)
        }

        composable(ShopHomeScreen.HelpDeskScreen.route) {
            HelpDesk()
        }

        //detail graph
        detailNavGraph(navController = navController)
    }

}

private fun getUserType(callback : (Boolean) -> Unit): String {
    // Get all the users under admin and check if the current user is an admin
    val database = FirebaseDatabase.getInstance()
    val userReference: DatabaseReference = database.getReference("admins")

    // Check if the current user is an admin
    userReference.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            for (postSnapshot in dataSnapshot.children) {
                val admin = postSnapshot.getValue(Admin::class.java)
                if (admin?.email == FirebaseAuth.getInstance().currentUser?.email) {
                    // The current user is an admin
                    callback(true)
                    return
                }
            }
            callback(false)
        }

        override fun onCancelled(databaseError: DatabaseError) {
            callback(false)
            return
        }
    });
    return ""
}

data class Admin(
    val email: String? = null,
    val name: String? = null,
)
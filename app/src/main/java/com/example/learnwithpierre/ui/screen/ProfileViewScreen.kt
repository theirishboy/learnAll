package com.example.learnwithpierre.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.learnwithpierre.LearnAllTopAppBar
import com.example.learnwithpierre.NavigationBottomBar
import com.example.learnwithpierre.R
import com.example.learnwithpierre.model.AuthState
import com.example.learnwithpierre.model.DataProvider
import com.example.learnwithpierre.ui.AppViewModelProvider
import com.example.learnwithpierre.ui.navigation.NavigationDestination
import com.example.learnwithpierre.ui.theme.LearnWithPierreTheme

object ProfileViewDestination : NavigationDestination {
    override val route = "trainScreen"
    override val titleRes: Int = R.string.app_name
    const val deckIdArg = "cardId"
    val routeWithArgs = "$route/{$deckIdArg}"}


@Composable
fun ProfileViewScreen(authViewModel: AuthViewModel = viewModel(),
                      navigateToHomeScreen: () -> Unit,
                      navigateToTraining: () -> Unit,){
    Log.e("","We enter the Screen")

    val authState = DataProvider.authState
    val currentUser = authViewModel.currentUser.collectAsState().value
    val openLoginDialog = remember { mutableStateOf(false) }
    var name : String? = null
    var email : String? = null
    currentUser?.let {
        // Name, email address, and profile photo Url
        name = it.displayName
        email = it.email
        val photoUrl = it.photoUrl
        // Check if user's email is verified
        val emailVerified = it.isEmailVerified
        // The user's ID, unique to the Firebase project. Do NOT use this value to
        // authenticate with your backend server, if you have one. Use
        // FirebaseUser.getIdToken() instead.
        val uid = it.uid
    }

    DataProvider.updateAuthState(currentUser)
    Log.e("","We reach the Screen")

    if (DataProvider.authState != AuthState.SignedOut) {
        Log.e("","We reach the Screen")
        Scaffold(
            topBar = { LearnAllTopAppBar(title = "Profil", canNavigateBack = false) },
            bottomBar = { NavigationBottomBar(navigateToHomeScreen, navigateToTraining, {}, 2) }

        ) {
            ProfileViewScreenBody(it, authState, authViewModel, openLoginDialog = {openLoginDialog.value = true}, name,email)

        }
        if (openLoginDialog.value){
            Dialog(
                onDismissRequest = { openLoginDialog.value = false },
                properties = DialogProperties(
                    usePlatformDefaultWidth = false
                )
            ) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    LoginScreen(authViewModel, openLoginDialog)
                }
            }
        }
    }
    else {
        Log.e("","We reach the LoginScreen")
        LoginScreen(authViewModel)

    }




}

@Composable
private fun ProfileViewScreenBody(
    it: PaddingValues,
    authState: AuthState,
    authViewModel: AuthViewModel = viewModel(factory = AppViewModelProvider.Factory),
    openLoginDialog: () -> Unit,
    name: String?,
    email: String?
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(it)
    ) {
        Spacer(modifier = Modifier.padding(25.dp))
        if (authState != AuthState.SignedIn) {
        Row(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
        ) {
                Text(
                    text = "Please connect to show this page",
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.labelLarge,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center
                )

            }}
        else {
            Row (
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()){
                Text(text = "Name", style = MaterialTheme.typography.labelLarge)
                Text(
                    text = name ?: "Please add a name",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.bodyMedium
                )
                Divider()
            }

            Row (modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()) {
                    Text(text = "Email", style = MaterialTheme.typography.labelLarge)
                    Text(
                        text = email ?: "Please enter email",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
        }

        Row(
            modifier = Modifier
                .fillMaxHeight()
                .padding(10.dp), verticalAlignment = Alignment.Bottom
        ) {
            Button(

                onClick = {
                    if (authState != AuthState.SignedIn) openLoginDialog()
                    else authViewModel.signOut()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.08f),
                shape = RoundedCornerShape(15.dp),

            ) {
                Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
                    Spacer(modifier = Modifier.padding(5.dp))
                    if (authState != AuthState.SignedIn) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_login_24),
                            contentDescription = "Connect",
                            tint = Color.White
                        )
                        Text(modifier = Modifier, text = "Connect", color = Color.White)
                    } else {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = "Disconnect",
                            tint = Color.White
                        )
                        Text(modifier = Modifier, text = "Disconnect", color = Color.White)

                    }
                }
            }
        }




    }
}

@Preview
@Composable
fun ProfilViewScreenPreview(){
    LearnWithPierreTheme {
        ProfileViewScreenBody(
            PaddingValues(5.dp),
            authState = AuthState.SignedOut,
            openLoginDialog = {},
            name = "name",
            email = "email",
        )
    }
}

package com.mobile.karadanatv.utils

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import com.mobile.karadanatv.DestinationScreen
import com.mobile.karadanatv.data.Event
import com.mobile.karadanatv.viewmodels.AuthViewModel
import kotlinx.coroutines.tasks.await

suspend fun resolvePlayableUrl(url: String): String {
    return if (url.startsWith("gs://")) {
        Firebase.storage.getReferenceFromUrl(url).downloadUrl.await().toString()
    } else {
        url
    }
}

fun navigateTo(navController: NavController, route : String){
    navController.navigate(route){
        popUpTo(route)
        launchSingleTop=true
    }

}

fun isShortOrLong(input: String): Boolean {
    return input.length < 6
}


@Composable
fun ObserveErrorMessage(event: Event<String>?) {
    val context = LocalContext.current

    event?.getContentIfNotHandled()?.let { message ->
        LaunchedEffect(message) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }
}

@Composable
fun CheckSignedIn(
    viewModel: AuthViewModel,
    navController: NavController
) {
    val signIn = viewModel.signIn.collectAsState()
    if(signIn.value){
      navigateTo(navController, DestinationScreen.VideoList.route)
    }
}

@Composable
fun CheckSignedOut(
    viewModel: AuthViewModel,
    navController: NavController
) {
    val signIn = viewModel.signIn.collectAsState()
    if(!signIn.value){
        navigateTo(navController, DestinationScreen.Login.route)
    }
}
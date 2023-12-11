package com.example.learnwithpierre.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Divider
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learnwithpierre.LearnAllTopAppBar
import com.example.learnwithpierre.NavigationBottomBar
import com.example.learnwithpierre.R
import com.example.learnwithpierre.ui.navigation.NavigationDestination
import com.example.learnwithpierre.ui.theme.LearnWithPierreTheme
import java.time.format.TextStyle

object ProfilViewDestination : NavigationDestination {
    override val route = "trainScreen"
    override val titleRes: Int = R.string.app_name
    const val deckIdArg = "cardId"
    val routeWithArgs = "$route/{$deckIdArg}"}


@Composable
fun ProfilViewScreen(){
    var name = ""
    var email = ""
    Scaffold(
        topBar = { LearnAllTopAppBar(title = "Profil", canNavigateBack = false) },
        bottomBar = { NavigationBottomBar({},{},{}) }

    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(it)) {
            Spacer(modifier = Modifier.padding(25.dp))

            Row(modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()) {
                Text(text = "Name", style = MaterialTheme.typography.labelLarge, fontSize = 15.sp)
                Text(text = "JeanKevin", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.End, style = MaterialTheme.typography.bodyMedium)
            }
            Divider()

            Row(modifier = Modifier.padding(8.dp)) {
                Text(text = "Email", style = MaterialTheme.typography.labelLarge)
                Text(text = "Email" , modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.End, style = MaterialTheme.typography.bodyMedium)
            }
            Divider()
            Row(modifier = Modifier
                .fillMaxHeight()
                .padding(10.dp), verticalAlignment = Alignment.Bottom) {
                Button(onClick = { /*TODO*/ },
                    shape =  RoundedCornerShape(15.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.08f)) {
                    Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically){
                        Icon(imageVector = Icons.Outlined.Close, contentDescription = "Disconnect", tint = Color.White)
                        Spacer(modifier = Modifier.padding(5.dp))
                        Text(modifier = Modifier, text = "Disconnect", )
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
         ProfilViewScreen()
    }
}
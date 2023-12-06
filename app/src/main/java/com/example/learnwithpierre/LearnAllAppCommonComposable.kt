package com.example.learnwithpierre

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.learnwithpierre.ui.navigation.LearnAllNavHost

@Composable
fun LearnAllApp(navController: NavHostController = rememberNavController()) {
    LearnAllNavHost(navController = navController)
}

/**
 * App bar to display title and conditionally display the back navigation.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckViewTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit = {},
    deleteDeck: () -> Unit
) {
    if (canNavigateBack) {
        CenterAlignedTopAppBar(
            title = { Text(title, style = MaterialTheme.typography.h6) },
            modifier = modifier,
            navigationIcon = {
                IconButton(onClick = navigateBack) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            actions = {
                IconButton(onClick = deleteDeck) {
                    Icon(
                        painterResource(id =R.drawable.baseline_delete_outline_24),
                        contentDescription = "Delete Deck" )
                }
            }
        )
    } else {
        CenterAlignedTopAppBar(title = { Text(title) }, modifier = modifier)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearnAllTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit = {},
) {
    if (canNavigateBack) {
        CenterAlignedTopAppBar(
            title = { Text(title, style = MaterialTheme.typography.h6) },
            modifier = modifier,
            navigationIcon = {
                IconButton(onClick = navigateBack) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
        )
    } else {
        CenterAlignedTopAppBar(title = { Text(title) }, modifier = modifier)
    }
}
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TopAppBarAddCardScreen(
    dismissOnBackPress: () -> Unit,
    onSavePress: () -> Unit,
    title: String
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = androidx.compose.material3.MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(
                title,
                maxLines = 1,
                style = MaterialTheme.typography.h6
            )
        },
        navigationIcon = {
           IconButton(onClick = dismissOnBackPress) {
               Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Localized description"
                )
            }
        },
        actions = {
           IconButton(onClick = onSavePress) {
             Icon(
                    painter = painterResource(id = R.drawable.baseline_save_24),
                    contentDescription = "Localized description"
                )
            }
        },

        )

}
/*@Composable
fun LearnAllBottomAppBar(
    title: String,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit = {}
) {
    val items = listOf("Save","Train","Data")
    val icons = listOf(R.drawable.baseline_download_24,R.drawable.baseline_model_training_24,R.drawable.baseline_dataset_24)
    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { androidx.compose.material3.Icon(painter = painterResource(id = icons[index]), contentDescription ="" ) },
                label = { androidx.compose.material3.Text(item) },
                selected = selectedItem == index,
                onClick = navigateToTraining
            )
        }
    }
}*/

@Composable
fun DialogContent() {
    Box(
        modifier = Modifier
            .size(76.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(4.dp)
            )
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .align(
                    Alignment.Center
                ),
            color = Color.Gray
        )
    }
}
@Composable
fun LoadingDialog(isShowingDialog: Boolean, dismissOnBackPress: Boolean = false, dismissOnClickOutside: Boolean = false) {
    if (isShowingDialog) {
        Dialog(
            onDismissRequest = { },
            DialogProperties(
                dismissOnBackPress = dismissOnBackPress,
                dismissOnClickOutside = dismissOnClickOutside
            )
        ) {
            DialogContent()
        }
    }
}
@Preview
@Composable
private fun TopAppBarPreview(modifier: Modifier = Modifier){
    DeckViewTopAppBar(title = "Bonjour", canNavigateBack = true, deleteDeck = {})
}
@Preview
@Composable
private fun TopAppBarAddCardScreenPreview(modifier: Modifier = Modifier){
    TopAppBarAddCardScreen({},{},title = "Hello")
}
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
fun PreviewSomeDialogContent() {
    Scaffold(
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.LightGray)
                    .padding(20.dp),
                contentAlignment = Alignment.Center,
            ) {
                DialogContent()
            }
        }
    )
}
package com.example.learnwithpierre

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
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
fun LearnAllTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit = {}
) {
    if (canNavigateBack) {
        CenterAlignedTopAppBar(
            title = { Text(title) },
            modifier = modifier,
            navigationIcon = {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        )
    } else {
        CenterAlignedTopAppBar(title = { Text(title) }, modifier = modifier)
    }
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
@Preview
@Composable
private fun TopAppBarPreview(modifier: Modifier = Modifier){
    LearnAllTopAppBar(title = "Bonjour", canNavigateBack = true)
}
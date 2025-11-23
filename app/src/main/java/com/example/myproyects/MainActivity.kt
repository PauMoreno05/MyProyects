package com.example.myproyects

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myproyects.CoffeeShopScreen
import com.example.myproyects.ElSolScreen
import com.example.myproyects.MyPhotosScreen
import com.example.myproyects.ui.*
import com.example.myproyects.ui.theme.MyProyectsTheme

enum class ProyectScreen(val title: String, val icon: ImageVector, val route: String) {
    HOME("Portada", Icons.Default.Person, "route_portada"),
    MY_PHOTOS("MyPhotos", Icons.Default.Lock, "route_my_photos"),
    COFFEE_SHOPS("CoffeeShops", Icons.Default.Favorite, "route_coffee_shops"),
    EL_SOL("ElSol", Icons.Default.Face, "route_el_sol")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyProyectsTheme {
                MyProyectsApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyProyectsApp() {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            if (currentRoute == ProyectScreen.HOME.route) {
                TopAppBar(title = { Text(ProyectScreen.HOME.title) })
            }
        },
        bottomBar = {
            ProyectsBottomBar(
                navController = navController,
                currentRoute = currentRoute
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = ProyectScreen.HOME.route,
            modifier = Modifier.fillMaxSize()
        ) {
            composable(ProyectScreen.HOME.route) { PortadaScreen(innerPadding) }
            composable(ProyectScreen.MY_PHOTOS.route) { MyPhotosScreen(innerPadding) }
            composable(ProyectScreen.COFFEE_SHOPS.route) { CoffeeShopScreen(innerPadding) }
            composable(ProyectScreen.EL_SOL.route) { ElSolScreen(innerPadding) }
        }
    }
}

@Composable
fun ProyectsBottomBar(
    navController: NavController,
    currentRoute: String?
) {
    NavigationBar {
        ProyectScreen.entries.forEach { screen ->
            if (screen != ProyectScreen.HOME) {
                val selected = currentRoute == screen.route
                NavigationBarItem(
                    icon = { Icon(screen.icon, contentDescription = screen.title) },
                    label = { Text(screen.title) },
                    selected = selected,
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun PortadaScreen(paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "MyProjects",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {

}
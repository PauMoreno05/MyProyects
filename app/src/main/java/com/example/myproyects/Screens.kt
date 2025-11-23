package com.example.myproyects


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myproyects.R
import com.example.myproyects.data.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.URLDecoder
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


//MY PHOTOS SCREEN


@Composable
fun MyPhotosScreen(paddingValues: PaddingValues) {
    var fotoSelecionada by remember { mutableStateOf(fotos.first().id) }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)) {

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color.White)
                .padding(2.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(fotos) { photo ->
                Image(
                    painter = painterResource(id = photo.resId),
                    contentDescription = photo.description,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(180.dp)
                        .clickable { fotoSelecionada = photo.id }
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            val fotoPorDefecto = fotos.find { it.id == fotoSelecionada }

            if (fotoPorDefecto != null) {
                Image(
                    painter = painterResource(id = fotoPorDefecto.resId),
                    contentDescription = fotoPorDefecto.description,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize(1f)
                )
            } else {
                Text("Selecciona una foto", color = Color.White, style = MaterialTheme.typography.headlineSmall)
            }
        }
    }
}


//COFFEE SHOP SCREEN

sealed class Pantalla(val ruta: String) {
    object ListaCafeterias : Pantalla("coffeeshops_list")
    object DetalleCafeteria : Pantalla("coffeeshop_detail/{shopTitle}") {
        fun crearRuta(tituloCafeteria: String) = "coffeeshop_detail/${URLEncoder.encode(tituloCafeteria, "UTF-8")}"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoffeeShopScreen(mainPaddingValues: PaddingValues) {
    val controladorNavegacion = rememberNavController()
    val backStackEntry by controladorNavegacion.currentBackStackEntryAsState()
    val rutaActual = backStackEntry?.destination?.route
    val cafeterias = remember { listaCafeterias }

    val mostrarBotonFlotanteDetalle = remember { mutableStateOf(false) }

    val esPantallaDetalle = rutaActual?.startsWith(Pantalla.DetalleCafeteria.ruta.substringBefore("/{")) ?: false

    Scaffold(
        modifier = Modifier.padding(mainPaddingValues),
        topBar = {
            val titleText = "CoffeeShops"

            if (rutaActual == Pantalla.ListaCafeterias.ruta) {
                TopAppBar(
                    title = { Text(titleText, color = MarronOscuro) },
                    navigationIcon = {
                        IconButton(onClick = { /**/ }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menú", tint = MarronOscuro)
                        }
                    },
                    actions = { MenuOpciones(colorContenido = MarronOscuro) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = FondoRosa)
                )
            } else if (esPantallaDetalle) {
                TopAppBar(
                    title = { Text(titleText, color = MarronOscuro) },
                    navigationIcon = {
                        IconButton(onClick = { controladorNavegacion.popBackStack() }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Atrás", tint = MarronOscuro)
                        }
                    },
                    actions = { MenuOpciones(colorContenido = MarronOscuro) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = FondoRosa)
                )
            }
        },
        floatingActionButton = {
            if (esPantallaDetalle) {
                FloatingActionButton(
                    onClick = { },
                    containerColor = FondoRosa,
                    contentColor = MarronOscuro,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Add new comment", fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp))
                }
            }
        },
        floatingActionButtonPosition = if (esPantallaDetalle) FabPosition.Center else FabPosition.End
    ) { valoresRellenoInterno ->
        NavHost(
            navController = controladorNavegacion,
            startDestination = Pantalla.ListaCafeterias.ruta,
            modifier = Modifier.padding(valoresRellenoInterno)
        ) {
            composable(Pantalla.ListaCafeterias.ruta) {
                PantallaListaCafeterias(
                    cafeterias = cafeterias,
                    alNavegarADetalle = { tituloCafeteria ->
                        controladorNavegacion.navigate(Pantalla.DetalleCafeteria.crearRuta(tituloCafeteria))
                    }
                )
            }
            composable(
                route = Pantalla.DetalleCafeteria.ruta,
                arguments = listOf(navArgument("shopTitle") { type = NavType.StringType })
            ) { entradaPila ->
                val tituloCafeteriaCodificado = entradaPila.arguments?.getString("shopTitle")
                val tituloCafeteria = tituloCafeteriaCodificado?.let { URLDecoder.decode(it, "UTF-8") }

                val cafeteria = cafeterias.firstOrNull { it.titulo == tituloCafeteria }

                if (cafeteria != null) {
                    PantallaDetalleCafeteria(
                        cafeteria = cafeteria,
                        onClickAtras = { controladorNavegacion.popBackStack() },
                        setMostrarFab = { mostrarBotonFlotanteDetalle.value = it }
                    )
                } else {
                    Text("Error: Cafetería no encontrada.", modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}

@Composable
fun PantallaListaCafeterias(
    cafeterias: List<Cafeteria>,
    alNavegarADetalle: (String) -> Unit
) {
    val estadoDesplazamiento = rememberLazyListState()

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        state = estadoDesplazamiento,
        modifier = Modifier.fillMaxSize()
    ) {
        items(cafeterias) { cafeteria ->
            TarjetaCafeteriaUI(
                cafeteria = cafeteria,
                onClick = { alNavegarADetalle(cafeteria.titulo) }
            )
        }
    }
}

@Composable
fun TarjetaCafeteriaUI(cafeteria: Cafeteria, onClick: () -> Unit) {
    var valoracionActual by remember { mutableStateOf(cafeteria.valoracionFija) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            Image(
                painter = painterResource(id = cafeteria.imagenRecurso),
                contentDescription = cafeteria.titulo,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(FondoRosa)
                    .padding(horizontal = 16.dp, vertical = 20.dp)
            ) {
                Text(
                    text = cafeteria.titulo,
                    fontSize = 32.sp,
                    fontFamily = FuenteCursiva,
                    color = MarronOscuro,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))

                BarraEstrellas(
                    valoracion = valoracionActual,
                    alCambiarValoracion = { nuevaValoracion -> valoracionActual = nuevaValoracion }
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = cafeteria.subtitulo,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MarronOscuro
                )
            }

            Divider(color = ColorDivisor, thickness = 1.dp)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(FondoRosa)
                    .height(48.dp)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                TextButton(
                    onClick = { },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = RojoReservar
                    ),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(text = "RESERVE", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun BarraEstrellas(valoracion: Float, alCambiarValoracion: (Float) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        repeat(5) { indice ->
            val valorEstrella = indice + 1
            val estaSeleccionada = valorEstrella <= valoracion.toInt()

            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "Estrella $valorEstrella",
                tint = if (estaSeleccionada) AmarilloEstrella else Color.Gray,
                modifier = Modifier
                    .size(36.dp)
                    .clickable { alCambiarValoracion(valorEstrella.toFloat()) }
                    .padding(4.dp)
            )
        }
    }
}

@Composable
fun MenuOpciones(colorContenido: Color) {
    var expandido by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.wrapContentSize(Alignment.TopEnd)
    ) {
        IconButton(onClick = { expandido = true }) {
            Icon(
                Icons.Filled.MoreVert,
                contentDescription = "Más opciones",
                tint = colorContenido
            )
        }
        DropdownMenu(
            expanded = expandido,
            onDismissRequest = { expandido = false }
        ) {
            DropdownMenuItem(
                text = { Text("Compartir", color = MarronOscuro) },
                onClick = { expandido = false },
                leadingIcon = {
                    Icon(Icons.Filled.Share, contentDescription = "Compartir icono", tint = MarronOscuro)
                }
            )
            DropdownMenuItem(
                text = { Text("Album", color = MarronOscuro) },
                onClick = { expandido = false },
                leadingIcon = {
                    Icon(Icons.Filled.Lock, contentDescription = "Album icono", tint = MarronOscuro)
                }
            )
        }
    }
}

@Composable
fun PantallaDetalleCafeteria(
    cafeteria: Cafeteria,
    onClickAtras: () -> Unit,
    setMostrarFab: (Boolean) -> Unit
) {
    val estadoDesplazamiento = rememberLazyStaggeredGridState()

    val mostrarBotonFlotante by remember {
        derivedStateOf { estadoDesplazamiento.firstVisibleItemIndex == 0 }
    }

    LaunchedEffect(mostrarBotonFlotante) {
        setMostrarFab(mostrarBotonFlotante)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        EncabezadoDetalle(cafeteria = cafeteria)

        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            verticalItemSpacing = 8.dp,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            state = estadoDesplazamiento,
            modifier = Modifier.fillMaxSize()
        ) {
            items(todosComentarios) { comentario ->
                TarjetaComentario(comentario)
            }
        }
    }
}

@Composable
fun EncabezadoDetalle(cafeteria: Cafeteria) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = cafeteria.titulo,
            fontSize = 28.sp,
            fontFamily = FuenteCursiva,
            color = MarronOscuro,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}
@Composable
fun TarjetaComentario(comentario: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = FondoRosa),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.padding(8.dp)) {
            Text(
                text = comentario,
                style = MaterialTheme.typography.bodySmall,
                color = MarronOscuro,
            )
        }
    }
}



//EL SOL SCREEN


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailedDrawerContent(onNavigate: (Screen) -> Unit) {
    ModalDrawerSheet(modifier = Modifier.width(300.dp)) {
        Column {
            Image(
                painter = painterResource(R.drawable.solsolet),
                contentDescription = "Imagen del menu",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(165.dp)
            )
            Divider()
            NavigationDrawerItem(
                label = { Text("Home") },
                selected = false,
                icon = { Icon(Icons.Outlined.Build, contentDescription = null) },
                onClick = { onNavigate(Screen.Home) }
            )
            NavigationDrawerItem(
                label = { Text("Info & Download") },
                selected = false,
                icon = { Icon(Icons.Outlined.Info, contentDescription = null) },
                onClick = { onNavigate(Screen.Info) }
            )
            NavigationDrawerItem(
                label = { Text("Email") },
                selected = false,
                icon = { Icon(Icons.Outlined.Email, contentDescription = null) },
                onClick = { }
            )
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
fun BottomAppBarWithDrawerControl(onOpenDrawer: () -> Unit, onAddItem: () -> Unit) {
    var itemCount by remember { mutableStateOf(0) }

    BottomAppBar(
        actions = {
            IconButton(onClick = onOpenDrawer) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Abrir Menú Lateral")
            }
            IconButton(onClick = { itemCount++ }) {
                BadgedBox(
                    badge = {
                        if (itemCount > 0) {
                            Badge(containerColor = Color.Gray, contentColor = Color.White) {
                                Text("$itemCount")
                            }
                        }
                    }
                ) {
                    Icon(Icons.Filled.Favorite, contentDescription = "Favoritos")
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddItem,
                containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
            ) {
                Icon(Icons.Filled.Add, "Añadir elemento")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SolarImageCard(
    image: SolImage,
    onCopy: (SolImage) -> Unit,
    onDelete: (SolImage) -> Unit,
    onCardClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = { onCardClick(image.name) }
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(image.resId),
                contentDescription = image.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = image.name, modifier = Modifier.weight(1f))

                Box {
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menú de acciones")
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        DropdownMenuItem(
                            text = { Text("Copiar") },
                            onClick = { onCopy(image); expanded = false }
                        )
                        DropdownMenuItem(
                            text = { Text("Eliminar") },
                            onClick = { onDelete(image); expanded = false }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoScreen(paddingValues: PaddingValues, snackbarHostState: SnackbarHostState) {
    var isDownloading by remember { mutableStateOf(false) }
    var downloadProgress by remember { mutableStateOf(0.0f) }
    var showIndicator by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(isDownloading) {
        if (isDownloading) {
            showIndicator = true
            downloadProgress = 0.0f
            for (i in 1..20) {
                delay(100)
                downloadProgress = i / 20f
            }
            delay(500)
            showIndicator = false
            isDownloading = false
            scope.launch {
                snackbarHostState.showSnackbar(message = "Descarga completada!", withDismissAction = true)
            }
        }
    }

    if (showDatePicker) {
        val dateState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                Button(onClick = {
                    showDatePicker = false
                    val selectedDateMillis = dateState.selectedDateMillis
                    if (selectedDateMillis != null) {
                        val selectedDate = Date(selectedDateMillis)
                        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        scope.launch {
                            snackbarHostState.showSnackbar(message = "Fecha seleccionada: ${formatter.format(selectedDate)}", withDismissAction = true)
                        }
                    }
                }) { Text("OK") }
            },
            dismissButton = { Button(onClick = { showDatePicker = false }) { Text("Cancel") } }
        ) {
            DatePicker(state = dateState)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(top = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Button(
            onClick = { if (!isDownloading) isDownloading = true },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A1B9A), contentColor = Color.White),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.width(200.dp).padding(bottom = 24.dp)
        ) {
            Text(text = "Download more info")
        }

        if (showIndicator) {
            CircularProgressIndicator(
                progress = { downloadProgress },
                modifier = Modifier.width(64.dp),
                color = Color(0xFF6A1B9A),
                strokeWidth = 6.dp
            )
        }

        Spacer(Modifier.height(if (showIndicator) 32.dp else 96.dp))

        Button(
            onClick = { showDatePicker = true },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A1B9A)),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.width(250.dp)
        ) {
            Text("Visit Planetarium. Select date", color = Color.White)
        }
    }
}

enum class Screen { Home, Info }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ElSolScreen(mainPaddingValues: PaddingValues) {
    val imagesState = remember { mutableStateListOf(*initialSolImages.toTypedArray()) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var currentScreen by remember { mutableStateOf(Screen.Home) }
    val snackbarHostState = remember { SnackbarHostState() }

    val navigateTo: (Screen) -> Unit = { screen ->
        currentScreen = screen
        scope.launch { drawerState.close() }
    }

    val onCopyImage: (SolImage) -> Unit = { imageToCopy ->
        val newId = currentId++
        val newImage = imageToCopy.copy(
            id = newId,
            name = imageToCopy.name,
            resId = imageToCopy.resId
        )

        imagesState.add(newImage)
        scope.launch {
            snackbarHostState.showSnackbar(message = "Copia de ${imageToCopy.name} añadida.", withDismissAction = true)
        }
    }


    val onDeleteImage: (SolImage) -> Unit = { imageToDelete ->
        imagesState.remove(imageToDelete)
        scope.launch { snackbarHostState.showSnackbar(message = "${imageToDelete.name} eliminada.", withDismissAction = true) }
    }

    val onImageCardClick: (String) -> Unit = { imageName ->
        scope.launch {
            snackbarHostState.showSnackbar(message = "Has pulsado: $imageName", withDismissAction = false)
        }
    }

    ModalNavigationDrawer(
        drawerContent = { DetailedDrawerContent(onNavigate = navigateTo) },
        drawerState = drawerState
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            bottomBar = {
                BottomAppBarWithDrawerControl(
                    onOpenDrawer = { scope.launch { drawerState.open() } },
                    onAddItem = {}
                )
            },
            modifier = Modifier.padding(mainPaddingValues)
        ) { innerPadding ->
            when (currentScreen) {
                Screen.Home -> {
                    LazyVerticalGrid(
                        contentPadding = innerPadding,
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(imagesState.toList(), key = { it.id }) { image ->
                            SolarImageCard(
                                image = image,
                                onCopy = onCopyImage,
                                onDelete = onDeleteImage,
                                onCardClick = onImageCardClick
                            )
                        }
                    }
                }
                Screen.Info -> {
                    InfoScreen(paddingValues = innerPadding, snackbarHostState = snackbarHostState)
                }
            }
        }
    }
}
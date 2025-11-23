package com.example.myproyects.data

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.example.myproyects.R
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.Font



//DATOS EL SOL


data class SolImage(val id: Int, val name: String, @DrawableRes val resId: Int)

val initialSolImages = listOf(
    SolImage(1, "Corona solar", R.drawable.corona_solar),
    SolImage(2, "Erupción solar", R.drawable.erupcionsolar),
    SolImage(3, "Espículas", R.drawable.espiculas),
    SolImage(4, "Filamentos", R.drawable.filamentos),
    SolImage(5, "Magneto Esfera", R.drawable.magnetosfera),
    SolImage(6, "Mancha Solar", R.drawable.manchasolar)
)

var currentId = initialSolImages.size + 1


//DATOS MY PHOTOS


data class Photo(
    val id: Int,
    @DrawableRes val resId: Int,
    val description: String = "Imagen de la galería"
)

val fotos = listOf(
    Photo(1, R.drawable.image1, "foto1"),
    Photo(2, R.drawable.image2, "foto2"),
    Photo(3, R.drawable.image3, "foto3"),
    Photo(4, R.drawable.image4, "foto4"),
    Photo(5, R.drawable.image5, "foto5"),
    Photo(6, R.drawable.image6, "foto5"),
    Photo(7, R.drawable.image7, "foto6"),
    Photo(8, R.drawable.image8, "foto7")
)


//
// DATOS COFFEE SHOP
//

@Immutable
data class Cafeteria(
    val titulo: String,
    val subtitulo: String,
    val imagenRecurso: Int,
    val valoracionFija: Float
)
val MarronOscuro = Color(0xFF6D4C41)
val FondoRosa = Color(0xFFFBE5E5)
val ColorDivisor = Color(0xFFE0E0E0)
val AmarilloEstrella = Color(0xFFFFC107)
val RojoReservar = Color(0xFFD32F2F)
val ColorTarjetaResena = Color(0xFFFFF8E1)

val FuenteCursiva = FontFamily(
    Font(R.font.aliviaregular)
)
val listaCafeterias = listOf(
    Cafeteria("Antico Caffè Greco", "St. Italy, Rome", R.drawable.images, 0.0f),
    Cafeteria("Coffee Room", "St. Germany, Berlin", R.drawable.images1, 0.0f),
    Cafeteria("Coffee Ibiza", "St. Colón, Madrid", R.drawable.images2, 0.0f),
    Cafeteria("Pudding Coffee Shop", "St. Diagonal, Barcelona", R.drawable.images3, 0.0f),
    Cafeteria("L'Express", "St. Picadilly Circus, London", R.drawable.images4, 0.0f),
    Cafeteria("Coffee Corner", "St. Ángel Guimerá, Valencia", R.drawable.images5, 0.0f),
    Cafeteria("Sweet Cup", "St.Kinkerstraat, Amsterdam", R.drawable.images6, 0.0f)
)
val todosComentarios = listOf(
    "Muy bueno",
    "Buen ambiente y buen servicio. Lo recomiendo.",
    "Repetiremos. Gran selección de tartas y cafés.",
    "Puntos negativos: el servicio es muy lento y los precios son un poco elevados.",
    "Céntrica y acogedora. Volveremos seguro",
    "La comida estaba deliciosa y bastante bien de precio, mucha variedad de platos.\nEl personal muy amable, nos permitieron ver todo el establecimiento.",
    "Excelente. Destacable la extensa carta de cafés",
    "En días festivos demasiado tiempo de espera. Los camareros/as no dan abasto. No lo recomiendo. No volveré",
    "Todo lo que he probado en la cafetería está riquísimo, dulce o salado.\nLa vajilla muy bonita todo de diseño que en el entorno del bar queda ideal.",
    "La ambientacion muy buena, pero en la planta de arriba un poco escasa.",
    "Muy bueno",
    "Excelente. Destacable la extensa carta de cafés",
    "En días festivos demasiado tiempo de espera. Los camareros/as no dan abasto. No lo recomiendo. No volveré",
    "Buen ambiente y buen servicio. Lo recomiendo."
)
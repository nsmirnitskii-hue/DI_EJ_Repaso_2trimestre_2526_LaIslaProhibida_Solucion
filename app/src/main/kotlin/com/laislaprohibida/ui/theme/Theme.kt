package com.laislaprohibida.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext



import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.sp

// ---------- COLORES TEMA CLARO ----------

val Blue500 = Color(0xFF2196F3)
val Green500 = Color(0xFF4CAF50)
val Yellow500 = Color(0xFFFFC107)
val Red500 = Color(0xFFF44336)
val LightGray = Color(0xFFF5F5F5)
val DarkGray = Color(0xFF212121)

// Tema claro
private val LightColorScheme = lightColorScheme(
    primary = Green500,
    onPrimary = Color.White,
    secondary = Blue500,
    onSecondary = Color.White,
    error = Red500,
    background = LightGray,
    surface = Color.White,
    onBackground = DarkGray,
    onSurface = DarkGray
)

// ---------- TIPOGRAFÍAS ----------

val GameTypography = Typography(
    titleLarge = androidx.compose.ui.text.TextStyle(fontSize = 24.sp, color = DarkGray),
    titleMedium = androidx.compose.ui.text.TextStyle(fontSize = 20.sp, color = DarkGray),
    bodyMedium = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, color = DarkGray)
)

// ---------- FORMAS ----------

val GameShapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(8.dp),
    large = RoundedCornerShape(16.dp)
)

// ---------- THEME COMPOSABLE ----------

@Composable
fun GameTheme(
    darkTheme: Boolean = false, // Por defecto claro
    content: @Composable () -> Unit
) {
    val colors = LightColorScheme // En examen solo claro, ellos implementan el oscuro

    MaterialTheme(
        colorScheme = colors,
        typography = GameTypography,
        shapes = GameShapes,
        content = content
    )
}

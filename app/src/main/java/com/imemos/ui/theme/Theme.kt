package com.imemos.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// 默认浅色主题颜色
private val LightColors = lightColorScheme(
    primary = Color(0xFF006A6A),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF6FF7F6),
    onPrimaryContainer = Color(0xFF002020),
    secondary = Color(0xFF4A6363),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFCCE8E7),
    onSecondaryContainer = Color(0xFF051F1F),
    tertiary = Color(0xFF4B607C),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFD3E4FF),
    onTertiaryContainer = Color(0xFF041C35),
    error = Color(0xFFBA1A1A),
    errorContainer = Color(0xFFFFDAD6),
    onError = Color.White,
    onErrorContainer = Color(0xFF410002),
    background = Color(0xFFFAFDFC),
    onBackground = Color(0xFF191C1C),
    surface = Color(0xFFFAFDFC),
    onSurface = Color(0xFF191C1C),
    surfaceVariant = Color(0xFFDAE5E4),
    onSurfaceVariant = Color(0xFF3F4948),
    outline = Color(0xFF6F7979),
    inverseOnSurface = Color(0xFFEFF1F0),
    inverseSurface = Color(0xFF2D3131),
    inversePrimary = Color(0xFF4CDADA),
    surfaceTint = Color(0xFF006A6A),
    outlineVariant = Color(0xFFBEC9C8),
    scrim = Color(0xFF000000)
)

// 默认深色主题颜色
private val DarkColors = darkColorScheme(
    primary = Color(0xFF4CDADA),
    onPrimary = Color(0xFF003737),
    primaryContainer = Color(0xFF004F4F),
    onPrimaryContainer = Color(0xFF6FF7F6),
    secondary = Color(0xFFB0CCCB),
    onSecondary = Color(0xFF1B3534),
    secondaryContainer = Color(0xFF324B4B),
    onSecondaryContainer = Color(0xFFCCE8E7),
    tertiary = Color(0xFFB4C8E9),
    onTertiary = Color(0xFF1C314B),
    tertiaryContainer = Color(0xFF334863),
    onTertiaryContainer = Color(0xFFD3E4FF),
    error = Color(0xFFFFB4AB),
    errorContainer = Color(0xFF93000A),
    onError = Color(0xFF690005),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Color(0xFF191C1C),
    onBackground = Color(0xFFE0E3E2),
    surface = Color(0xFF191C1C),
    onSurface = Color(0xFFE0E3E2),
    surfaceVariant = Color(0xFF3F4948),
    onSurfaceVariant = Color(0xFFBEC9C8),
    outline = Color(0xFF899392),
    inverseOnSurface = Color(0xFF191C1C),
    inverseSurface = Color(0xFFE0E3E2),
    inversePrimary = Color(0xFF006A6A),
    surfaceTint = Color(0xFF4CDADA),
    outlineVariant = Color(0xFF3F4948),
    scrim = Color(0xFF000000)
)

// 自定义主题数据类
data class CustomTheme(
    val id: Int,
    val name: String,
    val isDark: Boolean,
    val colorScheme: ColorScheme
)

// 预定义的主题列表
val predefinedThemes = listOf(
    CustomTheme(
        id = 0,
        name = "默认浅色",
        isDark = false,
        colorScheme = LightColors
    ),
    CustomTheme(
        id = 1,
        name = "默认深色",
        isDark = true,
        colorScheme = DarkColors
    ),
    CustomTheme(
        id = 2,
        name = "蓝色海洋",
        isDark = false,
        colorScheme = lightColorScheme(
            primary = Color(0xFF0066CC),
            onPrimary = Color.White,
            primaryContainer = Color(0xFFD1E4FF),
            onPrimaryContainer = Color(0xFF001D36),
            secondary = Color(0xFF535F70),
            onSecondary = Color.White,
            secondaryContainer = Color(0xFFD7E3F7),
            onSecondaryContainer = Color(0xFF101C2B),
            tertiary = Color(0xFF6B5778),
            onTertiary = Color.White,
            tertiaryContainer = Color(0xFFF2DAFF),
            onTertiaryContainer = Color(0xFF251431),
            background = Color(0xFFF8FBFF),
            surface = Color(0xFFF8FBFF),
            onBackground = Color(0xFF1A1C1E),
            onSurface = Color(0xFF1A1C1E)
        )
    ),
    CustomTheme(
        id = 3,
        name = "蓝色海洋深色",
        isDark = true,
        colorScheme = darkColorScheme(
            primary = Color(0xFF9ECAFF),
            onPrimary = Color(0xFF00325A),
            primaryContainer = Color(0xFF004881),
            onPrimaryContainer = Color(0xFFD1E4FF),
            secondary = Color(0xFFBBC7DB),
            onSecondary = Color(0xFF253140),
            secondaryContainer = Color(0xFF3B4858),
            onSecondaryContainer = Color(0xFFD7E3F7),
            tertiary = Color(0xFFD6BEE4),
            onTertiary = Color(0xFF3B2948),
            tertiaryContainer = Color(0xFF523F5F),
            onTertiaryContainer = Color(0xFFF2DAFF),
            background = Color(0xFF1A1C1E),
            surface = Color(0xFF1A1C1E),
            onBackground = Color(0xFFE2E2E6),
            onSurface = Color(0xFFE2E2E6)
        )
    ),
    CustomTheme(
        id = 4,
        name = "紫色梦幻",
        isDark = false,
        colorScheme = lightColorScheme(
            primary = Color(0xFF9C27B0),
            onPrimary = Color.White,
            primaryContainer = Color(0xFFF3E5F5),
            onPrimaryContainer = Color(0xFF3F1048),
            secondary = Color(0xFF7B1FA2),
            onSecondary = Color.White,
            secondaryContainer = Color(0xFFE1BEE7),
            onSecondaryContainer = Color(0xFF4A148C),
            tertiary = Color(0xFF6A1B9A),
            onTertiary = Color.White,
            tertiaryContainer = Color(0xFFEA80FC),
            onTertiaryContainer = Color(0xFF38006B),
            background = Color(0xFFFCF6FF),
            surface = Color(0xFFFCF6FF),
            onBackground = Color(0xFF1D1B1E),
            onSurface = Color(0xFF1D1B1E)
        )
    ),
    CustomTheme(
        id = 5,
        name = "紫色梦幻深色",
        isDark = true,
        colorScheme = darkColorScheme(
            primary = Color(0xFFD05CE3),
            onPrimary = Color(0xFF650070),
            primaryContainer = Color(0xFF8B2BA8),
            onPrimaryContainer = Color(0xFFF9D8FF),
            secondary = Color(0xFFCF93D9),
            onSecondary = Color(0xFF492456),
            secondaryContainer = Color(0xFF613B6E),
            onSecondaryContainer = Color(0xFFEBD6F0),
            tertiary = Color(0xFFBB86FC),
            onTertiary = Color(0xFF38006B),
            tertiaryContainer = Color(0xFF5C1E99),
            onTertiaryContainer = Color(0xFFF6DEFF),
            background = Color(0xFF1D1B1E),
            surface = Color(0xFF1D1B1E),
            onBackground = Color(0xFFE6E1E6),
            onSurface = Color(0xFFE6E1E6)
        )
    ),
    CustomTheme(
        id = 6,
        name = "绿色自然",
        isDark = false,
        colorScheme = lightColorScheme(
            primary = Color(0xFF4CAF50),
            onPrimary = Color.White,
            primaryContainer = Color(0xFFE8F5E9),
            onPrimaryContainer = Color(0xFF1B5E20),
            secondary = Color(0xFF388E3C),
            onSecondary = Color.White,
            secondaryContainer = Color(0xFFC8E6C9),
            onSecondaryContainer = Color(0xFF1B5E20),
            tertiary = Color(0xFF2E7D32),
            onTertiary = Color.White,
            tertiaryContainer = Color(0xFFAED581),
            onTertiaryContainer = Color(0xFF1B5E20),
            background = Color(0xFFF1F8F2),
            surface = Color(0xFFF1F8F2),
            onBackground = Color(0xFF1A1C19),
            onSurface = Color(0xFF1A1C19)
        )
    ),
    CustomTheme(
        id = 7,
        name = "绿色自然深色",
        isDark = true,
        colorScheme = darkColorScheme(
            primary = Color(0xFF81C784),
            onPrimary = Color(0xFF005100),
            primaryContainer = Color(0xFF2E7D32),
            onPrimaryContainer = Color(0xFFB9F6CA),
            secondary = Color(0xFFA5D6A7),
            onSecondary = Color(0xFF00390D),
            secondaryContainer = Color(0xFF388E3C),
            onSecondaryContainer = Color(0xFFCCFFCE),
            tertiary = Color(0xFF8BC34A),
            onTertiary = Color(0xFF0F2000),
            tertiaryContainer = Color(0xFF689F38),
            onTertiaryContainer = Color(0xFFDCEFB8),
            background = Color(0xFF1A1C19),
            surface = Color(0xFF1A1C19),
            onBackground = Color(0xFFE1E3DE),
            onSurface = Color(0xFFE1E3DE)
        )
    ),
    CustomTheme(
        id = 8,
        name = "橙色活力",
        isDark = false,
        colorScheme = lightColorScheme(
            primary = Color(0xFFFF9800),
            onPrimary = Color.White,
            primaryContainer = Color(0xFFFFE0B2),
            onPrimaryContainer = Color(0xFFE65100),
            secondary = Color(0xFFFF5722),
            onSecondary = Color.White,
            secondaryContainer = Color(0xFFFFCCBC),
            onSecondaryContainer = Color(0xFFBF360C),
            tertiary = Color(0xFFFF7043),
            onTertiary = Color.White,
            tertiaryContainer = Color(0xFFFFAB91),
            onTertiaryContainer = Color(0xFFBF360C),
            background = Color(0xFFFFF8F6),
            surface = Color(0xFFFFF8F6),
            onBackground = Color(0xFF1F1B16),
            onSurface = Color(0xFF1F1B16)
        )
    ),
    CustomTheme(
        id = 9,
        name = "橙色活力深色",
        isDark = true,
        colorScheme = darkColorScheme(
            primary = Color(0xFFFFB74D),
            onPrimary = Color(0xFF452B00),
            primaryContainer = Color(0xFFFF9800),
            onPrimaryContainer = Color(0xFFFFECCE),
            secondary = Color(0xFFFF8A65),
            onSecondary = Color(0xFF481800),
            secondaryContainer = Color(0xFFFF5722),
            onSecondaryContainer = Color(0xFFFFDBD0),
            tertiary = Color(0xFFFFAB91),
            onTertiary = Color(0xFF5C1900),
            tertiaryContainer = Color(0xFFFF7043),
            onTertiaryContainer = Color(0xFFFFDBD0),
            background = Color(0xFF1F1B16),
            surface = Color(0xFF1F1B16),
            onBackground = Color(0xFFEAE1D9),
            onSurface = Color(0xFFEAE1D9)
        )
    )
)

// 当前选中的主题ID
private var currentThemeId = mutableStateOf(0)

// 获取当前主题
fun getCurrentTheme(): CustomTheme {
    return predefinedThemes.find { it.id == currentThemeId.value } ?: predefinedThemes[0]
}

// 设置当前主题
fun setCurrentTheme(themeId: Int) {
    currentThemeId.value = themeId
}

// 主题应用Composable函数
@Composable
fun iMemosTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // 动态颜色在Android 12+上可用
    dynamicColor: Boolean = false,
    customThemeId: Int = currentThemeId.value,
    content: @Composable () -> Unit
) {
    // 获取自定义主题
    val customTheme = predefinedThemes.find { it.id == customThemeId } ?: predefinedThemes[0]
    
    // 根据自定义主题设置是否使用深色主题
    val isDarkTheme = if (customThemeId > 0) customTheme.isDark else darkTheme
    
    // 获取颜色方案
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (isDarkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        else -> customTheme.colorScheme
    }
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isDarkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

// Typography定义
val Typography = Typography(
    bodyLarge = androidx.compose.ui.text.TextStyle(
        fontFamily = androidx.compose.ui.text.font.FontFamily.Default,
        fontWeight = androidx.compose.ui.text.font.FontWeight.Normal,
        fontSize = androidx.compose.ui.unit.sp.Sp(16)
    )
    // 可以根据需要添加更多文字样式
)
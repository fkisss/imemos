package com.imemos.ui.navigation

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.imemos.data.repository.UserRepository
import com.imemos.ui.auth.LoginScreen
import com.imemos.ui.memo.MemoDetailScreen
import com.imemos.ui.memo.MemoEditScreen
import com.imemos.ui.memo.MemoListScreen
import com.imemos.ui.settings.SettingsScreen
import com.imemos.ui.theme.getCurrentTheme
import com.imemos.ui.theme.setCurrentTheme

// 导航路由
sealed class Screen(val route: String) {
    object Login : Screen("login")
    object MemoList : Screen("memos")
    object MemoDetail : Screen("memos/{memoId}") {
        fun createRoute(memoId: Int) = "memos/$memoId"
    }
    object MemoCreate : Screen("memos/create")
    object MemoEdit : Screen("memos/{memoId}/edit") {
        fun createRoute(memoId: Int) = "memos/$memoId/edit"
    }
    object Settings : Screen("settings")
}

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    userRepository: UserRepository,
    startDestination: String = if (userRepository.isLoggedIn()) Screen.MemoList.route else Screen.Login.route
) {
    var currentThemeId by remember { mutableStateOf(getCurrentTheme().id) }
    
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // 登录页面
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.MemoList.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        
        // 笔记列表页面
        composable(Screen.MemoList.route) {
            MemoListScreen(
                onMemoClick = { memoId ->
                    navController.navigate(Screen.MemoDetail.createRoute(memoId))
                },
                onCreateMemoClick = {
                    navController.navigate(Screen.MemoCreate.route)
                },
                onSettingsClick = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }
        
        // 笔记详情页面
        composable(
            route = Screen.MemoDetail.route,
            arguments = listOf(navArgument("memoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val memoId = backStackEntry.arguments?.getInt("memoId") ?: return@composable
            MemoDetailScreen(
                memoId = memoId,
                onBackClick = {
                    navController.popBackStack()
                },
                onEditClick = { id ->
                    navController.navigate(Screen.MemoEdit.createRoute(id))
                }
            )
        }
        
        // 创建笔记页面
        composable(Screen.MemoCreate.route) {
            MemoEditScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        
        // 编辑笔记页面
        composable(
            route = Screen.MemoEdit.route,
            arguments = listOf(navArgument("memoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val memoId = backStackEntry.arguments?.getInt("memoId") ?: return@composable
            MemoEditScreen(
                memoId = memoId,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        
        // 设置页面
        composable(Screen.Settings.route) {
            SettingsScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onThemeChanged = { themeId ->
                    currentThemeId = themeId
                    setCurrentTheme(themeId)
                },
                currentThemeId = currentThemeId
            )
        }
    }
}
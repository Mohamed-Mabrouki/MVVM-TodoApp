package com.example.mvvmtodoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mvvmtodoapp.ui.add_edit_todo.AddEditTodoScreen
import com.example.mvvmtodoapp.ui.theme.MVVMTodoAppTheme
import com.example.mvvmtodoapp.ui.todo_list.TodoListScreen
import com.example.mvvmtodoapp.util.Routes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState : Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			MVVMTodoAppTheme { // A surface container using the 'background' color from the theme
				Surface(
					modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
				) {
					val navController = rememberNavController()
					NavHost(navController = navController, startDestination = Routes.TODO_LIST) {
						composable(route = Routes.TODO_LIST) {
							TodoListScreen(onNavigate = {
								navController.navigate(it.route)
							})
						}
						composable(route = Routes.ADD_EDIT_TODO + "?todoId={todoId}",
							arguments = listOf(navArgument(name = "todoId") {
								type = NavType.IntType
								defaultValue = -1
							})) {
							AddEditTodoScreen(onPopBackStack = {
								navController.popBackStack()
							})
						}
					}
				}
			}
		}
	}
}

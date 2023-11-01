package com.example.mvvmtodoapp.ui.todo_list

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarResult
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mvvmtodoapp.util.UiEvent

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TodoListScreen(
	onNavigate : (UiEvent.Navigate) -> Unit,
	viewModel : TodoListViewModel = hiltViewModel(),
) {
	val todos = viewModel.todos.collectAsState(initial = emptyList())
	val scaffoldState = rememberScaffoldState()
	LaunchedEffect(key1 = true) {
		viewModel.uiEvent.collect { event ->
			when (event) {
				is UiEvent.Navigate     -> onNavigate(event)
				is UiEvent.ShowSnackBar -> {
					val result = scaffoldState.snackbarHostState.showSnackbar(
						message = event.message, actionLabel = event.action
					)
					if (result == SnackbarResult.ActionPerformed) {
						viewModel.onEvent(TodoListEvent.OnUndoDeleteClick)
					}
				}

				else                    -> Unit
			}
		}
	}
	Scaffold(scaffoldState = scaffoldState, floatingActionButton = {
		FloatingActionButton(onClick = {
			viewModel.onEvent(TodoListEvent.OnAddTodoClick)
		}) {
			Icon(
				imageVector = Icons.Default.Add, contentDescription = "Add"
			)
		}
	}) {
		LazyColumn(modifier = Modifier.fillMaxSize()) {
			items(todos.value) { todo ->
				TodoItem(
					todo = todo,
					onEvent = viewModel::onEvent,
					modifier = Modifier
						.fillMaxWidth()
						.clickable {
							viewModel.onEvent(TodoListEvent.OnTodoClick(todo))
						}
						.padding(16.dp)
					)
			}
		}
	}

}
package com.example.mvvmtodoapp.ui.add_edit_todo

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mvvmtodoapp.util.UiEvent

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AddEditTodoScreen(
	onPopBackStack : () -> Unit, viewModel : AddEditTodoViewModel = hiltViewModel()
) {
	val scaffoltState = rememberScaffoldState()
	LaunchedEffect(key1 = true) {
		viewModel.uiEvent.collect { event ->
			when (event) {
				is UiEvent.PopBackStack -> onPopBackStack()
				is UiEvent.ShowSnackBar -> {
					scaffoltState.snackbarHostState.showSnackbar(
						message = event.message, actionLabel = event.action
					)
				}

				else                    -> Unit
			}
		}
	}
	Scaffold(scaffoldState = scaffoltState, modifier = Modifier
		.fillMaxSize()
		.padding(
			16.dp
		), floatingActionButton = {
		FloatingActionButton(onClick = {
			viewModel.onEvent(
				AddEditTodoEvent.OnSaveTodoClick
			)
		}) {
			Icon(imageVector = Icons.Default.Check, contentDescription = "Save")
		}
	}) {
		Column(modifier = Modifier.fillMaxSize()) {
			TextField(value = viewModel.title, onValueChange = {
				viewModel.onEvent(AddEditTodoEvent.OnTitleChange(it))
			},
				placeholder = {
					Text(text = "Title" )
				}, modifier = Modifier.fillMaxWidth()
			)
			Spacer(modifier = Modifier.height(8.dp))
			TextField(value = viewModel.description, onValueChange = {
				viewModel.onEvent(AddEditTodoEvent.OnDescriptionChange(it))
			},
				placeholder = {
					Text(text = "Description" )
				}, modifier = Modifier.fillMaxWidth(),
				singleLine = false,
				maxLines = 5
			)
		}
	}

}
package com.example.mvvmtodoapp.ui.add_edit_todo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmtodoapp.data.Todo
import com.example.mvvmtodoapp.data.TodoRepository
import com.example.mvvmtodoapp.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditTodoViewModel @Inject constructor(
	private val repository : TodoRepository, savedStateHandle : SavedStateHandle
) : ViewModel() {
	var todo by mutableStateOf<Todo?>(null)
		private set
	var title by mutableStateOf("")
		private set
	var description by mutableStateOf("")
		private set
	private val _uiEvent = Channel<UiEvent>()
	val uiEvent = _uiEvent.receiveAsFlow()

	init {
		val todoId = savedStateHandle.get<Int>("todoId")
		if (todoId != -1) {
			viewModelScope.launch {
				if (todoId != null) {
					repository.getTodoById(todoId)?.let { todo ->
						title = todo.title
						description = todo.description ?: ""
						this@AddEditTodoViewModel.todo = todo
					}
				}
			}

		}
	}

	fun onEvent(event : AddEditTodoEvent) {
		when (event) {
			is AddEditTodoEvent.OnDescriptionChange -> {
				description = event.description
			}

			is AddEditTodoEvent.OnSaveTodoClick     -> {
				viewModelScope.launch {
					if (title.isBlank()) {
						sendUiEvent(UiEvent.ShowSnackBar(message = "the title cant be empty"))
						return@launch
					}
					repository.insertTodo(
						Todo(
							title = title,
							description = description,
							isDone = todo?.isDone ?:false,
							id = todo?.id

						)
					)
					sendUiEvent(UiEvent.PopBackStack)
				}

			}

			is AddEditTodoEvent.OnTitleChange       -> {
				title = event.title
			}
		}

	}

	private fun sendUiEvent(event : UiEvent) {
		viewModelScope.launch {
			_uiEvent.send(event)
		}
	}


}
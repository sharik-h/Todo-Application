package com.example.todo.ViewModel

import androidx.lifecycle.*
import com.example.todo.data.Todo
import com.example.todo.data.TodoDao
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class TodoViewModel(private val todoDao: TodoDao): ViewModel() {

    val allItems: LiveData<List<Todo>> = todoDao.getAllData().asLiveData()

    fun isEntryValid(Title: String): Boolean {
        if (Title.isBlank()) return false
        return true
    }

    fun addNewTodoData(
        Title: String,
        subtitle: String,
        Notes: String,
        color: String
    ) {
               val newData = getNewData(Title, subtitle, Notes, color)
        InsertNewTodo(newData)
    }

    private fun getNewData(
        title: String,
        subtitle: String,
        notes: String,
        color: String
    ): Todo  {
        return Todo(
            Title = title,
            Subtitle = subtitle,
            Notes = notes,
            color = color)
    }

    private fun InsertNewTodo(todo: Todo) {
        viewModelScope.launch {
            todoDao.insert(todo)
        }
    }

    fun retriveData(id: Int): LiveData<Todo>{
        return todoDao.getData(id).asLiveData()
    }

    fun updateTodo(
        id: Int,
        Title: String,
        subtitle: String,
        notes: String,
        color: String
    ) {
        val updatedtodo = getupdatedtodoEntry(id, Title, subtitle, notes, color )
        UpdateTodo(updatedtodo)
    }

    private fun UpdateTodo(updatedtodo: Todo) {
        viewModelScope.launch {
            todoDao.update(updatedtodo)
        }
    }

    private fun getupdatedtodoEntry(
        id: Int,
        title: String,
        subtitle: String,
        notes: String,
        color: String
    ): Todo {
        return Todo(
            id = id,
            Title = title,
            Subtitle = subtitle,
            Notes = notes,
            color = color
        )
    }

    fun deleteTodoData(todo: Todo) {
        viewModelScope.launch {
            todoDao.delete(todo)
        }
    }

}

class TodoViewModelFactory(private val todoDao: TodoDao): ViewModelProvider.Factory {
    override fun <T: ViewModel?> create(modelclass: Class<T>): T {
        if (modelclass.isAssignableFrom(TodoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TodoViewModel(todoDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
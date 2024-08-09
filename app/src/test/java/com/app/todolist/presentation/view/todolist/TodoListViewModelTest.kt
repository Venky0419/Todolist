package com.app.todolist.presentation.view.todolist

import android.content.SharedPreferences
import androidx.lifecycle.SavedStateHandle
import com.app.todolist.data.dao.TodoDao
import com.app.todolist.data.preferences.DefaultPreferences
import com.app.todolist.data.repositories.TodoRepositoryImpl
import com.app.todolist.domain.models.TodoTaskModel
import com.app.todolist.domain.preferences.Preferences
import com.app.todolist.domain.repositories.TodoRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * Created by P,Venkatesh on 07-Aug-24
 *
 */
class TodoListViewModelTest {

    @Mock
    lateinit var repository:TodoRepository

    @Mock
    lateinit var preferences: Preferences

    @Mock
    lateinit var sharedPreferences: SharedPreferences

    @Mock
    lateinit var viewModel: TodoListViewModel

    @Mock
    lateinit var savedStateHandle: SavedStateHandle

    @Mock
    lateinit var dao: TodoDao

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        repository = TodoRepositoryImpl(dao)
        preferences = DefaultPreferences(sharedPreferences)
        viewModel = TodoListViewModel(savedStateHandle,repository,preferences)
    }

    @Test
    fun getUiEvent() = runTest {

    }

    @Test
    fun changePriority() {
    }

    @Test
    fun getUiState() {

    }

    @Test
    fun deleteAllTasks() = runTest {


    }

    @Test
    fun selectTask() {
        val model = TodoTaskModel(1,"","",1)

    }

    @Test
    fun undoDeletedTask() = runTest {
    }

    @Test
    fun searchTextChange() {
        val text = "test"

    }

    @Test
    fun showSearchBar() {
    }

    @Test
    fun closeSearchBar() {
    }
}
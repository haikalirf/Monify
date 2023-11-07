package com.bleh.monify.feature_book

import androidx.lifecycle.ViewModel
import com.bleh.monify.feature_auth.GoogleAuthUiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class BookState(
    val searchState: String = "",
    val note: String = "",
    val nominal: String = "",
    val date: String = "",
)

@HiltViewModel
class BookViewModel @Inject constructor(

): ViewModel() {
    private val _state = MutableStateFlow(BookState())
    val state = _state.asStateFlow()

    fun updateSearchState (search: String) {
        _state.update {
            it.copy(searchState = search)
        }
    }

    fun updateNoteState (note: String) {
        _state.update {
            it.copy(note = note)
        }
    }

    fun updateNominalState (nominal: String) {
        _state.update {
            it.copy(nominal = nominal)
        }
    }

    fun updateDateState (date: String) {
        _state.update {
            it.copy(date = date)
        }
    }
}
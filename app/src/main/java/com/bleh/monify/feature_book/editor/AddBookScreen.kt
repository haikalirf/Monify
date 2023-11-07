package com.bleh.monify.feature_book.editor

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bleh.monify.R
import com.bleh.monify.core.ui_components.AccentedButton
import com.bleh.monify.feature_book.BookViewModel
import com.bleh.monify.feature_book.book.AddButton
import com.bleh.monify.feature_book.book.BookList
import com.bleh.monify.feature_book.book.BottomBar
import com.bleh.monify.feature_book.book.SearchBar
import java.util.Locale

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AddBookScreen(
    navController: NavController,
    viewModel: BookViewModel
) {
    val state by viewModel.state.collectAsState()
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        containerColor = Color.Transparent,
        modifier = Modifier
            .paint(
                painter = painterResource(R.drawable.main_background),
                contentScale = ContentScale.Crop
            )
    ) {
        Column {
            SelectionBar(
                viewModel = viewModel,
                modifier = Modifier
                    .padding(top = 60.dp)
            )
            EditorCard(
                viewModel = viewModel,
            )
        }
    }
}

@Composable
fun SelectionBar(
    viewModel: BookViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    val search = state.searchState
    OutlinedTextField(
        value = search,
        onValueChange = {
            viewModel.updateSearchState(it)
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(40.dp),
        trailingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = "Search Icon"
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorCard(
    viewModel: BookViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    val note = state.note
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 20.dp)
            .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
            .background(Color.White)
            .padding(horizontal = 15.dp, vertical = 20.dp)
    ) {
        Text(
            text = "Catatan Pemasukan",
            style = MaterialTheme.typography.bodyLarge,
        )
        OutlinedTextField(
            value = note,
            onValueChange = {
                viewModel.updateNoteState(it)
            },
            shape = RoundedCornerShape(30.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Black,
            ),
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                )
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        )
        Text(
            text = "Nominal",
            style = MaterialTheme.typography.bodyLarge,
        )
        OutlinedTextField(
            value = note,
            onValueChange = {
                viewModel.updateNoteState(it)
            },
            shape = RoundedCornerShape(30.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Black,
            ),
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                )
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        )
        Text(
            text = "Tanggal",
            style = MaterialTheme.typography.bodyLarge,
        )
        OutlinedTextField(
            value = note,
            onValueChange = {
                viewModel.updateNoteState(it)
            },
            shape = RoundedCornerShape(30.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Black,
            ),
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                )
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        )
        Text(
            text = "Dompet",
            style = MaterialTheme.typography.bodyLarge,
        )
        OutlinedTextField(
            value = note,
            onValueChange = {
                viewModel.updateNoteState(it)
            },
            shape = RoundedCornerShape(30.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Black,
            ),
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                )
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        )
        Text(
            text = "Kategori",
            style = MaterialTheme.typography.bodyLarge,
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier
                .weight(1f)
                .border(
                    width = 1.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(30.dp)
                )
                .padding(horizontal = 20.dp)
        ) {
            items(4) {
                Spacer(modifier = Modifier.size(20.dp))
            }
            items(20) {
                CategoryItem(
                    viewModel = viewModel,
                    icon = R.drawable.ic_paper_checkmark,
                    text = "Beasiswa"
                )
            }
            items(4) {
                Spacer(modifier = Modifier.size(20.dp))
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        ) {
            AccentedButton(
                onClick = { /*TODO*/ },
                text = "Kembali",
                modifier = Modifier
                    .size(160.dp, 50.dp)
            )
            AccentedButton(
                onClick = { /*TODO*/ },
                text = "Tambah",
                modifier = Modifier
                    .size(160.dp, 50.dp)
            )
        }
    }
}

@Composable
fun CategoryItem(
    viewModel: BookViewModel,
    icon: Int,
    text: String,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(10.dp)
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = text
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
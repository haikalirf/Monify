package com.bleh.monify.feature_more.category

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bleh.monify.R
import com.bleh.monify.core.enums.CategoryType
import com.bleh.monify.core.ui_components.AccentedButton
import com.bleh.monify.core.ui_components.ButtonCombinations
import com.bleh.monify.feature_book.add.CategoryItem
import com.bleh.monify.feature_more.category.helper.categoryIconList
import com.bleh.monify.ui.theme.Grey

@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun EditCategoryScreen(
    navController: NavController,
    viewModel: CategoryViewModel,
) {
    val state by viewModel.state.collectAsState()
    Scaffold(
        containerColor = Color.Transparent,
        modifier = Modifier
            .paint(
                painter = painterResource(R.drawable.main_background),
                contentScale = ContentScale.Crop
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            EditCategoryComposable(
                viewModel = viewModel,
                categoryType = state.categoryType,
                modifier = Modifier.padding(top = 60.dp)
            )
            EditCategoryCard(
                viewModel = viewModel,
                navController = navController
            )
        }
    }
}

@Composable
fun EditCategoryComposable(
    viewModel: CategoryViewModel,
    categoryType: CategoryType,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .height(55.dp)
            .clip(shape = RoundedCornerShape(30.dp))
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {
            val titleText = if(state.isEdit) {
                Log.d("EditCategoryComposable", "Category Type: ${categoryType.categoryName}")
                "Ubah / Hapus Kategori " + categoryType.categoryName
            } else {
                "Tambah Kategori " + categoryType.categoryName
            }
            Text(
                text = titleText,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

@Composable
fun EditCategoryCard(
    viewModel: CategoryViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    val name = state.categoryName
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 20.dp)
            .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
            .background(Color.White)
            .padding(horizontal = 15.dp, vertical = 20.dp)
    ) {
        Text(
            text = "Nama Kategori",
            style = MaterialTheme.typography.bodyLarge,
        )
        OutlinedTextField(
            value = name,
            onValueChange = {
                viewModel.updateCategoryName(it)
            },
            shape = RoundedCornerShape(30.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Black,
            ),
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                )
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .padding(bottom = 5.dp)
        )
        Text(
            text = "Ikon",
            style = MaterialTheme.typography.bodyLarge,
        )
        AddCategoryGrid(viewModel = viewModel, modifier = Modifier.weight(1f))
        ButtonCombinations(
            backButton = {
                viewModel.resetInputState()
                navController.popBackStack()
            },
            addButton = {
                if(viewModel.upsertCategory(context) == null) {
                    viewModel.resetInputState()
                    navController.popBackStack()
                }
            },
            saveButton = {
                if(viewModel.upsertCategory(context) == null) {
                    viewModel.resetInputState()
                    navController.popBackStack()
                }
            },
            deleteButton = {
                viewModel.setDeletedTrue(state.currentEditId)
                viewModel.resetInputState()
                navController.popBackStack()
            },
            isEdit = state.isEdit,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        )
    }
}

@Composable
fun AddCategoryGrid(
    viewModel: CategoryViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    val iconList = categoryIconList()
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        contentPadding = PaddingValues(vertical = 20.dp),
        modifier = modifier
            .border(
                width = 1.dp,
                color = Color.Black,
                shape = RoundedCornerShape(30.dp)
            )
            .padding(horizontal = 20.dp)
    ) {
        iconList.forEachIndexed { index, it ->
            item {
                CategoryItem(
                    icon = it,
                    isSelected = index == state.selectedCategory
                ) {
                    viewModel.updateSelectedCategory(index)
                }
            }
        }
    }
}

@Composable
fun CategoryItem(
    icon: Int,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) Grey else Color.Transparent
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) {
                onClick()
            }
            .clip(RoundedCornerShape(10.dp))
            .background(backgroundColor)
            .padding(10.dp)
            .size(70.dp)
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = ""
        )
    }
}
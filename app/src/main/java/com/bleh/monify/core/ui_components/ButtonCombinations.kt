package com.bleh.monify.core.ui_components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.bleh.monify.feature_wallet.WalletViewModel

@Composable
fun ButtonCombinations(
    backButton: () -> Unit,
    addButton: () -> Unit,
    saveButton: () -> Unit,
    deleteButton: () -> Unit,
    isEdit: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = modifier
    ) {
        if(isEdit) {
            AccentedButton(
                onClick = {
                    backButton()
                },
                text = "Kembali",
                modifier = Modifier
                    .size(120.dp, 50.dp)
            )
            AccentedButton(
                onClick = {
                    saveButton()
                },
                text = "Simpan",
                modifier = Modifier
                    .size(120.dp, 50.dp)
            )
            AccentedButton(
                onClick = {
                    deleteButton()
                },
                text = "Hapus",
                modifier = Modifier
                    .size(120.dp, 50.dp)
            )
        } else {
            AccentedButton(
                onClick = {
                    backButton()
                },
                text = "Kembali",
                modifier = Modifier
                    .size(160.dp, 50.dp)
            )
            AccentedButton(
                onClick = {
                    addButton()
                },
                text = "Tambah",
                modifier = Modifier
                    .size(160.dp, 50.dp)
            )
        }
    }
}
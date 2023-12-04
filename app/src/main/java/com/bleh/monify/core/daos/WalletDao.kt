package com.bleh.monify.core.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.bleh.monify.core.entities.Wallet
import kotlinx.coroutines.flow.Flow

@Dao
interface WalletDao {
    @Upsert
    suspend fun upsertWallet(wallet: Wallet)

    @Query("SELECT * FROM wallet")
    fun getWallets(): Flow<List<Wallet>>

    @Query("UPDATE wallet SET isDeleted = 1 WHERE id = :id")
    fun setDeletedTrue(id: Int)

    @Query("SELECT SUM(balance) as sum FROM wallet")
    fun walletSum(): Flow<Double>

    @Query("UPDATE Wallet SET balance = balance + :balanceToAdd WHERE id = :id")
    fun addWalletBalance(id: Int, balanceToAdd: Double)
}
package ru.rayanis.cryptocurrencyapp.domain.repository

import ru.rayanis.cryptocurrencyapp.data.remote.dto.CoinDetailDto
import ru.rayanis.cryptocurrencyapp.data.remote.dto.CoinDto

interface CoinRepository {
    suspend fun getCoins(): List<CoinDto>

    suspend fun getCoinById(coinId: String): CoinDetailDto
}
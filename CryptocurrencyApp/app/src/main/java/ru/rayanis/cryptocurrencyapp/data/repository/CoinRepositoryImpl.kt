package ru.rayanis.cryptocurrencyapp.data.repository

import ru.rayanis.cryptocurrencyapp.data.remote.CoinPaprikaApi
import ru.rayanis.cryptocurrencyapp.data.remote.dto.CoinDetailDto
import ru.rayanis.cryptocurrencyapp.data.remote.dto.CoinDto
import ru.rayanis.cryptocurrencyapp.domain.repository.CoinRepository
import javax.inject.Inject

class CoinRepositoryImpl @Inject constructor(
    private val api: CoinPaprikaApi
) : CoinRepository{
    override suspend fun getCoins(): List<CoinDto> {
        return api.getCoins()
    }

    override suspend fun getCoinById(coinId: String): CoinDetailDto {
        return api.getCoinById(coinId)
    }
}
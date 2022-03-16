package ru.rayanis.cryptocurrencyapp.presentation.coin_detail

import ru.rayanis.cryptocurrencyapp.domain.model.Coin
import ru.rayanis.cryptocurrencyapp.domain.model.CoinDetail

data class CoinDetailState(
    val isLoading: Boolean = false,
    val coin: CoinDetail? = null,
    val error: String = ""
)

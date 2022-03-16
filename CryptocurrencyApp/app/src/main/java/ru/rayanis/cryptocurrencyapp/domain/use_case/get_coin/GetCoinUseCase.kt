package ru.rayanis.cryptocurrencyapp.domain.use_case.get_coin

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import ru.rayanis.cryptocurrencyapp.common.Resource
import ru.rayanis.cryptocurrencyapp.data.remote.dto.toCoin
import ru.rayanis.cryptocurrencyapp.data.remote.dto.toCoinDetail
import ru.rayanis.cryptocurrencyapp.domain.model.Coin
import ru.rayanis.cryptocurrencyapp.domain.model.CoinDetail
import ru.rayanis.cryptocurrencyapp.domain.repository.CoinRepository
import java.io.IOException
import javax.inject.Inject

class GetCoinUseCase @Inject constructor(
    private val repository: CoinRepository
) {
    operator fun invoke(coinId: String): Flow<Resource<CoinDetail>> = flow {
        try {
            emit(Resource.Loading())
            val coin = repository.getCoinById(coinId).toCoinDetail()
            emit(Resource.Success(coin))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection"))
        }
    }
}
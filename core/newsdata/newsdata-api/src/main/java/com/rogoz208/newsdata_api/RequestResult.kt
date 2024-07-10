package com.rogoz208.newsdata_api

sealed class RequestResult<out E : Any>(open val data: E? = null, open val totalResults: Int = 0) {

    class Success<E : Any>(override val data: E, override val totalResults: Int = 0) :
        RequestResult<E>(data, totalResults)

    class InProgress<E : Any>(data: E? = null, override val totalResults: Int = 0) :
        RequestResult<E>(data, totalResults)

    class Error<E : Any>(
        data: E? = null,
        val error: Throwable?,
        override val totalResults: Int = 0
    ) : RequestResult<E>(data, totalResults)
}

fun <I : Any, O : Any> RequestResult<I>.map(mapper: (I) -> O): RequestResult<O> {
    return when (this) {
        is RequestResult.Success -> RequestResult.Success(data = mapper(data), totalResults)
        is RequestResult.InProgress -> RequestResult.InProgress(
            data = data?.let(mapper),
            totalResults
        )

        is RequestResult.Error -> RequestResult.Error(
            data = data?.let(mapper),
            error = error,
            totalResults
        )
    }
}
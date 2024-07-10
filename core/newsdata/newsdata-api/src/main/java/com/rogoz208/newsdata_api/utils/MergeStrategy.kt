package com.rogoz208.newsdata_api.utils

import com.rogoz208.newsdata_api.RequestResult
import com.rogoz208.newsdata_api.RequestResult.Error
import com.rogoz208.newsdata_api.RequestResult.InProgress
import com.rogoz208.newsdata_api.RequestResult.Success

interface MergeStrategy<E> {

    fun merge(left: E, right: E): E
}

internal class RequestResponseMergeStrategy<T : Any> : MergeStrategy<RequestResult<T>> {

    override fun merge(left: RequestResult<T>, right: RequestResult<T>): RequestResult<T> {
        return when {
            left is InProgress && right is InProgress -> merge(left, right)
            left is Success && right is InProgress -> merge(left, right)
            left is InProgress && right is Success -> merge(left, right)
            left is Success && right is Error -> merge(left, right)
            left is Success && right is Success -> merge(left, right)
            left is InProgress && right is Error -> merge(left, right)
            left is Error && right is InProgress -> merge(left, right)
            left is Error && right is Success -> merge(left, right)

            else -> error("Unimplemented branch left=$left & right=$right")
        }
    }

    private fun merge(cache: InProgress<T>, server: InProgress<T>): RequestResult<T> {
        return when {
            server.data != null -> InProgress(server.data, server.totalResults)
            else -> InProgress(cache.data)
        }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun merge(cache: Success<T>, server: InProgress<T>): RequestResult<T> {
        return InProgress(cache.data)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun merge(cache: InProgress<T>, server: Success<T>): RequestResult<T> {
        return InProgress(server.data, server.totalResults)
    }

    private fun merge(cache: Success<T>, server: Error<T>): RequestResult<T> {
        return Error(data = cache.data, error = server.error)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun merge(cache: Success<T>, server: Success<T>): RequestResult<T> {
        return Success(data = server.data, server.totalResults)
    }

    private fun merge(cache: InProgress<T>, server: Error<T>): RequestResult<T> {
        return Error(data = server.data ?: cache.data, error = server.error)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun merge(cache: Error<T>, server: InProgress<T>): RequestResult<T> {
        return server
    }

    private fun merge(cache: Error<T>, server: Success<T>): RequestResult<T> {
        return Error(data = server.data, error = cache.error)
    }
}

package com.example.chillitest.data.states

import retrofit2.HttpException
import java.io.IOException

/**
 * Sealed class representing the result of an operation, which can be one of the following types:
 *
 * @param T The type of FollowingData expected in a successful operation.
 */
sealed class ResultTypes<out T> {

    /**
     * Represents a successful result of the operation.
     *
     * @param T The type of FollowingData returned in case of success.
     * @property data The FollowingData obtained from the successful operation. Can be null.
     */
    data class Success<out T>(val data: T?) : ResultTypes<T>()

    /**
     * Represents an input/output (IO) error occurred during the operation.
     *
     * @property exception The IOException that describes the IO error.
     *
     * This type of error typically occurs when there are network issues,
     * such as when the device has no internet connection, the server is unavailable,
     * or a timeout occurs.
     */
    data class IOError(val exception: IOException) : ResultTypes<Nothing>()

    /**
     * Represents an HTTP error occurred during the operation.
     *
     * @property exception The HttpException that describes the HTTP error.
     *
     * This type of error occurs when the server responds with an HTTP status code
     * indicating a failure, such as 4xx for client errors (e.g., 404 Not Found, 401 Unauthorized)
     * or 5xx for server errors (e.g., 500 Internal Server Error).
     */
    data class HttpError(val exception: HttpException) : ResultTypes<Nothing>()

    /**
     * Represents a generic error occurred during the operation.
     *
     * @property exception The exception that describes the error. Can be any type of exception.
     *
     * This type of error captures any other exceptions that are not specifically
     * an IOException or HttpException. This can include programming errors, FollowingData conversion issues,
     * unexpected errors, etc.
     */
    data class Error(val exception: Throwable) : ResultTypes<Nothing>()

    /**
     * Represents the loading state of the operation.
     * This state can be used to indicate that the operation is in progress.
     */
    object Loading : ResultTypes<Nothing>()
}

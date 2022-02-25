package com.hugo.study_dialog_demo.ui

import java.io.Serializable

/**
 * 密封类不能被实例化
 */
sealed class UIState<out T> constructor(val value: T?) {
    data class Success<out T>(val data: T?) : UIState<T>(data)
    data class Failure(val error: ApiError) : UIState<ApiError>(error)

    val isSuccess: Boolean get() = value !is ApiError

    val isFailure: Boolean get() = value is ApiError

    companion object {
        public inline fun <T> success(value: T): UIState<T> =
            Success(value)

        public inline fun failure(errorCode: Int, errorMsg: String): UIState<ApiError> =
            Failure(ApiError(errorCode, errorMsg))

    }

    class ApiError(val errorCode: Int, val errorMsg: String?) : Serializable {
        override fun equals(other: Any?): Boolean = other is ApiError && errorCode == other.errorCode
        override fun hashCode(): Int {
            var result = errorCode
            result = 31 * result + (errorMsg?.hashCode() ?: 0)
            return result
        }
        override fun toString(): String {
            return "ApiError(errorCode=$errorCode, errorMsg=$errorMsg)"
        }
    }
}

/**
 * Used as a wrapper for data that is exposed via a LiveData that represents an event.
 */
open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}




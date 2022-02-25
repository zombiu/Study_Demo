package com.hugo.study_dialog_demo.ui

import java.io.Serializable

sealed class UIResult<out T>() {
    data class Success<out T>(val data: T) : UIResult<T>()

    //    data class Failure(val errorCode: Int, val errorMsg: String) : UIResult<Nothing>()
    data class Failure(val error: ApiError) : UIResult<ApiError>()


    companion object {
        public inline fun <T> success(value: T): UIResult<T> =
            Success(value)

        public inline fun failure(errorCode: Int, errorMsg: String): UIResult<ApiError> =
            Failure(ApiError(errorCode, errorMsg))

    }

}


class ApiError(val errorCode: Int, val errorMsg: String) : Serializable {
    override fun equals(other: Any?): Boolean = other is ApiError && errorCode == other.errorCode
//    override fun hashCode(): Int = exception.hashCode()
//    override fun toString(): String = "Failure($exception)"
}

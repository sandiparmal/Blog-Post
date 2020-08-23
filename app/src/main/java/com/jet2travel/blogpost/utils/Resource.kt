package com.jet2travel.blogpost.utils

class Resource<T> private constructor(
    val status: Status, val data: T?,
    val message: String?
) {

    companion object {
        fun <T> success(data: T, msg: String?): Resource<T> {
            return Resource(Status.SUCCESS, data, msg)
        }

        fun <T> error(msg: String?, data: T?): Resource<T> {
            return Resource(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T?): Resource<T?> {
            return Resource(Status.LOADING, data, null)
        }
    }

}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}
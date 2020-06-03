package com.example.uscovidstatistics.presenter

interface BaseView<T> {
    fun setPresenter(presenter: T)
}
package com.aeroshi.movies.util

import java.util.concurrent.Executors

abstract class Executors {

    companion object {
        private val IO_EXECUTOR = Executors.newSingleThreadExecutor()

        fun ioThread(f: () -> Unit) {
            IO_EXECUTOR.execute(f)
        }

    }
}
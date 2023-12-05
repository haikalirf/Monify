package com.bleh.monify.core.helper

import java.util.concurrent.Executors

private val ioExecutors = Executors.newSingleThreadExecutor()

fun ioThread(f : () -> Unit) {
    ioExecutors.execute(f)
}
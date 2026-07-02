package com.kotich.app.core.util

interface CloseableSequence<T> : Sequence<T>, AutoCloseable

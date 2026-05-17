package com.example.kur.application.port;

import java.util.function.Supplier;

public interface UnitOfWork {
    void run(Runnable work);

    <T> T call(Supplier<T> work);
}


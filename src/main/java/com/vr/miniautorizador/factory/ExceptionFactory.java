package com.vr.miniautorizador.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ExceptionFactory {

    public static <T extends Throwable> void throwException(Class<T> clazz) throws T {
        try {
            Constructor<T> constructor = clazz.getConstructor(String.class);
            throw constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}

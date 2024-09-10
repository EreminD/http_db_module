package ru.inno.sandbox;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;

public class ExceptionHandler implements TestExecutionExceptionHandler {
    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {

        if (throwable.getClass().equals(NullPointerException.class)) {
            System.out.println("Success");
        } else {
            throw throwable;
        }
    }
}

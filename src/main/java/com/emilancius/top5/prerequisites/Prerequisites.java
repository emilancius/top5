package com.emilancius.top5.prerequisites;

public final class Prerequisites {

    private Prerequisites() {
        throw new InstantiationError(Prerequisites.class.toString() + " cannot be instantiated");
    }

    public static <T> void existingArgument(final T argument, final String message) {
        if (argument == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void stringContainsText(final String string, final String message) {
        if (string == null || string.trim().isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }
}

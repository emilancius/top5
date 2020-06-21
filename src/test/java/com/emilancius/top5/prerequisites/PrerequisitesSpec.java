package com.emilancius.top5.prerequisites;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PrerequisitesSpec {

    @Test
    public void nullArgumentProvided_producesIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> Prerequisites.existingArgument(null, null));
    }

    @Test
    public void existingArgumentProvided_doesntProduceAnyException() {
        Prerequisites.existingArgument("", null);
    }

    @Test
    public void emptyStringProvided_producesIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> Prerequisites.stringContainsText("", null));
    }

    @Test
    public void nonEmptyStringProvided_doesntProduceAnyException() {
        Prerequisites.stringContainsText("_", null);
    }
}

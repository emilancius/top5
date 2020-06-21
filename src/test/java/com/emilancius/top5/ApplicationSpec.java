package com.emilancius.top5;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ApplicationSpec {

    @Test
    public void loadsContext() {
        Application.main(new String[]{});
    }
}

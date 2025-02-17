package br.com.nu.domain;

import br.com.nu.constant.OperationType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OperationTest {

    @Test
    void testJsonConversion() {
        Operation operation = new Operation(OperationType.buy, new BigDecimal("20.00"), 10000);
        assertEquals("buy", operation.operationType().name());
        assertEquals(new BigDecimal("20.00"), operation.unitCost());
        assertEquals(10000, operation.quantity());

        Gson gson = new GsonBuilder().create();
        String expectedJson = "{\"operation\":\"buy\",\"unit-cost\":20.00,\"quantity\":10000}";
        assertEquals(expectedJson, gson.toJson(operation));
    }

}
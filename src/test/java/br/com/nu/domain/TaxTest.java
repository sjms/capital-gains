package br.com.nu.domain;

import br.com.nu.domain.Tax;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TaxTest {

    @Test
    void testJsonConversion() {
        var expected = "{\"tax\":10000.00}";
        var tax = new Tax(new BigDecimal("10000.00"));
        var actual = new GsonBuilder().create().toJson(tax);
        assertEquals(expected, actual);
    }
}
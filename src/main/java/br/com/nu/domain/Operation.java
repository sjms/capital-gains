package br.com.nu.domain;

import br.com.nu.constant.OperationType;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public record Operation(
        @SerializedName("operation")
        OperationType operationType,
        @SerializedName("unit-cost")
        BigDecimal unitCost,
        Integer quantity) {
}

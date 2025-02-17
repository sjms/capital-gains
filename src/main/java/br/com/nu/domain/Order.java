package br.com.nu.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Order {

    public Order() {
        this.averagePrice = BigDecimal.ZERO;
        this.taxList = new ArrayList<>();
        this.currentBalance = BigDecimal.ZERO;
    }

    private BigDecimal averagePrice;
    private int stockQuantity;
    private final List<Tax> taxList;
    private BigDecimal currentBalance;

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }

    public BigDecimal getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(BigDecimal averagePrice) {
        this.averagePrice = averagePrice;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public List<Tax> getTaxList() {
        return taxList;
    }

    public void addTax(BigDecimal tax) {
        this.taxList.add(new Tax(Objects.requireNonNullElse(tax, BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP)));
    }

    public void includeTaxFree() {
        this.taxList.add(new Tax(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)));
    }

}

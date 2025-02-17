package br.com.nu.usecases;

import br.com.nu.domain.Operation;
import br.com.nu.domain.Order;

import java.math.BigDecimal;

public class SellStockUseCase {

    private static final BigDecimal TAX_APPLICATION_THRESHOLD = new BigDecimal("20000.00");
    private static final BigDecimal TAX_PERCENTAGE = new BigDecimal("0.20");

    private static SellStockUseCase INSTANCE;

    public static SellStockUseCase getInstance() {
        if (INSTANCE == null) INSTANCE = new SellStockUseCase();
        return INSTANCE;
    }

    public void executeOperation(Order order, Operation operation) {
        var totalSales = calculateTotalSales(operation);
        var averagePrice = calculateAveragePrice(order, operation.quantity());
        var totalStockProfit = calculateProfit(totalSales, averagePrice);
        updateCurrentBalance(order, totalStockProfit);
        updateStockQuantity(order, operation.quantity());
        var tax = applyTax(order, totalSales);
        order.addTax(tax);
    }

    private BigDecimal applyTax(Order order, BigDecimal totalSales) {
        if (!isTaxApplicable(totalSales, order.getCurrentBalance())) {
            return BigDecimal.ZERO;
        }
        var totalTax = calculateTax(order.getCurrentBalance());
        if (totalTax.signum() > 0) {
            order.setCurrentBalance(BigDecimal.ZERO);
            return totalTax;
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal calculateTotalSales(Operation operation) {
        return operation.unitCost().multiply(BigDecimal.valueOf(operation.quantity()));
    }

    private BigDecimal calculateAveragePrice(Order order, int quantity) {
        return order.getAveragePrice().multiply(BigDecimal.valueOf(quantity));
    }

    private BigDecimal calculateProfit(BigDecimal totalSales, BigDecimal averagePrice) {
        return totalSales.subtract(averagePrice);
    }

    private void updateCurrentBalance(Order order, BigDecimal value) {
        order.setCurrentBalance(order.getCurrentBalance().add(value));
    }

    private void updateStockQuantity(Order order, int quantity) {
        order.setStockQuantity(order.getStockQuantity() - quantity);
    }

    private boolean isTaxApplicable(BigDecimal totalSales, BigDecimal currentBalance) {
        if (totalSales.compareTo(TAX_APPLICATION_THRESHOLD) > 0) {
            return true;
        }
        return currentBalance.compareTo(TAX_APPLICATION_THRESHOLD) > 0;
    }

    private BigDecimal calculateTax(BigDecimal profit) {
        return TAX_PERCENTAGE.multiply(profit);
    }
}
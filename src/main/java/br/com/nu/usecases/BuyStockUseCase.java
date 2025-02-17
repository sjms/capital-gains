package br.com.nu.usecases;

import br.com.nu.domain.Operation;
import br.com.nu.domain.Order;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BuyStockUseCase {

    private static BuyStockUseCase INSTANCE;

    public static BuyStockUseCase getInstance() {
        if (INSTANCE == null) INSTANCE = new BuyStockUseCase();
        return INSTANCE;
    }

    public void executeOperation(Order order, Operation operation) {
        var newStockQuantity = order.getStockQuantity() + operation.quantity();
        var totalCurrentValue = order.getAveragePrice().multiply(new BigDecimal(order.getStockQuantity()));
        var totalPurchaseValue = operation.unitCost().multiply(new BigDecimal(operation.quantity()));
        var newAverage = totalCurrentValue.add(totalPurchaseValue).divide(new BigDecimal(newStockQuantity), 2, RoundingMode.HALF_UP);

        order.setStockQuantity(newStockQuantity);
        order.setAveragePrice(newAverage);
        order.includeTaxFree();
    }
}

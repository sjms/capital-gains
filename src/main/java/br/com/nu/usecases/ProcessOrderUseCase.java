package br.com.nu.usecases;

import br.com.nu.domain.Operation;
import br.com.nu.domain.Order;
import br.com.nu.domain.Tax;

import java.util.List;

public class ProcessOrderUseCase {

    private static ProcessOrderUseCase INSTANCE;

    public static ProcessOrderUseCase getInstance() {
        if (INSTANCE == null) INSTANCE = new ProcessOrderUseCase();
        return INSTANCE;
    }

    public List<Tax> calculate(List<Operation> operations) {
        Order order = new Order();
        operations.forEach(operation -> {
            switch (operation.operationType()) {
                case buy -> BuyStockUseCase.getInstance().executeOperation(order, operation);
                case sell -> SellStockUseCase.getInstance().executeOperation(order, operation);
                default ->
                        throw new IllegalArgumentException(String.format("Invalid operation type %s", operation.operationType()));
            }
        });
        return order.getTaxList();
    }
}
package br.com.nu.application;

import br.com.nu.domain.Operation;
import br.com.nu.domain.Tax;
import br.com.nu.usecases.ProcessOrderUseCase;

import java.util.LinkedList;
import java.util.List;

public class TaxesController {

    private TaxesController() {
    }

    public static List<List<Tax>> calculateOperations(List<List<Operation>> input) {
        List<List<Tax>> multipleTaxesResult = new LinkedList<>();
        for (List<Operation> operations : input) {
            List<Tax> taxes = ProcessOrderUseCase.getInstance().calculate(operations);
            multipleTaxesResult.add(taxes);
        }
        return multipleTaxesResult;
    }
}

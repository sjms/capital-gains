package br.com.nu.usecases;

import br.com.nu.application.TaxesController;
import br.com.nu.constant.OperationType;
import br.com.nu.domain.Operation;
import br.com.nu.domain.Tax;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

class ProcessOrderUseCaseTest {

    private static final BigDecimal ZERO_TAX = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

    private void assertEquality(List<Tax> expected, List<Tax> actual) {
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.size(), actual.size());
        for (int i = 0; i < actual.size(); i++) {
            Assertions.assertEquals(expected.get(i), actual.get(i));
        }
    }

    @DisplayName("Case 1# - Should return tax free for all operations")
    @Test
    void testCase1PurchaseAndSalesWithZeroTax() {

        var operationList = Arrays.asList(
                new Operation(OperationType.buy, new BigDecimal("10.00"), 100),
                new Operation(OperationType.sell, new BigDecimal("15.00"), 50),
                new Operation(OperationType.sell, new BigDecimal("15.00"), 50)
        );

        var taxList = ProcessOrderUseCase.getInstance().calculate(operationList);

        Assertions.assertEquals(taxList.size(), operationList.size());
        taxList.forEach(tax -> Assertions.assertEquals(ZERO_TAX, tax.tax()));
    }

    @DisplayName("Case 2# - Should pay tax for sale operation")
    @Test
    void testCase2PurchaseAndSalesWithZeroTax() {
        var expectedTaxList = Arrays.asList(new Tax(ZERO_TAX), new Tax(new BigDecimal("10000.00")), new Tax(ZERO_TAX));
        var operationList = Arrays.asList(
                new Operation(OperationType.buy, new BigDecimal("10.00"), 10000),
                new Operation(OperationType.sell, new BigDecimal("20.00"), 5000),
                new Operation(OperationType.buy, new BigDecimal("5.00"), 5000)
        );

        var givenTaxList = ProcessOrderUseCase.getInstance().calculate(operationList);

        Assertions.assertEquals(3, givenTaxList.size());
        this.assertEquality(expectedTaxList, givenTaxList);
    }

    @DisplayName("Case 1# + Case 2# - Should not share any state")
    @Test
    void testCase1AndCase2PurchaseAndSalesNotSharingAnyState() {
        var case1OperationList = Arrays.asList(
                new Operation(OperationType.buy, new BigDecimal("10.00"), 100),
                new Operation(OperationType.sell, new BigDecimal("15.00"), 50),
                new Operation(OperationType.sell, new BigDecimal("15.00"), 50)
        );

        var expectedCase1TaxList = Arrays.asList(new Tax(ZERO_TAX), new Tax(ZERO_TAX), new Tax(ZERO_TAX));

        var case2OperationList = Arrays.asList(
                new Operation(OperationType.buy, new BigDecimal("10.00"), 10000),
                new Operation(OperationType.sell, new BigDecimal("20.00"), 5000),
                new Operation(OperationType.sell, new BigDecimal("5.00"), 5000)
        );

        var expectedCase2TaxList = Arrays.asList(new Tax(ZERO_TAX), new Tax(new BigDecimal("10000.00")), new Tax(ZERO_TAX));

        var actualCase1taxList = ProcessOrderUseCase.getInstance().calculate(case1OperationList);
        var actualCase2taxList = ProcessOrderUseCase.getInstance().calculate(case2OperationList);

        Assertions.assertEquals(3, actualCase1taxList.size());
        Assertions.assertEquals(3, actualCase2taxList.size());
        this.assertEquality(expectedCase1TaxList, actualCase1taxList);
        this.assertEquality(expectedCase2TaxList, actualCase2taxList);
    }

    @DisplayName("Case 3# - Should pay tax for sale operation with deducted loss")
    @Test
    void testCase3PurchaseAndSalesWithTax() {
        var expectedTaxList = Arrays.asList(new Tax(ZERO_TAX), new Tax(ZERO_TAX), new Tax(new BigDecimal("1000.00")));
        var operationList = Arrays.asList(
                new Operation(OperationType.buy, new BigDecimal("10.00"), 10000),
                new Operation(OperationType.sell, new BigDecimal("5.00"), 5000),
                new Operation(OperationType.sell, new BigDecimal("20.00"), 3000)
        );

        var givenTaxList = ProcessOrderUseCase.getInstance().calculate(operationList);

        Assertions.assertEquals(3, givenTaxList.size());
        this.assertEquality(expectedTaxList, givenTaxList);
    }

    @DisplayName("Case 4# - Should not pay tax because has no gain/loss")
    @Test
    void testCase4PurchaseAndSalesWithZeroTax() {
        var expectedTaxList = Arrays.asList(new Tax(ZERO_TAX), new Tax(ZERO_TAX), new Tax(ZERO_TAX));
        var operationList = Arrays.asList(
                new Operation(OperationType.buy, new BigDecimal("10.00"), 10000),
                new Operation(OperationType.buy, new BigDecimal("25.00"), 5000),
                new Operation(OperationType.sell, new BigDecimal("15.00"), 10000)
        );

        var givenTaxList = ProcessOrderUseCase.getInstance().calculate(operationList);

        Assertions.assertEquals(3, givenTaxList.size());
        this.assertEquality(expectedTaxList, givenTaxList);
    }

    @DisplayName("Case 5# - Should pay tax because has profit in the last sale")
    @Test
    void testCase5PurchaseAndSalesWithTax() {
        var expectedTaxList = Arrays.asList(new Tax(ZERO_TAX), new Tax(ZERO_TAX), new Tax(ZERO_TAX), new Tax(new BigDecimal("10000.00")));
        var operationList = Arrays.asList(
                new Operation(OperationType.buy, new BigDecimal("10.00"), 10000),
                new Operation(OperationType.buy, new BigDecimal("25.00"), 5000),
                new Operation(OperationType.sell, new BigDecimal("15.00"), 10000),
                new Operation(OperationType.sell, new BigDecimal("25.00"), 5000)
        );

        var givenTaxList = ProcessOrderUseCase.getInstance().calculate(operationList);

        Assertions.assertEquals(4, givenTaxList.size());
        this.assertEquality(expectedTaxList, givenTaxList);
    }

    @DisplayName("Case 6# - Should pay tax because has profit in the last sale")
    @Test
    void testCase6PurchaseAndSalesWithTax() {
        var expectedTaxList = Arrays.asList(new Tax(ZERO_TAX), new Tax(ZERO_TAX), new Tax(ZERO_TAX), new Tax(ZERO_TAX), new Tax(new BigDecimal("3000.00")));
        var operationList = Arrays.asList(
                new Operation(OperationType.buy, new BigDecimal("10.00"), 10000),
                new Operation(OperationType.sell, new BigDecimal("2.00"), 5000),
                new Operation(OperationType.sell, new BigDecimal("20.00"), 2000),
                new Operation(OperationType.sell, new BigDecimal("20.00"), 2000),
                new Operation(OperationType.sell, new BigDecimal("25.00"), 1000)
        );

        var givenTaxList = ProcessOrderUseCase.getInstance().calculate(operationList);

        Assertions.assertEquals(5, givenTaxList.size());
        this.assertEquality(expectedTaxList, givenTaxList);
    }

    @DisplayName("Case 7# - Should pay tax for sale operations 7, 8")
    @Test
    void testCase7PurchaseAndSalesWithTax() {
        var expectedTaxList = Arrays.asList(
                new Tax(ZERO_TAX),
                new Tax(ZERO_TAX),
                new Tax(ZERO_TAX),
                new Tax(ZERO_TAX),
                new Tax(new BigDecimal("3000.00")),
                new Tax(ZERO_TAX),
                new Tax(ZERO_TAX),
                new Tax(new BigDecimal("3700.00")),
                new Tax(ZERO_TAX));

        var operationList = Arrays.asList(
                new Operation(OperationType.buy, new BigDecimal("10.00"), 10000),
                new Operation(OperationType.sell, new BigDecimal("2.00"), 5000),
                new Operation(OperationType.sell, new BigDecimal("20.00"), 2000),
                new Operation(OperationType.sell, new BigDecimal("20.00"), 2000),
                new Operation(OperationType.sell, new BigDecimal("25.00"), 1000),
                new Operation(OperationType.buy, new BigDecimal("20.00"), 10000),
                new Operation(OperationType.sell, new BigDecimal("15.00"), 5000),
                new Operation(OperationType.sell, new BigDecimal("30.00"), 4350),
                new Operation(OperationType.sell, new BigDecimal("30.00"), 650)
        );
        var givenTaxList = ProcessOrderUseCase.getInstance().calculate(operationList);

        Assertions.assertEquals(9, givenTaxList.size());
        this.assertEquality(expectedTaxList, givenTaxList);
    }

    @DisplayName("Case 8# - Should pay tax for sale operations 7, 8")
    @Test
    void testCase8PurchaseAndSalesWithTax() {
        var expectedTaxList = Arrays.asList(
                new Tax(ZERO_TAX),
                new Tax(new BigDecimal("80000.00")),
                new Tax(ZERO_TAX),
                new Tax(new BigDecimal("60000.00"))
        );
        var operationList = Arrays.asList(
                new Operation(OperationType.buy, new BigDecimal("10.00"), 10000),
                new Operation(OperationType.sell, new BigDecimal("50.00"), 10000),
                new Operation(OperationType.buy, new BigDecimal("20.00"), 10000),
                new Operation(OperationType.sell, new BigDecimal("50.00"), 10000)
        );
        var givenTaxList = ProcessOrderUseCase.getInstance().calculate(operationList);

        Assertions.assertEquals(4, givenTaxList.size());
        this.assertEquality(expectedTaxList, givenTaxList);
    }

    @DisplayName("Should return distinct taxes without any share any state")
    @Test
    void testMultipleOperations() {
        var case1OperationList = Arrays.asList(
                new Operation(OperationType.buy, new BigDecimal("10.00"), 100),
                new Operation(OperationType.sell, new BigDecimal("15.00"), 50),
                new Operation(OperationType.sell, new BigDecimal("15.00"), 50)
        );

        var case2OperationList = Arrays.asList(
                new Operation(OperationType.buy, new BigDecimal("10.00"), 10000),
                new Operation(OperationType.sell, new BigDecimal("20.00"), 5000),
                new Operation(OperationType.sell, new BigDecimal("5.00"), 5000)
        );

        var expectedCase1TaxList = Arrays.asList(new Tax(ZERO_TAX), new Tax(ZERO_TAX), new Tax(ZERO_TAX));
        var expectedCase2TaxList = Arrays.asList(new Tax(ZERO_TAX), new Tax(new BigDecimal("10000.00")), new Tax(ZERO_TAX));

        List<List<Operation>> multipleOperationList = Arrays.asList(case1OperationList, case2OperationList);
        List<List<Tax>> actualMultipleTaxResultList = TaxesController.calculateOperations(multipleOperationList);

        Assertions.assertNotNull(actualMultipleTaxResultList);
        Assertions.assertEquals(2, actualMultipleTaxResultList.size());

        var actualCase1TaxList = actualMultipleTaxResultList.get(0);
        this.assertEquality(expectedCase1TaxList, actualCase1TaxList);

        var actualCase2TaxList = actualMultipleTaxResultList.get(1);
        this.assertEquality(expectedCase2TaxList, actualCase2TaxList);
    }


}
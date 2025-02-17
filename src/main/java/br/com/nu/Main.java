package br.com.nu;

import br.com.nu.application.TaxesController;
import br.com.nu.domain.Operation;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        var multipleOperationList = readTheSystemInput();
        var multipleTaxesResult = TaxesController.calculateOperations(multipleOperationList);
        multipleTaxesResult.forEach(System.out::println);
    }

    private static List<Operation> convertJsonInput(String line) {
        try {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Operation>>() {
            }.getType();
            return gson.fromJson(line, type);
        } catch (Exception e) {
            System.err.println("An error occurred converting the json data. Error: " + e.getMessage());
        }
        return null;
    }

    private static List<List<Operation>> readTheSystemInput() {
        List<List<Operation>> multipleOperationsList = new LinkedList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) break;
                if (line.startsWith("[")) stringBuilder = new StringBuilder();
                stringBuilder.append(line.trim());
                if (line.endsWith("]")) {
                    List<Operation> operationList = convertJsonInput(stringBuilder.toString());
                    multipleOperationsList.add(operationList);
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao processar as simulações: " + e.getMessage());
        }
        return multipleOperationsList;
    }

}
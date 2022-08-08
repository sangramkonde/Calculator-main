package com.calculator.service;

import java.util.HashMap;
import java.util.Map;

import com.calculator.operation.Addition;
import com.calculator.operation.Division;
import com.calculator.operation.Multiplication;
import com.calculator.operation.Subtraction;


@SuppressWarnings("serial")
public class OperationFactory {

	private static Map<String, Operation> instances = null;

    static {
    	instances = new HashMap<String, Operation>(){{
    		put("+", new Addition());
            put("-", new Subtraction());
            put("*", new Multiplication());
            put("/", new Division());
        }};
    }

	public static void register(String operationMedium, Operation instance) {
		if (operationMedium != null && instance != null) {
			instances.put(operationMedium, instance);
		}
	}

	public static Operation getOperation(String operationMedium) {
		if (instances.containsKey(operationMedium)) {
			return instances.get(operationMedium);
		}
		return null;
	}
}
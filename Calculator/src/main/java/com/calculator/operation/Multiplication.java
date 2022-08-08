package com.calculator.operation;

import com.calculator.service.Operation;

public class Multiplication implements Operation {

	@Override
	public Double execute(Double a, Double b) {
		return a * b;
	}
}
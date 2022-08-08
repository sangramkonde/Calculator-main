package com.calculator.serviceImpl;

import java.text.DecimalFormat;
import java.util.ArrayDeque;
import java.util.Deque;

import com.calculator.service.Operation;
import com.calculator.service.OperationFactory;
import com.calculator.utility.ExpressionUtility;

public class CalculationImpl {
	
	public CalculationImpl() {}
	
	public static String solveExpression(String infixExpression) {
		DecimalFormat format = new DecimalFormat("0.#");
		infixExpression = ExpressionUtility.simplify(infixExpression);
		if (!infixExpression.equals("")) {
			// code handling
			if (infixExpression.startsWith("/")) {
				return responseCode(infixExpression);
			}
			String postFixExp = ExpressionUtility.convertToPostfix(infixExpression);
			String result = format.format(solvePostfix(postFixExp));
			return result;
		}
		return null;
	}
	
	private static Double solvePostfix(String postFixExp) {
		// To reset the stack
		Deque<Double> numberStack = new ArrayDeque<>();
		String[] postFixElemens = postFixExp.split("\\s");
		
		for(String elem: postFixElemens) {
			if(ExpressionUtility.isBigInteger(elem)) {
				numberStack.offerLast(new Double(elem));
			} else {
				Double num2 = numberStack.pollLast();
				Double num1 = numberStack.pollLast();
				// Get the operation instance
				Operation operation = OperationFactory.getOperation(elem);
				numberStack.offerLast(operation.execute(num1, num2));
			}
		}
		return numberStack.peekLast();
	}
	
	private static String responseCode(String code) {
		if(code.equals("/exit")) {
			return "Bye!";
		} else if(code.equals("/help")) {
			return "Program that calculates the arithmetic expression. Examples of valid expressions: \n" +
					"2 * (25+8)-1 \n" +
					"9 +++ 10 -- 8 \n" +
					"14  *   12 \n" +
					"available commands: \n" + 
					"/help: show this message\n" +
					"/exit: close the program \n";
		} else {
			return "Command: '" + code + "' not recognized by the system";
		}
	}
}
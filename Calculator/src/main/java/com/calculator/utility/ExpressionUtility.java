package com.calculator.utility;

import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.calculator.exception.InvalidExpressionException;

public class ExpressionUtility {

	private static Deque<String> operandStack;
	private static final String INVALID_EXPRESSION = "Invalid Expression";
	// Operator's priority map
	@SuppressWarnings("serial")
	private static final Map<String, Integer> PRECEDENCE_ORDER = new HashMap<String, Integer>() {
		{
			put("+", 1);
			put("-", 1);
			put("*", 2);
			put("/", 2);
			put("^", 3);
			put("(", 4);
		}
	};

	// Simplify expressions (remove spaces, repeated symbols, etc.)
	public static String simplify(String expression) {
		if (!expression.startsWith("/")) {

			return transformToValidExpression(expression);
		}
		return expression;
	}

	private static String transformToValidExpression(String expression) {

		// Checks if there are spaces without operations between numberss
		if (expression.replaceAll("\\w\\s+\\w", "").length() != expression.length()) {
		}

		// Remove the spaces
		expression = expression.replaceAll("\\s", "");

		// Substitutes number together with a parentheses/variable in a multiplication
		expression = expression.replaceAll("(\\d)([a-zA-Z(])", "$1*$2");
		expression = expression.replaceAll("([a-zA-Z])([(])", "$1*$2");

		int normalSize = expression.length();

		// Check for invalid characters
		if (expression.replaceAll("[^+\\-*/0-9A-z)(.]", "").length() != normalSize) {
			throw new InvalidExpressionException(INVALID_EXPRESSION);
		}

		// Checks if there are no asterisks or slashes
		if (expression.replaceAll("(\\*{2,})|(/{2,})", "").length() != normalSize) {
			throw new InvalidExpressionException(INVALID_EXPRESSION);
		}

		// Checks if the expression does not end with a symbol
		if (!expression.matches(".*[\\d)]$") && !expression.isEmpty()) {
			throw new InvalidExpressionException(INVALID_EXPRESSION);
		}

		// After all, simplify additions and subtractions as ++++, ---, ...
		return reduceArithmeticSymbols(expression);
	}

	private static String reduceArithmeticSymbols(String expression) {
		boolean isReduced = false;

		while (!isReduced) {
			String reducedExpression = expression.replaceAll("(\\++)|(--)", "+").replaceAll("(\\+-)|(-\\+)", "-");

			// Checks if the changes no longer take effect
			if (reducedExpression.equals(expression)) {
				isReduced = true;
			} else {
				expression = reducedExpression;
			}
		}
		return expression;
	}

	public static String convertToPostfix(String expression) {
		operandStack = new ArrayDeque<>();

		// Removes the blanks to avoid getting in the way (Consider that the operands
		// have already been checked)
		expression = expression.replaceAll("\\s", "");
		if (expression.startsWith("-")) {
			expression = expression.replaceFirst("-", "0-");
		} else if (expression.startsWith("+")) {
			expression = expression.replaceFirst("\\+", "");
		}

		String[] symbolArr = expression.split("(?<=\\d)(?=\\D)|(?<=\\D)(?=\\d)|(?<=\\D)(?=\\D)");

		// New string where the new postfix expression will be
		StringBuilder postfixExp = new StringBuilder();
		for (String symbol : symbolArr) {
			if (isBigInteger(symbol)) {
				postfixExp.append(symbol).append(" ");
			} else {
				postfixExp.append(addOperandToStackAndReturnPostFix(symbol));
			}
		}

		// Check if there are no parentheses left in the stack
		if (operandStack.contains("(") || operandStack.contains(")")) {
			throw new InvalidExpressionException(INVALID_EXPRESSION);
		}

		while (!operandStack.isEmpty()) {
			postfixExp.append(operandStack.pollLast()).append(" ");
		}
		return postfixExp.toString();
	}

	private static String addOperandToStackAndReturnPostFix(String operand) {
		StringBuilder postFixExp = new StringBuilder();
		String headSymbol = operandStack.peekLast();

		// Check the priority of each operand
		if ((operandStack.isEmpty() || headSymbol.equals("(")) && !operand.equals(")")) {
			operandStack.offerLast(operand);
		} else if (operand.equals(")")) {
			// Plays all stack operands to postfix until it finds the "("
			if (headSymbol == null) {
				throw new InvalidExpressionException(INVALID_EXPRESSION);
			}

			while (!headSymbol.equals("(")) {
				postFixExp.append(operandStack.pollLast()).append(" ");
				headSymbol = Optional.ofNullable(operandStack.peekLast())
						.orElseThrow(() -> new InvalidExpressionException(INVALID_EXPRESSION));
			}

			// When you find the parentheses, throw it away.
			operandStack.pollLast();

		} else {
			while (headSymbol != null && !headSymbol.equals("(")
					&& PRECEDENCE_ORDER.get(operand) <= PRECEDENCE_ORDER.get(headSymbol)) {
				// Remove all operands of greater or equal importance and send them to posfix
				// Only after finding a minor operand or a '(' that adds the symbol to the stack
				postFixExp.append(operandStack.pollLast()).append(" ");
				headSymbol = operandStack.peekLast();
			}
			operandStack.offerLast(operand);
		}

		return postFixExp.toString();
	}

	public static boolean isBigInteger(String symbol) {
		try {
			new BigInteger(symbol);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
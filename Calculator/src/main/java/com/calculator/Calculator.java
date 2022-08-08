package com.calculator;

import java.util.Scanner;
import com.calculator.exception.InvalidExpressionException;
import com.calculator.serviceImpl.CalculationImpl;

/**
 * Calculator Application
 *
 */
public class Calculator 
{
	public static void main(String[] args) {
		System.out.println("Calculator started. Type '/help' for more details");
		Scanner scanner = new Scanner(System.in);
		String ans = "";
		while (!ans.equals("Bye!")) {
			System.out.print("> ");
			String expression = scanner.nextLine();
			try {
				ans = CalculationImpl.solveExpression(expression);
				System.out.println(ans);

			} catch (InvalidExpressionException exc) {
				System.out.println(exc.getMessage());
			}
		}
		scanner.close();
	}
}
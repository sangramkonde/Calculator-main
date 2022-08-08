package com.calculator;

import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Test;

import com.calculator.serviceImpl.CalculationImpl;

/**
 * Unit test for Calculator Application.
 */
public class CalculatorTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }
    
    @Test
	public void testExression() {
    	
    	Assert.assertEquals("13", CalculationImpl.solveExpression("1 + (3 * 4)"));
	}
    
    @Test
	public void testAddition() {
    	
    	Assert.assertEquals("9", CalculationImpl.solveExpression("5+4"));
	}

	@Test
	public void testSubtraction() {
		
		Assert.assertEquals("0", CalculationImpl.solveExpression("2-2"));
	}

	@Test
	public void testDivision() {
		
		Assert.assertEquals("2", CalculationImpl.solveExpression("6/3"));
	}
	
	@Test
	public void testMultiplication() {
    	
    	Assert.assertEquals("100", CalculationImpl.solveExpression("20*5"));
	}

}

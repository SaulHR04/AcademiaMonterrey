package com.curso.v0;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

@DisplayName("Calculator Tests")
class CalculatorTest {

    private Calculator calculator;

    @BeforeEach                                         
    void setUp() {
        calculator = new Calculator();
    }

    @Test                                               
    @DisplayName("Multiplication of positive numbers")  
    void testMultiplyPositiveNumbers() {
        assertEquals(20, calculator.multiply(4, 5),     
                "4 * 5 should equal 20");               
    }

    @RepeatedTest(3)                                    
    @DisplayName("Multiplication with zero should return zero")
    void testMultiplyWithZero() {
        assertEquals(0, calculator.multiply(0, 5), "0 * 5 should equal 0");
        assertEquals(0, calculator.multiply(5, 0), "5 * 0 should equal 0");
    }

    @Test
    @DisplayName("Addition of positive numbers")
    void testAddPositiveNumbers() {
        assertEquals(9, calculator.add(4, 5), "4 + 5 should equal 9");
    }

    @Test
    @DisplayName("Division should work correctly")
    void testDivision() {
        assertEquals(2.5, calculator.divide(5.0, 2.0), 0.001, "5.0 / 2.0 should equal 2.5");
    }

    @Test
    @DisplayName("Division by zero should throw exception")
    void testDivisionByZero() {
        assertThrows(IllegalArgumentException.class,
            () -> calculator.divide(5.0, 0.0),
            "Division by zero should throw IllegalArgumentException");
    }
}
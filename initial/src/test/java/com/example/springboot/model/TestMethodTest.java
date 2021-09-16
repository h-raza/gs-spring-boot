package com.example.springboot.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestMethodTest {

    @Test
    public void quotient() {
        TestMethod tester=new TestMethod();
        assertEquals(2,tester.quotient(10,5));
    }
}
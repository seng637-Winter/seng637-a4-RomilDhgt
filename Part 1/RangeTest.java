package org.jfree.data;

import static org.junit.Assert.*;
import org.jfree.data.Range;
import org.junit.*;


public class RangeTest{   
    
	@Test
    public void invalidRangeThrowsException() {
        try {
        	// Create range where lower > upper
        	Range invalidRange = new Range(9, 6);
        } catch (IllegalArgumentException e) {
        	// Assert that the expected exception was thrown
            assertNotNull(e);
            return;
        }
        
        fail("IllegalArgumentException not thrown!");
    }
	
	@Test
    public void validEqualsTrue() {
    	Range exampleRange = new Range(4, 20);
    	assertTrue("The new range should be from 4 to 20", exampleRange.equals(new Range(4,20)));
    }
	
	@Test
    public void validEqualsFalse() {
    	Range exampleRange = new Range(4, 20);
    	assertFalse("The new range should NOT be from 1 to 10", exampleRange.equals(new Range(1,10)));
    }
	
	@Test
    public void invalidEqualsFalse() {
    	Range exampleRange = new Range(4, 20);
    	int notARange = 42;
    	assertFalse("The new range should NOT be from 1 to 10", exampleRange.equals(notARange));
    }
	
	@Test
    public void nullEqualsFalse() {
    	Range exampleRange = new Range(4, 20);
    	assertFalse("The new range should NOT be from 1 to 10", exampleRange.equals(null));
    }
	
	
	
    @Test
    public void combineNullRanges() {
    	Range nullRange = null;
    	Range combinedRange = Range.combine(nullRange, nullRange);
    	assertNull("The combined range of two nulls should be also null", combinedRange);
    }    
    
    @Test
    public void combineValidRanges() {
    	Range exampleRange1 = new Range(2, 4);
    	Range exampleRange2 = new Range(7, 10);
    	Range combinedRange = Range.combine(exampleRange1, exampleRange2);
    	assertTrue("The new range should be from 2 to 10", combinedRange.equals(new Range(2,10)));
    }  
    
    @Test
    public void combineFirstRangeNull() {
    	Range exampleRange1 = null;
    	Range exampleRange2 = new Range(7, 10);
    	Range combinedRange = Range.combine(exampleRange1, exampleRange2);
    	assertTrue("The new range is the second range", combinedRange.equals(exampleRange2));
    }  
    
    @Test
    public void combineSecondRangeNull() {
    	Range exampleRange1 = new Range(2, 4);
    	Range exampleRange2 = null;
    	Range combinedRange = Range.combine(exampleRange1, exampleRange2);
    	assertTrue("The new range is the first range", combinedRange.equals(exampleRange1));
    }  
    
    
    
    @Test 
    public void getUpperBoundShouldBeTwo() {
    	Range exampleRange = new Range(-2, 2);
    	assertEquals("The upper bound between -2 and 2 should be 2",
    			2, exampleRange.getUpperBound(), .000000001d);
    }
    
    @Test 
    public void getUpperBoundShouldBeDecimal() {
    	Range exampleRange = new Range(4.2, 6.9);
    	assertEquals("The upper bound should be 6.9",
    			6.9, exampleRange.getUpperBound(), .000000001d);
    }
    
    @Test 
    public void getUpperBoundOfNegatives() {
    	Range exampleRange = new Range(-6.9, -4.2);
    	assertEquals("The upper bound should be -4.2",
    			-4.2, exampleRange.getUpperBound(), .000000001d);
    }
    
    @Test 
    public void getLowerBoundShouldBeNegativeTwo() {
    	Range exampleRange = new Range(-2, 2);
    	assertEquals("The lower bound between -2 and 2 should be -2",
    			-2, exampleRange.getLowerBound(), .000000001d);
    }
    
    @Test 
    public void getLowerBoundShouldBeDecimal() {
    	Range exampleRange = new Range(4.2, 6.9);
    	assertEquals("The lower bound should be 4.2",
    			4.2, exampleRange.getLowerBound(), .000000001d);
    }
    
    @Test 
    public void getLowerBoundOfNegatives() {
    	Range exampleRange = new Range(-6.9, -4.2);
    	assertEquals("The lower bound should be -6.9",
    			-6.9, exampleRange.getLowerBound(), .000000001d);
    }
    
    @Test
    public void containsShouldBeTrue() {
    	Range exampleRange = new Range(-2, 2);
    	assertTrue("1 should be in between range -2 and 2", exampleRange.contains(1));
    }
    
    @Test
    public void containsShouldBeFalse() {
    	Range exampleRange = new Range(-2, 2);
    	assertFalse("20 should not be in between range -2 and 2", exampleRange.contains(20));
    }

    @Test
    public void testGetLength_PositiveRange() {
        Range range = new Range(1.0, 5.0);
        double expectedLength = 4.0; // because 5 - 1 = 4
        double actualLength = range.getLength();

        // Assert
        assertEquals("The length of the range should be 4.", expectedLength, actualLength, 0.0001);
    }
    
    @Test
    public void testGetLength_ZeroLength() {
        Range range = new Range(5.0, 5.0);
        double length = range.getLength();

        // Assert
        assertEquals("The length of a range where both bounds are the same should be 0.", 0.0, length, 0.0001);
    }

    @Test
    public void testGetLength_NegativeRange() {
        Range range = new Range(-5.0, -1.0);
        double length = range.getLength();

        // Assert
        assertEquals("The length of a negative range should be calculated as the absolute difference between the bounds.", 4.0, length, 0.0001);
    }

    
    @Test
    public void testConstrain_ValueWithinRange() {
        Range range = new Range(1.0, 5.0);
        double valueToConstrain = 3.0;
        double constrainedValue = range.constrain(valueToConstrain);

        // Assert
        assertEquals("The constrain method should return the original value when it is within the range.",
                     valueToConstrain, constrainedValue, 0.0001);
    }
    
    @Test
    public void testConstrain_ValueBelowRange() {
        Range range = new Range(1.0, 5.0);
        double valueToConstrain = -9.0;
        double constrainedValue = range.constrain(valueToConstrain);

        // Assert
        assertEquals("The constrain method should return the lower bound when the value is below the range.",
                     1.0, constrainedValue, 0.0001);
    }

    @Test
    public void testConstrain_ValueAboveRange() {
        Range range = new Range(1.0, 5.0);
        double valueToConstrain = 6.0;
        double constrainedValue = range.constrain(valueToConstrain);

        // Assert
        assertEquals("The constrain method should return the upper bound when the value is above the range.",
                     5.0, constrainedValue, 0.0001);
    }

}

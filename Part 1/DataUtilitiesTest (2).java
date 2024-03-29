package org.jfree.data.test;
import static org.junit.Assert.*;
import java.security.InvalidParameterException;
import java.util.Arrays;

import org.jfree.data.KeyedValues;
import org.jfree.data.Values2D;
import org.jfree.data.DataUtilities;
import org.jfree.data.DefaultKeyedValues;
import org.junit.*;
import org.jmock.*;
import org.jmock.Expectations;

public class DataUtilitiesTest extends DataUtilities {
	//test is the cumulative Percentages are calculated the same way as the java docs
    private Mockery mockingContext;
    private Values2D values;

    @Before
    public void setUp() {
        mockingContext = new Mockery();
        values = mockingContext.mock(Values2D.class);
    }
    
    @Test
    public void calculateColumnTotalWithNegativeValues() {
        mockingContext.checking(new Expectations() {{
            oneOf(values).getRowCount();
            will(returnValue(2)); // Corrected to return 2 instead of -2
            oneOf(values).getValue(0, 0);
            will(returnValue(-5.0)); // Ensure you're returning a double
            oneOf(values).getValue(1, 0);
            will(returnValue(-2.0)); // Ensure you're returning a double
        }});

        double result = DataUtilities.calculateColumnTotal(values, 0);

        assertEquals(-7.0, result, .000000001d); // The expected result should be the sum of -5.0 and -2.0
    }
	
	//Checking test case with all positive values for calculateColumnTotal
	@Test
	public void calculateColumnTotalWithPositiveValues() {

	    mockingContext.checking(new Expectations() {{
	    	oneOf(values).getRowCount(); will(returnValue(2));
	        oneOf(values).getValue(0, 0); will(returnValue(3));
	        oneOf(values).getValue(1, 0); will(returnValue(8));
	    }});

	    double result = DataUtilities.calculateColumnTotal(values, 0);

	    assertEquals(11.0, result, 0.000000001d);
	}
	
	//Checking test case with all zero values for calculateColumnTotal
	@Test
	public void calculateColumnTotalWithAllZeroValues() {
	    mockingContext.checking(new Expectations() {{
	        oneOf(values).getRowCount(); will(returnValue(3));
	        oneOf(values).getValue(0, 0); will(returnValue(0));
	        oneOf(values).getValue(1, 0); will(returnValue(0));
	        oneOf(values).getValue(2, 0); will(returnValue(0));
	    }});

	    double result = DataUtilities.calculateColumnTotal(values, 0);

	    assertEquals(0.0, result, 0.000000001d);
	}

	//Checking test case with mixed values for calculateColumnTotal
	@Test
	public void calculateColumnTotalWithMixedValues() {
	    mockingContext.checking(new Expectations() {{
	        oneOf(values).getRowCount(); will(returnValue(3));
	        oneOf(values).getValue(0, 0); will(returnValue(-5));
	        oneOf(values).getValue(1, 0); will(returnValue(0));
	        oneOf(values).getValue(2, 0); will(returnValue(10));
	    }});

	    double result = DataUtilities.calculateColumnTotal(values, 0);

	    assertEquals(5.0, result, 0.000000001d);
	}
	
	//Checking test case with null values for calculateColumnTotal
	@Test
	public void calculateColumnTotalWithNullValues() {
	    mockingContext.checking(new Expectations() {{
	        oneOf(values).getRowCount(); will(returnValue(3));
	        oneOf(values).getValue(0, 0); will(returnValue(null));
	        oneOf(values).getValue(1, 0); will(returnValue(10));
	        oneOf(values).getValue(2, 0); will(returnValue(null));
	    }});

	    double result = DataUtilities.calculateColumnTotal(values, 0);

	    assertEquals(10.0, result, 0.000000001d);
	}
	
	//Checking test case with no values for calculateColumnTotal
	@Test
	public void calculateColumnTotalWithEmptyData() {
	    mockingContext.checking(new Expectations() {{
	        oneOf(values).getRowCount(); will(returnValue(0));
	    }});

	    double result = DataUtilities.calculateColumnTotal(values, 0);

	    assertEquals(0.0, result, 0.000000001d);
	}

	@Test
	public void createNumberArraywithEmptyArray() {
	    double[] data = {};
	    Number[] expected = {};
	    Number[] result = DataUtilities.createNumberArray(data);
	    assertArrayEquals("The returned Number array should be empty.", expected, result);
	    
	}
	
	@Test
	public void createNumberArrayWithSingleElement() {
	    double[] data = {5.0};
	    Number[] expected = {5.0};
	    Number[] result = DataUtilities.createNumberArray(data);
	    
	    assertArrayEquals("The returned Number array should correctly represent a single-element array.", expected, result);
	}
	
	@Test
	public void createNumberArrayWithPositiveValues() {
	    double[] data = {10.0, 20.0, 30.0};
	    Number[] expected = {10.0, 20.0, 30.0};
	    Number[] result = DataUtilities.createNumberArray(data);
	    assertArrayEquals("The returned Number array should correctly represent an array with positive values.", expected, result);
	}

	@Test
	public void createNumberArrayWithNegativeValues() {
	    double[] data = {-1.0, -2.0, -3.0};
	    Number[] expected = {-1.0, -2.0, -3.0};
	    Number[] result = DataUtilities.createNumberArray(data);
	    assertArrayEquals("The returned Number array should correctly represent an array with negative values.", expected, result);
	}

	@Test
	public void createNumberArrayWithZero() {
	    double[] data = {0.0};
	    Number[] expected = {0.0};
	    Number[] result = DataUtilities.createNumberArray(data);
	    assertArrayEquals("The returned Number array should correctly represent an array with a zero value.", expected, result);
	}

	@Test
	public void createNumberArrayWithMixedValues() {
	    double[] data = {-1.0, 0.0, 1.0};
	    Number[] expected = {-1.0, 0.0, 1.0};
	    Number[] result = DataUtilities.createNumberArray(data);
	    assertArrayEquals("The returned Number array should correctly represent an array with mixed values.", expected, result);
	}

	@Test
	public void createNumberArrayWithLargeValues() {
	    double[] data = {Double.MAX_VALUE, -Double.MAX_VALUE};
	    Number[] expected = {Double.MAX_VALUE, -Double.MAX_VALUE};
	    Number[] result = DataUtilities.createNumberArray(data);
	    assertArrayEquals("The returned Number array should correctly represent an array with large values.", expected, result);
	}
	
	@Test
	public void createNumberArrayLengthTest() {

	    double[] data = {1.0, 2.0, 3.0, 4.5, 5.5};

	    Number[] result = DataUtilities.createNumberArray(data);

	    assertEquals("Array lengths should be equal", data.length, result.length);
	}


    @Test
    public void CumulativePercentagesNegValue() {
    	//setup
        Mockery mockingContext = new Mockery();
        final KeyedValues keyedValues = mockingContext.mock(KeyedValues.class);
        
        //add the values
        mockingContext.checking(new Expectations() {{
            allowing(keyedValues).getItemCount(); will(returnValue(3));
            allowing(keyedValues).getKey(0); will(returnValue("0")); 
            allowing(keyedValues).getKey(1); will(returnValue("1"));
            allowing(keyedValues).getKey(2); will(returnValue("2"));
            allowing(keyedValues).getValue(0); will(returnValue(-1.0)); 
            allowing(keyedValues).getValue(1); will(returnValue(-2.0));
            allowing(keyedValues).getValue(2); will(returnValue(-3.0));
        }});
        
        // Define the expected results for comparison
        final double[] expectedCumulativePercentage = {0.16667, 0.5, 1.0};

        // exercise
        KeyedValues result = DataUtilities.getCumulativePercentages(keyedValues);

        // verify
        for (int i = 0; i < result.getItemCount(); i++) {
            assertEquals("The cumulative percentage method should return the percentage of the cumulative sum against the total sum of values", expectedCumulativePercentage[i], result.getValue(i).doubleValue(), 0.0001);
        }
        
        //teardown
        mockingContext.assertIsSatisfied();
    }
	
    @Test
    public void CumulativePercentagesThreeValues() {
    	//setup
        Mockery mockingContext = new Mockery();
        final KeyedValues keyedValues = mockingContext.mock(KeyedValues.class);
        
        //add the values
        mockingContext.checking(new Expectations() {{
            allowing(keyedValues).getItemCount(); will(returnValue(3));
            allowing(keyedValues).getKey(0); will(returnValue("0")); 
            allowing(keyedValues).getKey(1); will(returnValue("1"));
            allowing(keyedValues).getKey(2); will(returnValue("2"));
            allowing(keyedValues).getValue(0); will(returnValue(5.0)); 
            allowing(keyedValues).getValue(1); will(returnValue(9.0));
            allowing(keyedValues).getValue(2); will(returnValue(2.0));
        }});
        
        // Define the expected results for comparison
        final double[] expectedCumulativePercentage = {0.3125, 0.875, 1.0};

        // exercise
        KeyedValues result = DataUtilities.getCumulativePercentages(keyedValues);

        // verify
        for (int i = 0; i < result.getItemCount(); i++) {
            assertEquals("The cumulative percentage method should return the percentage of the cumulative sum against the total sum of values", expectedCumulativePercentage[i], result.getValue(i).doubleValue(), 0.0001);
        }
        
        //teardown
        mockingContext.assertIsSatisfied();
    }
    
    @Test
    public void CumulativePercentagesInvalidData() {
        
        try {
            KeyedValues result = DataUtilities.getCumulativePercentages(null);
            fail("Expected IllegalArgumentException to be thrown, but it wasn't.");

        } catch (IllegalArgumentException e) {

        	assertNotNull(e.getMessage());
        }

    }

    
    @Test
    public void CumulativePercentagesMaxInt() {
        Mockery mockingContext = new Mockery();
        final KeyedValues keyedValues = mockingContext.mock(KeyedValues.class);

        // Add the values
        mockingContext.checking(new Expectations() {{
            allowing(keyedValues).getItemCount(); will(returnValue(2));
            allowing(keyedValues).getKey(0); will(returnValue("0")); 
            allowing(keyedValues).getKey(1); will(returnValue("1"));
            allowing(keyedValues).getValue(0); will(returnValue((double) Integer.MAX_VALUE - 1)); 
            allowing(keyedValues).getValue(1); will(returnValue(1.0));
        }});

        // Exercise
        KeyedValues result = DataUtilities.getCumulativePercentages(keyedValues);

        // Verify
        // The first value should be very close to 100% as it dominates the total sum
        assertEquals("The cumulative percentage of the first value should be close to 100%", 
                     1.0, result.getValue(0).doubleValue(), 0.0001);
        // The second value should bring the total to exactly 100%
        assertEquals("The cumulative percentage of the second value should bring the total to 100%", 
                     1.0, result.getValue(1).doubleValue(), 0.0001);

        // Teardown
        mockingContext.assertIsSatisfied();
    }
    
    @Test
    public void CumulativePercentagesNoValues() {
    	//setup
        Mockery mockingContext = new Mockery();
        final KeyedValues keyedValues = mockingContext.mock(KeyedValues.class);
        
        //add the values
        mockingContext.checking(new Expectations() {{
            allowing(keyedValues).getItemCount(); will(returnValue(0));
        }});
        
        // Define the expected results for comparison
        final double[] expectedCumulativePercentage = {};

        // exercise
        KeyedValues result = DataUtilities.getCumulativePercentages(keyedValues);

        // verify
        for (int i = 0; i < result.getItemCount(); i++) {
            assertEquals("The cumulative percentage method should return the percentage of the cumulative sum against the total sum of values", expectedCumulativePercentage[i], result.getValue(i).doubleValue(), 0.0001);
        }
        
        //teardown
        mockingContext.assertIsSatisfied();
    }
    
	
    @Test
    public void calculateRowTotalForTwoPosValues() {
        // setup
        Mockery mockingContext = new Mockery();
        final Values2D values = mockingContext.mock(Values2D.class);

        mockingContext.checking(new Expectations() {{
            oneOf(values).getColumnCount();
            will(returnValue(2));
            oneOf(values).getValue(0, 0);
            will(returnValue(7.5));
            oneOf(values).getValue(0, 1);
            will(returnValue(2.5));
        }});
        
        double result = DataUtilities.calculateRowTotal(values, 0);
        System.out.println(result);

        // verify
        assertEquals(10.0, result, .000000001d);
    }
    
    @Test
    public void calculateRowTotalForTwoNegValues() {
        // setup
        Mockery mockingContext = new Mockery();
        final Values2D values = mockingContext.mock(Values2D.class);

        mockingContext.checking(new Expectations() {{
            oneOf(values).getColumnCount();
            will(returnValue(2));
            oneOf(values).getValue(0, 0);
            will(returnValue(-7.5));
            oneOf(values).getValue(0, 1);
            will(returnValue(-2.5));
        }});
        
        double result = DataUtilities.calculateRowTotal(values, 0);
        System.out.println(result);

        // verify
        assertEquals(-10.0, result, .000000001d);
    }
    
    @Test
    public void calculateRowTotalOneValue() {
        // setup
        Mockery mockingContext = new Mockery();
        final Values2D values = mockingContext.mock(Values2D.class);

        mockingContext.checking(new Expectations() {{
            oneOf(values).getColumnCount();
            will(returnValue(1));
            oneOf(values).getValue(0, 0);
            will(returnValue(7.5));
        }});
        
        double result = DataUtilities.calculateRowTotal(values, 0);
        System.out.println(result);

        // verify
        assertEquals(7.5, result, .000000001d);
    }
    
    
    @Test
    public void CreateNumberArray2D_validOutput() {
	// This test is to check if the output of matches a valid input
	// This test fails because createNumberArray2D is returning null on the last element of each of the inner arrays
	double[][] input = {{0,-1,2},{3,4,-5}};
	Number[][] result = createNumberArray2D(input);
	assertNotNull(result);
	for (int i = 0; i < result.length; i++) {
		for (int j = 0; j < result[i].length; j++) {
			assertEquals(input[i][j], result[i][j]);
		}
	}
    }

	
	@Test
    public void CreateNumberArray2D_outputDiffSizes() {
	double[][] input = {{1,2,3},{4,5}};
	Number[][] result = createNumberArray2D(input);		
	assertNotNull(result);
	for (int i = 0; i < result.length; i++) {
		assertEquals(input[i].length, result[i].length);
	}
	}
	
	@Test
    public void CreateNumberArray2D_validOutputLength() {
	// This test is to check if the output length of the arrays in the input matches the output
	double[][] input = {{0,1,2},{3,4,5}};
	Number[][] result = createNumberArray2D(input);
	assertNotNull(result);
	for (int i = 0; i < result.length; i++) {
		assertEquals(input[i].length, result[i].length);
	}
    }
	
	@Test
    public void CreateNumberArray2D_validOutputType() {
	// This test is to check if the output of createNumberArray2D is an array of Number objects  
	double[][] input = {{0,1,2},{3,4,5}};
	Number[][] result = createNumberArray2D(input);
	assertNotNull(result);
	for (Number[] row : result) {
		for (Number num: row) {
			assertTrue(num instanceof Number);
		}
	}
    }
	
	
	
	
	@Test
	public void testEqual_IdenticalArrays() {
	    double[][] a = {{1.0, Double.NaN}, {Double.POSITIVE_INFINITY, -1.0}};
	    double[][] b = {{1.0, Double.NaN}, {Double.POSITIVE_INFINITY, -1.0}};
	    assertTrue(DataUtilities.equal(a, b));
	}
	
	@Test
	public void testEqual_DifferentArrays() {
	    double[][] a = {{1.0, 2.0}, {3.0, 4.0}};
	    double[][] b = {{1.0, 2.0}, {3.0}};
	    assertFalse(DataUtilities.equal(a, b));
	}
	
	@Test
	public void testEqual_NullHandling() {
	    double[][] a = null;
	    double[][] b = {{1.0, 2.0}, {3.0, 4.0}};
	    assertFalse(DataUtilities.equal(a, b));
	}

	@Test
	public void calculateColumnTotalForPositiveValues() {
	    Mockery mockingContext = new Mockery();
	    final Values2D values = mockingContext.mock(Values2D.class);

	    mockingContext.checking(new Expectations() {{
	        oneOf(values).getRowCount(); will(returnValue(2));
	        oneOf(values).getValue(0, 0); will(returnValue(5.0));
	        oneOf(values).getValue(1, 0); will(returnValue(10.0));
	    }});

	    double result = DataUtilities.calculateColumnTotal(values, 0);
	    assertEquals("Total should be the sum of positive values", 15.0, result, 0.000000001d);
	}

	@Test
	public void calculateColumnTotalHandlingNulls() {
	    Mockery mockingContext = new Mockery();
	    final Values2D values = mockingContext.mock(Values2D.class);

	    mockingContext.checking(new Expectations() {{
	        oneOf(values).getRowCount(); will(returnValue(3));
	        oneOf(values).getValue(0, 0); will(returnValue(5.0));
	        oneOf(values).getValue(1, 0); will(returnValue(null));
	        oneOf(values).getValue(2, 0); will(returnValue(10.0));
	    }});

	    double result = DataUtilities.calculateColumnTotal(values, 0);
	    assertEquals("Total should ignore nulls and sum up the rest", 15.0, result, 0.000000001d);
	}

	@Test
	public void calculateColumnTotalForNegativeValues() {
	    Mockery mockingContext = new Mockery();
	    final Values2D values = mockingContext.mock(Values2D.class);

	    mockingContext.checking(new Expectations() {{
	        oneOf(values).getRowCount(); will(returnValue(2));
	        oneOf(values).getValue(0, 0); will(returnValue(-5.0));
	        oneOf(values).getValue(1, 0); will(returnValue(-10.0));
	    }});

	    double result = DataUtilities.calculateColumnTotal(values, 0);
	    assertEquals("Total should be the sum of negative values", -15.0, result, 0.000000001d);
	}

	@Test
	public void testClone_NonEmptyArray() {
	    double[][] source = {
	        {1.0, 2.0, 3.0},
	        {4.0, 5.0, 6.0}
	    };
	    double[][] clonedArray = DataUtilities.clone(source);
	    assertNotSame("The cloned array should not be the same instance as the source", source, clonedArray);
	    assertTrue("The cloned array should be equal to the source array", Arrays.deepEquals(source, clonedArray));
	}

	@Test
	public void testClone_ArrayWithNullEntries() {
	    double[][] source = {
	        {1.0, 2.0},
	        null,
	        {3.0, 4.0}
	    };
	    double[][] clonedArray = DataUtilities.clone(source);
	    assertNotSame("The cloned array should not be the same instance as the source", source, clonedArray);
	    assertTrue("The cloned array should handle null rows correctly", Arrays.deepEquals(source, clonedArray));
	}

	@Test
	public void testClone_EmptyArray() {
	    double[][] source = new double[0][];
	    double[][] clonedArray = DataUtilities.clone(source);
	    assertNotSame("The cloned array should not be the same instance as the source", source, clonedArray);
	    assertEquals("The cloned array should be empty", 0, clonedArray.length);
	}

    
    
    
    
}
package org.grampus;

import org.grampus.core.GTester;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;
import org.opentest4j.MultipleFailuresError;

import java.util.Collection;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class GAssert {

    public static void assertTrue(boolean condition) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertTrue(condition));
    }

    public static void assertTrue(boolean condition, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertTrue(condition, messageSupplier));
    }

    public static void assertTrue(BooleanSupplier booleanSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertTrue(booleanSupplier));
    }

    public static void assertTrue(BooleanSupplier booleanSupplier, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertTrue(booleanSupplier, message));
    }

    public static void assertTrue(boolean condition, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertTrue(condition, message));
    }

    public static void assertTrue(BooleanSupplier booleanSupplier, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertTrue(booleanSupplier, messageSupplier));
    }

    public static void assertFalse(boolean condition) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertFalse(condition));
    }

    public static void assertFalse(boolean condition, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertFalse(condition, message));
    }

    public static void assertFalse(boolean condition, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertFalse(condition, messageSupplier));
    }

    public static void assertFalse(BooleanSupplier booleanSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertFalse(booleanSupplier));
    }

    public static void assertFalse(BooleanSupplier booleanSupplier, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertFalse(booleanSupplier, message));
    }

    public static void assertFalse(BooleanSupplier booleanSupplier, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertFalse(booleanSupplier, messageSupplier));
    }

    public static void assertNull(Object actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNull(actual));
    }

    public static void assertNull(Object actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNull(actual, message));
    }

    public static void assertNull(Object actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNull(actual, messageSupplier));
    }

    public static void assertNotNull(Object actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotNull(actual));
    }

    public static void assertNotNull(Object actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotNull(actual, message));
    }

    public static void assertNotNull(Object actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotNull(actual, messageSupplier));
    }

    public static void assertEquals(short expected, short actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual));
    }

    public static void assertEquals(short expected, Short actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual));
    }

    public static void assertEquals(Short expected, short actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual));
    }

    
    public static void assertEquals(Short expected, Short actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual));
    }

    public static void assertEquals(short expected, short actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, message));
    }

    public static void assertEquals(short expected, Short actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, message));
    }

    public static void assertEquals(Short expected, short actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, message));
    }

    
    public static void assertEquals(Short expected, Short actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, message));
    }

    public static void assertEquals(short expected, short actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, messageSupplier));
    }

    public static void assertEquals(short expected, Short actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, messageSupplier));
    }

    public static void assertEquals(Short expected, short actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, messageSupplier));
    }

    
    public static void assertEquals(Short expected, Short actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, messageSupplier));
    }

    public static void assertEquals(byte expected, byte actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual));
    }

    public static void assertEquals(byte expected, Byte actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual));
    }

    public static void assertEquals(Byte expected, byte actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual));
    }

    
    public static void assertEquals(Byte expected, Byte actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual));
    }

    public static void assertEquals(byte expected, byte actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, message));
    }

    public static void assertEquals(byte expected, Byte actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, message));
    }

    public static void assertEquals(Byte expected, byte actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, message));
    }

    
    public static void assertEquals(Byte expected, Byte actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, message));
    }

    public static void assertEquals(byte expected, byte actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, messageSupplier));
    }

    public static void assertEquals(byte expected, Byte actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, messageSupplier));
    }

    public static void assertEquals(Byte expected, byte actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, messageSupplier));
    }

    
    public static void assertEquals(Byte expected, Byte actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, messageSupplier));
    }

    public static void assertEquals(int expected, int actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual));
    }

    public static void assertEquals(int expected, Integer actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual));
    }

    public static void assertEquals(Integer expected, int actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual));
    }

    
    public static void assertEquals(Integer expected, Integer actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual));
    }

    public static void assertEquals(int expected, int actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, message));
    }

    public static void assertEquals(int expected, Integer actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, message));
    }

    public static void assertEquals(Integer expected, int actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, message));
    }

    
    public static void assertEquals(Integer expected, Integer actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, message));
    }

    public static void assertEquals(int expected, int actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, messageSupplier));
    }

    public static void assertEquals(int expected, Integer actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, messageSupplier));
    }

    public static void assertEquals(Integer expected, int actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, messageSupplier));
    }

    
    public static void assertEquals(Integer expected, Integer actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, messageSupplier));
    }

    public static void assertEquals(long expected, long actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual));
    }

    public static void assertEquals(long expected, Long actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual));
    }

    public static void assertEquals(Long expected, long actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual));
    }

    
    public static void assertEquals(Long expected, Long actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual));
    }

    public static void assertEquals(long expected, long actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, message));
    }

    public static void assertEquals(long expected, Long actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, message));
    }

    public static void assertEquals(Long expected, long actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, message));
    }

    
    public static void assertEquals(Long expected, Long actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, message));
    }

    public static void assertEquals(long expected, long actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, messageSupplier));
    }

    public static void assertEquals(long expected, Long actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, messageSupplier));
    }

    public static void assertEquals(Long expected, long actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, messageSupplier));
    }

    
    public static void assertEquals(Long expected, Long actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, messageSupplier));
    }

    public static void assertEquals(float expected, float actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual));
    }

    public static void assertEquals(float expected, Float actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual));
    }

    public static void assertEquals(Float expected, float actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual));
    }

    
    public static void assertEquals(Float expected, Float actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual));
    }

    public static void assertEquals(float expected, float actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, message));
    }

    public static void assertEquals(float expected, Float actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, message));
    }

    public static void assertEquals(Float expected, float actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, message));
    }

    
    public static void assertEquals(Float expected, Float actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, message));
    }

    public static void assertEquals(float expected, float actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, messageSupplier));
    }

    public static void assertEquals(float expected, Float actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, messageSupplier));
    }

    public static void assertEquals(Float expected, float actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, messageSupplier));
    }

    
    public static void assertEquals(Float expected, Float actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, messageSupplier));
    }

    public static void assertEquals(float expected, float actual, float delta) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, delta));
    }

    public static void assertEquals(float expected, float actual, float delta, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, delta, message));
    }

    public static void assertEquals(float expected, float actual, float delta, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, delta, messageSupplier));
    }

    public static void assertEquals(double expected, double actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual));
    }

    public static void assertEquals(double expected, Double actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual));
    }

    public static void assertEquals(Double expected, double actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual));
    }

    
    public static void assertEquals(Double expected, Double actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual));
    }

    public static void assertEquals(double expected, double actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, message));
    }

    public static void assertEquals(double expected, Double actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, message));
    }

    public static void assertEquals(Double expected, double actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, message));
    }

    
    public static void assertEquals(Double expected, Double actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, message));
    }

    public static void assertEquals(double expected, double actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, messageSupplier));
    }

    public static void assertEquals(double expected, Double actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, messageSupplier));
    }

    public static void assertEquals(Double expected, double actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, messageSupplier));
    }

    
    public static void assertEquals(Double expected, Double actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, messageSupplier));
    }

    public static void assertEquals(double expected, double actual, double delta) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, delta));
    }

    public static void assertEquals(double expected, double actual, double delta, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, delta, message));
    }

    public static void assertEquals(double expected, double actual, double delta, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, delta, messageSupplier));
    }

    public static void assertEquals(char expected, char actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual));
    }

    public static void assertEquals(char expected, Character actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual));
    }

    public static void assertEquals(Character expected, char actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual));
    }

    
    public static void assertEquals(Character expected, Character actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual));
    }

    public static void assertEquals(char expected, char actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, message));
    }

    public static void assertEquals(char expected, Character actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, message));
    }

    public static void assertEquals(Character expected, char actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, message));
    }

    
    public static void assertEquals(Character expected, Character actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, message));
    }

    public static void assertEquals(char expected, char actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, messageSupplier));
    }

    public static void assertEquals(char expected, Character actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, messageSupplier));
    }

    public static void assertEquals(Character expected, char actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, messageSupplier));
    }

    
    public static void assertEquals(Character expected, Character actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, messageSupplier));
    }

    public static void assertEquals(Object expected, Object actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual));
    }

    public static void assertEquals(Object expected, Object actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, message));
    }

    public static void assertEquals(Object expected, Object actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertEquals(expected, actual, messageSupplier));
    }

    public static void assertArrayEquals(boolean[] expected, boolean[] actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertArrayEquals(expected, actual));
    }

    public static void assertArrayEquals(boolean[] expected, boolean[] actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertArrayEquals(expected, actual, message));
    }

    public static void assertArrayEquals(boolean[] expected, boolean[] actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertArrayEquals(expected, actual, messageSupplier));
    }

    public static void assertArrayEquals(char[] expected, char[] actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertArrayEquals(expected, actual));
    }

    public static void assertArrayEquals(char[] expected, char[] actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertArrayEquals(expected, actual, message));
    }

    public static void assertArrayEquals(char[] expected, char[] actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertArrayEquals(expected, actual, messageSupplier));
    }

    public static void assertArrayEquals(byte[] expected, byte[] actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertArrayEquals(expected, actual));
    }

    public static void assertArrayEquals(byte[] expected, byte[] actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertArrayEquals(expected, actual, message));
    }

    public static void assertArrayEquals(byte[] expected, byte[] actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertArrayEquals(expected, actual, messageSupplier));
    }

    public static void assertArrayEquals(short[] expected, short[] actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertArrayEquals(expected, actual));
    }

    public static void assertArrayEquals(short[] expected, short[] actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertArrayEquals(expected, actual, message));
    }

    public static void assertArrayEquals(short[] expected, short[] actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertArrayEquals(expected, actual, messageSupplier));
    }

    public static void assertArrayEquals(int[] expected, int[] actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertArrayEquals(expected, actual));
    }

    public static void assertArrayEquals(int[] expected, int[] actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertArrayEquals(expected, actual, message));
    }

    public static void assertArrayEquals(int[] expected, int[] actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertArrayEquals(expected, actual, messageSupplier));
    }

    public static void assertArrayEquals(long[] expected, long[] actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertArrayEquals(expected, actual));
    }

    public static void assertArrayEquals(long[] expected, long[] actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertArrayEquals(expected, actual, message));
    }

    public static void assertArrayEquals(long[] expected, long[] actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertArrayEquals(expected, actual, messageSupplier));
    }

    public static void assertArrayEquals(float[] expected, float[] actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertArrayEquals(expected, actual));
    }

    public static void assertArrayEquals(float[] expected, float[] actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertArrayEquals(expected, actual, message));
    }

    public static void assertArrayEquals(float[] expected, float[] actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertArrayEquals(expected, actual, messageSupplier));
    }

    public static void assertArrayEquals(float[] expected, float[] actual, float delta) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertArrayEquals(expected, actual, delta));
    }

    public static void assertArrayEquals(float[] expected, float[] actual, float delta, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertArrayEquals(expected, actual, delta, message));
    }

    public static void assertArrayEquals(float[] expected, float[] actual, float delta, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertArrayEquals(expected, actual, delta, messageSupplier));
    }

    public static void assertArrayEquals(double[] expected, double[] actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertArrayEquals(expected, actual));
    }

    public static void assertArrayEquals(double[] expected, double[] actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertArrayEquals(expected, actual, message));
    }

    public static void assertArrayEquals(double[] expected, double[] actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertArrayEquals(expected, actual, messageSupplier));
    }

    public static void assertArrayEquals(double[] expected, double[] actual, double delta) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertArrayEquals(expected, actual, delta));
    }

    public static void assertArrayEquals(double[] expected, double[] actual, double delta, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertArrayEquals(expected, actual, delta, message));
    }

    public static void assertArrayEquals(double[] expected, double[] actual, double delta, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertArrayEquals(expected, actual, delta, messageSupplier));
    }

    public static void assertArrayEquals(Object[] expected, Object[] actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertArrayEquals(expected, actual));
    }

    public static void assertArrayEquals(Object[] expected, Object[] actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertArrayEquals(expected, actual, message));
    }

    public static void assertArrayEquals(Object[] expected, Object[] actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertArrayEquals(expected, actual, messageSupplier));
    }

    public static void assertIterableEquals(Iterable<?> expected, Iterable<?> actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertIterableEquals(expected, actual));
    }

    public static void assertIterableEquals(Iterable<?> expected, Iterable<?> actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertIterableEquals(expected, actual, message));
    }

    public static void assertIterableEquals(Iterable<?> expected, Iterable<?> actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertIterableEquals(expected, actual, messageSupplier));
    }

    public static void assertLinesMatch(List<String> expectedLines, List<String> actualLines) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertLinesMatch(expectedLines, actualLines));
    }

    public static void assertLinesMatch(List<String> expectedLines, List<String> actualLines, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertLinesMatch(expectedLines, actualLines, message));
    }

    public static void assertLinesMatch(List<String> expectedLines, List<String> actualLines, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertLinesMatch(expectedLines, actualLines, messageSupplier));
    }

    public static void assertLinesMatch(Stream<String> expectedLines, Stream<String> actualLines) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertLinesMatch(expectedLines, actualLines));
    }

    public static void assertLinesMatch(Stream<String> expectedLines, Stream<String> actualLines, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertLinesMatch(expectedLines, actualLines, message));
    }

    public static void assertLinesMatch(Stream<String> expectedLines, Stream<String> actualLines, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertLinesMatch(expectedLines, actualLines, messageSupplier));
    }

    
    public static void assertNotEquals(byte unexpected, byte actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual));
    }

    
    public static void assertNotEquals(byte unexpected, Byte actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual));
    }

    
    public static void assertNotEquals(Byte unexpected, byte actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual));
    }

    
    public static void assertNotEquals(Byte unexpected, Byte actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual));
    }

    
    public static void assertNotEquals(byte unexpected, byte actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, message));
    }

    
    public static void assertNotEquals(byte unexpected, Byte actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, message));
    }

    
    public static void assertNotEquals(Byte unexpected, byte actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, message));
    }

    
    public static void assertNotEquals(Byte unexpected, Byte actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, message));
    }

    
    public static void assertNotEquals(byte unexpected, byte actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, messageSupplier));
    }

    
    public static void assertNotEquals(byte unexpected, Byte actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, messageSupplier));
    }

    
    public static void assertNotEquals(Byte unexpected, byte actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, messageSupplier));
    }

    
    public static void assertNotEquals(Byte unexpected, Byte actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, messageSupplier));
    }

    
    public static void assertNotEquals(short unexpected, short actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual));
    }

    
    public static void assertNotEquals(short unexpected, Short actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual));
    }

    
    public static void assertNotEquals(Short unexpected, short actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual));
    }

    
    public static void assertNotEquals(Short unexpected, Short actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual));
    }

    
    public static void assertNotEquals(short unexpected, short actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, message));
    }

    
    public static void assertNotEquals(short unexpected, Short actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, message));
    }

    
    public static void assertNotEquals(Short unexpected, short actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, message));
    }

    
    public static void assertNotEquals(Short unexpected, Short actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, message));
    }

    
    public static void assertNotEquals(short unexpected, short actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, messageSupplier));
    }

    
    public static void assertNotEquals(short unexpected, Short actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, messageSupplier));
    }

    
    public static void assertNotEquals(Short unexpected, short actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, messageSupplier));
    }

    
    public static void assertNotEquals(Short unexpected, Short actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, messageSupplier));
    }

    
    public static void assertNotEquals(int unexpected, int actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual));
    }

    
    public static void assertNotEquals(int unexpected, Integer actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual));
    }

    
    public static void assertNotEquals(Integer unexpected, int actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual));
    }

    
    public static void assertNotEquals(Integer unexpected, Integer actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual));
    }

    
    public static void assertNotEquals(int unexpected, int actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, message));
    }

    
    public static void assertNotEquals(int unexpected, Integer actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, message));
    }

    
    public static void assertNotEquals(Integer unexpected, int actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, message));
    }

    
    public static void assertNotEquals(Integer unexpected, Integer actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, message));
    }

    
    public static void assertNotEquals(int unexpected, int actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, messageSupplier));
    }

    
    public static void assertNotEquals(int unexpected, Integer actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, messageSupplier));
    }

    
    public static void assertNotEquals(Integer unexpected, int actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, messageSupplier));
    }

    
    public static void assertNotEquals(Integer unexpected, Integer actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, messageSupplier));
    }

    
    public static void assertNotEquals(long unexpected, long actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual));
    }

    
    public static void assertNotEquals(long unexpected, Long actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual));
    }

    
    public static void assertNotEquals(Long unexpected, long actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual));
    }

    
    public static void assertNotEquals(Long unexpected, Long actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual));
    }

    
    public static void assertNotEquals(long unexpected, long actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, message));
    }

    
    public static void assertNotEquals(long unexpected, Long actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, message));
    }

    
    public static void assertNotEquals(Long unexpected, long actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, message));
    }

    
    public static void assertNotEquals(Long unexpected, Long actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, message));
    }

    
    public static void assertNotEquals(long unexpected, long actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, messageSupplier));
    }

    
    public static void assertNotEquals(long unexpected, Long actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, messageSupplier));
    }

    
    public static void assertNotEquals(Long unexpected, long actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, messageSupplier));
    }

    
    public static void assertNotEquals(Long unexpected, Long actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, messageSupplier));
    }

    
    public static void assertNotEquals(float unexpected, float actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual));
    }

    
    public static void assertNotEquals(float unexpected, Float actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual));
    }

    
    public static void assertNotEquals(Float unexpected, float actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual));
    }

    
    public static void assertNotEquals(Float unexpected, Float actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual));
    }

    
    public static void assertNotEquals(float unexpected, float actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, message));
    }

    
    public static void assertNotEquals(float unexpected, Float actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, message));
    }

    
    public static void assertNotEquals(Float unexpected, float actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, message));
    }

    
    public static void assertNotEquals(Float unexpected, Float actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, message));
    }

    
    public static void assertNotEquals(float unexpected, float actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, messageSupplier));
    }

    
    public static void assertNotEquals(float unexpected, Float actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, messageSupplier));
    }

    
    public static void assertNotEquals(Float unexpected, float actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, messageSupplier));
    }

    
    public static void assertNotEquals(Float unexpected, Float actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, messageSupplier));
    }

    
    public static void assertNotEquals(float unexpected, float actual, float delta) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, delta));
    }

    
    public static void assertNotEquals(float unexpected, float actual, float delta, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, delta, message));
    }

    
    public static void assertNotEquals(float unexpected, float actual, float delta, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, delta, messageSupplier));
    }

    
    public static void assertNotEquals(double unexpected, double actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual));
    }

    
    public static void assertNotEquals(double unexpected, Double actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual));
    }

    
    public static void assertNotEquals(Double unexpected, double actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual));
    }

    
    public static void assertNotEquals(Double unexpected, Double actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual));
    }

    
    public static void assertNotEquals(double unexpected, double actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, message));
    }

    
    public static void assertNotEquals(double unexpected, Double actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, message));
    }

    
    public static void assertNotEquals(Double unexpected, double actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, message));
    }

    
    public static void assertNotEquals(Double unexpected, Double actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, message));
    }

    
    public static void assertNotEquals(double unexpected, double actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, messageSupplier));
    }

    
    public static void assertNotEquals(double unexpected, Double actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, messageSupplier));
    }

    
    public static void assertNotEquals(Double unexpected, double actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, messageSupplier));
    }

    
    public static void assertNotEquals(Double unexpected, Double actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, messageSupplier));
    }

    
    public static void assertNotEquals(double unexpected, double actual, double delta) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, delta));
    }

  
    public static void assertNotEquals(double unexpected, double actual, double delta, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, delta, message));
    }

  
    public static void assertNotEquals(double unexpected, double actual, double delta, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, delta, messageSupplier));
    }


    public static void assertNotEquals(char unexpected, char actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual));
    }

    public static void assertNotEquals(char unexpected, Character actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual));
    }
    
    public static void assertNotEquals(Character unexpected, char actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual));
    }
    
    public static void assertNotEquals(Character unexpected, Character actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual));
    }

  
    public static void assertNotEquals(char unexpected, char actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, message));
    }

    public static void assertNotEquals(char unexpected, Character actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, message));
    }
    
    public static void assertNotEquals(Character unexpected, char actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, message));
    }
    
    public static void assertNotEquals(Character unexpected, Character actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, message));
    }
    
    public static void assertNotEquals(char unexpected, char actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, messageSupplier));
    }

    public static void assertNotEquals(char unexpected, Character actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, messageSupplier));
    }
    public static void assertNotEquals(Character unexpected, char actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, messageSupplier));
    }

    public static void assertNotEquals(Character unexpected, Character actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, messageSupplier));
    }

    public static void assertNotEquals(Object unexpected, Object actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual));
    }

    public static void assertNotEquals(Object unexpected, Object actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, message));
    }

    public static void assertNotEquals(Object unexpected, Object actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotEquals(unexpected, actual, messageSupplier));
    }

    public static void assertSame(Object expected, Object actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertSame(expected, actual));
    }

    public static void assertSame(Object expected, Object actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertSame(expected, actual, message));
    }

    public static void assertSame(Object expected, Object actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertSame(expected, actual, messageSupplier));
    }

    public static void assertNotSame(Object unexpected, Object actual) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotSame(unexpected, actual));
    }

    public static void assertNotSame(Object unexpected, Object actual, String message) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotSame(unexpected, actual, message));
    }

    public static void assertNotSame(Object unexpected, Object actual, Supplier<String> messageSupplier) {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertNotSame(unexpected, actual, messageSupplier));
    }

    public static void assertAll(Executable... executables) throws MultipleFailuresError {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertAll(executables));
    }

    public static void assertAll(String heading, Executable... executables) throws MultipleFailuresError {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertAll(heading, executables));
    }

    public static void assertAll(Collection<Executable> executables) throws MultipleFailuresError {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertAll(executables));
    }

    public static void assertAll(String heading, Collection<Executable> executables) throws MultipleFailuresError {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertAll(heading, executables));
    }

    public static void assertAll(Stream<Executable> executables) throws MultipleFailuresError {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertAll(executables));
    }

    public static void assertAll(String heading, Stream<Executable> executables) throws MultipleFailuresError {
        GTester.getCurrent().addAssertTask(() -> Assertions.assertAll(heading, executables));
    }

   
}

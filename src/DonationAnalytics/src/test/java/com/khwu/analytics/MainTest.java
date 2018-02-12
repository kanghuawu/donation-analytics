package com.khwu.analytics;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class MainTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final String message = "Incorrect input. Expecting: java -cp donation-analytics.jar <input file> <percentile file> <output file>\n";

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(System.out);
    }

    @Test
    public void testMainNullArg() {
        String[] args = null;
        Main.main(args);
        assertEquals(message, outContent.toString());
    }

    @Test
    public void testMainOneArg() {
        String[] args = new String[]{"b"};
        Main.main(args);
        assertEquals(message, outContent.toString());
    }

    @Test
    public void testMainTwoArg() {
        String[] args = new String[]{"a", "b"};
        Main.main(args);
        assertEquals(message, outContent.toString());
    }
}
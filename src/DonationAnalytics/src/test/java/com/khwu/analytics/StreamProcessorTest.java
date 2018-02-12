package com.khwu.analytics;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class StreamProcessorTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(System.out);
    }

    @Test
    public void testStartAnalysisWithWrongInputFile() {
        String inputFile = "../../input/itcont.";
        String percentileFile = "../../input/percentile.txt";
        String outputFile = "../../output/repeat_donors.txt";
        StreamProcessor streamProcessor = new StreamProcessor();
        streamProcessor.startAnalysis(inputFile, percentileFile, outputFile);
        assertEquals(String.format("Problem handling files: %s%n", inputFile), outContent.toString());
    }

    @Test
    public void testStartAnalysisWithWrongPercentileFile() {
        String inputFile = "../../input/itcont.txt";
        String percentileFile = "../../input/percent.txt";
        String outputFile = "../../output/repeat_donors.txt";
        StreamProcessor streamProcessor = new StreamProcessor();
        streamProcessor.startAnalysis(inputFile, percentileFile, outputFile);
        assertEquals(String.format("Problem handling files: %s%n", percentileFile), outContent.toString());
    }
}
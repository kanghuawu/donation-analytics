package com.khwu.analytics;

import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.*;

public class PercentileTest {

    private Random random = new Random();

    @Test
    public void testSize0Percentile30GetPerIdx() {
        int size = 0;
        int per = 30;
        Percentile p = new Percentile(per);
        for (int i = 0; i < size; i++) {
            double rand = random.nextDouble() * 200;
            p.add(rand);
        }
        assertEquals(-1, p.getPerIdx());
    }

    @Test
    public void testSize0Percentile1GetPerIdx() {
        int size = 0;
        int per = 1;
        Percentile p = new Percentile(per);
        for (int i = 0; i < size; i++) {
            double rand = random.nextDouble() * 200;
            p.add(rand);
        }
        assertEquals(-1, p.getPerIdx());
    }



    @Test
    public void testSize100Percentile1GetPerIdx() {
        int size = 100;
        int per = 1;
        Percentile p = new Percentile(per);
        for (int i = 0; i < size; i++) {
            double rand = random.nextDouble() * 200;
            p.add(rand);
        }
        assertEquals(0, p.getPerIdx());
    }

    @Test
    public void testSize100Percentile30GetPerIdx() {
        int size = 100;
        int per = 30;
        Percentile p = new Percentile(per);
        for (int i = 0; i < size; i++) {
            double rand = random.nextDouble() * 200;
            p.add(rand);
        }
        assertEquals(29, p.getPerIdx());
    }

    @Test
    public void testSize100Percentile99GetPerIdx() {
        int size = 100;
        int per = 99;
        Percentile p = new Percentile(per);
        for (int i = 0; i < size; i++) {
            double rand = random.nextDouble() * 200;
            p.add(rand);
        }
        assertEquals(98, p.getPerIdx());
    }

    @Test
    public void testSize100Percentile100GetPerIdx() {
        int size = 100;
        int per = 100;
        Percentile p = new Percentile(per);
        for (int i = 0; i < size; i++) {
            double rand = random.nextDouble() * 200;
            p.add(rand);
        }
        assertEquals(99, p.getPerIdx());
    }

    @Test
    public void testSize200Percentile1GetPerIdx() {
        int size = 200;
        int per = 1;
        Percentile p = new Percentile(per);
        for (int i = 0; i < size; i++) {
            double rand = random.nextDouble() * 200;
            p.add(rand);
        }
        assertEquals(1, p.getPerIdx());
    }

    @Test
    public void testSize200Percentile30GetPerIdx() {
        int size = 200;
        int per = 30;
        Percentile p = new Percentile(per);
        for (int i = 0; i < size; i++) {
            double rand = random.nextDouble() * 200;
            p.add(rand);
        }
        assertEquals(59, p.getPerIdx());
    }

    @Test
    public void testSize200Percentile200GetPerIdx() {
        int size = 200;
        int per = 100;
        Percentile p = new Percentile(per);
        for (int i = 0; i < size; i++) {
            double rand = random.nextDouble() * 200;
            p.add(rand);
        }
        assertEquals(199, p.getPerIdx());
    }

    @Test
    public void testSize300Percentile97GetPerIdx() {
        int size = 300;
        int per = 97;
        Percentile p = new Percentile(per);
        for (int i = 0; i < size; i++) {
            double rand = random.nextDouble() * 200;
            p.add(rand);
        }
        assertEquals(290, p.getPerIdx());
    }

    @Test
    public void testSize20GetPerNum() {
        int size = 20;

        random.setSeed(size);
        double[] arr = new double[size];
        for (int i = 0; i < size; i++) {
            arr[i] = random.nextDouble() * 200;
        }
        Arrays.sort(arr);
        assertTrue(testGetPerNum(arr));
    }

    @Test
    public void testSize30GetPerNum() {
        int size = 30;

        random.setSeed(size);
        double[] arr = new double[size];
        for (int i = 0; i < size; i++) {
            arr[i] = random.nextDouble() * 200;
        }
        Arrays.sort(arr);
        assertTrue(testGetPerNum(arr));
    }

    @Test
    public void testSize50GetPerNum() {
        int size = 50;

        random.setSeed(size);
        double[] arr = new double[size];
        for (int i = 0; i < size; i++) {
            arr[i] = random.nextDouble() * 200;
        }
        Arrays.sort(arr);
        assertTrue(testGetPerNum(arr));
    }

    @Test
    public void testSize100GetPerNum() {
        int size = 100;

        random.setSeed(size);
        double[] arr = new double[size];
        for (int i = 0; i < size; i++) {
            arr[i] = random.nextDouble() * 200;
        }
        Arrays.sort(arr);
        assertTrue(testGetPerNum(arr));
    }

    @Test
    public void testSize200GetPerNum() {
        int size = 200;

        random.setSeed(size);
        double[] arr = new double[size];
        for (int i = 0; i < size; i++) {
            arr[i] = random.nextDouble() * 200;
        }
        Arrays.sort(arr);
        assertTrue(testGetPerNum(arr));
    }

    @Test
    public void testSize1000GetPerNum() {
        int size = 1000;

        random.setSeed(size);
        double[] arr = new double[size];
        for (int i = 0; i < size; i++) {
            arr[i] = random.nextDouble() * 200;
        }
        Arrays.sort(arr);
        assertTrue(testGetPerNum(arr));
    }

    public boolean testGetPerNum(double[] arr) {

        for (int i = 1; i <= 100; i++) {
            Percentile p = new Percentile(i);
            for (int j = 0; j < arr.length; j++) {
                p.add(arr[j]);
            }
            boolean res = p.getPerNum().get() == arr[p.getPerIdx()];
            if (!res) {
                System.out.println(i + " " + p.getPerNum() + " " + arr[p.getPerIdx()]);
                return false;
            }
        }
        return true;
    }

    @Test
    public void testItcontFile() {
        Percentile p = new Percentile(30);
        p.add(313);
        p.add(384);
        assertEquals(313, p.getPerNum().get(), 0);
    }
}
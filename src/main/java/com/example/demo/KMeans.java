package com.example.demo;

import java.util.*;

public class KMeans {

    public static String[] work(double[] x, double[] y, int KlasterQuantity) {
        if (x.length != y.length) {
            throw new IllegalArgumentException("The length of x and y arrays must be the same.");
        }

        int n = x.length;

        int[] clusters = new int[n];

        double[] centroidX = new double[KlasterQuantity];
        double[] centroidY = new double[KlasterQuantity];
        Random rand = new Random();
        for (int i = 0; i < KlasterQuantity; i++) {
            int randomIndex = rand.nextInt(n);
            centroidX[i] = x[randomIndex];
            centroidY[i] = y[randomIndex];
        }

        boolean centroidsChanged = true;
        while (centroidsChanged) {
            centroidsChanged = false;

            for (int i = 0; i < n; i++) {
                double minDistance = Double.MAX_VALUE;
                int minCluster = -1;
                for (int j = 0; j < KlasterQuantity; j++) {
                    double distance = Math.sqrt(Math.pow(x[i] - centroidX[j], 2) + Math.pow(y[i] - centroidY[j], 2));
                    if (distance < minDistance) {
                        minDistance = distance;
                        minCluster = j;
                    }
                }
                if (clusters[i] != minCluster) {
                    clusters[i] = minCluster;
                    centroidsChanged = true;
                }
            }

            if (centroidsChanged) {
                for (int i = 0; i < KlasterQuantity; i++) {
                    double sumX = 0;
                    double sumY = 0;
                    int clusterSize = 0;
                    for (int j = 0; j < n; j++) {
                        if (clusters[j] == i) {
                            sumX += x[j];
                            sumY += y[j];
                            clusterSize++;
                        }
                    }
                    centroidX[i] = sumX / clusterSize;
                    centroidY[i] = sumY / clusterSize;
                }
            }
        }

        String[] colors = new String[n];
        String[] clusterColors = {"Red", "Green", "Blue", "Cyan", "Orange", "Purple", "Pink", "Yellow", "Magenta", "Lime"};
        System.out.println("Центры кластеров: ");
        for (int i = 0; i < 4; i++) {
            System.out.print(centroidX[i]);
            System.out.print(" ; ");
            System.out.println(centroidY[i]);
        }
        for (int i = 0; i < n; i++) {
            colors[i] = clusterColors[clusters[i] % clusterColors.length];
        }


        return colors;
    }

}

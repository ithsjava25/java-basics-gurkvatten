package com.example;

import com.example.api.ElpriserAPI.Elpris;

import java.util.Date;
import java.util.List;
import java.time.format.DateTimeFormatter;
import java.util.Locale;


// Sliding window

public class ChargeSorter {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final Locale SVEDISH_LOCALE = new Locale("sv", "SE");

    public static void findAndPrintOptimalWindow(List<Elpris> allPrices, int durationHours) {


        if (allPrices.size() < durationHours) {
            System.err.printf("\nOBS: Hittade bara %d timmars priser. Kan inte garantera ett %d timmars fönster.\n",
                    allPrices.size(), durationHours);
            return;
        }

        double minTotalCost = Double.MAX_VALUE;
        int optimalStartIndex = -1;

        for (int i = 0; i <= allPrices.size() - durationHours; i++) {
            double currentTotalCost = 0.0;

            for (int j = 0; j < durationHours; j++) {
                currentTotalCost += allPrices.get(i + j).sekPerKWh();
            }

            if (currentTotalCost < minTotalCost) {
                minTotalCost = currentTotalCost;
                optimalStartIndex = i;
            }
        }

        if (optimalStartIndex != -1) {
            Elpris startPrice = allPrices.get(optimalStartIndex);
            Elpris endPrice = allPrices.get(optimalStartIndex + durationHours - 1);

            double meanPrice = minTotalCost / durationHours;
            double meanPriceOre = meanPrice * 100;

            String formattedMeanPrice = String.format(SVEDISH_LOCALE, "%.2f", meanPriceOre);

            String formattedTotalCost = String.format(SVEDISH_LOCALE, "%.4f", minTotalCost);

            System.out.println("\nPåbörja laddning...");
            System.out.println("\n=========================================");
            System.out.printf("   Optimalt Laddningsfönster (%dh)   \n", durationHours);
            System.out.println("=========================================");
            System.out.printf("Starttid:    kl %s\n", startPrice.timeStart().format(TIME_FORMATTER));
            System.out.printf("Sluttid:    kl %s\n", endPrice.timeEnd().format(TIME_FORMATTER));
            System.out.printf("Medelpris för fönster: %s öre\n", formattedMeanPrice);
            System.out.printf("Total kostn: %s SEK (över %d timmar)\n", formattedTotalCost, durationHours);
            System.out.println("-----------------------------------------");
        }
    }


    }


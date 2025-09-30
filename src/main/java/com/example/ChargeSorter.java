package com.example;

import com.example.api.ElpriserAPI.Elpris;

import java.util.Date;
import java.util.List;
import java.time.format.DateTimeFormatter;


// Sliding window

public class ChargeSorter {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

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


            System.out.println("\nPåbörja laddning...");
            System.out.println("\n=========================================");
            System.out.printf("   Optimalt Laddningsfönster (%dh)   \n", durationHours);
            System.out.println("=========================================");
            System.out.printf("Starttid:    %s\n", startPrice.timeStart().format(TIME_FORMATTER));
            System.out.printf("Sluttid:     %s\n", endPrice.timeEnd().format(TIME_FORMATTER));
            System.out.printf("Medelpris:   %.4f SEK/kWh\n", minTotalCost / durationHours);
            System.out.printf("Total kostn: %.4f SEK (över %d timmar)\n", minTotalCost, durationHours);
            System.out.println("-----------------------------------------");
        }
    }


    }


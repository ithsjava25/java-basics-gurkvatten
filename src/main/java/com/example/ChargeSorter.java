package com.example;

import com.example.api.ElpriserAPI.Elpris;

import java.util.Date;
import java.util.List;
import java.time.format.DateTimeFormatter;


// Sliding window

public class ChargeSorter {

    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public static void findOptimalWindow(List<Elpris> elprises, int durationHours) {

        if (elprises.size() < durationHours) {
            System.err.printf("\nOBS: Hittade bara %d timmars priser. Kan inte garantera ett %d timmars fönster.\n",
                    elprises.size(), durationHours);
            return;
        }

        double minTotalCost = Double.MAX_VALUE;
        int optimalStart = -1;

        for ( int i = 0; i <= elprises.size() - durationHours; i++) {
            double currentTotalCost = 0.0;

            for (int j = 0; j < durationHours; j++)  {
                currentTotalCost += elprises.get(i + j).sekPerKWh();
            }
            if (currentTotalCost < minTotalCost) {
                minTotalCost = currentTotalCost;
                optimalStart = i;
            }
        }
        if (optimalStart == -1) {
            Elpris startPrice = elprises.get(optimalStart);
            Elpris endPrice = elprises.get(optimalStart + durationHours - 1);

            System.out.println("\n=========================================");
            System.out.printf("   Optimalt Laddningsfönster (%dh)   \n", durationHours);
            System.out.println("=========================================");
            System.out.printf("Starttid:    %s\n", startPrice.timeStart().format(timeFormatter));
            System.out.printf("Sluttid:     %s\n", endPrice.timeEnd().format(timeFormatter));
            System.out.printf("Medelpris:   %.4f SEK/kWh\n", minTotalCost / durationHours);
            System.out.printf("Total kostn: %.4f SEK (över %d timmar)\n", minTotalCost, durationHours);
            System.out.println("-----------------------------------------");
        }
        }


    }


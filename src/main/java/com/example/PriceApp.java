package com.example;

import com.example.api.ElpriserAPI;
import com.example.api.ElpriserAPI.Elpris;
import com.example.api.ElpriserAPI.Prisklass;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PriceApp {

    private final ElpriserAPI api;

    public PriceApp() {
        this.api = new ElpriserAPI();
    }

    public void run(Prisklass zone, LocalDate date, String chargingDurationStr, boolean isSorted) {
        System.out.println("--- APP KÖRNING STARTAR ---");
        System.out.println("Vald Zon: " + zone);
        System.out.println("Startdatum: " + date);

        List<Elpris> allPrices = fetchPrices(date, zone);

        if (allPrices.isEmpty()) {
            System.out.println("Kunde inte hämta några priser. Avslutar.");
            return;
        }

        PriceLogic priceLogic = new PriceLogic(allPrices);
        priceLogic.printStatistics();

        if (chargingDurationStr != null) {
            try {
                int duration = Integer.parseInt(chargingDurationStr.replace("h", ""));

                if (duration == 2 || duration == 4 || duration == 8) {
                    ChargeSorter.findAndPrintOptimalWindow(allPrices, duration);
                } else {
                    System.err.println("Fel: Laddningen måste vara 2h, 4h eller 8h.");
                }
            } catch (NumberFormatException e) {
                System.err.println("Fel: Ogiltig längd för laddning.");
            }
        }

        priceLogic.printDetailedPrices(isSorted);
    }

    private List<Elpris> fetchPrices(LocalDate startDay, Prisklass prisklass) {
        System.out.printf("Hämtar priser för %s i %s...\n", startDay, prisklass);
        List<Elpris> todayPrices = api.getPriser(startDay, prisklass);

        LocalDate nextDay = startDay.plusDays(1);
        System.out.printf("Hämtar priser för %s i %s...\n", nextDay, prisklass);
        List<Elpris> tomorrowPrices = api.getPriser(nextDay, prisklass);

        List<Elpris> allPrices = new ArrayList<>(todayPrices);
        allPrices.addAll(tomorrowPrices);

        System.out.printf("Totalt hämtade priser: %d timmar.\n", allPrices.size());

        return allPrices;
    }
}
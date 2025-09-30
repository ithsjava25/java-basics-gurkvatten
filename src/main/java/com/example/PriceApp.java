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

    public void run(Prisklass zone, LocalDate date,String chargingDuration, boolean isSorted) {
        System.out.println("--- App Körs---");
        System.out.println("Zon: " + zone);
        System.out.println("Datum: " + date);
        System.out.println("Laddningstid: " + chargingDuration);

        List<Elpris> allPrices = fetchPrices(date, zone);

        if (allPrices.isEmpty()) {
            System.out.println("Nothing to do.");
            return;
        }

        PriceLogic priceLogic = new PriceLogic(allPrices);

        priceLogic.printStatistics();

        if (chargingDuration != null) {
            try {
                int duration = Integer.parseInt(chargingDuration.replace("h",""));

                if (duration == 2 || duration == 4 || duration == 8 ) {
                    ChargeSorter.findOptimalWindow(allPrices, duration);
                } else  {
                    System.out.println("Fel: Laddningen måste vara 2h, 4h eller 8h.");
                }
            } catch (NumberFormatException e) {
                System.err.println("Fel ogiltig längd för laddning");
            }

        }

    }

    private List<Elpris> fetchPrices(LocalDate startDay, Prisklass prisklass) {
        System.out.println("hämtar priser för valt datum");
        List<ElpriserAPI.Elpris> todayPrices = api.getPriser(startDay, prisklass);

        List<Elpris> tomorrowPrices = Collections.emptyList();

        LocalDate nextDay = startDay.plusDays(1);

        System.out.println("Hämtar priser för...");
        tomorrowPrices = api.getPriser(nextDay, prisklass);

        List<Elpris> allPrices = new ArrayList<>(todayPrices);
        allPrices.addAll(tomorrowPrices);

        return allPrices;

    }



}

package com.example;

import com.example.api.ElpriserAPI;
import com.example.api.ElpriserAPI.Prisklass;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import com.example.PriceApp;

public class Main {

    public static void printHelp() {
        System.out.println("Elpris hjälpen");
        System.out.println("Användning: --zone <SE1-SE4> [--date YYYY-MM-DD] [--charging 2h|4h|8h] [--sorted] [--help]");
    }

    public static void main(String[] args) {
        ElpriserAPI elpriserAPI = new ElpriserAPI();


        for (String arg : args) {
            String zoneStr = null;
            String dateStr = null;
            String chargingDurationStr = null;
            boolean isSorted = false;
            boolean showHelp = false;


            for (int i = 0; i < args.length; i++) {
                switch (args[i]) {
                    case "--zone":
                        if (i + 1 < args.length) zoneStr = args[++i];
                        break;
                    case "--date":
                        if (i + 1 < args.length) dateStr = args[++i];
                        break;
                    case "--charging":
                        if (i + 1 < args.length) chargingDurationStr = args[++i];
                        break;
                    case "--sorted":
                        isSorted = true;
                        break;
                    case "--help":
                        showHelp = true;
                        break;
                }
            }
            if (showHelp) {
                printHelp();
                return;
            }

            if (zoneStr == null) return;
            if (dateStr == null) return;

            PriceApp priceApp = new PriceApp();


        }

    }
    private static Prisklass validateAndParseZone(String zoneStr) {
        if (zoneStr == null) {
            System.err.println("Fel: Zonen måste anges via --zone.");
            printHelp();
            return null;
        }
        try {
            // valueOf konverterar "SE3" till Prisklass.SE3
            return Prisklass.valueOf(zoneStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.err.println("Fel: Ogiltig zon '" + zoneStr + "'. Välj SE1, SE2, SE3 eller SE4.");
            return null;
        }


    }
    private static LocalDate validateAndParseDate(String dateStr) {
        if (dateStr == null) {
            return LocalDate.now(); // Standard: Idag
        }
        try {
            // LocalDate.parse klarar YYYY-MM-DD formatet som standard
            return LocalDate.parse(dateStr);
        } catch (DateTimeParseException e) {
            System.err.println("Fel: Ogiltigt datumformat '" + dateStr + "'. Använd YYYY-MM-DD.");
            return null;
        }
    }
}

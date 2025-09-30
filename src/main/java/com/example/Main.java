package com.example;

import com.example.api.ElpriserAPI;

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
        }
    }
}

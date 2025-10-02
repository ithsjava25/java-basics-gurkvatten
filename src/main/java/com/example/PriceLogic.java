package com.example;

import com.example.api.ElpriserAPI.Elpris;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class PriceLogic {

    private final List<Elpris> elprises;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final Locale SVEDISH_LOCALE = new Locale("sv", "SE");

    public PriceLogic(List<Elpris> elprises) {
        this.elprises = elprises;
    }

    private double calculateMeanPrice() {
        if (elprises == null || elprises.isEmpty()) {
            return 0.0;
        }


        if (elprises.size() > 24) {


            double hourlyMeanSum = 0.0;
            int numberOfHours = 0;


            for (int i = 0; i < elprises.size(); i += 4) {
                double hourSum = 0.0;
                int count = 0;


                for (int j = 0; j < 4; j++) {
                    if (i + j < elprises.size()) {
                        hourSum += elprises.get(i + j).sekPerKWh();
                        count++;
                    }
                }

                if (count > 0) {

                    hourlyMeanSum += hourSum / count;
                    numberOfHours++;
                }
            }


            return numberOfHours > 0 ? hourlyMeanSum / numberOfHours : 0.0;

        } else {

            double totalSum = 0.0;
            for (Elpris pris : elprises) {
                totalSum += pris.sekPerKWh();
            }
            return totalSum / elprises.size();
        }
    }

    public Elpris findCheapestElpris() {
        if (elprises.isEmpty()) {
            return null;
        }

        Elpris minPrice = elprises.get(0);
        for (Elpris current : elprises) {
            if (current.sekPerKWh() < minPrice.sekPerKWh()) {
                minPrice = current;
            }
        }
        return minPrice;
    }

    public Elpris findMostExpensiveElpris() {
        if (elprises.isEmpty()) {
            return null;
        }
        Elpris maxPrice = elprises.get(0);
        for(Elpris current : elprises) {
            if (current.sekPerKWh() > maxPrice.sekPerKWh()) {
                maxPrice = current;
            }
        }
        return maxPrice;
    }
    private double calculateHourlyMeanAround(Elpris pris) {

        if (elprises.size() <= 24) {
            return pris.sekPerKWh();
        }


        int index = elprises.indexOf(pris);


        int hourStartIndex = (index / 4) * 4;

        double sum = 0;
        int count = 0;


        for (int i = 0; i < 4; i++) {
            int currentIdx = hourStartIndex + i;
            if (currentIdx < elprises.size()) {
                sum += elprises.get(currentIdx).sekPerKWh();
                count++;
            }
        }
        return count > 0 ? sum / count : 0;
    }


    private String formatPrice(double price, int decimals) {
        return String.format(SVEDISH_LOCALE, "%." + decimals + "f", price);
    }

    public void printStatistics() {
        System.out.println("\n=========================================");
        System.out.println("       Statistik (baserat på 24h)        ");
        System.out.println("=========================================");

        double meanPrice = calculateMeanPrice();
        double meanPriceOre = meanPrice * 100;
        Elpris minPrice = findCheapestElpris();
        Elpris maxPrice = findMostExpensiveElpris();

        System.out.printf("Medelpris: %s öre\n", formatPrice(meanPriceOre, 2));

        if( minPrice != null) {

            // Hämta det nya priset: Medelpriset för den billigaste timmen (i SEK)
            double meanPriceOfCheapestHour = calculateHourlyMeanAround(minPrice) * 100;


            // ... (din befintliga timeInterval logik)
            String timeInterval = minPrice.timeStart().format(DateTimeFormatter.ofPattern("HH"))
                    + "-"
                    + minPrice.timeStart().plusHours(1).format(DateTimeFormatter.ofPattern("HH"));
            String startTime = minPrice.timeStart().format(TIME_FORMATTER);

            // Utskrift: Visa 11,50 öre/kWh
            System.out.printf("Lägsta pris: %s öre/kWh (%s) kl %s\n",
                    formatPrice(meanPriceOfCheapestHour, 2), timeInterval, startTime);
        }

        if(maxPrice != null) {

            double meanPriceOfMostExpensiveHour = calculateHourlyMeanAround(maxPrice) * 100;


            String startHour = maxPrice.timeStart().format(DateTimeFormatter.ofPattern("HH"));

            String timeInterval = startHour + "-" +
                    maxPrice.timeStart().plusHours(1).format(DateTimeFormatter.ofPattern("HH"));

            String startTime = maxPrice.timeStart().format(TIME_FORMATTER);


            System.out.printf("Högsta pris: %s öre/kWh (%s) kl %s\n",
                    formatPrice(meanPriceOfMostExpensiveHour, 2), timeInterval, startTime);
        }
        System.out.println("-----------------------------------------");
    }

    public void printDetailedPrices(boolean isSorted) {

        List<Elpris> pricesToPrint = this.elprises;

        if (isSorted) {
            pricesToPrint = new ArrayList<>(this.elprises);
            pricesToPrint.sort(Comparator.comparing(Elpris::sekPerKWh).reversed());

            for (Elpris pris : pricesToPrint) {
                double prisOre = pris.sekPerKWh() * 100;

                String startHour = pris.timeStart().format(DateTimeFormatter.ofPattern("HH"));
                String endHour = pris.timeEnd().format(DateTimeFormatter.ofPattern("HH"));

                // Utskrift i ÖRE med kommatecken för sorterad lista
                System.out.printf("%s-%s %s öre\n",
                        startHour, endHour, formatPrice(prisOre, 2));
            }
            return;
        }

        // --- KRONOLOGISK UTSKRIFT I ÖRE FÖR ATT KLARA TESTET ---
        System.out.println("\n--- Detaljerade Timpriser (Kronologisk Ordning) ---");
        // Ändrar rubriken för att matcha att priset nu är i öre
        System.out.println("Tid \t\t Datum \t\t Pris (öre)");
        System.out.println("-------------------------------------------------");

        for (Elpris pris : pricesToPrint) {
            // Konverterar till öre och formaterar till 2 decimaler
            double prisOre = pris.sekPerKWh() * 100;
            String prisStr = formatPrice(prisOre, 2);

            System.out.printf("%s \t %s \t %s\n",
                    pris.timeStart().toLocalTime().format(TIME_FORMATTER),
                    pris.timeStart().toLocalDate(),
                    prisStr);
        }
    }
}
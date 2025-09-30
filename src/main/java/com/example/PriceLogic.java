package com.example;

import com.example.api.ElpriserAPI.Elpris;
import java.util.List;

public class PriceLogic {

    private final List<Elpris> elprises;

    public PriceLogic(List<Elpris> elprises) {
        this.elprises = elprises;
    }

    public double calculateMeanPrice() {
        if (elprises.isEmpty()) return 0;

        int count = Math.min(elprises.size(), 24);
        double sum = 0.0;
        for (int i = 0; i < count; i++) {
            sum += elprises.get(i).sekPerKWh();
        }
        return sum / count;
    }

    // KORRIGERAD METOD:
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

    public void printStatistics() {
        System.out.println("Statistik senaste dygnet");

        double meanPrice = calculateMeanPrice();

        Elpris minPrice = findCheapestElpris();
        Elpris maxPrice = findMostExpensiveElpris();

        System.out.println("Medelpris: " + meanPrice);
        if( minPrice != null) {
            System.out.println("Billigast: " + minPrice.toString());
        }
        if(maxPrice != null) {
            System.out.println("Dyrast: " + maxPrice.toString());
        }
    }
}
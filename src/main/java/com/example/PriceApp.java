package com.example;

import com.example.api.ElpriserAPI;
import com.example.api.ElpriserAPI.Prisklass;

import java.time.LocalDate;

public class PriceApp {


    private final ElpriserAPI api;


    public PriceApp() {

        this.api = new ElpriserAPI();

    }

    public void run(Prisklass zone, LocalDate date,String chargingDuration, boolean isSorted) {

    }
}

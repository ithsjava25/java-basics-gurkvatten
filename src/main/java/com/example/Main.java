package com.example;

import com.example.api.ElpriserAPI;

public class Main {
    public static void main(String[] args) {
        ElpriserAPI elpriserAPI = new ElpriserAPI();


        for (String arg : args) {
            System.out.println("Antal argument" + args.length);
        }
    }
}

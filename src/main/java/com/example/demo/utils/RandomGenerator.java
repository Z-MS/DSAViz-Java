package com.example.demo.utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class RandomGenerator {
    public static int generateRandomNumber(int min, int max) {
        // Create a Random object
        Random random = new Random();

        // Calculate the range (max - min + 1) and add min to get inclusive random number
        int randomNumber = random.nextInt(max - min + 1) + min;

        return randomNumber;
    }

    public static ArrayList<Comparable> generateRandomNumbers(int min, int max, int length) {
        ArrayList<Comparable> randomNumbers = new ArrayList<>();

        for(int i = 0; i < length; i++) {
            // generate a random number
            int generatedNum = generateRandomNumber(min, max);
            // check if the array contains the newly generated number. If so, generate a new one
            while (randomNumbers.contains(generatedNum)) {
                generatedNum = generateRandomNumber(min, max);
            }
            randomNumbers.add(generatedNum);
        }
        return randomNumbers;
    }

    public static char generateRandomCharacter() {
        // Create a Random object
        Random random = new Random();

        // Generate a random number between 0 and 25 (inclusive) representing the alphabet character
        int randomNum = random.nextInt(26);

        // Convert the random number to the corresponding lowercase alphabet character ('a' to 'z')
        char randomChar = (char) ('a' + randomNum);

        return randomChar;
    }

    public static ArrayList<Comparable> generateRandomCharacters(int length) {
        ArrayList<Comparable> randomCharacters = new ArrayList<>();
        for(int i = 0; i < length; i++) {
            // generate a random number
            char generatedChar = generateRandomCharacter();
            // check if the array contains the newly generated number. If so, generate a new one
            while (randomCharacters.contains(generatedChar)) {
                generatedChar = generateRandomCharacter();
            }
            randomCharacters.add((char)generatedChar);
        }
        return randomCharacters;
    }
}

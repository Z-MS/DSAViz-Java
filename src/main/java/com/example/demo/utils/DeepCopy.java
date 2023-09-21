package com.example.demo.utils;

import java.util.ArrayList;

public class DeepCopy {
    public static void copy(ArrayList<Comparable> sourceList, ArrayList<Comparable> destinationList) {
        destinationList.clear();
        for(Comparable elem: sourceList) {
            destinationList.add(elem);
        }
    }
}

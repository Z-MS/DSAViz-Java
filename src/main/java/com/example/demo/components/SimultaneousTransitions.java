package com.example.demo.components;

import javafx.animation.Transition;

import java.util.ArrayList;
import java.util.Collection;

public class SimultaneousTransitions {
    ArrayList<Transition> simultTransitions;

    public SimultaneousTransitions(ArrayList<Transition> transitions) {
        simultTransitions.addAll(transitions);
    }

    void addTransition(Transition transition) {
        simultTransitions.add(transition);
    }

}

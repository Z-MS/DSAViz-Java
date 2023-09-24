package com.example.demo.utils;

import javafx.animation.Transition;
import javafx.util.Duration;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class PlayingQueue {
    Queue<Transition> playingQueue;

    static class CustomDurationComparator implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {

            Duration duration1 = ((Transition) o1).getTotalDuration();
            Duration duration2 = ((Transition) o2).getTotalDuration();

            if ((duration1).compareTo(duration2) > 0) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    public PlayingQueue() {
        playingQueue = new PriorityQueue<>(new CustomDurationComparator());
    }

    public Queue<Transition> getPlayingQueue() {
        return playingQueue;
    }
}

package model;

import view.Observer;

import java.util.ArrayList;
import java.util.List;

public abstract class Observable {

    // Attributes
    private List<Observer> observers = new ArrayList<>(); // each object keeps track of all the views it needs to update if it changes

    // Methods
    public void notifyObservers() {
        for (Observer observer: observers) {
            observer.update();
        }
    }

    public List<Observer> getObservers() {
        return observers;
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }
}
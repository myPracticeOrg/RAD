package main;

import java.util.Calendar;
import java.util.HashMap;

public class Rad {
    
    private final static double POWER_LEVEL_ON = 4.2;
    
    private final static double POWER_LEVEL_SLEEP = 1.2;
    
    private final static double POWER_LEVEL_OFF = 0.0;
    
    public final static double MIN_RADIATION = 0.1;
    
    public final static double MAX_RADIATION = Math.pow(10, 4);
    
    private double powerLevel;
    
    private HashMap<Long, Double> data = new HashMap<Long, Double>();
    
    private String state = "RAD_OFF";
    
    public Rad() {
    }
    
    // Change state
    
    public void off() {
        setState("RAD_OFF");
    }
    
    public void bootup() {
        setState("RAD_BOOTUP");
    }
    
    public void science() {
        setState("RAD_SCIENCE");
    }
    
    public void checkout() {
        setState("RAD_CHECKOUT");
    }
    
    public void shutdown() {
        setState("RAD_SHUTDOWN");
    }
    
    public void sleep() {
        setState("RAD_SLEEP");
    }
    
    // Rover interaction
    
    public boolean isOn() {
        return !state.equals("RAD_OFF");
    }
    
    public boolean isSleeping() {
        return state.equals("RAD_SLEEP");
    }
    
    public boolean isScience() {
        return state.equals("RAD_SCIENCE");
    }
    
    public double getPowerConsumption() {
        if (state.equals("RAD_OFF")) {
            return POWER_LEVEL_OFF;
        }
        if (state.equals("RAD_SLEEP")) {
            return POWER_LEVEL_SLEEP;
        } else {
            return POWER_LEVEL_ON;
        }
    }
    
    public void addMeasurement(Double radiationLevel) {
        data.put(Calendar.getInstance().getTimeInMillis(), radiationLevel);
    }
    
    public void clearData() {
        data.clear();
    }
    
    // Getters/Setters
    
    public HashMap<Long, Double> getData() {
        if (state.equals("RAD_CHECKOUT")) {
            return data;
        } else {
            return null;
        }
    }
    
    public void setData(HashMap<Long, Double> data) {
        this.data = data;
    }
    
    public String getState() {
        return state;
    }
    
    public void setState(String state) {
        this.state = state;
        
        setPowerLevel(getPowerConsumption());
    }
    
    public double getPowerLevel() {
        return powerLevel;
    }
    
    public void setPowerLevel(double powerLevel) {
        this.powerLevel = powerLevel;
    }
    
}

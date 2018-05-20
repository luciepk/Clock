/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clock;

/**
 *
 * @author LUCIE
 */
public class Alarm {
    
    /**
     *an integer to stock the hour of the alarm
     */
    protected int hour;

    /**
     *an integer to stock the second of the alarm
     */
    protected int min;
    
    /**
     *cnstructor of the alarm
     * @param hour
     * @param min
     */
    public Alarm(int hour,int min) {
        this.hour = hour;
        this.min = min;
    }

    /**
     *to get the hour 
     * @return
     */
    public int getHour() {
        return hour;
    }
    
    /**
     *to get the minute
     * @return
     */
    public int getMin() {
        return min;
    }
    
    @Override
    public String toString() {
        return getHour()+":"+getMin();
    }
}

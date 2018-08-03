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
    
  private long dateInMilliseconds;

    public Alarm(long dateInMilliseconds) {
        this.dateInMilliseconds = dateInMilliseconds;
    }

    public long getDateInMilliseconds() {
        return dateInMilliseconds;
    }

    @Override
    public String toString() {
        return String.valueOf(getDateInMilliseconds());
    }
}

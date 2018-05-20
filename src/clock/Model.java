package clock;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

//import java.util.GregorianCalendar;

/**
 *
 * @author LUCIE
 */
public class Model extends Observable {

    SortedArrayPriorityQueue arrayAlarm = new SortedArrayPriorityQueue<>(20);//create a list for all the alarm  
    int hour = 0;
    int minute = 0;
    int second = 0;

    int oldSecond = 0;

   
    public Model() {
        update();
    }

  
    public SortedArrayPriorityQueue getArray() {
        return arrayAlarm;
    }

   
    public void update() {
        Calendar date = Calendar.getInstance();
        hour = date.get(Calendar.HOUR);
        minute = date.get(Calendar.MINUTE);
        oldSecond = second;
        second = date.get(Calendar.SECOND);
        if (oldSecond != second) {
            setChanged();
            notifyObservers();
        }

    }

    /**
     *this method is suppose to create a pop up p   ge whan the time and the firt alarm are equal
     */
    public void AlarmTrigger() {

        class timerListener implements ActionListener {

            public void actionPerformed(ActionEvent e) {
                String alarm = null;
                try {
                    alarm = arrayAlarm.head().toString();
                } catch (QueueUnderflowException ex) {
                    Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
                }
                String time = hour + ":" + minute;
                if (alarm == time) {
                    JOptionPane.showMessageDialog(null, "Wake UP Wake UP", "Alarm Ringing", JOptionPane.PLAIN_MESSAGE);
                }

            }
        }
    }
}

package clock;

import java.awt.*;
import javax.swing.*;
import java.util.Observer;
import java.util.Observable;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import java.text.*;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import queuemanager.QueueUnderflowException;

/**
 *
 * @author LUCIE
 */
public class View implements Observer {

    ClockPanel panel;

    public View(Model model) {

        JFrame frame = new JFrame();
        panel = new ClockPanel(model);

        frame.setTitle("Java Clock");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container pane = frame.getContentPane();

        JButton button = new JButton("Button 1 (PAGE_START)");
        pane.add(button, BorderLayout.PAGE_START);

        panel.setPreferredSize(new Dimension(200, 200));
        pane.add(panel, BorderLayout.CENTER);

        // creation of the update about the alarm
        final JPanel jPanel2 = new JPanel();

        JLabel jLabel4 = new JLabel();
        jLabel4.setText("Next Alarm: ");
        jPanel2.add(jLabel4);

        final JLabel nextAlarm = new JLabel();
        nextAlarm.setText("Alarm Not Set");
        jPanel2.add(nextAlarm);

        pane.add(jPanel2, BorderLayout.LINE_START);

        //Creation of the bottom of the clock
        JPanel jPanel1 = new JPanel();

        JLabel jLabel1 = new JLabel();
        jLabel1.setText("Set Alaram:");
        jPanel1.add(jLabel1);

        final JLabel hours = new JLabel();
        hours.setText("HH:");
        jPanel1.add(hours);

        final JTextField hh = new JTextField(2);
        jPanel1.add(hh);

        final JLabel minutes = new JLabel();
        minutes.setText("MM:");
        jPanel1.add(minutes);

        final JTextField min = new JTextField(2);
        jPanel1.add(min);

        //to save an alarm
        JButton jButton1 = new JButton();
        jButton1.setText("Set Alarm");
        jPanel1.add(jButton1);

        class jButton1Listener implements ActionListener {

            public void actionPerformed(ActionEvent event) {
                String result = " ";
                // add an alarm
                int hour = Integer.parseInt(hh.getText());
                int minute = Integer.parseInt(min.getText());
                AlarmClock.addAlarm(hour, minute);

                //put the field to nothing
                hh.setText("");
                min.setText("");

                //update the status of the next alarm
                // get the head in miliseconds
                Long[] priorityArray = AlarmClock.getPriorityArray();
                long dateInMilliseconds = priorityArray[0];
                //put the millisecond in hour and minute
                int hours = AlarmClock.millisecondsToHours(dateInMilliseconds);
                int minutes = AlarmClock.millisecondsToMinutes(dateInMilliseconds);
                // create the string to update
                result = " " + hours + ":" + minutes;
                nextAlarm.setText(result);

            }
        }
        jButton1.addActionListener(new jButton1Listener());

        pane.add(jPanel1, BorderLayout.PAGE_END);

        button = new JButton("5 (LINE_END)");
        pane.add(button, BorderLayout.LINE_END);

        // End of borderlayout code
        // adding a menus bar
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        //adding list to the menu bar
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        JMenu actionMenu = new JMenu("Action");
        menuBar.add(actionMenu);

        //add some action in this list
        //action for the file list
        JMenuItem exitItem = new JMenuItem("Exit"); //to exit the clock
        class ExitItemListener implements ActionListener {

            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        }
        exitItem.addActionListener(new ExitItemListener());
        fileMenu.add(exitItem);

        //action for the action list
        JMenuItem listAlarm = new JMenuItem("Alarm"); //to see all the alarm
        class ListAlarmListener implements ActionListener {

            public void actionPerformed(ActionEvent event) {
                SpinnerNumberModel modelHours = new SpinnerNumberModel(0, 0, 23, 1);
                SpinnerNumberModel modelMinutes = new SpinnerNumberModel(0, 0, 59, 1);

                final Long[] priorityArray = AlarmClock.getPriorityArray();
                String[] labels = new String[priorityArray.length];

                // create and add labels to the labels array
                for (int i = 0; i < priorityArray.length; i++) {
                    long dateInMilliseconds = priorityArray[i];
                    int hour = AlarmClock.millisecondsToHours(dateInMilliseconds);
                    int minute = AlarmClock.millisecondsToMinutes(dateInMilliseconds);

                    String label = String.format("%02d:%02d", hour, minute);
                    labels[i] = label;
                }

                final JComboBox alarmList = new JComboBox(labels);
                final JSpinner hours = new JSpinner(modelHours);
                final JSpinner minutes = new JSpinner(modelMinutes);

                // action listener to input hours and minutes of selected alarm to JSpinner fields
                class alarmListListener implements ActionListener {

                    public void actionPerformed(ActionEvent event) {

                        int position = alarmList.getSelectedIndex();
                        long dateInMilliseconds = priorityArray[position];
                        int hour = AlarmClock.millisecondsToHours(dateInMilliseconds);
                        int minute = AlarmClock.millisecondsToMinutes(dateInMilliseconds);

                        hours.setValue(hour);
                        minutes.setValue(minute);
                    }
                
                }

                if (!AlarmClock.isEmpty()) {
                    alarmList.setSelectedIndex(0); // runs the event listener for the first item
                }

                // custom names of the buttons
                Object[] options = {"Delete",
                    "Edit",
                    "Cancel"};

                final JComponent[] inputs = new JComponent[]{
                    new JLabel("Select Alarm"),
                    alarmList,
                    new JLabel("Hours"),
                    hours,
                    new JLabel("Minutes"),
                    minutes
                };

                int result = JOptionPane.showOptionDialog(null, inputs, "Edit Alarm",
                        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, 0);

                if (result == JOptionPane.YES_OPTION) { try {
                    // deletes the alarm when user clicks on Delete button
                    AlarmClock.removeChoose(alarmList.getSelectedIndex());
                    } catch (QueueUnderflowException ex) {
                        Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (result == JOptionPane.NO_OPTION) { try {
                    // edits the alarm when user clicks on Edit button
                    AlarmClock.removeChoose(alarmList.getSelectedIndex()); // removes the chosen alarm
                    } catch (QueueUnderflowException ex) {
                        Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    int hour = (int) hours.getValue();
                    int minute = (int) minutes.getValue();

                    AlarmClock.addAlarm(hour, minute); // adds a new edited alarm
                }
            }
        }
        listAlarm.addActionListener(new ListAlarmListener());
        actionMenu.add(listAlarm);

        frame.pack();
        frame.setVisible(true);
    }
// activate the alarm with a pop up comming to say wake up

    // when the update happen check if it's time for the alarm 
    public void update(Observable o, Object arg) {
        panel.repaint();

    }

}

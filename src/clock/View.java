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

/**
 *
 * @author LUCIE
 */
public class View implements Observer {

    ClockPanel panel;

    /**
     *
     * @param model
     */
    public View(Model model) {

        final SortedArrayPriorityQueue arrayAlarm = model.getArray();
        JFrame frame = new JFrame();
        panel = new ClockPanel(model);
        //frame.setContentPane(panel);
        frame.setTitle("Java Clock");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Start of border layout code
        // I've just put a single button in each of the border positions:
        // PAGE_START (i.e. top), PAGE_END (bottom), LINE_START (left) and
        // LINE_END (right). You can omit any of these, or replace the button
        // with something else like a label or a menu bar. Or maybe you can
        // figure out how to pack more than one thing into one of those
        // positions. This is the very simplest border layout possible, just
        // to help you get started.
        Container pane = frame.getContentPane();

        JButton button = new JButton("Button 1 (PAGE_START)");
        pane.add(button, BorderLayout.PAGE_START);

        panel.setPreferredSize(new Dimension(200, 200));
        pane.add(panel, BorderLayout.CENTER);

        // creation of the update about the alarm
        JPanel jPanel2 = new JPanel();

        JLabel jLabel4 = new JLabel();
        jLabel4.setText("Next Alarm: ");
        jPanel2.add(jLabel4);

        final JLabel jLabel5 = new JLabel();
        jLabel5.setText("Alarm Not Set");
        jPanel2.add(jLabel5);

        pane.add(jPanel2, BorderLayout.LINE_START);

        //Creation of the bottom of the clock
        JPanel jPanel1 = new JPanel();

        JLabel jLabel1 = new JLabel();
        jLabel1.setText("Set Alaram:");
        jPanel1.add(jLabel1);

        JLabel jLabel2 = new JLabel();
        jLabel2.setText("HH:");
        jPanel1.add(jLabel2);

        final JTextField jTextField1 = new JTextField(2);
        jPanel1.add(jTextField1);

        JLabel jLabel3 = new JLabel();
        jLabel3.setText("MM:");
        jPanel1.add(jLabel3);

        final JTextField jTextField2 = new JTextField(2);
        jPanel1.add(jTextField2);

        //to save an alarm
        JButton jButton1 = new JButton();
        jButton1.setText("Set Alarm");
        jPanel1.add(jButton1);

        class jButton1Listener implements ActionListener {

            public void actionPerformed(ActionEvent event) {

                //put the new alarm in the array
                
                int hh = Integer.parseInt(jTextField1.getText());
                int mm = Integer.parseInt(jTextField2.getText());
                Alarm alarm = new Alarm(hh, mm);
                int priority = hh * 60 + mm;
                try {
                    arrayAlarm.add(alarm, priority);
                } catch (QueueOverflowException e) {
                    System.out.println("Add operation failed: " + e);
                }
                //put the field to nothing
                jTextField1.setText("");
                jTextField2.setText("");
                try {
                    //update the status of the next alarm
                    jLabel5.setText(arrayAlarm.head().toString());
                } catch (QueueUnderflowException ex) {
                    Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
                }
          

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
                 if (arrayAlarm.isEmpty()==true){
                 JOptionPane.showMessageDialog(panel, "No alarm created");
                 }
                 else{
                String showInputDialog = JOptionPane.showInputDialog(null, arrayAlarm.toString() + "Which alarm do you want to change", "1326");
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

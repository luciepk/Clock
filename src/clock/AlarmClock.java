package clock;

import queuemanager.PriorityQueue;
import queuemanager.QueueUnderflowException;
import queuemanager.SortedLinkedPriorityQueue;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import queuemanager.QueueOverflowException;

// help to manage the alarm with all the function we need
class AlarmClock {
    static PriorityQueue priorityQueue = new SortedLinkedPriorityQueue();
    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd'T'HHmmss");

   
    static void checkAlarms(int currentHour, int currentMinute) throws QueueUnderflowException {
        if (priorityQueue.isEmpty()) {
            return; // look if there is any alarm
        }

        long dateInMilliseconds = Long.parseLong(priorityQueue.head().toString());
        int hour = millisecondsToHours(dateInMilliseconds);
        int minute = millisecondsToMinutes(dateInMilliseconds);

        
        if (currentHour == hour && currentMinute == minute) {
            priorityQueue.remove();// remove the alarm once use
            JOptionPane.showMessageDialog(null, "Alarm activated.",
                    "Alarm Activated", JOptionPane.INFORMATION_MESSAGE);// activate the alarm
        }
    }


 
    static String generateICalendar() {// create an I calender
        Long[] priorities = priorityQueue.getPriorityArray();// to get all the priority

        StringBuilder content = new StringBuilder("BEGIN:VCALENDAR\r\n" +
                "VERSION:2.0\r\n" +
                "PRODID:Alarm Clock\r\n");// what the content is going to be

        for (int i = 0; i < priorities.length; i++) {
            Calendar calendar = Calendar.getInstance();// get the time
            calendar.setTimeInMillis(priorities[i]);// put in milli
            String eventDate = formatter.format(calendar.getTime());

            content.append("BEGIN:VEVENT\r\n" +
                    "UID:" + i + "\r\n" +
                    "DTSTAMP:" + formatter.format(new Date()) + "Z\r\n" +
                    "DTSTART:" + eventDate + "Z\r\n" +
                    "DTEND:" + eventDate + "Z\r\n" +
                    "END:VEVENT\r\n");
        }

        content.append("END:VCALENDAR");

        return content.toString();
    }


     
    static void saveICalendar() {//once you create it you save it
        String iCalendar = generateICalendar();

        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("iCalendar files", "ics");
        chooser.setFileFilter(filter);
        chooser.setSelectedFile(new File(formatter.format(new Date()) + ".ics"));
        int retrieval = chooser.showSaveDialog(null);
        if (retrieval == JFileChooser.APPROVE_OPTION) {
            try (FileWriter fw = new FileWriter(chooser.getSelectedFile())) {
                fw.write(iCalendar);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * A dialogue box allowing users to load the alarms from an iCalendar file.
     * 
     */
    static void loadICalendar() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("iCalendar files", "ics");
        chooser.setFileFilter(filter);
        int retrieval = chooser.showOpenDialog(null);
        try {
            if (retrieval == JFileChooser.APPROVE_OPTION) {
                Scanner scanner = new Scanner(chooser.getSelectedFile());
                long datestamp = new Date().getTime();

                while (scanner.hasNextLine()) {
                    String line = scanner.next();
                    if (line.startsWith("DTSTART:")) {
                        Date date = formatter.parse(line.substring(8));
                        long dateInMilliseconds = date.getTime();

                        if (dateInMilliseconds > datestamp) {
                            Alarm alarm = new Alarm(dateInMilliseconds);// create the alarm
                            priorityQueue.add(alarm, dateInMilliseconds);
                        }
                    }
                }

                scanner.close();
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Invalid file!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

   // add alarm to the queue
    static void addAlarm(int hour, int minute) {
        long dateInMilliseconds = AlarmClock.getDateInMillisecondsForAlarm(hour, minute);// put the hour and min into milli

        Alarm alarm = new Alarm(dateInMilliseconds);// create the alarm
        priorityQueue.add(alarm, dateInMilliseconds);
    }

   

 
    static long getDateInMillisecondsForAlarm(int hour, int minute) {//get the current hour to be able to compare it
        LocalDateTime currentDate = LocalDateTime.now();

        // if the alarm date will be set in the past, add one day to the date
        if (currentDate.getHour() >= hour && currentDate.getMinute() >= minute) {
            currentDate = LocalDateTime.from(currentDate).plusDays(1);
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String formattedDate = dtf.format(currentDate);
        String myDate = String.format("%s %02d:%02d:00", formattedDate, hour, minute);

        return LocalDateTime.parse(myDate, DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"))
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
    }


    static Long[] getPriorityArray() {// get all the priority
        return priorityQueue.getPriorityArray();
    }

    static boolean isEmpty() {// check if it is empty
        return priorityQueue.isEmpty();
    }


    static String getHead() throws QueueUnderflowException {// get the first alarm
        if (isEmpty()) {
            return "0";
        }

        return priorityQueue.head().toString();
    }

    
    static void removeChoose(int position) throws QueueUnderflowException {// remove from the choosen pos
        priorityQueue.removeChoose(position);
    }

 
    static int millisecondsToHours(long dateInMilliseconds) {
        int hours = (int) (dateInMilliseconds / (1000 * 60 * 60)) % 24 + 2 ;// put it in hour

        return (hours == 24) ? 0 : hours;
    }

 
    static int millisecondsToMinutes(long dateInMilliseconds) {
        return (int) (dateInMilliseconds / (1000 * 60)) % 60;// put it in minute
    }
}
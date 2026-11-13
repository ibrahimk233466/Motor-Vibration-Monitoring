package com.ece;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class VibrationMonitor {

    public static double calculateAverage(double[] readings) {
        if (readings == null || readings.length == 0) return 0.0;
        double sum = 0;
        for (double r : readings) {
            sum += r;
        }
        return sum / readings.length;
    }

    public static String classifyCondition(double avgVibration) {
        if (avgVibration < 2.8) {
            return "Normal";
        } else if (avgVibration <= 7.1) {
            return "Warning";
        } else {
            return "Severe";
        }
    }

    public static void generateHtmlReport(String motorId, double avg, String status) {
        try {
            File dir = new File("reports");
            if (!dir.exists()) dir.mkdirs();

            String color = status.equals("Normal") ? "green" : (status.equals("Warning") ? "orange" : "red");
            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            String html = "<!DOCTYPE html><html><head><title>Motor Vibration Report</title>" +
                    "<style>body{font-family:Arial;margin:40px;} table{border-collapse:collapse;width:50%;}" +
                    "th,td{border:1px solid #ddd;padding:10px;text-align:left;} th{background:#333;color:white;}</style></head><body>" +
                    "<h2>Industrial Motor Vibration Maintenance Report</h2>" +
                    "<p><b>Generated On:</b> " + time + "</p>" +
                    "<table><tr><th>Parameter</th><th>Value</th></tr>" +
                    "<tr><td>Motor ID</td><td>" + motorId + "</td></tr>" +
                    "<tr><td>Average Vibration (mm/s)</td><td>" + String.format("%.2f", avg) + "</td></tr>" +
                    "<tr><td>Machine Condition</td><td style='color:" + color + ";font-weight:bold;'>" + status + "</td></tr>" +
                    "</table></body></html>";

            FileWriter writer = new FileWriter("reports/maintenance_report.html");
            writer.write(html);
            writer.close();
            System.out.println("HTML Report generated successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String motorId = System.getProperty("motorId", "MOTOR-IND-01");
        String mode = System.getProperty("testMode", "NORMAL");

        double[] sensorReadings;
        if (mode.equalsIgnoreCase("SEVERE")) {
            sensorReadings = new double[]{8.2, 9.1, 7.8, 8.9, 9.5};
        } else if (mode.equalsIgnoreCase("WARNING")) {
            sensorReadings = new double[]{4.1, 5.2, 4.8, 6.0, 5.5};
        } else {
            sensorReadings = new double[]{1.2, 1.5, 1.8, 2.1, 1.4};
        }

        double avg = calculateAverage(sensorReadings);
        String condition = classifyCondition(avg);

        System.out.println("Motor ID: " + motorId);
        System.out.println("Average Vibration: " + avg + " mm/s");
        System.out.println("Condition: " + condition);

        generateHtmlReport(motorId, avg, condition);
    }
}
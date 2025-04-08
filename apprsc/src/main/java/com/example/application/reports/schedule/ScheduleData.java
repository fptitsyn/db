package com.example.application.reports.schedule;

public class ScheduleData {
    private String employeeName;
    private int time09_10;
    private int time10_11;
    private int time11_12;
    private int time12_13;
    private int time14_15;
    private int time15_16;
    private int time16_17;
    private int time17_18;
    private int total;

    // Конструктор, геттеры и сеттеры
    public ScheduleData(String employeeName, int time09_10, int time10_11, int time11_12,
                        int time12_13, int time14_15, int time15_16, int time16_17,
                        int time17_18, int total) {
        this.employeeName = employeeName;
        this.time09_10 = time09_10;
        this.time10_11 = time10_11;
        this.time11_12 = time11_12;
        this.time12_13 = time12_13;
        this.time14_15 = time14_15;
        this.time15_16 = time15_16;
        this.time16_17 = time16_17;
        this.time17_18 = time17_18;
        this.total = total;
    }

    // Геттеры и сеттеры для всех полей

    public String getEmployeeName() {
        return employeeName;
    }

    public int getTime09_10() {
        return time09_10;
    }

    public int getTime10_11() {
        return time10_11;
    }

    public int getTime11_12() {
        return time11_12;
    }

    public int getTime12_13() {
        return time12_13;
    }

    public int getTime14_15() {
        return time14_15;
    }

    public int getTime15_16() {
        return time15_16;
    }

    public int getTime16_17() {
        return time16_17;
    }

    public int getTime17_18() {
        return time17_18;
    }

    public int getTotal() {
        return total;
    }
}

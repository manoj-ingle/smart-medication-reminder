package com.example.medialert;

public class MedicineModel {

    private int id;
    private String name;
    private String dose;
    private String time;
    private String repeat; // ⭐ NEW

    public MedicineModel(int id, String name, String dose, String time, String repeat) {
        this.id = id;
        this.name = name;
        this.dose = dose;
        this.time = time;
        this.repeat = repeat;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDose() { return dose; }
    public String getTime() { return time; }
    public String getRepeat() { return repeat; } // ⭐

}
package com.mycompany.quanlykhachsan.model;

public class Equipment {
    private String equipmentID;
    private String name;
    private String description;

    public Equipment(String equipmentID, String name, String description) {
        this.equipmentID = equipmentID;
        this.name = name;
        this.description = description;
    }

    // Getter và Setter
    public String getEquipmentID() { return equipmentID; }
    public String getName() { return name; }
    public String getDescription() { return description; }
}

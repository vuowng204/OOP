package com.mycompany.quanlykhachsan.model;

public class RoomType {
    private String typeID;
    private String description;

    public RoomType(String typeID, String description) {
        this.typeID = typeID;
        this.description = description;
    }

    // Getter và Setter
    public String getTypeID() { return typeID; }
    public String getDescription() { return description; }
}

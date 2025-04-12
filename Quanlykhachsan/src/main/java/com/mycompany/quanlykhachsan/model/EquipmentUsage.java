package com.mycompany.quanlykhachsan.model;

public class EquipmentUsage {
    private Room room;
    private Equipment equipment;
    private int quantity;

    public EquipmentUsage(Room room, Equipment equipment, int quantity) {
        this.room = room;
        this.equipment = equipment;
        this.quantity = quantity;
    }

    // Getter
    public Room getRoom() {
        return room;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public int getQuantity() {
        return quantity;
    }

    // Setter
    public void setRoom(Room room) {
        this.room = room;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

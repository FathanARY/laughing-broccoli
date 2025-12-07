/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.sql.Timestamp;

public class Room {
    private int id;
    private String roomNumber;
    private int roomTypeId;       // FK: Sesuai kolom database 'room_type_id'
    private String roomTypeName;  // Helper: Untuk menampung nama tipe (e.g., "Deluxe") dari hasil JOIN
    private double price;
    private String status;
    private Timestamp createdAt;
    
    // Constructor Kosong
    public Room() {}
    
    // Constructor Lengkap (Biasanya dipakai saat SELECT dengan JOIN)
    public Room(int id, String roomNumber, int roomTypeId, String roomTypeName, double price, String status) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.roomTypeId = roomTypeId;
        this.roomTypeName = roomTypeName;
        this.price = price;
        this.status = status;
    }
    
    // Constructor Simpel (Biasanya dipakai saat INSERT data baru)
    public Room(String roomNumber, int roomTypeId, double price, String status) {
        this.roomNumber = roomNumber;
        this.roomTypeId = roomTypeId;
        this.price = price;
        this.status = status;
    }
    
    // --- GETTERS & SETTERS ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(int roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public String getRoomTypeName() {
        return roomTypeName;
    }

    public void setRoomTypeName(String roomTypeName) {
        this.roomTypeName = roomTypeName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    // Override toString untuk memudahkan debugging atau tampilan di List
    @Override
    public String toString() {
        return roomNumber + " - " + roomTypeName;
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author Arkan Bintang
 */
import java.sql.Timestamp;

public class RoomType {
    private int id;
    private String name;
    private String description;
    private int capacity;
    private double basePrice;
    private Timestamp createdAt;

    // Constructor Kosong
    public RoomType() {}

    // Constructor Lengkap
    public RoomType(int id, String name, String description, int capacity, double basePrice) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.capacity = capacity;
        this.basePrice = basePrice;
    }

    // --- GETTERS & SETTERS ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    // Override toString agar mudah ditampilkan di ComboBox (Pilihan Tipe Kamar)
    @Override
    public String toString() {
        return this.name; 
    }
}

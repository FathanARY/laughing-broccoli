/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import config.DatabaseConnection;
import models.Room;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomController {
    
    // Mengambil semua data kamar (dengan JOIN ke room_types)
    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        // Query menggunakan JOIN untuk mengambil nama tipe kamar dari tabel sebelah
        String sql = "SELECT r.id, r.room_number, r.room_type_id, rt.name AS type_name, r.price, r.status " +
                     "FROM rooms r " +
                     "JOIN room_types rt ON r.room_type_id = rt.id " +
                     "ORDER BY r.room_number";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Room room = new Room(
                    rs.getInt("id"),
                    rs.getString("room_number"),
                    rs.getInt("room_type_id"),   // Ambil ID Tipe
                    rs.getString("type_name"),   // Ambil Nama Tipe (dari JOIN)
                    rs.getDouble("price"),
                    rs.getString("status")
                );
                rooms.add(room);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return rooms;
    }
    
    // Mengambil satu kamar berdasarkan Nomor Kamar
    public Room getRoomByNumber(String roomNumber) {
        String sql = "SELECT r.id, r.room_number, r.room_type_id, rt.name AS type_name, r.price, r.status " +
                     "FROM rooms r " +
                     "JOIN room_types rt ON r.room_type_id = rt.id " +
                     "WHERE r.room_number = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, roomNumber);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Room(
                    rs.getInt("id"),
                    rs.getString("room_number"),
                    rs.getInt("room_type_id"),
                    rs.getString("type_name"),
                    rs.getDouble("price"),
                    rs.getString("status")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Method Seeding (Opsional: Jika ingin isi data otomatis lewat kodingan)
    public void seedRooms() {
        if (!getAllRooms().isEmpty()) return;
        
        String sql = "INSERT INTO rooms (room_number, room_type_id, price, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // 101-105: Standard Room (ID 1)
            for (int i = 101; i <= 105; i++) {
                pstmt.setString(1, String.valueOf(i));
                pstmt.setInt(2, 1); // ID 1 = Standard
                pstmt.setDouble(3, 400000.0);
                pstmt.setString(4, "available");
                pstmt.addBatch();
            }
            
            // 201-205: Deluxe Room (ID 2)
            for (int i = 201; i <= 205; i++) {
                pstmt.setString(1, String.valueOf(i));
                pstmt.setInt(2, 2); // ID 2 = Deluxe
                pstmt.setDouble(3, 850000.0);
                pstmt.setString(4, "available");
                pstmt.addBatch();
            }
            
            pstmt.executeBatch();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Mengambil kamar yang statusnya 'Available'
    public List<Room> getAvailableRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT r.id, r.room_number, r.room_type_id, rt.name AS type_name, r.price, r.status " +
                     "FROM rooms r " +
                     "JOIN room_types rt ON r.room_type_id = rt.id " +
                     "WHERE r.status = 'available' " +
                     "ORDER BY r.room_number";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Room room = new Room(
                    rs.getInt("id"),
                    rs.getString("room_number"),
                    rs.getInt("room_type_id"),
                    rs.getString("type_name"),
                    rs.getDouble("price"),
                    rs.getString("status")
                );
                rooms.add(room);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return rooms;
    }
    
    // Menambah Kamar Baru (Admin)
    public boolean addRoom(String roomNumber, int roomTypeId, double price, String status) {
        String sql = "INSERT INTO rooms (room_number, room_type_id, price, status) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, roomNumber);
            pstmt.setInt(2, roomTypeId); // Menggunakan ID, bukan String
            pstmt.setDouble(3, price);
            pstmt.setString(4, status);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Update Status Kamar (Misal: saat Check-In/Check-Out)
    public boolean updateRoomStatus(int id, String status) {
        String sql = "UPDATE rooms SET status = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            pstmt.setInt(2, id);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update Data Kamar (Admin)
    public boolean updateRoom(int id, String roomNumber, int roomTypeId, double price, String status) {
        String sql = "UPDATE rooms SET room_number = ?, room_type_id = ?, price = ?, status = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, roomNumber);
            pstmt.setInt(2, roomTypeId); // Update ID Tipe
            pstmt.setDouble(3, price);
            pstmt.setString(4, status);
            pstmt.setInt(5, id);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Hapus Kamar
    public boolean deleteRoom(int id) {
        String sql = "DELETE FROM rooms WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Ambil Kamar by ID
    public Room getRoomById(int id) {
        String sql = "SELECT r.id, r.room_number, r.room_type_id, rt.name AS type_name, r.price, r.status " +
                     "FROM rooms r " +
                     "JOIN room_types rt ON r.room_type_id = rt.id " +
                     "WHERE r.id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Room(
                    rs.getInt("id"),
                    rs.getString("room_number"),
                    rs.getInt("room_type_id"),
                    rs.getString("type_name"),
                    rs.getDouble("price"),
                    rs.getString("status")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
}
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
    
    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT r.*, rt.name as room_type_name FROM rooms r JOIN room_types rt ON r.room_type_id = rt.id ORDER BY r.room_number";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Room room = new Room(
                    rs.getInt("id"),
                    rs.getString("room_number"),
                    rs.getString("room_type_name"),
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
    
    public Room getRoomByNumber(String roomNumber) {
        String sql = "SELECT r.*, rt.name as room_type_name FROM rooms r JOIN room_types rt ON r.room_type_id = rt.id WHERE r.room_number = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, roomNumber);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Room(
                    rs.getInt("id"),
                    rs.getString("room_number"),
                    rs.getString("room_type_name"),
                    rs.getDouble("price"),
                    rs.getString("status")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void seedRooms() {
        if (!getAllRooms().isEmpty()) return;
        
        String sql = "INSERT INTO rooms (room_number, room_type_id, price, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // 101-120: Standard (Type 1)
            for (int i = 101; i <= 120; i++) {
                pstmt.setString(1, String.valueOf(i));
                pstmt.setInt(2, 1);
                pstmt.setDouble(3, 400000.0);
                pstmt.setString(4, "available");
                pstmt.addBatch();
            }
            
            // 121-140: Deluxe (Type 2)
            for (int i = 121; i <= 140; i++) {
                pstmt.setString(1, String.valueOf(i));
                pstmt.setInt(2, 2);
                pstmt.setDouble(3, 850000.0);
                pstmt.setString(4, "available");
                pstmt.addBatch();
            }
            
            // 141-150: Suite (Type 3)
            for (int i = 141; i <= 150; i++) {
                pstmt.setString(1, String.valueOf(i));
                pstmt.setInt(2, 3);
                pstmt.setDouble(3, 2500000.0);
                pstmt.setString(4, "available");
                pstmt.addBatch();
            }
            
            pstmt.executeBatch();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public List<Room> getAvailableRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT r.*, rt.name as room_type_name FROM rooms r JOIN room_types rt ON r.room_type_id = rt.id WHERE r.status = 'available' ORDER BY r.room_number";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Room room = new Room(
                    rs.getInt("id"),
                    rs.getString("room_number"),
                    rs.getString("room_type"),
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
    
    public boolean addRoom(String roomNumber, String roomType, double price, String status) {
        String sql = "INSERT INTO rooms (room_number, room_type, price, status) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, roomNumber);
            pstmt.setString(2, roomType);
            pstmt.setDouble(3, price);
            pstmt.setString(4, status);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
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

    public boolean updateRoom(int id, String roomNumber, String roomType, double price, String status) {
        String sql = "UPDATE rooms SET room_number = ?, room_type = ?, price = ?, status = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, roomNumber);
            pstmt.setString(2, roomType);
            pstmt.setDouble(3, price);
            pstmt.setString(4, status);
            pstmt.setInt(5, id);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
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
    
    public Room getRoomById(int id) {
        String sql = "SELECT * FROM rooms WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Room(
                    rs.getInt("id"),
                    rs.getString("room_number"),
                    rs.getString("room_type"),
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
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import config.DatabaseConnection;
import models.Reservation;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationController {
    
    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT r.*, ro.room_number FROM reservations r " +
                     "JOIN rooms ro ON r.room_id = ro.id ORDER BY r.id DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Reservation reservation = new Reservation();
                reservation.setId(rs.getInt("id"));
                reservation.setCustomerName(rs.getString("customer_name"));
                reservation.setCustomerContact(rs.getString("customer_contact"));
                reservation.setCheckInDate(rs.getDate("check_in_date"));
                reservation.setCheckOutDate(rs.getDate("check_out_date"));
                reservation.setRoomId(rs.getInt("room_id"));
                reservation.setStatus(rs.getString("status"));
                reservation.setTotalAmount(rs.getDouble("total_amount"));
                reservation.setRoomNumber(rs.getString("room_number"));
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return reservations;
    }
    
    public List<Reservation> getReservationsByStatus(String status) {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT r.*, ro.room_number FROM reservations r " +
                     "JOIN rooms ro ON r.room_id = ro.id WHERE r.status = ? ORDER BY r.check_in_date";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Reservation reservation = new Reservation();
                reservation.setId(rs.getInt("id"));
                reservation.setCustomerName(rs.getString("customer_name"));
                reservation.setCustomerContact(rs.getString("customer_contact"));
                reservation.setCheckInDate(rs.getDate("check_in_date"));
                reservation.setCheckOutDate(rs.getDate("check_out_date"));
                reservation.setRoomId(rs.getInt("room_id"));
                reservation.setStatus(rs.getString("status"));
                reservation.setTotalAmount(rs.getDouble("total_amount"));
                reservation.setRoomNumber(rs.getString("room_number"));
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return reservations;
    }
    
    public Reservation getActiveReservationByRoomId(int roomId) {
        String sql = "SELECT r.*, ro.room_number FROM reservations r " +
                     "JOIN rooms ro ON r.room_id = ro.id " +
                     "WHERE r.room_id = ? AND r.status IN ('booked', 'checked_in')";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, roomId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Reservation reservation = new Reservation();
                reservation.setId(rs.getInt("id"));
                reservation.setCustomerName(rs.getString("customer_name"));
                reservation.setCustomerContact(rs.getString("customer_contact"));
                reservation.setCheckInDate(rs.getDate("check_in_date"));
                reservation.setCheckOutDate(rs.getDate("check_out_date"));
                reservation.setRoomId(rs.getInt("room_id"));
                reservation.setStatus(rs.getString("status"));
                reservation.setTotalAmount(rs.getDouble("total_amount"));
                reservation.setRoomNumber(rs.getString("room_number"));
                return reservation;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean cancelReservation(int reservationId) {
        String sql = "UPDATE reservations SET status = 'cancelled' WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, reservationId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addReservation(String customerName, String customerContact, 
                                   Date checkInDate, Date checkOutDate, 
                                   int roomId, double totalAmount, int userId) {
        String sql = "INSERT INTO reservations (customer_name, customer_contact, check_in_date, " +
                     "check_out_date, room_id, status, total_amount, user_id) VALUES (?, ?, ?, ?, ?, 'booked', ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customerName);
            pstmt.setString(2, customerContact);
            pstmt.setDate(3, checkInDate);
            pstmt.setDate(4, checkOutDate);
            pstmt.setInt(5, roomId);
            pstmt.setDouble(6, totalAmount);
            pstmt.setInt(7, userId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateReservation(int id, String customerName, String customerContact, 
                                      Date checkInDate, Date checkOutDate, int roomId, double totalAmount) {
        String sql = "UPDATE reservations SET customer_name = ?, customer_contact = ?, " +
                     "check_in_date = ?, check_out_date = ?, room_id = ?, total_amount = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customerName);
            pstmt.setString(2, customerContact);
            pstmt.setDate(3, checkInDate);
            pstmt.setDate(4, checkOutDate);
            pstmt.setInt(5, roomId);
            pstmt.setDouble(6, totalAmount);
            pstmt.setInt(7, id);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateReservationStatus(int id, String status) {
        String sql = "UPDATE reservations SET status = ? WHERE id = ?";
        
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
    
    public boolean deleteReservation(int id) {
        String sql = "DELETE FROM reservations WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Reservation getReservationById(int id) {
        String sql = "SELECT r.*, ro.room_number FROM reservations r " +
                     "JOIN rooms ro ON r.room_id = ro.id WHERE r.id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Reservation reservation = new Reservation();
                reservation.setId(rs.getInt("id"));
                reservation.setCustomerName(rs.getString("customer_name"));
                reservation.setCustomerContact(rs.getString("customer_contact"));
                reservation.setCheckInDate(rs.getDate("check_in_date"));
                reservation.setCheckOutDate(rs.getDate("check_out_date"));
                reservation.setRoomId(rs.getInt("room_id"));
                reservation.setStatus(rs.getString("status"));
                reservation.setTotalAmount(rs.getDouble("total_amount"));
                reservation.setRoomNumber(rs.getString("room_number"));
                return reservation;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    // Report Methods
    public double getTotalRevenueByDateRange(Date startDate, Date endDate) {
        String sql = "SELECT COALESCE(SUM(total_amount), 0) as total FROM reservations " +
                     "WHERE status = 'checked_out' AND check_out_date BETWEEN ? AND ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDate(1, startDate);
            pstmt.setDate(2, endDate);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0.0;
    }
    
    public int getOccupancyCount() {
        String sql = "SELECT COUNT(*) as count FROM rooms WHERE status = 'occupied'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0;
    }
    
    public int getTotalRoomCount() {
        String sql = "SELECT COUNT(*) as count FROM rooms";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0;
    }
}

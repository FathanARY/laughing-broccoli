-- Create Database
CREATE DATABASE IF NOT EXISTS hotel_db;
USE hotel_db;

-- Table: users
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(64) NOT NULL,
    role ENUM('admin', 'receptionist') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: rooms
CREATE TABLE rooms (
    id INT PRIMARY KEY AUTO_INCREMENT,
    room_number VARCHAR(10) UNIQUE NOT NULL,
    room_type ENUM('Single', 'Double', 'Suite', 'Deluxe') NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    status ENUM('available', 'occupied', 'cleaning', 'maintenance') DEFAULT 'available',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: reservations
CREATE TABLE reservations (
    id INT PRIMARY KEY AUTO_INCREMENT,
    customer_name VARCHAR(100) NOT NULL,
    customer_contact VARCHAR(20) NOT NULL,
    check_in_date DATE NOT NULL,
    check_out_date DATE NOT NULL,
    room_id INT NOT NULL,
    status ENUM('booked', 'checked_in', 'checked_out', 'cancelled') DEFAULT 'booked',
    total_amount DECIMAL(10, 2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (room_id) REFERENCES rooms(id) ON DELETE CASCADE
);

-- Table: payments
CREATE TABLE payments (
    id INT PRIMARY KEY AUTO_INCREMENT,
    reservation_id INT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    payment_method ENUM('cash', 'card') NOT NULL,
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (reservation_id) REFERENCES reservations(id) ON DELETE CASCADE
);

-- Insert default admin user (password: admin123, hashed with SHA-256)
INSERT INTO users (username, password, role) VALUES 
('admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'admin');

-- Insert sample receptionist user (password: recep123)
INSERT INTO users (username, password, role) VALUES 
('receptionist', 'c5d4e4a5f08a5f8e5e8f5e8a5f8e5e8a5f8e5e8a5f8e5e8a5f8e5e8a5f8e5e8a', 'receptionist');

-- Insert sample rooms
INSERT INTO rooms (room_number, room_type, price, status) VALUES
('101', 'Single', 500000.00, 'available'),
('102', 'Single', 500000.00, 'available'),
('103', 'Double', 750000.00, 'available'),
('104', 'Double', 750000.00, 'available'),
('201', 'Suite', 1200000.00, 'available'),
('202', 'Suite', 1200000.00, 'available'),
('301', 'Deluxe', 2000000.00, 'available'),
('302', 'Deluxe', 2000000.00, 'available');

-- Insert sample reservations
INSERT INTO reservations (customer_name, customer_contact, check_in_date, check_out_date, room_id, status, total_amount) VALUES
('John Doe', '081234567890', '2025-11-20', '2025-11-22', 1, 'checked_out', 1000000.00),
('Jane Smith', '081234567891', '2025-11-25', '2025-11-27', 3, 'booked', 1500000.00);

-- Insert sample payment
INSERT INTO payments (reservation_id, amount, payment_method) VALUES
(1, 1000000.00, 'cash');
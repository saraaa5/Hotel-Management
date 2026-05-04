# Hotel Management System

A desktop application for managing hotel operations, built using object-oriented design principles.  
The system supports multiple user roles and handles reservations, room management, pricing, and hotel staff workflows.

## Features

### User roles

- Administrator
- Receptionist
- Housekeeper
- Guest

Each role has access to different functionalities based on permissions.

## Core functionalities

### Reservation Management
- Create reservations
- Approve/reject reservations
- Cancel reservations
- Track reservation statuses:
  - Pending
  - Confirmed
  - Rejected
  - Cancelled

### Room Management
- Manage room types
- Track room availability
- Room assignment during check-in
- Room status tracking:
  - Available
  - Occupied
  - Cleaning

### Check-in / Check-out Workflow
- Guest registration
- Room assignment
- Check-in process
- Check-out process
- Automatic room assignment to housekeepers after checkout

### Housekeeping Management
- Automatically assigns rooms to the least busy housekeeper
- Tracks completed cleaning tasks
- Updates room availability after cleaning

### Pricing System
- Dynamic pricing based on date ranges
- Support for seasonal price changes
- Additional services:
  - breakfast
  - lunch
  - dinner
- Reservation price calculation at booking time

### Reporting & Analytics
- Revenue and expense reports
- Reservation statistics
- Room occupancy tracking
- Housekeeper workload reports

## Architecture Highlights

- Role-based access control
- Separation of concerns
- Modular manager classes
- Enum-based state handling
- GUI implementation using Swing

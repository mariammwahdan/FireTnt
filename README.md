# 🔥 FireTnT

**FireTnT** is a scalable, microservices-based platform for vacation rentals and property bookings across Egypt. Designed with a modular architecture using Spring Boot and secured by Firebase Authentication, the system supports multiple user roles (Guests, Hosts, Admins) with features tailored to each. FireTnT emphasizes performance, security, and usability.

---

## 📌 Table of Contents
- [Features](#features)
- [Tech Stack](#tech-stack)
- [System Architecture](#system-architecture)
- [Getting Started](#getting-started)
- [Project Structure](#project-structure)
- [Future Enhancements](#future-enhancements)
- [Authors](#authors)

---

## ✨ Features

### 🔑 Guest
- Register / Login
- Browse properties
- Book, cancel, and manage reservations
- Submit and read reviews
- Receive notifications

### 🏡 Host
- Manage property listings (Add / Edit / Delete)
- Browse properties
- View bookings

### 🛠️ Admin
- View and manage all users

---

## 🛠️ Tech Stack

| Layer         | Technology                       |
|--------------|----------------------------------|
| Backend       | Spring Boot, Spring Data JPA     |
| Frontend      | Thymeleaf + Tailwind CSS         |
| Database      | PostgreSQL                       |
| Authentication| Firebase Authentication          |
| Caching       | Redis                             |
| Containerization | Docker                        |

---

## 🧱 System Architecture

- **Microservices-based** modular backend
- **Role-based access control** using Firebase ID tokens
- **Redis caching** for fast data access and reduced DB load
- **PostgreSQL** for structured and reliable data storage
- **RESTful APIs** with rate limiting and validation
- **Real-time notifications** for user interactions

---

## 🚀 Getting Started

### Prerequisites
- Java 17+
- PostgreSQL 14+
- Redis
- Docker
- Firebase Project & Service Account Key

### Setup Instructions

1. **Clone the Repository**
   ```bash
   git clone https://github.com/your-username/FireTnT.git
   cd FireTnT

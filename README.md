# 🧓 Smart Elder Care System – Backend

## 📌 Project Overview

The **Smart Elder Care System Backend** is a server-side application designed to manage elderly care operations efficiently.

It provides secure RESTful APIs for managing **elders, caregivers, health records, medication schedules, administrators, alerts**, and monitoring data, ensuring reliable communication between the frontend and the database.

---

## 🎯 Objectives

* Centralized management of elder care data
* Secure handling of health and personal information
* Real-time data access for caregivers and elders
* Scalable backend architecture for future enhancements

---

## 🏗️ System Architecture

* **Client:** Web Frontend
* **Backend:** RESTful API
* **Database:** MySQL (Relational Database)
* **Authentication:** JWT-based authentication
* **Deployment:** Local / Cloud-based server

---

## ⚙️ Technologies Used

* **Backend Framework:** Spring Boot / Flask
* **Programming Languages:** Java / Python / JavaScript
* **Database:** MySQL
* **ORM:** Hibernate / SQLAlchemy / Sequelize
* **Security:** JWT, Role-Based Access Control (RBAC)
* **API Testing:** Postman
* **Version Control:** Git & GitHub

---

## 🔑 User Roles

### 👨‍💼 Admin

* Manage system users

### 🧓 Elder

* Add health records
* Add medication schedules (reminders)
* Request caregivers
* View health records, caregivers, and medication schedules

### 👩‍⚕️ Caregiver

* Accept or reject elder care requests

---

## 🧩 Core Features

* Elder profile management
* Caregiver management
* Health record tracking (BP, Sugar Level, Temperature, etc.)
* Medication scheduling and reminders
* Appointment management
* Emergency alert handling
* Secure authentication and authorization
* RESTful API endpoints

---

## 🔐 Authentication & Authorization

* JWT-based authentication
* Role-based access control (Admin, Elder, Caregiver)
* Protected API endpoints
* Secure password hashing

---

## 🗄️ Database Design

### Main Entities

* Elders
* Care_Giver
* Elder_Profile
* Health
* Medication_Schedule

---

## 🚀 Installation & Setup

### Prerequisites

* Java 21 **or** Python 3.x
* MySQL
* Git

### Steps

```bash
git clone https://github.com/hirush2001/Elder_Care_Backend.git
cd Elder_Care_Backend
```

### Configure Database

Update the database configuration in:

* `application.properties` (Spring Boot)
* `.env` (if applicable)

### Run the Project

```bash
mvn spring-boot:run
```

---

## 🧪 Testing

* API testing using Postman
* Unit and integration testing
* Input validation and error handling implemented

---

## 🛡️ Security Considerations

* Input validation
* Encrypted password storage
* Secure API access
* CORS configuration

---

## 📈 Future Enhancements

* Real-time monitoring using IoT devices
* AI-based health prediction
* Push notifications
* Mobile application integration
* Analytics and reporting dashboard

---

## 📄 License

This project is developed for **academic purposes** and is open for learning and improvements.

---

## 👨‍💻 Author

**Kavindu Hirushan**
Computer Science & Statistics
Full Stack Developer

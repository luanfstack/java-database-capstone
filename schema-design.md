## MySQL Database Design

### Table: appointments
- id: INT, Primary Key, Auto Increment
- doctor_id: INT, Foreign Key → doctors(id)
- patient_id: INT, Foreign Key → patients(id)
- appointment_time: DATETIME, Not Null
- status: INT (0 = Scheduled, 1 = Completed, 2 = Cancelled)

### Table: patients
- id: INT, Primary Key, Auto Increment
- first_name: VARCHAR(50), Not Null
- last_name: VARCHAR(50), Not Null
- email: VARCHAR(100), Unique, Not Null
- password_hash: CHAR(60), Not Null
- phone: VARCHAR(15)
- address: VARCHAR(255)
- created_at: DATETIME, Default CURRENT_TIMESTAMP
- updated_at: DATETIME, Default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP

### Table: doctors
- id: INT, Primary Key, Auto Increment
- first_name: VARCHAR(50), Not Null
- last_name: VARCHAR(50), Not Null
- email: VARCHAR(100), Unique, Not Null
- password_hash: CHAR(60), Not Null
- specialty: VARCHAR(100), Not Null
- phone: VARCHAR(15)
- address: VARCHAR(255)
- license_number: VARCHAR(50), Unique, Not Null
- created_at: DATETIME, Default CURRENT_TIMESTAMP
- updated_at: DATETIME, Default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP

### Table: admins
- id: INT, Primary Key, Auto Increment
- username: VARCHAR(50), Unique, Not Null
- password_hash: CHAR(60), Not Null
- role: VARCHAR(50), Not Null
- created_at: DATETIME, Default CURRENT_TIMESTAMP
- updated_at: DATETIME, Default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP

## MongoDB Collection Design

```json
{
  "_id": "ObjectId('64abc123456')",
  "patientName": "John Smith",
  "appointmentId": 51,
  "medication": "Paracetamol",
  "dosage": "500mg",
  "doctorNotes": "Take 1 tablet every 6 hours.",
  "refillCount": 2,
  "pharmacy": {
    "name": "Walgreens SF",
    "location": "Market Street"
  }
}
```

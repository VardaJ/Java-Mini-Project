# 🎮 KBC Game (Java GUI + JDBC + MySQL)

This is a Java-based KBC (Kaun Banega Crorepati)-style quiz game with a Swing GUI and MySQL database integration. The application connects to a database to load questions and allows users to play the game with lifelines.

---

## 🚀 How to Run

### ✅ Prerequisites

- Java JDK (8 or above)  
- MySQL Server installed and running  
- MySQL JDBC Connector (e.g., `mysql-connector-java-8.0.xx.jar`)  

---

## 📦 Setup Steps

1. **Clone or download the project**

2. **Setup database in MySQL**  
   *In MySQL terminal:*  
   ```sql
   CREATE DATABASE newdatabase;
   USE newdatabase;
   ```
   *Create table for storing questions:*  
   ```sql
   CREATE TABLE questions (
     id INT AUTO_INCREMENT PRIMARY KEY,
     question_text TEXT NOT NULL,
     option_a VARCHAR(255),
     option_b VARCHAR(255),
     option_c VARCHAR(255),
     option_d VARCHAR(255),
     correct_answer CHAR(1) -- A/B/C/D
   );
   ```
   *Insert sample question sets:*  
   ```sql
   INSERT INTO questions (question_text, option_a, option_b, option_c, option_d, correct_answer) VALUES
   ('What is the capital of France?', 'Paris', 'Berlin', 'Madrid', 'London', 'A'),
   ('Which planet is known as the Red Planet?', 'Earth', 'Mars', 'Jupiter', 'Saturn', 'B'),
   ('Who wrote Hamlet?', 'Dante', 'Homer', 'Shakespeare', 'Plato', 'C');
   ```

3. **Database Connection**  
   Default DB connection settings:  
   - **Host**: localhost  
   - **Port**: 3306  
   - **User**: root  
   - **Password**: tiger  
   - **Database**: newdatabase  

   You can change these in the `createMySQLConnection()` method in `KBCGame.java`.

4. **Add MySQL Connector to classpath and Run**

   If you're compiling manually:  
   ```bash
   javac -cp .:mysql-connector-java-8.0.xx.jar JavaFrontUpdated.java
   java -cp .:mysql-connector-java-8.0.xx.jar JavaFrontUpdated
   ```

   > On Windows, replace `:` with `;` in the classpath.

---

## 👩‍💻 Project by

1. Varda Joshi (23BCE10345)  
2. Harshita Rathor (23BCE10890)

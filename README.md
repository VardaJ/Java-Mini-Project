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

2. **Setup database in mysql**

   //In mysql terminal:
   CREATE DATABASE newdatabase;
   USE newdatabase;
   //create tablefor storing questions
   CREATE TABLE questions ( id INT AUTO_INCREMENT PRIMARY KEY, question_text TEXT NOT NULL, option_a VARCHAR(255), option_b VARCHAR(255), option_c VARCHAR(255), option_d VARCHAR(255), correct_answer CHAR(1) -- A/B/C/D);
   //insert sample question sets
   INSERT INTO questions (question_text, option_a, option_b, option_c, option_d, correct_answer) VALUES('What is the capital of France?', 'Paris', 'Berlin', 'Madrid', 'London', 'A'),('Which planet is known as the Red Planet?', 'Earth', 'Mars', 'Jupiter', 'Saturn', 'B'),('Who wrote Hamlet?', 'Dante', 'Homer', 'Shakespeare', 'Plato', 'C');

4. **Database Connection**

   Default DB connection settings:
     Host: localhost
     Port: 3306
     User: root
     Password: tiger
     Database: newdatabase

   You can change these in the createMySQLConnection() method in KBCGame.java.
   
5. **Add MySQL Connector to classpath**

   If you're compiling manually:
   ```bash
   javac -cp .:mysql-connector-java-8.0.xx.jar JavaFrontUpdated.java
   java -cp .:mysql-connector-java-8.0.xx.jar JavaFrontUpdated


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class KBCGame extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;

    private static class Question {
        String questionText;
        String[] options;
        String correctAnswer;

        Question(String questionText, String[] options, String correctAnswer) {
            this.questionText = questionText;
            this.options = options;
            this.correctAnswer = correctAnswer;
        }
    }

    private ArrayList<Question> questions;
    private int currentQuestionIndex;
    private int score;
    private boolean lifelineUsed = false;

    private QuestionPanel questionPanel;
    private JPanel centerPanel;
    private RoundedButton[] optionButtons;
    private JButton lifelineButton;
    private JTextArea resultArea;

    public KBCGame() {
        setTitle("KBC Style GUI");
        setSize(900, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(25, 0, 112));

        // Establish MySQL connection
        Connection conn = createMySQLConnection();
        if (conn != null) {
            System.out.println("MySQL Connection established successfully.");
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Failed to establish MySQL connection.");
        }

        questionPanel = new QuestionPanel();
        add(questionPanel, BorderLayout.NORTH);

        centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);

        JPanel optionsPanel = new JPanel(new GridLayout(2, 2, 50, 50));
        optionsPanel.setOpaque(false);

        optionButtons = new RoundedButton[4];
        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new RoundedButton();
            optionButtons[i].setPreferredSize(new Dimension(260, 70));
            optionButtons[i].setFont(new Font("Arial", Font.BOLD, 22));
            optionButtons[i].setHorizontalAlignment(SwingConstants.LEFT);
            optionButtons[i].addActionListener(this);
            optionsPanel.add(optionButtons[i]);
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        centerPanel.add(optionsPanel, gbc);
        add(centerPanel, BorderLayout.CENTER);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Arial", Font.PLAIN, 16));
        resultArea.setBackground(new Color(230, 230, 250));
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        add(new JScrollPane(resultArea), BorderLayout.SOUTH);

        lifelineButton = new JButton("50-50 Lifeline");
        lifelineButton.setFont(new Font("Arial", Font.BOLD, 18));
        lifelineButton.setBackground(new Color(255, 100, 0));
        lifelineButton.setForeground(Color.WHITE);
        lifelineButton.addActionListener(this);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.add(lifelineButton);
        add(bottomPanel, BorderLayout.EAST);

        resultArea.append("Game Started!\n");

        loadQuestionsFromDatabase();
        currentQuestionIndex = 0;
        score = 0;
        loadQuestion();

        setVisible(true);
    }

    private Connection createMySQLConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/newdatabase";
            String user = "root";
            String password = "tiger";
            connection = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Failed to connect to MySQL database.");
            e.printStackTrace();
        }
        return connection;
    }

    /*private void initializeQuestions() {
        // This method is replaced by loadQuestionsFromDatabase()
    }*/

    private void loadQuestionsFromDatabase() {
        questions = new ArrayList<>();
        Connection conn = null;
        try {
            conn = createMySQLConnection();
            int x;
            if (conn != null) {
                String query = "SELECT question_text, option_a, option_b, option_c, option_d, correct_answer FROM questions ORDER BY RAND() LIMIT 5";
;
                java.sql.Statement stmt = conn.createStatement();
                java.sql.ResultSet rs = stmt.executeQuery(query); 
                int count = 0;
                while (rs.next()) {
                    String questionText = rs.getString("question_text");
                    String[] options = new String[4];
                    options[0] = "A. " + rs.getString("option_a");
                    options[1] = "B. " + rs.getString("option_b");
                    options[2] = "C. " + rs.getString("option_c");
                    options[3] = "D. " + rs.getString("option_d");
                    String correctAnswer = rs.getString("correct_answer");
                    questions.add(new Question(questionText, options, correctAnswer));
                    count++;
                }
                System.out.println("Loaded " + count + " questions from database.");
                rs.close();
                stmt.close();
            } else {
                System.out.println("Failed to connect to database to load questions.");
            }
        } catch (Exception e) {
            System.out.println("Error loading questions from database: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (java.sql.SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void loadQuestion() {
        System.out.println("Loading question at index: " + currentQuestionIndex + ", total questions: " + questions.size());
        if (currentQuestionIndex < questions.size()) {
            Question q = questions.get(currentQuestionIndex);
            questionPanel.setQuestionText("Question " + (currentQuestionIndex + 1) + ": " + q.questionText);
            questionPanel.revalidate();
            questionPanel.repaint();
            for (int i = 0; i < optionButtons.length; i++) {
                String[] parts = q.options[i].split("\\. ", 2);
                optionButtons[i].setText("<html><font color='yellow'>" + parts[0] + ".</font> <font color='white'>" + parts[1] + "</font></html>");
                optionButtons[i].setEnabled(true);
                optionButtons[i].revalidate();
                optionButtons[i].repaint();
            }

            // Only enable lifeline button if not used yet
            lifelineButton.setEnabled(!lifelineUsed);

            resultArea.append("Score: " + score + "\n");
        } else {
            endGame();
        }
    }

    private void endGame() {
        questionPanel.setQuestionText("Game Over!");
        for (JButton button : optionButtons) {
            button.setEnabled(false);
        }
        lifelineButton.setEnabled(false);
        resultArea.append("Final Score: " + score + "\n");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clickedButton = (JButton) e.getSource();
        for (int i = 0; i < optionButtons.length; i++) {
            if (clickedButton == optionButtons[i]) {
                checkAnswer(i);
                return;
            }
        }

        if (clickedButton == lifelineButton) {
            useLifeline();
        }
    }

    private void checkAnswer(int optionIndex) {
        String selectedAnswer = getAnswerLetter(optionButtons[optionIndex].getText());
        System.out.println(selectedAnswer);
        Question currentQuestion = questions.get(currentQuestionIndex);
        if (selectedAnswer.equals(currentQuestion.correctAnswer)) {
            score++;
            resultArea.append("Correct answer!\n");
        } else {
            resultArea.append("Wrong answer! The correct answer was: " + currentQuestion.correctAnswer + "\n");
        }
        currentQuestionIndex++;
        loadQuestion();
    }

    private void useLifeline() {
        Question currentQuestion = questions.get(currentQuestionIndex);
        int correctIndex = -1;

        for (int i = 0; i < 4; i++) {
            if (getAnswerLetter(optionButtons[i].getText()).equals(currentQuestion.correctAnswer)) {
                correctIndex = i;
                break;
            }
        }

        Random rand = new Random();
        int removed = 0;
        while (removed < 2) {
            int r = rand.nextInt(4);
            if (r != correctIndex && optionButtons[r].isEnabled()) {
                optionButtons[r].setEnabled(false);
                removed++;
            }
        }

        lifelineButton.setEnabled(false);
        lifelineUsed = true; // Mark lifeline as used
        resultArea.append("Lifeline used! Two incorrect answers removed.\n");
    }

    private String getAnswerLetter(String htmlText) {
        return htmlText.replaceAll("<[^>]+>", "").trim().substring(0, 1);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(KBCGame::new);
    }
}

// Question Panel
class QuestionPanel extends JPanel {
    private JLabel questionLabel;

    public QuestionPanel() {
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(900, 100));
        setOpaque(false);
        questionLabel = new JLabel("", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 28));
        questionLabel.setForeground(Color.WHITE);
        add(questionLabel);
    }

    public void setQuestionText(String text) {
        questionLabel.setText(text);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint gp = new GradientPaint(0, 0, new Color(128, 0, 128), getWidth(), getHeight(), Color.BLACK);
        g2.setPaint(gp);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
        for (int i = 1; i <= 4; i++) {
            g2.setColor(new Color(255, 255, 255, 60 / i));
            g2.setStroke(new BasicStroke(i));
            g2.drawRoundRect(i, i, getWidth() - 2 * i, getHeight() - 2 * i, 30, 30);
        }
    }
}

// Fancy Button
class RoundedButton extends JButton {
    public RoundedButton() {
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint gp = new GradientPaint(0, 0, new Color(0, 0, 80), 0, getHeight(), new Color(0, 150, 255));
        g2.setPaint(gp);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

        // Draw white border around the button
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);

        super.paintComponent(g);
    }

    @Override
    public void setText(String text) {
        super.setText(text);
        setHorizontalTextPosition(SwingConstants.LEFT);
        setMargin(new Insets(10, 20, 10, 10));
    }

    @Override
    protected void paintBorder(Graphics g) {}
}
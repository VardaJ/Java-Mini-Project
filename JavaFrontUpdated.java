import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class JavaFrontUpdated extends JFrame implements ActionListener {
    private JButton startButton;
    private JButton quitButton;
    private Image backgroundImage;

    public JavaFrontUpdated() {
        setTitle("KBC Game Front");
        setSize(1200, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Load background image
        try {
            backgroundImage = ImageIO.read(new File("KBCbg.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        startButton = new JButton("Start");
        startButton.setPreferredSize(new Dimension(100, 40));
        startButton.setFont(new Font("Arial", Font.BOLD, 20));
        quitButton = new JButton("Quit");
        quitButton.setPreferredSize(new Dimension(100, 40));
        quitButton.setFont(new Font("Arial", Font.BOLD, 20));

        startButton.addActionListener(this);
        quitButton.addActionListener(this);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false); // Make panel transparent
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 20));
        buttonPanel.add(startButton);
        buttonPanel.add(quitButton);

        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            KBCGame.main(new String[]{}); // Start the KBC game
        } else if (e.getSource() == quitButton) {
            System.exit(0); // Quit the application
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(JavaFrontUpdated::new);
    }
}

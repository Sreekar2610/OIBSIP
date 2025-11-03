import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GuessTheNumberGame extends JFrame {

    private int targetNumber;
    private int tries;
    private int maxTries = 5;
    private int totalScore = 0;
    private int currentRound = 1;
    private final int totalRounds = 3;

    private JLabel lblInstruction, lblFeedback, lblScore, lblRound;
    private JTextField txtGuess;
    private JButton btnSubmit;

    // Define custom colors and font
    private final Color bgColor = new Color(30, 32, 48); // Dark background
    private final Color textColor = new Color(220, 220, 255); // Light text
    private final Color buttonColor = new Color(90, 189, 191); // Teal button
    private final Font mainFont = new Font("Arial", Font.BOLD, 18);

    public GuessTheNumberGame() {
        setTitle("Guess The Number");
        setSize(440, 270);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 1));

        // Initialize Labels and Fields
        lblInstruction = new JLabel("Enter a value between 1 and 100:");
        lblFeedback = new JLabel(" ");
        lblScore = new JLabel("Your Score: 0");
        lblRound = new JLabel("Level: 1");

        txtGuess = new JTextField();
        btnSubmit = new JButton("Guess!");

        // Set font and color for labels, button, & text field
        lblInstruction.setFont(mainFont);
        lblInstruction.setForeground(textColor);

        lblFeedback.setFont(mainFont);
        lblFeedback.setForeground(Color.ORANGE);

        lblScore.setFont(mainFont);
        lblScore.setForeground(new Color(0x2ecc71)); // Green

        lblRound.setFont(mainFont);
        lblRound.setForeground(new Color(0xe17055)); // Orange

        txtGuess.setFont(mainFont);
        txtGuess.setForeground(textColor);
        txtGuess.setBackground(new Color(0x353b48));

        btnSubmit.setFont(mainFont);
        btnSubmit.setBackground(buttonColor);
        btnSubmit.setForeground(bgColor);

        // Set background for JFrame content pane
        getContentPane().setBackground(bgColor);

        // Add components to JFrame
        add(lblRound);
        add(lblInstruction);
        add(txtGuess);
        add(btnSubmit);
        add(lblFeedback);
        add(lblScore);

        btnSubmit.addActionListener(new GuessListener());

        generateTarget();
        setVisible(true);
    }

    private void generateTarget() {
        Random random = new Random();
        targetNumber = random.nextInt(100) + 1;
        tries = 0;
    }

    private class GuessListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String userInput = txtGuess.getText();
            try {
                int userGuess = Integer.parseInt(userInput);
                tries++;

                if (userGuess == targetNumber) {
                    lblFeedback.setText("Correct! Tries: " + tries);
                    totalScore += (10 - tries * 2);
                    lblScore.setText("Your Score: " + totalScore);
                    goToNextRound();
                } else if (userGuess < targetNumber) {
                    lblFeedback.setText("Too Low! Try again.");
                } else {
                    lblFeedback.setText("Too High! Try again.");
                }

                if (tries >= maxTries && userGuess != targetNumber) {
                    lblFeedback.setText("No tries left! The number was: " + targetNumber);
                    goToNextRound();
                }

            } catch (NumberFormatException ex) {
                lblFeedback.setText("Please enter an integer value!");
            }
        }
    }

    private void goToNextRound() {
        currentRound++;
        if (currentRound > totalRounds) {
            JOptionPane.showMessageDialog(this, "Game Over! Final Score: " + totalScore);
            System.exit(0);
        } else {
            lblRound.setText("Level: " + currentRound);
            generateTarget();
            txtGuess.setText("");
        }
    }

    public static void main(String[] args) {
        new GuessTheNumberGame();
    }
}   
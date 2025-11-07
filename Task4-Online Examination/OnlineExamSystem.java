import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;

public class OnlineExamSystem {
    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private String currentUser = "";
    private String currentProfile = "";

    private int timerSeconds = 30; // Set exam time in seconds
    private Timer timer;
    private JLabel timerLabel;
    private int score = 0;

    public OnlineExamSystem() {
        frame = new JFrame("Online Examination System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 340);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createLoginPanel(), "Login");
        mainPanel.add(createProfilePanel(), "Profile");
        mainPanel.add(createExamPanel(), "Exam");

        frame.add(mainPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        cardLayout.show(mainPanel, "Login");
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel heading = new JLabel("Login");
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);
        heading.setFont(new Font("Arial", Font.BOLD, 20));

        JTextField usernameField = new JTextField(16);
        JPasswordField passwordField = new JPasswordField(16);

        JButton loginBtn = new JButton("Login");
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.addActionListener(e -> {
            if (usernameField.getText().equals("admin") && String.valueOf(passwordField.getPassword()).equals("admin123")) {
                currentUser = usernameField.getText();
                cardLayout.show(mainPanel, "Profile");
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid login");
            }
        });

        panel.add(heading);
        panel.add(Box.createVerticalStrut(15));
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(Box.createVerticalStrut(8));
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(Box.createVerticalStrut(15));
        panel.add(loginBtn);

        return panel;
    }

    private JPanel createProfilePanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel heading = new JLabel("Profile & Settings");
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);
        heading.setFont(new Font("Arial", Font.BOLD, 20));

        JTextField profileField = new JTextField(16);
        JPasswordField passwordField = new JPasswordField(16);

        JButton updateBtn = new JButton("Update Profile & Password");
        updateBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        updateBtn.addActionListener(e -> {
            currentProfile = profileField.getText();
            JOptionPane.showMessageDialog(frame, "Profile Updated!");
        });

        JButton startExamBtn = new JButton("Start Exam");
        startExamBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        startExamBtn.addActionListener(e -> cardLayout.show(mainPanel, "Exam"));

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutBtn.addActionListener(e -> {
            currentUser = "";
            cardLayout.show(mainPanel, "Login");
        });

        panel.add(heading);
        panel.add(Box.createVerticalStrut(15));
        panel.add(new JLabel("Update Profile Info:"));
        panel.add(profileField);
        panel.add(Box.createVerticalStrut(8));
        panel.add(new JLabel("Update Password:"));
        panel.add(passwordField);
        panel.add(Box.createVerticalStrut(12));
        panel.add(updateBtn);
        panel.add(Box.createVerticalStrut(8));
        panel.add(startExamBtn);
        panel.add(Box.createVerticalStrut(8));
        panel.add(logoutBtn);

        return panel;
    }

    private JPanel createExamPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        timerLabel = new JLabel("Time left: " + timerSeconds, SwingConstants.CENTER);
        timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));

        String[] questions = {
            "<html>1. Java is a:<br>A) Platform&nbsp; B) Language&nbsp; C) Both&nbsp; D) None</html>",
            "<html>2. Who invented Java?<br>A) Guido van Rossum&nbsp; B) Bjarne Stroustrup&nbsp; C) James Gosling&nbsp; D) Dennis Ritchie</html>"
        };
        char[] correctAnswers = {'C','C'};
        JComboBox<String>[] answerFields = new JComboBox[questions.length];

        JPanel qPanel = new JPanel(new GridLayout(questions.length, 2, 5, 8));
        qPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));

        String[] opts = {"A", "B", "C", "D"};
        for (int i = 0; i < questions.length; i++) {
            answerFields[i] = new JComboBox<>(opts);
            qPanel.add(new JLabel(questions[i]));
            qPanel.add(answerFields[i]);
        }

        JButton submitBtn = new JButton("Submit");
        submitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitBtn.addActionListener(e -> autoSubmit(answerFields, correctAnswers));

        panel.add(timerLabel);
        panel.add(Box.createVerticalStrut(15));
        panel.add(qPanel);
        panel.add(Box.createVerticalStrut(15));
        panel.add(submitBtn);

        startTimer(answerFields, correctAnswers);

        return panel;
    }

    private void startTimer(JComboBox<String>[] answerFields, char[] correctAnswers) {
        timerSeconds = 30;
        timerLabel.setText("Time left: " + timerSeconds);
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    timerSeconds--;
                    timerLabel.setText("Time left: " + timerSeconds);
                    if (timerSeconds <= 0) {
                        timer.cancel();
                        JOptionPane.showMessageDialog(frame, "Time up! Auto-submitting...");
                        autoSubmit(answerFields, correctAnswers);
                    }
                });
            }
        }, 1000, 1000);
    }

    private void autoSubmit(JComboBox<String>[] answerFields, char[] correctAnswers) {
        timer.cancel();
        score = 0;
        for (int i = 0; i < correctAnswers.length; i++) {
            if (answerFields[i].getSelectedItem().toString().charAt(0) == correctAnswers[i]) score++;
        }
        JOptionPane.showMessageDialog(frame, "Exam Submitted! Score: " + score + "/" + correctAnswers.length);
        cardLayout.show(mainPanel, "Profile");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(OnlineExamSystem::new);
    }
}

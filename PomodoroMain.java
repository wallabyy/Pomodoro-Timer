import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PomodoroMain {
    private static JLabel timerLabel;
    private static Timer timer;
    private static int remainingTime;
    private static TimerState currentState;
    private static int count;

    private static final Color POMODORO_COLOR = new Color(122, 174, 131);
    private static final Color SHORT_BREAK_COLOR = new Color(104, 167, 203);
    private static final Color LONG_BREAK_COLOR = new Color(150, 104, 203);

    private enum TimerState {
        POMODORO,
        SHORT_BREAK,
        LONG_BREAK
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowTimer());
    }

    private static void createAndShowTimer() {
        JFrame frame = new JFrame("Pomodoro Timer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300); 
        frame.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel(new FlowLayout());
        frame.add(buttonPanel, BorderLayout.NORTH);

        JButton pomodoroButton = new JButton("Pomodoro");
        buttonPanel.add(pomodoroButton);

        JButton shortBreakButton = new JButton("Short Break");
        buttonPanel.add(shortBreakButton);

        JButton longBreakButton = new JButton("Long Break");
        buttonPanel.add(longBreakButton);

        timerLabel = new JLabel();
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timerLabel.setFont(new Font("Courier New", Font.BOLD, 40)); 
        frame.add(timerLabel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new FlowLayout());
        frame.add(controlPanel, BorderLayout.SOUTH);

        JButton startButton = new JButton("Start");
        controlPanel.add(startButton);

        JButton pauseButton = new JButton("Pause");
        controlPanel.add(pauseButton);

        Font buttonFont = new Font("Consolas", Font.PLAIN, 14);
        pomodoroButton.setFont(buttonFont);
        shortBreakButton.setFont(buttonFont);
        longBreakButton.setFont(buttonFont);
        startButton.setFont(buttonFont);
        pauseButton.setFont(buttonFont);

        pomodoroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startTimer(25, POMODORO_COLOR);
                currentState = TimerState.POMODORO;
            }
        });

        shortBreakButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startTimer(5, SHORT_BREAK_COLOR);
                currentState = TimerState.SHORT_BREAK;
            }
        });

        longBreakButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startTimer(15, LONG_BREAK_COLOR);
                currentState = TimerState.LONG_BREAK;
            }
        });

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timer != null && !timer.isRunning()) {
                    timer.start();
                }
            }
        });

        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timer != null && timer.isRunning()) {
                    timer.stop();
                }
            }
        });

        startTimer(25, POMODORO_COLOR); 
        currentState = TimerState.POMODORO;

        frame.setVisible(true);
    }

    private static void startTimer(int minutes, Color backgroundColor) {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }

        remainingTime = minutes * 60;
        updateTimerLabel();
        setTimerBackgroundColor(backgroundColor);

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                remainingTime--;
                updateTimerLabel();

                if (remainingTime <= 0) {
                    timer.stop();
                    JOptionPane.showMessageDialog(null, "Timer has finished!");

                    switch (currentState) {
                        case POMODORO:
                            currentState = TimerState.SHORT_BREAK;
                            if (count == 3) {
                            	count = 0;
                            	startTimer(15, LONG_BREAK_COLOR);
                            } else if (currentState == TimerState.SHORT_BREAK) {
                            	count++;
                                startTimer(5, SHORT_BREAK_COLOR);
                            }
                            break;
                        case SHORT_BREAK:
                            currentState = TimerState.POMODORO;
                            if (currentState == TimerState.POMODORO) {
                                startTimer(25, POMODORO_COLOR);
                            }
                            break;
                        case LONG_BREAK:
                            currentState = TimerState.POMODORO;
                            if (currentState == TimerState.POMODORO) {
                                startTimer(25, POMODORO_COLOR);
                            }
                            break;
                    }
                }
            }
        });
        timer.start();
    }


    private static void updateTimerLabel() {
        int minutes = remainingTime / 60;
        int seconds = remainingTime % 60;
        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }

    private static void setTimerBackgroundColor(Color color) {
        JFrame frame = (JFrame) SwingUtilities.getRoot(timerLabel);
        frame.getContentPane().setBackground(color);
    }
}


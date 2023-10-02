package Timer;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.event.*;

public class MinimalistTimer extends JFrame {
    double frameHeight, frameWidth, fontSize;
    JLabel timeLabel;
    StopWatch stopWatch;
    Point initialPress;
    EventLogger logger;
    final double resizeRatio = 1.1;

    public static void main(String[] args) {
        new MinimalistTimer();
    }

    public MinimalistTimer() {
        stopWatch = new StopWatch();
        stopWatch.start();
        logger = new EventLogger();
        logger.logEvent(EventLogger.EventType.START_TIMER, stopWatch.elapsedTime());

        initUI();
        addListeners();
        initMenuBar();
    }

    public void initUI() {
        fontSize = 32;
        timeLabel = new JLabel(stopWatch.toString());
        timeLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, (int) fontSize));
        add(timeLabel);
        setTitle("Timer");

        setUndecorated(true);
        setLayout(new GridBagLayout()); // This centers the label
        setOpacity(0.3f);
        setLocationRelativeTo(null);
        setVisible(true);
        setAlwaysOnTop(true);
        resetSize();
    }

    public void resetSize() {
        timeLabel.setFont(timeLabel.getFont().deriveFont((float) fontSize));
        pack(); // this automatically determines the width of the timeLabel
        frameWidth = timeLabel.getWidth() * resizeRatio;
        frameHeight = fontSize * 1.25;
        setSize((int) (frameWidth), (int) frameHeight);
    }

    private void addListeners() {
        addMouseWheelListener(e -> {
            if (e.getWheelRotation() == 0) return;
            if (e.getWheelRotation() < 0) fontSize *= resizeRatio;
            else fontSize /= resizeRatio;
            resetSize();
        });

        Timer secondTimer = new Timer(1000, new ActionListener() {
            int lastSecHour = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                timeLabel.setText(stopWatch.toString());
                int thisSecHour = (int) (stopWatch.elapsedTime().getSeconds()) / 3600;
                if (thisSecHour != lastSecHour) resetSize();
                // Resize the frame when the timer changed between 0 and 1 hours
                lastSecHour = thisSecHour;
            }
        });
        secondTimer.start();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    if (stopWatch.isPaused()) {
                        logger.logEvent(EventLogger.EventType.START_TIMER,
                                stopWatch.elapsedTime());
                        stopWatch.start();
                        secondTimer.start();
                        secondTimer.getActionListeners()[0]
                                .actionPerformed(null);
                        // Let the labelUpdateTimer do its first
                        // action immediately without delay
                    } else {
                        logger.logEvent(EventLogger.EventType.PAUSE_TIMER,
                                stopWatch.elapsedTime());
                        stopWatch.pause();
                        secondTimer.stop();
                    }
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    logger.logEvent(EventLogger.EventType.RESET_TIMER,
                            stopWatch.elapsedTime());
                    stopWatch.reset();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                initialPress = e.getPoint();
            }
        });
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    Point pOnScreen = e.getLocationOnScreen();
                    setLocation(pOnScreen.x - initialPress.x,
                            pOnScreen.y - initialPress.y);
                }
            }
        });
    }

    public void initMenuBar() {
        System.setProperty("apple.laf.useScreenMenuBar", "true");

        JMenu logMenu = new JMenu("Log");
        logMenu.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                new LogFrame(MouseInfo.getPointerInfo().getLocation(), logger);
            }

            @Override
            public void menuDeselected(MenuEvent e) {
            }

            @Override
            public void menuCanceled(MenuEvent e) {
            }
        });

        JMenu startFromMenu = new JMenu("Start From...");
        startFromMenu.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                new TimeInputFrame(MouseInfo.getPointerInfo().getLocation(),
                        MinimalistTimer.this);
            }

            @Override
            public void menuDeselected(MenuEvent e) {
            }

            @Override
            public void menuCanceled(MenuEvent e) {
            }
        });

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(logMenu);
        menuBar.add(startFromMenu);
        setJMenuBar(menuBar);
    }

    public void startTimeFrom(int hour, int minute, int second) {
        java.time.Duration changedTo = StopWatch.toDuration(hour, minute, second);
        logger.logEvent(EventLogger.EventType.START_FROM,
                stopWatch.elapsedTime(), changedTo);
        stopWatch.startFrom(changedTo);
    }
}
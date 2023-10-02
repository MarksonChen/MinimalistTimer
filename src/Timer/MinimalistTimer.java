package Timer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class TimerApp extends JFrame {
    double frameHeight, frameWidth, fontSize;
    JLabel timeLabel;
    Timer timer;
    Point initialPress;

    public static void main(String[] args) {
        new TimerApp();
    }

    public TimerApp(){
        timer = new Timer();
        timer.start();
        initMenuBar();
        initUI();
        addListeners();
    }

    public void initMenuBar(){
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        JMenuBar menuBar = new JMenuBar();
        JMenu appMenu = new JMenu("AppName"); // This will appear as your app's name on macOS

        JMenuItem infoItem = new JMenuItem("Info");
        infoItem.addActionListener(e -> {
            // Display information when clicked
            JOptionPane.showMessageDialog(null, "This is the app info!");
        });

        appMenu.add(infoItem);
        menuBar.add(appMenu);

        setJMenuBar(menuBar);
    }

    public void initUI(){
        fontSize = 32;
        timeLabel = new JLabel(timer.toString());
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

    private void resetSize() {
        timeLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, (int) fontSize));
        pack(); // this automatically determines the width of the timeLabel
        frameWidth = timeLabel.getWidth()*1.1;
        frameHeight = fontSize * 1.25;
        setSize((int)(frameWidth), (int) frameHeight);
    }

    private void addListeners() {
        addMouseWheelListener(e -> {
            if (e.getWheelRotation() == 0) return;
            if (e.getWheelRotation() < 0)  fontSize *= 1.1;
            else fontSize /= 1.1;
            resetSize();
        });

        javax.swing.Timer secondTimer = new javax.swing.Timer(1000, e->{
            timeLabel.setText(timer.toString());
            if(timer.elapsedTime().getSeconds() == 3600) resetSize();
            // Expands the frame to display hours when the timer counts to 60 minutes
        });
        secondTimer.start();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    if (timer.isPaused()){
                        timer.start();
                        secondTimer.start();
                        secondTimer.getActionListeners()[0]
                                .actionPerformed(null);
                        // Let the labelUpdateTimer do its first
                        // action immediately without delay
                    }
                    else {
                        timer.pause();
                        secondTimer.stop();
                    }
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    timer.reset();
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
}
package Timer;
import javax.swing.*;
import java.awt.*;

public class TimeInputFrame extends JFrame {

    public TimeInputFrame(Point location, MinimalistTimer timerFrame) {
        setTitle("Enter Time");
        setLocation(location.x - 160, location.y);
        setLayout(new FlowLayout());
        setSize(325, 100);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        JSpinner hourSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 23, 1));
        JSpinner minuteSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));
        JSpinner secondSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));

        add(new JLabel("Hour:"));
        add(hourSpinner);
        add(new JLabel("Minute:"));
        add(minuteSpinner);
        add(new JLabel("Second:"));
        add(secondSpinner);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            int hour = (int) hourSpinner.getValue();
            int minute = (int) minuteSpinner.getValue();
            int second = (int) secondSpinner.getValue();
            timerFrame.startTimeFrom(hour, minute, second);
            dispose();
        });
        add(okButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> {
            dispose();
        });
        add(cancelButton);

        setVisible(true);
    }
}
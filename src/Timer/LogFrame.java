package Timer;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.format.DateTimeFormatter;

public class LogFrame extends JFrame{
    public LogFrame(Point location, EventLogger logger) {
        DefaultTableModel model = new DefaultTableModel();

        model.addColumn("Time");
        model.addColumn("Change");
        model.addColumn("What Occurred");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd HH:mm:ss");
        for (int i = logger.timestamp.size() -1; i >= 0; i--) {
            String logTime = logger.timestamp.get(i).format(formatter);
            String timerChange = StopWatch.formatDuration(logger.timerAt.get(i));
            String description = "";

            switch (logger.event.get(i)){
                case START_TIMER:
                    timerChange += " ⏵";
                    description = "Timer started";
                    break;
                case PAUSE_TIMER:
                    timerChange += " ⏸";
                    description = "Timer paused";
                    break;
                case RESET_TIMER:
                    timerChange += " -> 00:00";
                    description = "Timer restarted";
                    break;
                case START_FROM:
                    timerChange += " -> "
                            + StopWatch.formatDuration(logger.restartFrom.get(i));
                    description = "Timer restarted";
                    break;
            }
            model.addRow(new String[]{logTime, timerChange, description});
        }

        JTable table = new JTable(model);
        Font tableFont = table.getFont();
        tableFont = tableFont.deriveFont(tableFont.getSize() * 1.5f);
        table.setFont(tableFont);
        table.getTableHeader().setFont(tableFont);
        table.setRowHeight((int)(table.getRowHeight() * 1.5f));
        table.setEnabled(false);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane);

        setTitle("Timer History");
        pack();
        setSize(600, table.getRowHeight()
                * (logger.timestamp.size()+1) + 34);
        setLocation(location.x - this.getWidth()/2, location.y);
        setVisible(true);
    }
}

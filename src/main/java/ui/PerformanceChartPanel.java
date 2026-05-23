package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class PerformanceChartPanel extends JPanel {

    private Map<String, Double> values = new LinkedHashMap<>();

    public PerformanceChartPanel() {
        setPreferredSize(new Dimension(540, 260));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(16, 16, 16, 16));
    }

    public void updateValues(Map<String, Double> newValues) {
        values = new LinkedHashMap<>(newValues);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int left = 58;
        int right = 24;
        int top = 24;
        int bottom = 44;
        int chartWidth = width - left - right;
        int chartHeight = height - top - bottom;

        g.setColor(new Color(230, 236, 245));
        for (int i = 0; i <= 4; i++) {
            int y = top + (chartHeight * i / 4);
            g.drawLine(left, y, left + chartWidth, y);
        }

        g.setColor(new Color(80, 90, 105));
        g.setFont(new Font("Segoe UI", Font.BOLD, 15));
        g.drawString("Latency Snapshot (ms)", left, 16);

        if (values.isEmpty()) {
            g.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            g.setColor(new Color(120, 128, 142));
            g.drawString("Run a performance test to render the latency chart.", left, top + chartHeight / 2);
            g.dispose();
            return;
        }

        double max = 1;
        for (double value : values.values()) {
            max = Math.max(max, value);
        }

        int index = 0;
        int barWidth = Math.max(44, chartWidth / Math.max(1, values.size() * 2));
        int gap = Math.max(22, (chartWidth - (barWidth * values.size())) / Math.max(1, values.size() + 1));
        int x = left + gap;
        Color[] colors = {
                new Color(40, 110, 220),
                new Color(15, 145, 190),
                new Color(29, 179, 113),
                new Color(243, 156, 18)
        };

        g.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        for (Map.Entry<String, Double> entry : values.entrySet()) {
            int barHeight = (int) Math.round((entry.getValue() / max) * (chartHeight - 16));
            int y = top + chartHeight - barHeight;
            g.setColor(colors[index % colors.length]);
            g.fillRoundRect(x, y, barWidth, barHeight, 16, 16);

            g.setColor(new Color(40, 46, 58));
            String valueLabel = String.format("%.0f", entry.getValue());
            int valueWidth = g.getFontMetrics().stringWidth(valueLabel);
            g.drawString(valueLabel, x + (barWidth - valueWidth) / 2, y - 8);

            int labelWidth = g.getFontMetrics().stringWidth(entry.getKey());
            g.drawString(entry.getKey(), x + (barWidth - labelWidth) / 2, top + chartHeight + 18);
            x += barWidth + gap;
            index++;
        }

        g.setColor(new Color(110, 118, 132));
        g.drawLine(left, top + chartHeight, left + chartWidth, top + chartHeight);
        g.dispose();
    }
}

import javax.swing.*;
import java.awt.*;

public class SortingVisualizer extends JFrame {

    private int[] array;
    private VisualPanel panel;
    private JTextField input;
    private JSlider speedSlider;
    private JLabel status;

    public SortingVisualizer() {
        setTitle("🔥 Insertion Sort Visualizer");
        setSize(1000, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ---------- TOP PANEL ----------
        JPanel top = new JPanel();
        top.setBackground(new Color(30, 30, 30));

        input = new JTextField(25);
        input.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton sortBtn = new JButton("▶ Start Insertion Sort");
        sortBtn.setFocusPainted(false);

        speedSlider = new JSlider(50, 1000, 300);
        speedSlider.setBackground(new Color(30, 30, 30));
        speedSlider.setForeground(Color.WHITE);

        status = new JLabel("Status: Waiting...");
        status.setForeground(Color.GREEN);

        top.add(new JLabel("Input:") {{ setForeground(Color.WHITE); }});
        top.add(input);
        top.add(sortBtn);
        top.add(new JLabel("Speed:") {{ setForeground(Color.WHITE); }});
        top.add(speedSlider);
        top.add(status);

        add(top, BorderLayout.NORTH);

        // ---------- CENTER PANEL ----------
        panel = new VisualPanel();
        add(panel, BorderLayout.CENTER);

        sortBtn.addActionListener(e -> startSort());
    }

    private void startSort() {
        try {
            String[] parts = input.getText().split(",");
            array = new int[parts.length];
            for (int i = 0; i < parts.length; i++)
                array[i] = Integer.parseInt(parts[i].trim());

            panel.setArray(array);
            status.setText("Status: Sorting...");
            new Thread(this::insertionSort).start();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid input!");
        }
    }

    private void insertionSort() {
        for (int i = 1; i < array.length; i++) {
            int key = array[i];
            int j = i - 1;
            panel.setIndexes(i, j);

            while (j >= 0 && array[j] > key) {
                array[j + 1] = array[j];
                j--;
                panel.setIndexes(i, j);
                panel.repaint();
                sleep();
            }
            array[j + 1] = key;
            panel.repaint();
            sleep();
        }
        panel.clearIndexes();
        status.setText("Status: Sorting Completed ✔");
    }

    private void sleep() {
        try {
            Thread.sleep(speedSlider.getValue());
        } catch (Exception ignored) {}
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SortingVisualizer().setVisible(true));
    }
}

// ================= VISUAL PANEL =================
class VisualPanel extends JPanel {

    private int[] array;
    private int current = -1, compare = -1;

    public VisualPanel() {
        setBackground(new Color(18, 18, 18));
    }

    public void setArray(int[] arr) {
        array = arr;
        repaint();
    }

    public void setIndexes(int c, int comp) {
        current = c;
        compare = comp;
    }

    public void clearIndexes() {
        current = compare = -1;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (array == null) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth() / array.length;
        int max = getMax();

        for (int i = 0; i < array.length; i++) {
            int h = (array[i] * (getHeight() - 50)) / max;

            Color color;
            if (i == current) color = Color.RED;
            else if (i == compare) color = Color.YELLOW;
            else if (i < current) color = new Color(0, 200, 100);
            else color = new Color(70, 130, 255);

            GradientPaint gp = new GradientPaint(
                    i * width, 0,
                    color.brighter(),
                    i * width, h,
                    color.darker());

            g2.setPaint(gp);
            g2.fillRoundRect(
                    i * width + 5,
                    getHeight() - h,
                    width - 10,
                    h,
                    15,
                    15);
        }
    }

    private int getMax() {
        int max = array[0];
        for (int v : array)
            if (v > max) max = v;
        return max;
    }
}

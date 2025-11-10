import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class TodoApp extends JFrame {

    private JPanel carouselPanel;
    private JScrollPane scrollPane;
    private JTextField input;
    private JButton addBtn;
    private java.util.List<TaskCard> cards;
    private Random random;

    public TodoApp() {
        super("A To-Do Carousel");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(850, 520);
        setLocationRelativeTo(null);

        cards = new ArrayList<>();
        random = new Random();

        initComponents();
        layoutComponents();
        attachHandlers();

        setVisible(true);
    }

    private void initComponents() {
        input = new JTextField();
        input.setFont(new Font("Serif", Font.PLAIN, 16));
        addBtn = new JButton("➕ Add Task");
        addBtn.setBackground(new Color(102, 51, 0));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFocusPainted(false);
        addBtn.setFont(new Font("SansSerif", Font.BOLD, 14));

        carouselPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, new Color(255, 240, 220),
                        0, getHeight(), new Color(245, 230, 210));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        carouselPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));
        carouselPanel.setOpaque(false);

        scrollPane = new JScrollPane(carouselPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
    }

    private void layoutComponents() {
        JPanel topPanel = new JPanel(new BorderLayout(6, 6));
        topPanel.setOpaque(false);
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        topPanel.add(input, BorderLayout.CENTER);
        topPanel.add(addBtn, BorderLayout.EAST);

        Container c = getContentPane();
        c.setLayout(new BorderLayout(10, 10));
        c.add(topPanel, BorderLayout.NORTH);
        c.add(scrollPane, BorderLayout.CENTER);
    }

    private void attachHandlers() {
        addBtn.addActionListener(e -> addTask());
        input.addActionListener(e -> addTask());
    }

    private void addTask() {
        String text = input.getText().trim();
        if (text.isEmpty()) {
            Toolkit.getDefaultToolkit().beep();
            return;
        }

        String[] emojis = {"📝", "✅", "🔥", "🌟", "💡", "🎯", "📌"};
        String emoji = emojis[random.nextInt(emojis.length)];

        TaskCard card = new TaskCard(emoji + " " + text);
        cards.add(card);
        carouselPanel.add(card);
        carouselPanel.revalidate();
        carouselPanel.repaint();
        input.setText("");
        input.requestFocusInWindow();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TodoApp::new);
    }

    // Task card with programmatically drawn antique paper background
    private class TaskCard extends JPanel {
        private JLabel label;
        private JCheckBox checkBox;
        private JButton deleteBtn;
        private Font customFont;

        public TaskCard(String text) {
            setPreferredSize(new Dimension(200, 150));
            setLayout(new BorderLayout());
            setOpaque(false);

            customFont = new Font("Serif", Font.BOLD, 26);

            label = new JLabel("<html><div style='text-align: center;'>" + text + "</div></html>");
            label.setFont(customFont);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setVerticalAlignment(SwingConstants.CENTER);
            label.setOpaque(false);

            checkBox = new JCheckBox();
            checkBox.setOpaque(false);
            checkBox.addActionListener(e -> {
                if (checkBox.isSelected()) {
                    label.setText("<html><div style='text-align: center;'><strike>" + text + "</strike></div></html>");
                } else {
                    label.setText("<html><div style='text-align: center;'>" + text + "</div></html>");
                }
            });

            deleteBtn = new JButton("🗑");
            deleteBtn.setMargin(new Insets(2, 5, 2, 5));
            deleteBtn.setForeground(new Color(120, 0, 0));
            deleteBtn.setFocusPainted(false);
            deleteBtn.setContentAreaFilled(false);
            deleteBtn.setBorderPainted(false);
            deleteBtn.setOpaque(false);
            deleteBtn.addActionListener(e -> removeCard());

            JPanel top = new JPanel(new BorderLayout());
            top.setOpaque(false);
            top.add(checkBox, BorderLayout.WEST);
            top.add(deleteBtn, BorderLayout.EAST);

            add(top, BorderLayout.NORTH);
            add(label, BorderLayout.CENTER);

            setBorder(new LineBorder(new Color(150, 75, 0), 2, true));

            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    setBackground(new Color(255, 250, 200, 100));
                    repaint();
                }
                public void mouseExited(MouseEvent e) {
                    setBackground(new Color(255, 245, 210, 100));
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw shadow
            g2d.setColor(new Color(0,0,0,50));
            g2d.fillRoundRect(5,5,getWidth()-10,getHeight()-10,20,20);

            // Draw antique paper background programmatically
            g2d.setColor(new Color(250, 245, 210)); // base paper color
            g2d.fillRoundRect(0,0,getWidth(),getHeight(),20,20);

            // Add lines for paper texture
            g2d.setColor(new Color(220, 200, 140, 80));
            for (int i = 5; i < getHeight(); i += 6) {
                g2d.drawLine(5, i, getWidth()-5, i);
            }

            // Slight random noise for texture
            for (int i = 0; i < 100; i++) {
                int x = (int)(Math.random() * getWidth());
                int y = (int)(Math.random() * getHeight());
                g2d.setColor(new Color(220 + random.nextInt(20), 200 + random.nextInt(20), 150 + random.nextInt(20), 50));
                g2d.fillRect(x, y, 1, 1);
            }

            g2d.dispose();

            // Draw children on top
            paintChildren(g);
        }

        private void  removeCard() {
            carouselPanel.remove(this);
            cards.remove(this);
            carouselPanel.revalidate();
            carouselPanel.repaint();
        }
    }
}

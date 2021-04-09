package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Random;

public class Main extends JPanel {

    private final int WIDTH = 1000;
    private final int HEIGHT = WIDTH * 9 / 16;
    private final int SIZE = 200;
    private final float BAR_WIDTH = (float)WIDTH / SIZE;
    private final float[] BAR_HEIGHT = new float[SIZE];
    private SwingWorker<Void, Void> shuffler, sort;
    private int currentIndex, traversingIndex;

    private Main() {
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        initBarHeight();
        initSort();
        initShuffler();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Sorting visualizer");
            frame.setResizable(false);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setContentPane(new Main());
            frame.validate();
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics2D = (Graphics2D) g;
        g.setColor(Color.CYAN);
        Rectangle2D.Float bar;
        for (int counter = 0; counter < SIZE; counter++) {
            bar = new Rectangle2D.Float(counter * BAR_WIDTH, 0, BAR_WIDTH, BAR_HEIGHT[counter]);
            graphics2D.fill(bar);
        }
        graphics2D.setColor(Color.RED);
        bar = new Rectangle2D.Float(currentIndex * BAR_WIDTH, 0, BAR_WIDTH, BAR_HEIGHT[currentIndex]);
        graphics2D.fill(bar);
        graphics2D.setColor(Color.GREEN);
        bar = new Rectangle2D.Float(traversingIndex * BAR_WIDTH, 0, BAR_WIDTH, BAR_HEIGHT[traversingIndex]);
        graphics2D.fill(bar);
    }

    private void initBarHeight() {
        float interval = (float) HEIGHT / SIZE;
        for (int counter = 0; counter < SIZE; counter++) {
            BAR_HEIGHT[counter] = counter * interval;
        }
    }

    private void initShuffler() {
        shuffler = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                int middle = SIZE / 2;
                for (int i = 0, j = middle; i < middle; i++, j++) {
                    int randomIndex = new Random().nextInt(SIZE);
                    swap(i, randomIndex);
                    randomIndex = new Random().nextInt(SIZE);
                    swap(j, randomIndex);
                    Thread.sleep(10);
                    repaint();
                }
                return null;
            }

            @Override
            protected void done() {
                super.done();
                sort.execute();
            }
        };
        shuffler.execute();
    }

    private void initSort() {
        sort = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                for (currentIndex = 1; currentIndex < SIZE; currentIndex++) {
                    traversingIndex = currentIndex;
                    while(traversingIndex > 0 &&
                            BAR_HEIGHT[traversingIndex] < BAR_HEIGHT[traversingIndex - 1]) {
                        swap(traversingIndex, traversingIndex - 1);
                        traversingIndex--;
                        Thread.sleep(1);
                        repaint();
                    }
                }
                currentIndex = 0;
                traversingIndex = 0;
                return null;
            }
        };
    }

    private void swap(int index1, int index2) {
        float temp = BAR_HEIGHT[index1];
        BAR_HEIGHT[index1] = BAR_HEIGHT[index2];
        BAR_HEIGHT[index2] = temp;
    }
}

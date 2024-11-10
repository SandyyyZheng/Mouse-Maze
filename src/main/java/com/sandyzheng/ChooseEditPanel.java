package com.sandyzheng;

import javax.swing.*;
import java.awt.*;

public class ChooseEditPanel extends JPanel {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;
    private Image Background;
    private Image Title;
    public int mark;

    public ChooseEditPanel() {
        setLayout(null);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        loadImage();
        addButton();
        setFocusable(true);
    }

    public int getMark() {
        return mark;
    }

    private void addButton() {
        JButton button_puzzle1 = new JButton();
        button_puzzle1.setText("Puzzle 1");
        button_puzzle1.setBackground(new Color(220, 220, 220));
        button_puzzle1.setFont(new Font("plain", Font.BOLD, 14));
        button_puzzle1.setBounds(40, 200, 150, 40);
        add(button_puzzle1);

        JButton button_puzzle2 = new JButton();
        button_puzzle2.setText("Puzzle 2");
        button_puzzle2.setBackground(new Color(220, 220, 220));
        button_puzzle2.setFont(new Font("plain", Font.BOLD, 14));
        button_puzzle2.setBounds(40, 260, 150, 40);
        add(button_puzzle2);

        JButton button_puzzle3 = new JButton();
        button_puzzle3.setText("Puzzle 3");
        button_puzzle3.setBackground(new Color(220, 220, 220));
        button_puzzle3.setFont(new Font("plain", Font.BOLD, 14));
        button_puzzle3.setBounds(40, 320, 150, 40);
        add(button_puzzle3);

        JButton button_new = new JButton();
        button_new.setText("Blank Puzzle");
        button_new.setBackground(new Color(220, 220, 220));
        button_new.setFont(new Font("plain", Font.BOLD, 14));
        button_new.setBounds(40, 380, 150, 40);
        add(button_new);

        JButton button_ret = new JButton();
        button_ret.setText("Return");
        button_ret.setBackground(new Color(220, 220, 220));
        button_ret.setFont(new Font("plain", Font.BOLD, 14));
        button_ret.setBounds(40, 470, 150, 40);
        add(button_ret);

        button_puzzle1.addActionListener(e -> {
            mark = 1;
            setVisible(false);
            EnterPanel e1 = new EnterPanel();
            EditPanel e2 = new EditPanel(mark);
            e1.getMaze().add(e2);
            e2.requestFocus();
            e1.getMaze().pack();
            e1.setVisible(true);
        });

        button_puzzle2.addActionListener(e -> {
            mark = 2;
            setVisible(false);
            EnterPanel e1 = new EnterPanel();
            EditPanel e2 = new EditPanel(mark);
            e1.getMaze().add(e2);
            e2.requestFocus();
            e1.getMaze().pack();
            e1.setVisible(true);
        });

        button_puzzle3.addActionListener(e -> {
            mark = 3;
            setVisible(false);
            EnterPanel e1 = new EnterPanel();
            EditPanel e2 = new EditPanel(mark);
            e1.getMaze().add(e2);
            e2.requestFocus();
            e1.getMaze().pack();
            e1.setVisible(true);
        });

        button_new.addActionListener(e -> {
            mark = 4;
            setVisible(false);
            EnterPanel e1 = new EnterPanel();
            EditPanel e2 = new EditPanel(mark);
            e1.getMaze().add(e2);
            e2.requestFocus();
            e1.getMaze().pack();
            e1.setVisible(true);
        });

        button_ret.addActionListener(e -> {
            setVisible(false);
            EnterPanel e1 = new EnterPanel();
            e1.getMaze().add(e1);
            e1.requestFocus();
            e1.getMaze().pack();
            e1.getMaze().setVisible(true);
        });
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBackground(g);
        drawTitle(g);
    }

    private void drawBackground(Graphics g) {
        g.drawImage(Background, 0, 0, 600, 600, this);
    }

    private void drawTitle(Graphics g){
        g.drawImage(Title, 0, 0, 250, 250, this);
    }

    private void loadImage(){
        Background = new ImageIcon(getClass().getResource("/static/images/Edit.jpg")).getImage();
        Title = new ImageIcon(getClass().getResource("/static/images/Design.png")).getImage();
    }
}

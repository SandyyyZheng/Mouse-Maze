package com.sandyzheng;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class EnterPanel extends JPanel {
    private Image CoverImage;
    private Image TitleImage;
    static Maze maze;
    private int ROW = 15;
    private int COL = 16;
    private String mapName;

    public EnterPanel() {
        setPreferredSize(new Dimension(800, 600));
        setLayout(null);
        addImage();
        setFocusable(true);
        addButton();
    }

    public Maze getMaze() {
        return maze;
    }

    private void addButton() {
        JButton button_start = new JButton();
        button_start.setText("New Game");
        button_start.setBounds(600, 230, 160, 60);
        button_start.setBackground(new Color(220, 220, 220));
        button_start.setFont(new Font("plain", Font.BOLD, 17));
        add(button_start);

        JButton button_load = new JButton();
        button_load.setText("Load Map");
        button_load.setBounds(600, 310, 160, 60);
        button_load.setBackground(new Color(220, 220, 220));
        button_load.setFont(new Font("plain", Font.BOLD, 17));
        add(button_load);

        JButton button_edit = new JButton();
        button_edit.setText("Edit Map");
        button_edit.setBounds(600, 390, 160, 60);
        button_edit.setBackground(new Color(220, 220, 220));
        button_edit.setFont(new Font("plain", Font.BOLD, 17));
        add(button_edit);

        JButton button_exit = new JButton();
        button_exit.setText("Exit");
        button_exit.setBounds(600, 470, 160, 60);
        button_exit.setBackground(new Color(220, 220, 220));
        button_exit.setFont(new Font("plain", Font.BOLD, 17));
        add(button_exit);

        button_start.addActionListener(e -> {
            setVisible(false);
            MainPanel p1 = new MainPanel();
            maze.add(p1);
            p1.requestFocus();
            maze.pack();
            maze.setVisible(true);
        });

        button_load.addActionListener(e -> {
            mapName = JOptionPane.showInputDialog(null, "Please enter the name of your map:",
                    "Read Map", JOptionPane.PLAIN_MESSAGE);
            MyMapPanel p1 = new MyMapPanel();
            p1.map = getSparseArrayFromFile();
            maze.add(p1);
            p1.requestFocus();
            maze.pack();
            maze.setVisible(true);
        });

        button_edit.addActionListener(e -> {
            setVisible(false);
            ChooseEditPanel p3 = new ChooseEditPanel();
            maze.add(p3);
            p3.requestFocus();
            maze.pack();
            maze.setVisible(true);
        });

        button_exit.addActionListener(e -> System.exit(0));
    }

    public int[][] getSparseArrayFromFile() {
        //将稀疏矩阵从文件中读取出来
        BufferedReader bufferedReader;
        InputStreamReader inputStreamReader;
        //为保存的数组分配空间
        int[][] data = new int[ROW][COL];
        try {
            inputStreamReader = new InputStreamReader(new FileInputStream("mouse-maze/output/my-mazes/" + mapName + ".txt"));
            bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            int i = 0;
            //按行读取
            while ((line = bufferedReader.readLine()) != null) {
                //将按行读取的字符串按空格分割，得到一个string数组
                String[] strings = line.split("\\t");
                //依次转换为int类型存入到分配好空间的数组中
                for (int k = 0; k < strings.length; k++) {
                    data[i][k] = Integer.parseInt(strings[k]);
                }
                //行数加1
                i++;
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "File not found!", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        //返回读取的二维数组
        return data;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawCover(g);
        drawTitle(g);
    }

    private void addImage() {
        // 使用相对路径从resources/images目录加载图像
        CoverImage = new ImageIcon(getClass().getResource("/static/images/Cover.jpg")).getImage();
        TitleImage = new ImageIcon(getClass().getResource("/static/images/Title.png")).getImage();
    }

    private void drawCover(Graphics g) {
        g.drawImage(CoverImage, 0, 0, 800, 600, this);
    }

    private void drawTitle(Graphics g) {
        g.drawImage(TitleImage, 580, 45, 210, 210, this);
    }

    public static void main(String[] args) {
        maze = new Maze();
        maze.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        maze.setVisible(true);
    }
}

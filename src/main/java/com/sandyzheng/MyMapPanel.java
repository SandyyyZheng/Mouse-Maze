package com.sandyzheng;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.ArrayList;

//MyPanel面板类实现鼠标侦听接口，并定义一些成员变量
public class MyMapPanel extends JPanel implements KeyListener {
    @Serial
    private static final long serialVersionUID = 1L;
    //窗体的宽与高
    private static final int WIDTH = 600;
    private static final int HEIGHT = 640;
    //设定背景方格默认行列数
    private static final int ROW = 15;
    private static final int COL = 16;
    //单个图像大小
    private static final int CS = 40;

    //设定地图
    public int[][] map;

    //设定显示图像的对象
    public Image floorImage;
    public Image wallImage;
    public Image roleImage;
    public Image cheeseImage;
    public Image shortCut;
    public Image route;
    //角色坐标
    private int x, y;
    //添加一组常数，区分左右上下按键的触发
    //之所以采用数字进行区别，是因为数字运算效率高
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int UP = 2;
    public static final int DOWN = 3;
    //是否通关标记
    //增加计步器
    private int count;
    //定时器
    private Timer timer;
    JTextField cdText = new JTextField(10);

    static ArrayList<ArrayList<Integer>> res = new ArrayList<>();

    //MyMapPanel面板类的构造方法初始化人物角色的位置
    public MyMapPanel() {
        setLayout(null);
        //设定初始构造时面板的大小
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        //在初始化时载入图形
        loadImage();
        addButton();
        //角色起始坐标
        x = 1;
        y = 1;
        //在面板构建时赋予计步器初值
        count = 0;
        JPanel timePanel = new JPanel();
        JLabel timeLabel = new JLabel("Countdown");
        cdText.setText(30 + "s");
        timePanel.add(timeLabel);
        timePanel.add(cdText);
        cdText.setEditable(false);
        timePanel.setBounds(200, 6, 200, 28);
        this.add(timePanel);
        timer = new Timer(800, new TimeListener());
        timer.start();
        //设定焦点在本窗体并赋予监听对象
        setFocusable(true);
        addKeyListener(this);
        //实例化内部线程AnimationThread
        Thread threadAnime = new Thread(new AnimationThread());
        //启动线程
        threadAnime.start();
    }

    private void addButton() {
        JButton button_Esc = new JButton();
        button_Esc.setText("Return");
        button_Esc.setBounds(40, 580, 100, 40);
        button_Esc.setBackground(new Color(220, 220, 220));
        button_Esc.setFont(new Font("plain", Font.BOLD, 13));
        //button_Esc.setMargin(new Insets(0, 0, 0, 0));
        add(button_Esc);

        JButton button_route = new JButton();
        button_route.setText("Routes (shortest red)");
        button_route.setBounds(170, 580, 200, 40);
        button_route.setBackground(new Color(220, 220, 220));
        button_route.setFont(new Font("plain", Font.BOLD, 13));
        add(button_route);

        button_Esc.addActionListener(e -> {
            setVisible(false);
            timer.stop();
            removeAll();
            EnterPanel e1 = new EnterPanel();
            e1.getMaze().add(e1);
            e1.requestFocus();
            e1.getMaze().pack();
            e1.getMaze().setVisible(true);
        });

        button_route.addActionListener(e -> {
            //创建一个 ArrayList<Integer> step 记录步数
            ArrayList<Integer> step = new ArrayList<>();
            ArrayList<Integer> all = new ArrayList<>();
            //调用 dfs 函数
            dfs(map, 1, 1, ROW, COL, step);
            dfs(map, 1, 1, ROW, COL, all);

            //得到结果 筛选最短路径
            int size = Integer.MAX_VALUE;
            int index = 0;
            for (int i = 0; i < res.size(); i++) {
                if (res.get(i).size() < size) {
                    size = res.get(i).size();
                    index = i;
                }
            }

            //输出所有路径
            for (ArrayList<Integer> re : res) {
                all = re;
                for (int j = 0; j < all.size(); j += 2) {
                    map[(int) all.get(j)][(int) all.get(j + 1)] = 3;
                }
            }

            //将最短路径保存并输出
            step = res.get(index);
            for (int i = 0; i < step.size(); i += 2) {
                map[(int) step.get(i)][(int) step.get(i + 1)] = 2;
            }
            timer.stop();
            repaint();
            requestFocus();
            res.clear();
            step.clear();
        });
    }

    public static void dfs(int[][] maze, int i, int j, int n, int m, ArrayList<Integer> step) {
        // 越界 、 有墙 、 已经走过
        if (i <= 0 || i >= n || j < 0 || j >= m || maze[i][j] == 1 || maze[i][j] == 2) {
            return;
        }

        //如果到达终点
        if (i == n - 2 && j == m - 3) {
            //添加终点坐标
            step.add(i);
            step.add(j);
            res.add(new ArrayList<>(step));
            //回溯
            step.remove(step.size() - 1);
            step.remove(step.size() - 1);
        }
        //没有到达终点
        else {
            //添加当前坐标
            step.add(i);
            step.add(j);
            //标记为已经走过
            maze[i][j] = 2;
            // 递归
            dfs(maze, i + 1, j, n, m, step);
            dfs(maze, i, j + 1, n, m, step);
            dfs(maze, i - 1, j, n, m, step);
            dfs(maze, i, j - 1, n, m, step);
            // 回溯
            maze[i][j] = 0;
            step.remove(step.size() - 1);
            step.remove(step.size() - 1);
        }
    }

    //本关重来
    public void replay() {
        repaint();
        x = 1;
        y = 1;
        timer = new Timer(800, new TimeListener());
        timer.start();
    }

    //paintComponent(Graphics g)方法在 JPanel 基础上调用 drawMap(g)方法构建底层地图背景，
    //drawRole(g)方法在指定位置角色坐标（x, y）处画出人物
    //drawCheese(g)方法在指定位置画出奶酪
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawMap(g);  //画出地图
        drawCheese(g);  //画出奶酪
        drawRoute(g);
        drawShortCut(g);
        drawRole(g);  //画出人物
    }

    private void drawRoute(Graphics g) {
        for (int i = 1; i < ROW - 1; i++) {
            for (int j = 1; j < COL - 2; j++) {
                if (map[i][j] == 3)
                    g.drawImage(route, i * CS, j * CS, CS, CS, this);
            }
        }
    }

    private void drawShortCut(Graphics g) {
        for (int i = 1; i < ROW - 1; i++) {
            for (int j = 1; j < COL - 2; j++) {
                if (map[i][j] == 2)
                    g.drawImage(shortCut, i * CS, j * CS, CS, CS, this);
            }
        }
    }

    private void drawCheese(Graphics g) {
        g.drawImage(cheeseImage, (map.length - 2) * CS + 2, (map.length - 2) * CS + 2, CS - 5, CS - 5, this);
    }

    private void drawRole(Graphics g) {
        g.drawImage(roleImage, x * CS, y * CS, CS, CS, this);
        //以count作为图像的偏移数值
        g.drawImage(roleImage, x * CS, y * CS, x * CS + CS, y * CS + CS,
                count * CS, 0, CS + count * CS, CS, this);
    }

    //loadImage方法载入程序中使用的所有图像
    private void loadImage() {
        // 使用相对路径从resources/images目录加载图像
        floorImage = new ImageIcon(getClass().getResource("/static/images/floor.png")).getImage();
        wallImage = new ImageIcon(getClass().getResource("/static/images/wall.png")).getImage();
        roleImage = new ImageIcon(getClass().getResource("/static/images/mouse.png")).getImage();
        shortCut = new ImageIcon(getClass().getResource("/static/images/shortest.png")).getImage();
        route = new ImageIcon(getClass().getResource("/static/images/common.png")).getImage();
        try {
            cheeseImage = ImageIO.read(getClass().getResourceAsStream("/static/images/cheese.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void drawMap(Graphics g) {
        //双循环进行地图绘制
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                switch (map[i][j]) {
                    //map的标记为0时画出地板
                    case 0:
                    case 2:
                    case 3:
                        g.drawImage(floorImage, i * CS, j * CS, CS, CS, this);
                        break;
                    //map的标记为1是画出墙壁
                    case 1:
                        g.drawImage(wallImage, i * CS, j * CS, CS, CS, this);
                        break;
                    default:
                        break;
                }
            }

        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    //人物的移动在keyPressed(KeyEvent e)按键事件中处理，当人物角色的移动符合游戏规则时才可以移动，例如碰到墙不可以移动
    //无论是否移动均调用repaint()方法重新绘制窗体
    //在移动后，判断是否到达出口（13，13）坐标处，如果到达则游戏结束
    public void keyPressed(KeyEvent e) {
        //获得按键编号
        int keyCode = e.getKeyCode();
        //通过keyCode识别用户按键
        switch (keyCode) {
            case KeyEvent.VK_LEFT -> move(LEFT);
            case KeyEvent.VK_RIGHT -> move(RIGHT);
            case KeyEvent.VK_UP -> move(UP);
            case KeyEvent.VK_DOWN -> move(DOWN);
        }
        //重新绘制窗体图像
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    //isAllow(int x, int y)方法完成符合游戏规则的判断，参数(int x, int y)是人物移动的目的位置
    //如果(x, y)是墙则不能移动，返回False

    /**
     * 用于判定是否允许移动的发生，被move()函数调用
     */
    private boolean isAllow(int x, int y) {
        return map[x][y] != 1;
    }

    //move(int event)函数在移动人物角色时，调用isAllow(int x, int y)方法完成符合游戏规则的判断
    //如果符合游戏规则，则人物位置改变，同时修改人物朝向

    /**
     * 判断移动事件，关联isAllow()方法
     */
    private void move(int event) {
        switch (event) {
            case LEFT:
                if (isAllow(x - 1, y)) x--;
                break;
            case RIGHT:
                if (isAllow(x + 1, y)) x++;
                break;
            case UP:
                if (isAllow(x, y - 1)) y--;
                break;
            case DOWN:
                if (isAllow(x, y + 1)) y++;
                break;
            default:
                break;
        }
    }

    //内部类，用于处理计步动作，实现人物走动的效果
    private class AnimationThread extends Thread {
        public void run() {
            while (true) {
                if (count == 0) {
                    count = 1;
                } else if (count == 1) {
                    count = 0;
                }
                repaint();
            }
        }
    }

    //内部定时器类
    class TimeListener implements ActionListener {
        int second = 0;

        @Override
        public void actionPerformed(ActionEvent e) {
            second++;
            if (second > 30) {
                timer.stop();
                int answer = JOptionPane.showConfirmDialog(null, "哎呀！小老鼠饿晕了！是否复活？",
                        "Game Over", JOptionPane.YES_NO_OPTION);
                if (answer == JOptionPane.YES_OPTION)
                    replay();
                else {
                    setVisible(false);
                    EnterPanel e1 = new EnterPanel();
                    e1.getMaze().add(e1);
                    e1.requestFocus();
                    e1.getMaze().pack();
                    e1.getMaze().setVisible(true);
                }
            }
            else {
                cdText.setText((30 - second) + "s");
                if (x == 13 && y == 13) {
                    timer.stop();
                    JOptionPane.showMessageDialog(null, "小老鼠吃到奶酪了！",
                            "Success!", JOptionPane.INFORMATION_MESSAGE);
                    setVisible(false);
                    EnterPanel e1 = new EnterPanel();
                    e1.getMaze().add(e1);
                    e1.requestFocus();
                    e1.getMaze().pack();
                    e1.getMaze().setVisible(true);
                }
            }
        }
    }
}


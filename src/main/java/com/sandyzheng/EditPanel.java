package com.sandyzheng;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;

public class EditPanel extends JPanel implements MouseListener {
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
    //角色坐标
    private int x, y;

    private int mark;

    private String mapName;

    public EditPanel(int mark){
        setLayout(null);
        //设定初始构造时面板的大小
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        //在初始化时载入图形
        loadImage();
        //角色起始坐标
        x = 1;
        y = 1;
        this.mark = mark;
        map = getSparseArrayFromFile();
        //设定焦点在本窗体并赋予监听对象
        setFocusable(true);
        addButton();
        addMouseListener(this);
    }

    public void intDataToFileOut(int[][] sparseArray) {
        FileWriter out = null;
        try {
            File file = new File("mouse-maze/output/my-mazes/" + mapName + ".txt"); // 修改为项目根目录下的output文件夹
            file.getParentFile().mkdirs();
            out = new FileWriter(file);
            //二维数组按行存入到文件中
            for (int[] ints : sparseArray) {
                for (int j = 0; j < sparseArray.length + 1; j++) {
                    //将每个元素转换为字符串
                    String content = ints[j] + "";
                    assert out != null;
                    out.write(content + "\t");
                }
                assert out != null;
                out.write("\r\n");
            }
            assert out != null;
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to write to file: " + mapName + ".txt");
        }
    }

    private void addButton(){
        JButton button_save = new JButton();
        button_save.setText("Save your map!");
        button_save.setBounds(40, 580, 200, 40);
        button_save.setBackground(new Color(220, 220, 220));
        button_save.setFont(new Font("plain", Font.BOLD, 13));
        add(button_save);

        JButton button_ret = new JButton();
        button_ret.setText("Return");
        button_ret.setBounds(270, 580, 100, 40);
        button_ret.setBackground(new Color(220, 220, 220));
        button_ret.setFont(new Font("plain", Font.BOLD, 13));
        add(button_ret);

        button_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mapName = JOptionPane.showInputDialog(null, "Please enter the name of your map:",
                        "Save Your Map!", JOptionPane.PLAIN_MESSAGE);
                intDataToFileOut(map);
            }
        });

        button_ret.addActionListener(e -> {
            setVisible(false);
            EnterPanel e1 = new EnterPanel();
            ChooseEditPanel e2 = new ChooseEditPanel();
            e1.getMaze().add(e2);
            e1.requestFocus();
            e1.getMaze().pack();
            e1.getMaze().setVisible(true);
        });
    }

    public int[][] getSparseArrayFromFile() {
        //将稀疏矩阵从文件中读取出来
        BufferedReader bufferedReader;
        InputStreamReader inputStreamReader = null;
        //为保存的数组分配空间
        int[][] data = new int[ROW][COL];
        try {
            if (mark == 1)
                inputStreamReader = new InputStreamReader(new FileInputStream(getClass().getResource("/static/original-mazes/Maze1.txt").getPath()));
            else if (mark == 2)
                inputStreamReader = new InputStreamReader(new FileInputStream(getClass().getResource("/static/original-mazes/Maze2.txt").getPath()));
            else if (mark == 3)
                inputStreamReader = new InputStreamReader(new FileInputStream(getClass().getResource("/static/original-mazes/Maze3.txt").getPath()));
            else if (mark == 4)
                inputStreamReader = new InputStreamReader(new FileInputStream(getClass().getResource("/static/original-mazes/BlankMaze.txt").getPath()));
            assert inputStreamReader != null;
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回读取的二维数组
        return data;
    }

    //paintComponent(Graphics g)方法在 JPanel 基础上调用 drawMap(g)方法构建底层地图背景，
    //drawRole(g)方法在指定位置角色坐标（x, y）处画出人物
    //drawCheese(g)方法在指定位置画出奶酪
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawMap(g);  //画出地图
        drawCheese(g);  //画出奶酪
        drawRole(g);  //画出人物
    }

    private void drawCheese(Graphics g) {
        g.drawImage(cheeseImage, (map.length - 2) * CS + 2, (map.length - 2) * CS + 2, CS - 5, CS - 5, this);
    }

    private void drawRole(Graphics g) {
        g.drawImage(roleImage, x * CS, y * CS, CS, CS, this);
    }

    //loadImage方法载入程序中使用的所有图像
    private void loadImage() {
        // 使用相对路径从resources/images目录加载图像
        floorImage = new ImageIcon(getClass().getResource("/static/images/floor.png")).getImage();
        wallImage = new ImageIcon(getClass().getResource("/static/images/wall.png")).getImage();
        roleImage = new ImageIcon(getClass().getResource("/static/images/mouse.png")).getImage();
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
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Point p = MouseInfo.getPointerInfo().getLocation();
        int Mx = ((int)e.getX())/40 + 1;
        int My = ((int)e.getY())/40 + 1;
        if ((Mx > 1 && Mx < ROW) && (My > 1 && My < COL-1)){
            if(map[Mx-1][My-1] == 0){
                map[Mx-1][My-1] = 1;
                map[1][1] = 0;
                map[COL-3][COL-3] = 0;
                repaint();
            }
            else{
                map[Mx-1][My-1] = 0;
                map[1][1] = 0;
                map[COL-3][COL-3] = 0;
                repaint();
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}

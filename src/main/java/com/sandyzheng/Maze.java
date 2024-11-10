package com.sandyzheng;

import javax.swing.*;
import java.awt.*;

public class Maze extends JFrame {
    public Maze() {
        //默认窗体名称
        setTitle("Mouse Maze");
        //获得地图面板实例
        EnterPanel panel = new EnterPanel();
        Container contentPane = getContentPane();
        contentPane.add(panel);
        //执行并构建窗体设定
        pack();
    }
}

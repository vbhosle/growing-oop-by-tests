package com.example.ui;

import com.example.Main;

import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;

import static com.example.Main.STATUS_JOINING;

public class MainWindow extends JFrame {
    public static final String SNIPER_STATUS_NAME = "sniper status";
    private final JLabel sniperStatus = createLabel(STATUS_JOINING);

    public MainWindow() {
        super("Auction Sniper");
        setName(Main.MAIN_WINDOW_NAME);
        add(sniperStatus);
        setSize(200, 200);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private JLabel createLabel(String initialText) {
        JLabel label = new JLabel(initialText);
        label.setName(SNIPER_STATUS_NAME);
        label.setText(initialText);
        label.setBorder(new LineBorder(Color.BLACK));
        return label;
    }
}

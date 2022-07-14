package config;

import gui.MainMenuFrame;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.swing.*;
import java.awt.*;

@Configuration
public class GUIConfiguration {
    @Bean("frameTitle")
    @Scope("singleton")
    public JLabel getMainTitle() {
        final JLabel jLabel = new JLabel();
        jLabel.setText("Утилиты инженера-конструктора");
        jLabel.setVerticalTextPosition(SwingConstants.CENTER);
        jLabel.setVerticalAlignment(SwingConstants.TOP);
        jLabel.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel.setForeground(new Color(255,150,0));
        jLabel.setFont(new Font("gost", Font.BOLD, 24));
        return jLabel;
    }
    @Bean("menu")
    @Scope("singleton")
    public MainMenuFrame getMenu() {
        final MainMenuFrame menu = new MainMenuFrame();
        menu.setTitle("КалькуляторСпецификации 1.0");
        menu.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        final ImageIcon imageIcon = new ImageIcon("src/main/resources/gui/logos/logo.png");
        menu.setIconImage(imageIcon.getImage());
        menu.getContentPane().setBackground(new Color(25,137,125));
        menu.setVisible(true);
        menu.pack();
        return menu;
    }
}

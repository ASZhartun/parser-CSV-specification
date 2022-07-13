package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.swing.*;
import java.awt.*;

@Configuration
public class GUIConfiguration {
    @Bean("menu")
    @Scope("singleton")
    public JFrame getMenu() {
        final JLabel jLabel = new JLabel();
        jLabel.setText("Helloooooooooooooooooooooooooo!");
        jLabel.setVerticalTextPosition(SwingConstants.CENTER);
        jLabel.setVerticalAlignment(SwingConstants.CENTER);
        jLabel.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel.setForeground(new Color(255,150,0));
        jLabel.setFont(new Font("MV Boli", Font.BOLD, 12));
        jLabel.setBackground(new Color(150,0,150));
        jLabel.setOpaque(true);
//        jLabel.setBounds(15,15,320,60);


        final JFrame menu = new JFrame();
        menu.add(jLabel);
//        menu.setMinimumSize(new Dimension(1280, 720));
        menu.setTitle("КалькуляторСпецификации 1.0");
        menu.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        final ImageIcon imageIcon = new ImageIcon("src/main/resources/gui/logos/logo.png");
        menu.setIconImage(imageIcon.getImage());
        menu.getContentPane().setBackground(new Color(25,137,125));
//        menu.setLayout(null);
        menu.setVisible(true);
        menu.pack();


        return menu;
    }
}

package config;

import gui.BasicFunctionalPanel;
import gui.CalcSpecButton;
import gui.MainFrame;
import gui.TitlePanel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.swing.*;
import java.awt.*;

@Configuration
public class GUIConfiguration {
    @Bean("mainFrame")
    @Scope("singleton")
    public MainFrame getMainFrame() {
        final MainFrame menu = new MainFrame();
        menu.setTitle("КалькуляторСпецификации 1.0");
        menu.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        final ImageIcon imageIcon = new ImageIcon("src/main/resources/gui/logos/logo.png");
        menu.setIconImage(imageIcon.getImage());
        menu.getContentPane().setBackground(new Color(25, 137, 125));
        menu.setVisible(true);
        menu.pack();
        return menu;
    }

    @Bean("titlePanel")
    @Scope("singleton")
    public TitlePanel getTitlePanel() {
        final TitlePanel titlePanel = new TitlePanel();
        titlePanel.setBackground(new Color(150, 25, 150));
        titlePanel.setBounds(0, 0, 420, 50);
        return titlePanel;
    }

    @Bean("mainTitle")
    @Scope("singleton")
    public JLabel getMainTitle() {
        final JLabel jLabel = new JLabel();
        jLabel.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel.setText("Утилиты инженера-конструктора");
        jLabel.setVerticalTextPosition(SwingConstants.CENTER);
        jLabel.setVerticalAlignment(SwingConstants.CENTER);
        jLabel.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel.setForeground(new Color(255, 150, 0));
        jLabel.setFont(new Font("gost", Font.BOLD, 24));
        return jLabel;
    }

    @Bean("basicFunctionalPanel")
    @Scope("singleton")
    public BasicFunctionalPanel getBasic() {
        final BasicFunctionalPanel basics = new BasicFunctionalPanel();
        basics.setOpaque(false);
        basics.setBackground(new Color(0,0,0));
        basics.setBounds(0,100,250,250);
        return basics;
    }
    @Bean
    public CalcSpecButton getButton() {
        final CalcSpecButton calcSpecButton = new CalcSpecButton();
        calcSpecButton.setBackground(new Color(150,150,0));
        calcSpecButton.setBounds(250, 500, 50,50);
        calcSpecButton.setFont(new Font("gost", Font.BOLD, 18));
        calcSpecButton.setText("CalcSpec");
        return calcSpecButton;
    }
}

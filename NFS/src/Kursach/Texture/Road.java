package Kursach.Texture;

import Kursach.Game.Enemy;
import Kursach.Game.Player;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by daniil on 21.12.16.
 */
public class  Road extends JPanel implements ActionListener, Runnable{

    Timer mainTimer = new Timer(2, (ActionListener) this);

    Image img = new ImageIcon(getClass().getClassLoader().getResource("res/trassa.png")).getImage();

    public Player p = new Player();

    Thread enemiesFactory = new Thread(this);

    //Thread sound = new Thread(new Audio());

    List<Enemy> enemies = new ArrayList<Enemy>(); //список врагов

    public Road () {
        mainTimer.start();
        enemiesFactory.start(); //завод ботов
        //sound.start();
        addKeyListener(new Key());
        setFocusable(true);
    }

    private class Key extends KeyAdapter {
        public void keyPressed (KeyEvent e) {
            p.keyPressed(e);
        }

        public void keyReleased (KeyEvent e) {
            p.keyReleased(e);
        }
    }

    public void paint(Graphics g) {
        g = (Graphics2D) g;
        g.drawImage(img, 0, -p.layer1, null); //слой один
        g.drawImage(img, 0, -p.layer2, null); //слой два
        g.drawImage(p.img, p.x, p.y, null); //игрок

        double v = (200 / Player.MAX_V) * p.v;
        g.setColor(Color.BLACK);
        Font font = new Font("Arial", Font.ITALIC, 20);
        g.setFont(font);
        g.drawString("Скорость: " + ((int)v) + " км/ч", 170, 30);

        Iterator<Enemy> i = enemies.iterator();
        while (i.hasNext()) {
            Enemy e = i.next(); //получили новый обтект
            if (e.y >= 1900 || e.y <= -1900) {
                i.remove();
            }
            else {
                e.muve();
                g.drawImage(e.img, e.x, e.y, null);
            }
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        p.muve(); //ехать
        repaint(); //перерисовка ботов
        testCollisionWithEnemies();
        testWin();
    }

    private void testWin() {

        if (p.s > 200000) {
            JOptionPane.showMessageDialog(null, "Вы выиграли!");
            System.exit(0);
        }

    }

    private void testCollisionWithEnemies() {

        Iterator<Enemy> i = enemies.iterator();
        while (i.hasNext()) {
            Enemy e = i.next();
            if (p.getRect().intersects(e.getRect())) { //если пересикаются
                JOptionPane.showMessageDialog(null, "Вы проиграли!");
                System.exit(1);
            }
        }

    }

    @Override
    public void run() {
        while (true) {
            Random random = new Random();
            try {
                Thread.sleep(random.nextInt(9500));
                enemies.add(new Enemy(random.nextInt(500),1,random.nextInt(10), this));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

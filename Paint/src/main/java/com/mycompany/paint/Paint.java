/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.paint;

/**
 *
 * @author melis
 */
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Color;
import java.awt.Point;
import java.awt.Graphics;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.util.Random;
import javax.imageio.ImageIO;

public class Paint extends JFrame implements ActionListener {

    JMenu archivo, dibujar, ayuda;
    JMenuItem acerca, salir, nuevo, guardar, abrir, color;
    JRadioButtonMenuItem linea, rectangulo, elipse;
    JCheckBoxMenuItem relleno;
    JColorChooser colorChooser = new JColorChooser();
    ButtonGroup btn;
    MiPanel miPanel;

    public Paint() {
        crearmenu();
        addlisteners();
        miPanel = new MiPanel();
        this.add(miPanel);
        this.setSize(800, 600);
        this.setVisible(true);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setTitle("Paint básico - Melissa Peñaloza");
    }

    private void addlisteners() { // Acciones
        acerca.addActionListener(this);
        salir.addActionListener(this);
        nuevo.addActionListener(this);
        guardar.addActionListener(this);
        abrir.addActionListener(this);
        color.addActionListener(this);
        linea.addActionListener(this);
        rectangulo.addActionListener(this);
        elipse.addActionListener(this);
        relleno.addActionListener(this);
    }

    public void crearmenu() { // se crean los botones que tiene la pantalla
        JMenuBar menu = new JMenuBar();
        archivo = new JMenu("Archivo");
        nuevo = new JMenuItem("Nuevo");
        abrir = new JMenuItem("Abrir");
        guardar = new JMenuItem("Guardar");
        salir = new JMenuItem("Salir");
        archivo.add(nuevo);
        archivo.add(abrir);
        archivo.add(guardar);
        archivo.add(salir);
        menu.add(archivo);
        dibujar = new JMenu("Dibujar");
        btn = new ButtonGroup();
        linea = new JRadioButtonMenuItem("Línea");
        rectangulo = new JRadioButtonMenuItem("Rectángulo");
        elipse = new JRadioButtonMenuItem("Elipse");
        btn.add(linea);
        btn.add(rectangulo);
        btn.add(elipse);
        btn.setSelected(linea.getModel(), true);
        relleno = new JCheckBoxMenuItem("Relleno");
        color = new JMenuItem("Color");
        dibujar.add(linea);
        dibujar.add(rectangulo);
        dibujar.add(elipse);
        dibujar.add(relleno);
        dibujar.add(color);
        menu.add(dibujar);
        ayuda = new JMenu("Ayuda");
        acerca = new JMenuItem("Acerca de");
        ayuda.add(acerca);
        menu.add(ayuda);
        this.setJMenuBar(menu);
    }

    public static void main(String[] args) {
        Paint miVentana = new Paint();
        miVentana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == nuevo) {
            miPanel.resetALL();
        }
        if (e.getSource() == abrir) {
            miPanel.abrir();
        }
        if (e.getSource() == guardar) {
            miPanel.guardar();
        }
        if (e.getSource() == salir) {
            System.exit(0);
        }
        if (e.getSource() == linea) {
            miPanel.linea = true;
            miPanel.rectangulo = false;
        }
        if (e.getSource() == rectangulo) {
            miPanel.linea = false;
            miPanel.rectangulo = true;
        }
        if (e.getSource() == elipse) {
            miPanel.linea = false;
            miPanel.rectangulo = false;
        }
        if (e.getSource() == relleno) {
            miPanel.relleno = !miPanel.relleno;
        }
        if (e.getSource() == color) {
            Color color = JColorChooser.showDialog(this, "Selecciona un color", miPanel.getColorActual());
            miPanel.setColorActual(color);
        }
        if (e.getSource() == acerca) {
            JOptionPane.showMessageDialog(null, "Parcial 1 - Computación gráfica 202402");
        }
    }
}

class MiPanel extends JPanel {
    Point p1;
    Point p2;
    Shape figura;
    Random R = new Random();
    public Color coloractual = Color.MAGENTA;
    BufferedImage myImage;
    Graphics2D g2D;
    boolean rectangulo = false;
    boolean linea = true;
    boolean relleno = false;

    public MiPanel() {
        OyenteDeRaton miOyente = new OyenteDeRaton();
        OyenteDeMovimiento miOyente2 = new OyenteDeMovimiento();
        addMouseListener(miOyente);
        addMouseMotionListener(miOyente2);
    }

    public Color getColorActual() {
        return coloractual;
    }

    public void setColorActual(Color color) {
        coloractual = color;
    }

    public Graphics2D crearGraphics2D() {
        Graphics2D g2 = null;
        if (myImage == null || myImage.getWidth() != getSize().width || myImage.getHeight() != getSize().height) {
            myImage = (BufferedImage) createImage(getSize().width, getSize().height);
        }
        if (myImage != null) {
            g2 = myImage.createGraphics();
            g2.setColor(coloractual);
            g2.setBackground(getBackground());
        }
        g2.clearRect(0, 0, getSize().width, getSize().height);
        return g2;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (myImage == null) {
            g2D = crearGraphics2D();
        }
        if (figura != null) {
            g2D.setColor(coloractual);
            g2D.draw(figura);
            if (relleno) {
                g2D.fill(figura);
            }
            if (myImage != null && isShowing()) {
                g.drawImage(myImage, 0, 0, this);
            }
            figura = null;
        }
    }

    public void resetALL() {
        myImage = null;
        repaint();
    }

    public Shape crearFigura(Point p1, Point p2) {
        double xInicio = Math.min(p1.getX(), p2.getX());
        double yInicio = Math.min(p1.getY(), p2.getY());
        double ancho = Math.abs(p2.getX() - p1.getX());
        double altura = Math.abs(p2.getY() - p1.getY());
        return new Rectangle2D.Double(xInicio, yInicio, ancho, altura);
    }

    public Shape crearLinea(Point p1, Point p2) {
        return new Line2D.Double(p1.getX(), p1.getY(), p2.getX(), p2.getY());
    }

    public Shape crearElipse(Point p1, Point p2) {
        double xInicio = Math.min(p1.getX(), p2.getX());
        double yInicio = Math.min(p1.getY(), p2.getY());
        double ancho = Math.abs(p2.getX() - p1.getX());
        double altura = Math.abs(p2.getY() - p1.getY());
        return new Ellipse2D.Double(xInicio, yInicio, ancho, altura);
    }

    public void abrir() {
        JFileChooser fc = new JFileChooser();
        int seleccion = fc.showOpenDialog(this);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            try {
                File archivo = fc.getSelectedFile();
                myImage = ImageIO.read(archivo);
                repaint();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al abrir la imagen.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void guardar() {
        JFileChooser fc = new JFileChooser();
        int seleccion = fc.showSaveDialog(this);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            try {
                File archivo = fc.getSelectedFile();
                ImageIO.write(myImage, "png", archivo);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar la imagen.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    class OyenteDeRaton extends MouseAdapter {
        public void mousePressed(MouseEvent evento) {
            MiPanel.this.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            p1 = evento.getPoint();
        }

        public void mouseReleased(MouseEvent evento) {
            MiPanel.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            p2 = evento.getPoint();
            if (rectangulo) {
                figura = crearFigura(p1, p2);
            } else if (linea) {
                figura = crearLinea(p1, p2);
            } else {
                figura = crearElipse(p1, p2);
            }
            repaint();
        }
    }

    class OyenteDeMovimiento extends MouseMotionAdapter {
        public void mouseDragged(MouseEvent evento) {
            Graphics2D g2D;
            if (figura != null) {
                g2D = (Graphics2D) MiPanel.this.getGraphics();
                g2D.setXORMode(MiPanel.this.getBackground());
                g2D.setColor(coloractual);
                g2D.draw(figura);
            }
            p2 = evento.getPoint();
            if (rectangulo) {
                figura = crearFigura(p1, p2);
            } else if (linea) {
                figura = crearLinea(p1, p2);
            } else {
                figura = crearElipse(p1, p2);
            }
            g2D = (Graphics2D) MiPanel.this.getGraphics();
            g2D.setXORMode(MiPanel.this.getBackground());
            g2D.setColor(coloractual);
            g2D.draw(figura);
        }
    }
}


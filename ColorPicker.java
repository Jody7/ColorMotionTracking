import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;


public class ColorPicker implements MouseListener,KeyListener{
    static boolean stop=false;
    static int x=0;
    static int y=0;
    static boolean mouse=true;
    boolean debounce = false;


    static int r = 230;
    static int g = 0;
    static int b = 255;

    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == e.VK_F4){

            if(debounce==false){
               stop = !stop;
            }
            debounce = true;
        }
        if(e.getKeyCode() == e.VK_F5){

           if(debounce==false){
           mouse = !mouse;
           }
           debounce = true;
        }
    }

    public void keyReleased(KeyEvent e) {

            debounce = false;


    }


    public void mousePressed(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {}

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    public void mouseClicked(MouseEvent e) {
        BufferedImage img = Main.image;

        x = e.getX();
        y = e.getY();
        int pixel = img.getRGB(x, y);
        int alpha = (pixel >> 24) & 0xff;
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;


        System.out.println("R"+red+"G"+green+"B"+blue);
        r = red;
        g = green;
        b = blue;

    }


}

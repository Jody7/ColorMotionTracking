import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.image.BufferedImage;
import java.net.*;
import java.security.MessageDigest;

//
public class Main {
    static BufferedImage image = null;
    static String macd = null;


    public static void main(String[] args) {
        boolean auth = false;
        InetAddress ip;
        String digest = null;
        try {

            ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);

            byte[] mac = network.getHardwareAddress();



            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
            macd = sb.toString();

        } catch (UnknownHostException e) {

            e.printStackTrace();

        } catch (SocketException e){

            e.printStackTrace();

        }

        try {
            digest = sha256(macd+"1");
            HTTP http = new HTTP();
            digest = digest + "END";

            /*
           String d = http.sendGet("https://api.trello.com/1/lists/5624150af238f859bd31c184/cards?key=8ae99e0ea7b85ca2789086065afa508c&token=a092a3e6ff867566bdb47e0225767f7a317bb83c43e0a3f1b0c18526f77395e9");

            if(d.contains(digest)){
                 auth = true;
            }
            else{
                StringSelection selection = new StringSelection(digest);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selection, selection);
            }

            */
            //bypass Account Checking
            auth = true;

            System.out.println(auth);
        }catch(Exception e){
            e.printStackTrace();
        }




        Robot robot = null;
        try {
            robot = new Robot();
        } catch (Exception e) {
        }


        Canvas canvas = new Canvas();
        // i know this isn't good custom drawing btw

        JFrame frame = new JFrame();
        frame.add(canvas);
        frame.setVisible(true);

        frame.setTitle(digest);

        ColorPicker colorPicker = new ColorPicker();
        canvas.addMouseListener(colorPicker);



        while(true) {
            if(!auth)
            {
                try {
                    Thread.sleep(600000);
                    //wait... what?
                    break;
                }catch (Exception e){e.printStackTrace();}
            }


            int[] coords = Hwnd.WindowDimension("Untitled - Notepad");
            //Resize notepad, then open proccess and cover the notepad.


            int x = coords[0];
            int y = coords[1];
            int width = coords[2] - coords[0];
            int height = coords[3] - coords[1];

            //System.out.println(height);
            //System.out.println(width);


            Rectangle rec = new Rectangle();
            rec.setBounds(x, y, width, height);
            Dimension d = new Dimension(width,height);
            canvas.setSize(d);
            frame.setSize(d);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            canvas.addKeyListener(colorPicker);


            image = robot.createScreenCapture(rec);


            int[][] pixels = Scan(image);

            canvas.getGraphics().drawImage(image,0,0,null);

            if(ColorPicker.stop) {
                canvas.getGraphics().drawString("Tracking: Off", 0, 100);
            }else {
                canvas.getGraphics().drawString("Tracking: On", 0, 100);
            }

            if(ColorPicker.mouse) {
                canvas.getGraphics().drawString("Mouse: Off", 100, 100);
            }else {
                canvas.getGraphics().drawString("Mouse: On", 100, 100);
            }

            /*
            Point pointer = MouseInfo.getPointerInfo().getLocation();

            int ax=0;
            int ay=0;
            */

            for(int i=0; i<900; i++){

                for(int j=0; j<900; j++){

                    if(pixels[j][i]==1){
                        System.out.println(j+":"+i);
                        if(ColorPicker.stop) break;


                        canvas.getGraphics().setColor(Color.black);
                        canvas.getGraphics().drawRect(j,i,1,1);
                        canvas.getGraphics().drawLine(0,0,j,i);


                        if(!ColorPicker.mouse) {


                            robot.mouseMove(x + j, i + y);


                        }




                    }
                    else {

                    }
                }
            }






           // System.out.println(aaa+x + ":" + 0);

            try {
                Thread.sleep(16);
            } catch (Exception e) {
            }

        }






    }



    public static String sha256(String base) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (byte aHash : hash) {
                String hex = Integer.toHexString(0xff & aHash);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    private static int[][] Scan(BufferedImage image) {

        int range = 8;

        int[][] pix = new int[900][900];
        int w = image.getWidth();
        int h = image.getHeight();
        for (int i = 0; i < h; i=i+1) {
            for (int j = 0; j < w; j++) {
                int pixel = image.getRGB(j, i);
                int alpha = (pixel >> 24) & 0xff;
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;


                if(green>ColorPicker.g-range && green<ColorPicker.g+range && red<ColorPicker.r+
                        range&& red>ColorPicker.r-range && blue<ColorPicker.b+range && blue>ColorPicker.b-range){
                    pix[j][i] = 1;

                }

                /*
                if(green>=170 && red<50&& blue<50){
                    pix[j][i] = 1;

                }*/


            }
        }
        return pix;
    }


}




/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extractrgbcolor;

/**
 *
 * @author mike
 */
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class colorTester {

    public static void main(String args[]) throws Exception {
        File             file = new File(args[0]);
        ImageInputStream   is = ImageIO.createImageInputStream(file);
        Iterator         iter = ImageIO.getImageReaders(is);

        Map        m;
        int      rgb;
        int    width;
        int   height;
        int[] rgbArr;
        if (!iter.hasNext()) {
            System.out.println("Error al cargar " + file);
            System.exit(1);
        }
        ImageReader imageReader = (ImageReader) iter.next();
        imageReader.setInput(is);

        BufferedImage image = imageReader.read(0);
        
        m      = new HashMap();
        width  = image.getWidth();
        height = image.getHeight();
        
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                rgb    = image.getRGB(i, j);
                rgbArr = getRGBArr(rgb);
                if (!isGray(rgbArr)) {
                    Integer counter = (Integer) m.get(rgb);
                    if (counter == null) {
                        counter = 0;
                    }
                    counter++;
                    m.put(rgb, counter);
                }
            }
        }

        int[] colorArray = getMostCommonColour(m);
        ColorUtils c = new ColorUtils();
        String name = c.getColorNameFromRgb(colorArray[0], colorArray[1], colorArray[2]);
        System.out.println(name);
    }

    public static int[] getMostCommonColour(Map map) {
        List list = new LinkedList(map.entrySet());
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o1)).getValue())
                        .compareTo(((Map.Entry) (o2)).getValue());
            }
        });
        Map.Entry me = (Map.Entry) list.get(list.size() - 1);
        int[] rgb = getRGBArr((Integer) me.getKey());
        return rgb;
    }

    public static int[] getRGBArr(int pixel) {
        int alpha = (pixel >> 24) & 0xff;
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;
        return new int[]{red, green, blue};

    }

    public static boolean isGray(int[] rgbArr) {
        int rgDiff = rgbArr[0] - rgbArr[1];
        int rbDiff = rgbArr[0] - rgbArr[2];
        int tolerance = 10;
        if (rgDiff > tolerance || rgDiff < -tolerance) {
            if (rbDiff > tolerance || rbDiff < -tolerance) {
                return false;
            }
        }
        return true;
    }
}

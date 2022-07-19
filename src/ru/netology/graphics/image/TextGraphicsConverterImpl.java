package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;


public class TextGraphicsConverterImpl implements TextGraphicsConverter {

    private double maxRatio;
    private int maxWidth;
    private int maxHeight;
    private TextColorSchema schema = new TextColorSchemaImpl();


    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        // Вот так просто мы скачаем картинку из интернета :)
        BufferedImage img = ImageIO.read(new URL(url));
        if (maxRatio != 0) {
            double ratio;
            if (img.getWidth() > img.getHeight()) {
                ratio = (double) img.getWidth() / img.getHeight();
            } else {
                ratio = (double) img.getHeight() / img.getWidth();
            }
            if (ratio > maxRatio) {
                throw new BadImageSizeException(ratio, maxRatio);
            }
        }

        double widthDifference = 1;
        double heightDifference = 1;
        if (maxWidth > 0 && img.getWidth() > maxWidth) {
            widthDifference = (double) maxWidth / img.getWidth();

        }
        if (maxHeight > 0 && img.getHeight() > maxHeight) {
            heightDifference = (double) maxHeight / img.getHeight();
        }

        double minValue = Math.min(widthDifference, heightDifference);
        int newWidth = (int) (minValue * img.getWidth());
        int newHeight = (int) (minValue * img.getHeight());

        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);
        // Теперь сделаем её чёрно-белой. Для этого поступим так:
        // Создадим новую пустую картинку нужных размеров, заранее указав последним
        // параметром чёрно-белую цветовую палитру:
        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        // Попросим у этой картинки инструмент для рисования на ней:
        Graphics2D graphics = bwImg.createGraphics();
        // А этому инструменту скажем, чтобы он скопировал содержимое из нашей суженной картинки:
        graphics.drawImage(scaledImage, 0, 0, null);

        WritableRaster bwRaster = bwImg.getRaster();

        StringBuilder field = new StringBuilder();
        for (int h = 0; h < newHeight; h++) {
            for (int w = 0; w < newWidth; w++) {
                int color = bwRaster.getPixel(w, h, new int[3])[0];
                char c = schema.convert(color);
                field.append(c).append(c);

            }
            field.append("\n");
        }

        String result = field.toString();
        return result; // Возвращаем собранный текст.
    }


    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setMaxWidth(int width) {
        maxWidth = width;

    }

    @Override
    public void setMaxHeight(int height) {
        maxHeight = height;

    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.schema = schema;
    }
}


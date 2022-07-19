package ru.netology.graphics.image;

public class TextColorSchemaImpl implements TextColorSchema {

    public static final char[] symbols = {'#', '$', '@', '%', '*', '+', '-', '\''};

    @Override
    public char convert(int color) {
        return symbols[color * symbols.length / 256]; // замена ОДНОГО цвета (одного пикселя)  на символ
        // у нас значит 256 оттенков (от 0 до 255), мы цвета делим на 256
        // и умножаем на длину массива (на кол-во наших фактических символов)
        // и получаем некий промежуток цветовых оттенков на 1 символ
        // Ведь цветов же больше, чем заявленных символов, поэтому разговор о промежутке
    }

}

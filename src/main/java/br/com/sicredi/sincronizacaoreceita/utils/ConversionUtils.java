package br.com.sicredi.sincronizacaoreceita.utils;

import javax.swing.text.MaskFormatter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

public class ConversionUtils {

    private ConversionUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static Double parseStringToFormattedDouble(String value) throws ParseException {
        DecimalFormat df = new DecimalFormat("#.##");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        df.setDecimalFormatSymbols(symbols);
        return df.parse(value).doubleValue();
    }

    public static String parseDoubleToString(double value) {
        return String.format("%.2f", value);
    }

    public static String stringWithMask(String value, String mask) {
        try {
            MaskFormatter maskFormatter = new MaskFormatter(mask);
            maskFormatter.setValueContainsLiteralCharacters(false);
            return maskFormatter.valueToString(value);
        } catch (ParseException e) {
            return value;
        }
    }

}

package br.com.sicredi.sincronizacaoreceita.utils;

import javax.swing.text.MaskFormatter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

/** Utilities for converting data
 * @author Vinnicius Santos<vinnicius.santos@dcx.ufpb.br>
 */
public class ConversionUtils {

    /** Private constructor to avoid instantiation
     * @author Vinnicius Santos<vinnicius.santos@dcx.ufpb.br>
     */
    private ConversionUtils() {
        throw new IllegalStateException("Utility class");
    }

    /** Parse string to a formatted double representation
     * @author Vinnicius Santos<vinnicius.santos@dcx.ufpb.br>
     * @param value - String
     * @return Double - formatted double representation
     */
    public static Double parseStringToFormattedDouble(String value) throws ParseException {
        DecimalFormat df = new DecimalFormat("#.##");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        df.setDecimalFormatSymbols(symbols);
        return df.parse(value).doubleValue();
    }

    /** Parse double to a string representation
     * @author Vinnicius Santos<vinnicius.santos@dcx.ufpb.br>
     * @param value - double
     * @return String - formatted string representation
     */
    public static String parseDoubleToString(double value) {
        return String.format("%.2f", value);
    }

    /** Apply an mask to an string
     * @author Vinnicius Santos<vinnicius.santos@dcx.ufpb.br>
     * @param value - String
     * @param mask - String
     * @return String - formatted string representation
     */
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

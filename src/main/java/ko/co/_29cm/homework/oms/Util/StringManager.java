package ko.co._29cm.homework.oms.Util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class StringManager {
    public static boolean IsEmpty(String inputStr){
        return inputStr.trim().length() == 0 ? true : false;
    }
    public static String getFormatStr(BigDecimal value){
        DecimalFormat decimalFormat = new DecimalFormat("#,##0");
        return decimalFormat.format(value);
    }
}

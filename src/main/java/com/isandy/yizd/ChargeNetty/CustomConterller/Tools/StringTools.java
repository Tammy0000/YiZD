package com.isandy.yizd.ChargeNetty.CustomConterller.Tools;

import org.apache.commons.lang3.StringUtils;

public class StringTools {
    public String SubtoString(String string, String tag, String end) {
        return StringUtils.substringBetween(string, tag, end);
    }
}

package org.apache.accumulo.monitor.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Simple utility class to validate Accumulo Monitor Query and Path parameters
 */
public class ParameterValidator {

    private static final Logger LOG = LoggerFactory.getLogger(ParameterValidator.class);
    
    public static String validateBoundedNumber(String n, int lowerbound, int upperbound) {
        try {
            int i = Integer.parseInt(n);
            if (i > lowerbound && i < upperbound) {
                return n.trim();
            }
        } catch (NumberFormatException nfe) {
            LOG.error(nfe.getLocalizedMessage());
        }
        return StringUtils.EMPTY;
    }

    public static String validateBoundedNumber(String n, int lowerbound, int upperbound, String defaultValue) {
        String sanitized = validateBoundedNumber(n, lowerbound, upperbound);
        return StringUtils.EMPTY.equals(sanitized) ? defaultValue : sanitized;
    }

    public static String validatePositiveInteger(String n) {
        return validateBoundedNumber(n, 0, Integer.MAX_VALUE);
    }

    public static String validatePositiveInteger(String n, String defaultValue) {
        String sanitized = validatePositiveInteger(n);
        return StringUtils.EMPTY.equals(n) ? defaultValue : sanitized;
    }
    public static String sanitizeParameter(String s) throws UnsupportedEncodingException {
        return StringUtils.isEmpty(s) ? StringUtils.EMPTY : URLEncoder.encode(s, "UTF-8").trim();
    }

    public static String sanitizeParameter(String s, String defaultValue) throws UnsupportedEncodingException {
        String sanitized = sanitizeParameter(s);
        return StringUtils.EMPTY.equals(sanitized) ? defaultValue : sanitized;
    }
}

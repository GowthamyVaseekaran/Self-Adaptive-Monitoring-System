package monitoring.core.bean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Collection;
import java.util.Map;

/**
 * The {@code IFStats} Class used to define contains.
 *
 * @author joe
 *
 */
public final class IFStats {

    private static Log log = LogFactory.getLog(IFStats.class);

    /** The Constant 60*1000. */
    public static final long AMINUTE = 1000 * 60;

    /** The Constant 60*AMINUTE. */
    public static final long AHOUR = AMINUTE * 60;

    /** The Constant 24*AHOUR. */
    public static final long ADAY = 24 * AHOUR;

    /** The Constant 7*ADAY. */
    final static public long AWEEK = 7 * ADAY;

    /** The Constant 30*ADAY. */
    final static public long AMONTH = 30 * ADAY;

    /** The Constant 365*ADAY. */
    final static public long AYEAR = 365 * ADAY;

    /** The Constant "id". */
    public static final String ID = "id";

    /** The Constant "state". */
    public static final String STATE = "state";

    /** The Constant "". */
    public static final String EMPTY = "";

    /** The Constant "url". */
    public static final String URL = "url";

    /** The Constant "uri". */
    public static final String URI = "uri";

    /** The Constant "status". */
    public static final String STATUS = "status";

    /** The Constant "UTF-8". */
    public static final String UTF8 = "UTF-8";

    /** The Constant "none". */
    public static final String NONE = "none";

    /** The Constant "message". */
    public static final String MESSAGE = "message";

    /** The Constant "warn". */
    public static final String WARN = "warn";

    /** the Constant "error" . */
    public static final String ERROR = "error";

    /** the Constant "created" */
    public static final String CREATED = "created";

    /** the Constant "updated" */
    public static final String UPDATED = "updated";

    public static final int ITEMS_PER_PAGE = 10;

    public static IFStats inst = new IFStats();

    private IFStats() {
    }

    /**
     * trainWithMetrics whether equals the two objects.
     *
     * @param s1
     *            the object s1
     * @param s2
     *            the object s2
     * @return boolean
     */
    public static boolean isSame(Object s1, Object s2) {
        if (s1 == s2)
            return true;
        if (IFStats.isEmpty(s1) && IFStats.isEmpty(s2))
            return true;

        if (s1 instanceof String && s2 instanceof String) {
            return ((String) s1).equalsIgnoreCase((String) s2);
        }

        if (s1 != null) {
            return s1.equals(s2);
        }

        return false;
    }

    /**
     * safely parse the object to long, if failed return default value.
     *
     * @param v
     *            the object
     * @param defaultValue
     *            the default value
     * @return long
     */
    public static long toLong(Object v, long defaultValue) {
        if (v != null) {
            if (v instanceof Number) {
                return ((Number) v).longValue();
            }

            String s = v.toString().replaceAll(",", "").trim();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);

                c = IFStats.getNumber(c);
                if (c >= '0' && c <= '9') {
                    sb.append(c);
                } else if (c == '-' && sb.length() == 0) {
                    sb.append(c);
                } else if (sb.length() > 0) {
                    break;
                }
            }
            s = sb.toString();
            if (s.length() > 0) {
                try {
                    return Long.parseLong(s);
                } catch (Exception e) {
                    log.error(e);
                }
            }
        }
        return defaultValue;
    }

    /**
     * safely parse the object to integer, if failed return 0.
     *
     * @param v
     *            the object
     * @return int
     */
    public static int toInt(Object v) {
        return IFStats.toInt(v, 0);
    }

    /**
     * safely parse the object to integer. if failed return the default value
     *
     * @param v
     *            the object
     * @param defaultValue
     *            the default value
     * @return int
     */
    public static int toInt(Object v, int defaultValue) {
        if (v != null) {
            if (v instanceof Number) {
                return ((Number) v).intValue();
            }

            String s = v.toString().replaceAll(",", "").trim();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < s.length(); i++) {
                char c = IFStats.getNumber(s.charAt(i));
                if (c >= '0' && c <= '9') {
                    sb.append(c);
                } else if (c == '-' && sb.length() == 0) {
                    sb.append(c);
                } else if (sb.length() > 0) {
                    break;
                }
            }
            s = sb.toString();

            if (s.length() > 0) {
                try {
                    return Integer.parseInt(s);
                } catch (Exception e) {
                    log.error(e);
                }
            }
        }

        return defaultValue;
    }

    /**
     * trainWithMetrics the object is empty? null , empty string, empty collection, empty map.
     *
     * @param s
     *            the object, may string, list, map
     * @return boolean, return true if null, or empty
     */
    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Object s) {
        if (s == null) {
            return true;
        }
        if (s instanceof String) {
            return IFStats.EMPTY.equals(s);
        } else if (s instanceof Collection) {
            return ((Collection) s).isEmpty();
        } else if (s instanceof Map) {
            return ((Map) s).isEmpty();
        }
        return false;
    }

    /**
     * safely parse the object to float, if failed return 0.
     *
     * @param v
     *            the object
     * @return float
     */
    public static float toFloat(Object v) {
        return toFloat(v, 0);
    }

    /**
     * safely parse a object to a float, if failed return default value.
     *
     * @param v
     *            the v
     * @param defaultValue
     *            the default value
     * @return float
     */
    public static float toFloat(Object v, float defaultValue) {
        if (v != null) {
            if (v instanceof Number) {
                return ((Number) v).floatValue();
            }

            String s = v.toString().replaceAll(",", "").trim();

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < s.length(); i++) {
                char c = IFStats.getNumber(s.charAt(i));
                if (c >= '0' && c <= '9') {
                    sb.append(c);
                } else if (c == '-' && sb.length() == 0) {
                    sb.append(c);
                } else if (c == '.') {
                    if (sb.indexOf(".") > -1) {
                        break;
                    } else {
                        sb.append(c);
                    }
                } else if (sb.length() > 0) {
                    break;
                }
            }
            s = sb.toString();

            if (s.length() > 0) {
                try {
                    return Float.parseFloat(s);
                } catch (Exception e) {
                    log.error(e);
                }
            }
        }
        return defaultValue;
    }

    private static final char[][] DIGS = { "０１２３４５６７８９".toCharArray(), "零一二三四五六七八九".toCharArray(),
            "零壹贰叁肆伍陆柒捌玖".toCharArray() };

    /**
     * trainWithMetrics the "s" and return a number, that convert Chinese number to real number.
     *
     * @param s
     *            the s
     * @return char
     */
    public static char getNumber(char s) {
        if (s >= '0' && s <= '9') {
            return s;
        }

        for (char[] d : DIGS) {
            for (int i = 0; i < d.length; i++) {
                if (s == d[i]) {
                    return (char) ('0' + i);
                }
            }
        }
        return s;
    }

    /**
     * safely parse the object to double, if failed return 0.
     *
     * @param v
     *            the object
     * @return the double result
     */
    public static double toDouble(Object v) {
        return toDouble(v, 0);
    }

    /**
     * safely parse the object to double, if failed return default value.
     *
     * @param v
     *            the object
     * @param defaultValue
     *            the default value when the v is null or parse error
     * @return the double
     */
    public static double toDouble(Object v, double defaultValue) {
        if (v != null) {
            if (v instanceof Number) {
                return ((Number) v).doubleValue();
            }

            String s = v.toString().replaceAll(",", "").trim();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                if (c >= '0' && c <= '9') {
                    sb.append(c);
                } else if (c == '-' && sb.length() == 0) {
                    sb.append(c);
                } else if (c == '.') {
                    if (sb.indexOf(".") > -1) {
                        break;
                    } else {
                        sb.append(c);
                    }
                } else if (sb.length() > 0) {
                    break;
                }
            }
            s = sb.toString();

            try {
                return Double.parseDouble(s);
            } catch (Exception e) {
                log.error(e);

            }
        }
        return defaultValue;
    }
}

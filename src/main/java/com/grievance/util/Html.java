package com.grievance.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/** Small helpers used by the JSP pages. */
public final class Html {
    private Html() { }

    /** Makes user-entered text safe to print inside a page. */
    public static String esc(Object value) {
        if (value == null) return "";
        String s = String.valueOf(value);
        StringBuilder out = new StringBuilder(s.length() + 16);
        for (char c : s.toCharArray()) {
            switch (c) {
                case '&' -> out.append("&amp;");
                case '<' -> out.append("&lt;");
                case '>' -> out.append("&gt;");
                case '"' -> out.append("&quot;");
                case '\'' -> out.append("&#39;");
                default -> out.append(c);
            }
        }
        return out.toString();
    }

    /** IN_PROGRESS -> in-progress (used as a CSS class suffix). */
    public static String statusClass(String status) {
        return status == null ? "" : status.toLowerCase().replace('_', '-');
    }

    /** IN_PROGRESS -> In Progress. */
    public static String statusLabel(String status) {
        if (status == null || status.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (String p : status.toLowerCase().split("_")) {
            if (p.isEmpty()) continue;
            if (sb.length() > 0) sb.append(' ');
            sb.append(Character.toUpperCase(p.charAt(0))).append(p.substring(1));
        }
        return sb.toString();
    }

    /** 01 Nov 2026, 04:07 AM */
    public static String date(Date d) {
        return d == null ? "" : new SimpleDateFormat("dd MMM yyyy, hh:mm a").format(d);
    }

    /** November 1, Sunday */
    public static String today() {
        return new SimpleDateFormat("MMMM d, EEEE").format(new Date());
    }
}

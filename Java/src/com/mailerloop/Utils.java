package com.mailerloop;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mailerloop.Recipient.Attachment;

public class Utils {
    private final static int maxRecursionDepth = 32;

    public static void buildQuery(final String name, final String value,
	    final OutputStream output) throws IOException {
	buildQuery(name, value, false, output);
    }

    public static void buildQuery(final String name, final Object value,
	    boolean lastParam, final OutputStream output) throws IOException {
	if (value == null) {
	    return;
	}
	try {
	    output.write(URLEncoder.encode(name, "UTF-8").getBytes());
	    output.write("=".getBytes());
	    if (value instanceof byte[]) {
		encode((byte[]) value, output);
	    } else {
		encode(value.toString().getBytes(), output);
	    }

	    if (!lastParam) {
		output.write("&".getBytes());
	    }
	} catch (UnsupportedEncodingException e) {
	    throw new IllegalStateException("Unable to encode value");
	}
    }

    public static void buildQuery(final String name, Object value,
	    final OutputStream output) throws IOException {
	if (value instanceof Attachment) {
	    buildQuery(name, (Attachment) value, output);
	} else {
	    buildQuery(name, value, false, output);
	}
    }

    public static void buildQuery(final String name,
	    final Map<String, Object> map, final OutputStream output)
	    throws IOException {
	buildQuery(name, map, 0, output);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void buildQuery(final String name,
	    final Map<String, Object> map, final int depth,
	    final OutputStream output) throws IOException {
	if (depth > maxRecursionDepth) {
	    throw new IllegalStateException(
		    "Recursion is too deep to build query");
	}
	for (Map.Entry<String, Object> entry : map.entrySet()) {
	    if (entry.getValue() instanceof Map) {
		buildQuery(name + "[" + entry.getKey(), (Map) entry.getValue(),
			depth + 1, output);
	    } else if (entry.getValue() instanceof Attachment) {
		buildQuery(name + "[" + entry.getKey() + "]",
			(Attachment) entry.getValue(), output);
	    } else if (entry.getValue() instanceof List) {
		buildQuery(name + "[" + entry.getKey() + "]",
			(List) entry.getValue(), output);
	    } else {
		buildQuery(name + "[" + entry.getKey() + "]", entry.getValue(),
			output);
	    }
	}
    }

    protected static void buildQuery(final String name, Attachment attach,
	    final OutputStream output) throws IOException {
	Map<String, Object> map = new HashMap<String, Object>();
	map.put("filename", attach.filename);
	if (attach.content != null) {
	    map.put("content", attach.content);
	} else if (attach.bContent != null) {
	    map.put("content", attach.bContent);
	}
	buildQuery(name, map, output);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected static void buildQuery(final String name, final List<?> list,
	    final OutputStream output) throws IOException {
	int i = 0;
	for (Object entry : list) {
	    if (entry instanceof Map) {
		buildQuery(name + "[" + i + "]", (Map) entry, output);
	    } else if (entry instanceof Attachment) {
		buildQuery(name + "[" + i + "]", (Attachment) entry, output);
	    } else if (entry instanceof List) {
		buildQuery(name + "[" + i + "]", (List) entry, output);
	    } else {
		buildQuery(name + "[" + i + "]", entry, output);
	    }
	    i++;
	}
    }

    protected static void encode(final byte[] value, final OutputStream out)
	    throws IOException {
	for (final byte b : value) {
	    if ((b >= 'a' && b <= 'z') || (b >= 'A' && b <= 'Z')
		    || (b >= '0' && b <= '9') || b == '.' || b == '-'
		    || b == '_') {
		out.write(b);
	    } else if (b == ' ') {
		out.write('+');
	    } else {
		final byte[] hex = Integer.toHexString(b < 0 ? (256 + b) : b)
			.toUpperCase().getBytes();
		out.write('%');
		if (hex.length == 1) {
		    out.write('0');
		}
		out.write(hex);
	    }
	}
    }
}

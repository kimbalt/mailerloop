package com.mailerloop;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Sender {
    private final String apiKey;
    private final String apiEndpoint;
    private final static String mailServerUri = "http://api.mailerloop.com/mailerserver/send";
    private final static String format = "format";
    private final static String fromName = "fromName";
    private final static String fromEmail = "fromEmail";
    private final static String language = "language";
    private final static String apiKeyStr = "apiKey";
    private final static String templateId = "templateId";
    private final static String recipientName = "recipientName";
    private final static String recipientEmail = "recipientEmail";
    private final static String name = "name";
    private final static String email = "email";
    private final static String variables = "variables";
    private final static String attachments = "attachments";
    private final static String batch = "batch";
    private int connectTimeout = 5000;
    private int readTimeout = 5000;

    public Sender(final String apiKey) {
	this.apiKey = apiKey;
	this.apiEndpoint = mailServerUri;
    }

    public Sender(final String apiKey, final String apiEndpoint) {
	this.apiKey = apiKey;
	this.apiEndpoint = apiEndpoint;
    }

    public void setReadTimeout(final int timeoutMs) {
	readTimeout = timeoutMs;
    }

    public void setConnectTimeout(final int timeoutMs) {
	connectTimeout = timeoutMs;
    }

    public boolean send(final Email email) throws IOException,
	    com.mailerloop.Exception {
	return send(email, true);
    }

    public void send(final Email... emails) throws IOException,
	    com.mailerloop.Exception {
	if (emails != null) {
	    for (final Email email : emails) {
		send(email);
	    }
	}
    }

    public boolean send(final Email email, final boolean throwException)
	    throws IOException, com.mailerloop.Exception {
	final URL serverUrl;
	DataOutputStream out = null;
	DataInputStream in = null;
	HttpURLConnection conn = null;
	try {
	    serverUrl = new URL(apiEndpoint);
	} catch (MalformedURLException e) {
	    throw new IllegalArgumentException(e.getLocalizedMessage());
	}
	try {
	    if (email.recipients.size()==0) {
		throw new IllegalStateException("No recipients in email!");
	    }
	    conn = (HttpURLConnection) serverUrl.openConnection();
	    conn.setConnectTimeout(connectTimeout);
	    conn.setReadTimeout(readTimeout);
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type",
		    "application/x-www-form-urlencoded");
	    conn.setDoOutput(true);
	    conn.setDoInput(true);
	    out = new DataOutputStream(conn.getOutputStream());

	    Utils.buildQuery(format, "plaintext", out);
	    Utils.buildQuery(fromName, email.from.name, out);
	    Utils.buildQuery(fromEmail, email.from.email, out);
	    Utils.buildQuery(templateId, email.templateId, out);
	    Utils.buildQuery(language, email.language, out);

	    String prefix = "";
	    final boolean batchMode = email.recipients.size() > 1;
	    int i = 0;
	    for (Recipient rec : email.recipients) {
		if (batchMode) {
		    prefix = batch + "[" + i + "]";
		    i++;
		    Utils.buildQuery(prefix + "[" + name + "]", rec.to.name,
			    out);
		    Utils.buildQuery(prefix + "[" + Sender.email + "]",
			    rec.to.email, out);
		} else {
		    Utils.buildQuery(prefix + recipientName,
			    rec.to.name, out);
		    Utils.buildQuery(prefix + recipientEmail,
			    rec.to.email, out);
		}
		Utils.buildQuery(prefix + "[" + variables + "]", rec.variables,
			out);
		Utils.buildQuery(prefix + "[" + attachments + "]",
			rec.attachments, out);
	    }
	    Utils.buildQuery(apiKeyStr, apiKey, true, out);
	    out.flush();

	    final byte[] buffer = new byte[256];
	    in = new DataInputStream(conn.getInputStream());
	    StringBuffer output = new StringBuffer();
	    int readBytes = 0;
	    while ((readBytes = in.read(buffer)) != -1) {
		output.append(new String(buffer, 0, readBytes));
	    }
	    if (output.toString().compareToIgnoreCase("ok") != 0) {
		if (throwException) {
		    throw new com.mailerloop.Exception(output.toString());
		} else {
		    return false;
		}
	    } else {
		return true;
	    }
	} finally {
	    if (out != null) {
		try {
		    out.close();
		} catch (Throwable e) {

		}
	    }
	    if (in != null) {
		try {
		    in.close();
		} catch (Throwable e) {

		}
	    }
	    if (conn != null) {
		try {
		    conn.disconnect();
		} catch (Throwable e) {

		}
	    }
	}
    }
}

package com.mailerloop;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.mailerloop.Email.EmailAddress;

public class Recipient {
    protected final EmailAddress to;
    protected final List<Attachment> attachments = new LinkedList<Attachment>();
    protected final Map<String, Object> variables = new HashMap<String, Object>();
    private final Email email;

    protected Recipient(final String email, final String name,
	    final Email emailObj) {
	if (email == null) {
	    throw new IllegalArgumentException("toEmail must not be null!");
	}
	to = new EmailAddress(email, name);
	this.email = emailObj;
    }

    protected static class Attachment {
	public final String filename;
	public final String content;
	public final byte[] bContent;
	private Attachment(final String filename, final String content) {
	    if (filename == null) {
		throw new IllegalArgumentException("filename must not be null!");
	    }
	    if (content == null) {
		throw new IllegalArgumentException("content must not be null!");
	    }
	    this.filename = filename;
	    this.content = content;
	    this.bContent = null;
	}
	private Attachment(final String filename, final byte[] bContent) {
	    if (filename == null) {
		throw new IllegalArgumentException("filename must not be null!");
	    }
	    if (bContent == null) {
		throw new IllegalArgumentException("bContent must not be null!");
	    }
	    this.filename = filename;
	    this.content = null;
	    this.bContent = bContent;
	}
    }

    public Recipient addAttachment(final String filename, final String content) {
	attachments.add(new Attachment(filename, content));
	return this;
    }

    public Recipient addAttachment(final String filename, final byte[] bContent) {
	attachments.add(new Attachment(filename, bContent));
	return this;
    }
    
    public Recipient addVariable(final String name, final Object value) {
	variables.put(name, value);
	return this;
    }

    public Recipient addVariables(final Map<String, ?> variables) {
	this.variables.putAll(variables);
	return this;
    }

    public Recipient addRecipient(final String email, final String name) {
	return this.email.addRecipient(email, name);
    }
}
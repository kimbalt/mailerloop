package com.mailerloop;

import java.util.LinkedList;
import java.util.List;

public class Email {
    protected final EmailAddress from;
    protected final int templateId;
    protected final String language;

    protected final List<Recipient> recipients = new LinkedList<Recipient>();

    public Email(final String fromEmail, final String fromName,
	    final int templateId, final String language) {
	if (templateId < 0) {
	    throw new IllegalArgumentException("templateId must not be negative!");
	}
	this.from = new EmailAddress(fromEmail, fromName);
	this.templateId = templateId;
	this.language = language;
    }
    
    public Email(final int templateId, final String language) {
	this(null, null, templateId, language);
    }

    public Email(final int templateId) {
	this(null, null, templateId, null);
    }
    
    protected static class EmailAddress {
	public final String name;
	public final String email;
	public EmailAddress(final String email, final String name) {
	    this.name = name;
	    this.email = email;
	}
    }

    public Recipient addRecipient(final String email, final String name) {
	final Recipient recipient = new Recipient(email, name, this);
	recipients.add(recipient);
	return recipient;
    }
    
    public Recipient addRecipient(final String email) {
	return addRecipient(email, null);
    }
}

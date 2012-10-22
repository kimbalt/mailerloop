package com.mailerloop;

import java.io.IOException;


public class Example {
    public static void main(String[] argv) throws IOException, Exception {
	com.mailerloop.Sender sender = new com.mailerloop.Sender("some_api_key");
	java.util.Map<String, String> variables = new java.util.HashMap<String, String>();
	variables.put("url", "");
	variables.put("dietLink", "");
	com.mailerloop.Email email = new com.mailerloop.Email(290, "LT");
	email.addRecipient("pagalba@geradieta.lt", "geradieta").addVariables(variables);
	sender.send(email);
    }
}

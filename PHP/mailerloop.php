<?php

class MailerLoop {

    const MAILER_SERVER_URI = 'https://api.mailerloop.com/api/v1/messages/';

    const TEST_SERVER_URI = 'https://test.mailerloop.com/api/v1/messages/';

    private $apiKey;

    private $fromName;

	private $fromEmail;

	private $variables = array();

	private $mailId;

	private $recipientEmail;

	private $recipientName;

	private $type;

	private $language;

	private $batchRecipients;

	private $attachments;

	private $replyToEmail;

	private $replyToName;

    private $isTest = 0;

	public function __construct( $apiKey ) {

		$this->setApiKey( $apiKey );

	}

    public function setTest() {

        $this->isTest = 1;

        return $this;
    }

	public function setRecipient( $email, $name = '' ) {

	   $this->recipientEmail = $email;
           $this->recipientName = $name;

	   return $this;
	}

	public function setApiKey( $apiKey ) {

	   $this->apiKey = $apiKey;

	   return $this;
	}

	public function setFromName( $name ) {

	   $this->fromName = $name;

	   return $this;
	}

	public function setFromEmail( $email ) {

	   $this->fromEmail = $email;

	   return $this;

	}

	public function setVariables( $variables ) {

	   if ( is_array( $variables ) ) {
	       foreach ( $variables as $name => $value ) {
	           $this->setVariable( $name, $value );
	       }
	   }

	   return $this;
	}

	public function setVariable( $name, $value ) {

	   $this->variables[ $name ] = $value;

	   return $this;
	}

	public function setMail( $id ) {

	   $this->mailId = $id;

	   return $this;
	}

    public function setType( $type ) {

        $this->type = $type;

        return $this;

    }

	public function setLanguage( $code ) {

		$this->language = $code;

		return $this;
	}

	public function addRecipient( $recipient ) {

		if ( !isset( $recipient['email'] ) ) {
			$recipient['email'] = '';
		}

		if ( !isset( $recipient['variables'] ) ) {
			$recipient['variables'] = '';
		}

    	$this->batchRecipients[] = $recipient;

    	return $this;
	}

	public function addRecipients( $recipients ) {

    	foreach ( $recipients as $recipient ) {
        	$this->addRecipient( $recipient );
    	}

    	return $this;
	}

	public function setReplyTo( $email, $name = '' ) {

		$this->replyToEmail = $email;
		$this->replyToName = $name;

		return $this;
	}

	public function addAttachment( $filename, $content ) {

		$this->attachments[] = array('filename' => $filename, 'content' => $content );

		return $this;
	}

	public function send() {

        $data = array(
            'fromName' => $this->fromName,
            'fromEmail' => $this->fromEmail,
            'apiKey' => $this->apiKey,
            'type' => $this->type,
            'mailId' => $this->mailId,
            'replyToEmail' => $this->replyToEmail,
            'replyToName' => $this->replyToName,
            'language' => $this->language,
        );

        if ( !empty( $this->batchRecipients ) ) {

            $data['batch'] = $this->batchRecipients;

        } else {

            $data['recipientName'] = $this->recipientName;
            $data['recipientEmail'] = $this->recipientEmail;
            $data['variables'] = $this->variables;
			$data['attachments'] = $this->attachments;

        }

        $url = ( $this->isTest ) ? self::TEST_SERVER_URI : self::MAILER_SERVER_URI;

		$ch = curl_init( $url );

		curl_setopt($ch, CURLOPT_POST, true);
		curl_setopt($ch, CURLOPT_POSTFIELDS, http_build_query( $data ) );
		curl_setopt($ch, CURLOPT_HEADER, false);
		curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, false );
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false );
        curl_setopt($ch, CURLOPT_FOLLOWLOCATION, true );

		$res = curl_exec($ch);

		curl_close($ch);

		return json_decode($res, true);
	}
}
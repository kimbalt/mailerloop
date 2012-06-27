<?

class MailerLoop {
		
	const MAILER_SERVER_URI = 'http://api.mailerloop.com/mailerserver/send';
	
	private $apiKey;
	
	private $fromName;
	
	private $fromEmail;
	
	private $variables = array();
	
	private $templateId;
	
	private $recipientEmail;
	
	private $recipientName;
	
	private $type;
	
	private $language;
	
	public function __construct( $apiKey ) {
	
		$this->setApiKey( $apiKey );
	
	}
	
	public function setRecipient( $email, $name = null ) {
	   
	   $this->recipientEmail = $email;
	   
	   if ( !empty( $name ) ) {
	       $this->recipientName = $name;
	   }
	   
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
	
	public function setTemplate( $id ) {
	   
	   $this->templateId = $id;
	   
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
	
	public function send() {

        $data = array(
            'fromName' => $this->fromName,
            'fromEmail' => $this->fromEmail,
            'recipientName' => $this->recipientName,
            'recipientEmail' => $this->recipientEmail,
            'apiKey' => $this->apiKey,
            'type' => $this->type,
            'variables' => $this->variables,
            'templateId' => $this->templateId,
			'language' => $this->language
        );

		$ch = curl_init( self::MAILER_SERVER_URI );
	
		curl_setopt($ch, CURLOPT_POST, true);
		curl_setopt($ch, CURLOPT_POSTFIELDS, http_build_query( $data ) );
		curl_setopt($ch, CURLOPT_HEADER, false);
		curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

		$res = curl_exec($ch);

		curl_close($ch);

		return json_decode($res, true);
	}
}
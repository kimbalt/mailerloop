<?php

	require_once('mailerloop.php');
	
	
	
	$mailer = new MailerLoop();
	
	$mailer->setApiKey( 'e15ab2614576d5f67e5a478cdeb76834' );
	
	$variables = array(
	   'discount_code' => 'A65CEF',
	   'discount_amount' => '20'
	);
	
	$result = $mailer->setVariables( $variables )
                     ->setRecipient( 'example@example.com' )
                     ->setTemplate( 37 )
                      ->send();

	var_dump( $result );


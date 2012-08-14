# MailerLoop API

Official PHP wrapper for MailerLoop API

## Methods

### setRecipient( $email [, $name = null ] )

'name' parameter is optional.

### setApiKey( $apiKey )
### setVariables( $variables )
### setVariable( $name, $value )
### setTemplate( $id )
### setLanguage( $code )
### addRecipient( $email, $variables [, $attachments = array() ] )
### addRecipients( $recipients )

$recipients must be an array of arrays. each of recipient must have fields 'email' and 'variables'. 
Optionally each recipient can have 'attachments' field. Each attachment mush have 'filename' and 'content' fields.

### addAttachment( $filename, $content )
### send()

## Unused methods:
### setFromName( $name )
### setFromEmail( $email )
### setType( $type )
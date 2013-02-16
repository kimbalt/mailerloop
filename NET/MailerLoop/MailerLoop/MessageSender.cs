using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Net.Mail;

using RestSharp;
using System.Net;

namespace MailerLoop
{

    public class MessageSender
    {
        private string BaseUrl = "https://app.mailerloop.com/api/";
        
        private string ApiKey;

        private string ApiVersion;

        private string ResponseFormat { get; set; }

        public MessageSender(String ApiKey, String ApiVersion )
        {
            this.ApiKey = ApiKey;
            this.ApiVersion = ApiVersion;
            this.ResponseFormat = "xml";
        }

        public string send( EmailMessage Message)
        {

            // validate cert by calling a function
            //ServicePointManager.ServerCertificateValidationCallback = delegate { return true; };

            //  public static RestResponse SendSimpleMessage() {
            RestClient client = new RestClient( this.BaseUrl );
            RestRequest request = new RestRequest();

            request.Resource = "{version}/{action}.{responseFormat}";           
            request.AddUrlSegment("version", this.ApiVersion );
            request.AddUrlSegment("action", "messages" );
            request.AddUrlSegment("responseFormat", this.ResponseFormat);
            request.Method = Method.POST;
            request.AddParameter("apiKey", this.ApiKey );            
            
            request.AddParameter("mailId", Message.MailId );

            if (Message.Recipient == null)
            {
                throw new Exception("Invalid message recipient");
            }

            request.AddParameter("recipientEmail", Message.Recipient.Address );
            request.AddParameter("recipientName", Message.Recipient.DisplayName );

            if (Message.ReplyTo != null)
            {
                request.AddParameter("replyToEmail", Message.ReplyTo.Address);
                request.AddParameter("replyToName", Message.ReplyTo.DisplayName );
            }

            if (Message.Sender != null)
            {
                request.AddParameter("fromEmail", Message.Sender.Address);
                request.AddParameter("fromName", Message.Sender.DisplayName);
            }

            if (Message.Variables != null)
            {
                foreach (var variable in Message.Variables)
                {
                    request.AddParameter("variables[" + variable.Key + "]", variable.Value);
                }
            }

            if (Message.Attachments != null)
            {
                foreach (var attachment in Message.Attachments)
                {
                    request.AddFile(attachment.Key, attachment.Value);
                }
            }

            if (Message.Repeaters != null)
            {
                foreach (var repeater in Message.Repeaters)
                {
                    int i = 0;
                    foreach (Dictionary<string, string> repeaterItem in repeater.Value)
                    {
                        foreach (var repeaterItemValue in repeaterItem)
                        {
                            request.AddParameter("variables[" + repeater.Key + "][" + i + "][" + repeaterItemValue.Key + "]", repeaterItemValue.Value );
                        }                        
                        i++;
                    }
                }
            }

            IRestResponse response = client.Execute(request);

            if (response.ErrorException != null)
            {
                throw response.ErrorException;
            }

            return response.Content;
            /*
            string Text = "";
            Text += "Error: " + response.ErrorMessage + "\r\n";
            Text += "Status Code: " + response.StatusCode + "\r\n";
            Text += "Response status: " + response.ResponseStatus + "\r\n";
            Text += "Content: " + response.Content + "\r\n";

            return Text;
            */
        }

    }

}

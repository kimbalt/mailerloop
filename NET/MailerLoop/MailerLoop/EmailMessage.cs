using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Collections;

using System.Net.Mail;

namespace MailerLoop
{
    public class EmailMessage
    {

        public EmailMessage(int MailId, String Language)
        {
            this.MailId = MailId;
            this.Language = Language;
        }

        public EmailMessage(int MailId)
        {
            this.MailId = MailId;
        }

        public String Language { get; set; }

        public MailAddress Sender { get; set; }

        public MailAddress Recipient { get; set; }

        public MailAddress ReplyTo { get; set; }

        public int MailId { get; set; }

        public Dictionary<string, object> Variables { get; set; }

        public Dictionary<string, string> Attachments { get; set; }

        public Dictionary<string, ArrayList> Repeaters { get; set; }

        public void AddAttachment(String Name, String Path)
        {
            if (this.Attachments == null)
            {
                this.Attachments = new Dictionary<string,string>();
            }

            this.Attachments.Add(Name, Path);
        }

        public void AddVariable(String Name, String Value)
        {
            if (this.Variables == null)
            {
                this.Variables = new Dictionary<string, object>();
            }

            this.Variables.Add(Name, Value);
        }

        public void AddRepeaterItem(String RepeaterName, Dictionary<string, string> RepeaterData)
        {

            if ( this.Repeaters == null ) {
                this.Repeaters = new Dictionary<string,ArrayList>();
            }

            if ( !this.Repeaters.ContainsKey( RepeaterName ) ) {         
                this.Repeaters.Add( RepeaterName, new ArrayList() );
            }

            this.Repeaters[ RepeaterName ].Add( RepeaterData );


        }


    }

}

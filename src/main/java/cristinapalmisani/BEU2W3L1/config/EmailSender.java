package cristinapalmisani.BEU2W3L1.config;

import cristinapalmisani.BEU2W3L1.entities.User;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EmailSender {
    private String mailgunApiKey;
    private String mailgunDomainname;

    public EmailSender(@Value("${mailgun.apikey}") String mailgunApiKey,
                         @Value("${mailgun.domainname}") String mailgunDomainName) {
        this.mailgunApiKey = mailgunApiKey;
        this.mailgunDomainname = mailgunDomainName;
    }

    public void sendRegistrationEmail(User recipient) {

        HttpResponse<JsonNode> response = Unirest.post("https://api.mailgun.net/v3/" + this.mailgunDomainname + "/messages")
                .basicAuth("api", this.mailgunApiKey)
                .queryString("from", "Cristina Palmisani <cristina.palmisani@outlook.it>")
                .queryString("to", recipient.getEmail())
                .queryString("subject", "Registrazione avvenuta con successo!")
                .queryString("text", "Complimenti " + recipient.getName() + " " + recipient.getSurname() + "  per esserti registrato!")
                .asJson();
        System.out.println(response);
    }

}

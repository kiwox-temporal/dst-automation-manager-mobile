package net.kiwox.manager.dst.service.impl;

import net.kiwox.manager.dst.enums.Parameter;
import net.kiwox.manager.dst.service.interfaces.ICodeMessageService;
import net.kiwox.manager.dst.service.interfaces.ICustomMailService;
import net.kiwox.manager.dst.service.interfaces.IMailService;
import net.kiwox.manager.dst.service.interfaces.IParameterService;
import net.kiwox.manager.dst.wrappers.MailHtmlArgs;
import net.kiwox.manager.dst.wrappers.MailSenderArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CustomMailServiceImpl implements ICustomMailService {

    @Autowired
    private IMailService mailService;

    @Autowired
    private IParameterService parameterService;

    @Autowired
    private ICodeMessageService messageService;


    @Value("${smrpa.timeout.mail.title}")
    private String emailTitle;
    @Value("${smrpa.timeout.mail.salute}")
    private String emailSalute;
    @Value("${smrpa.timeout.mail.presignature}")
    private String emailPresignature;
    @Value("${smrpa.timeout.mail.signature}")
    private String emailSignature;
    @Value("${smrpa.timeout.mail.footer}")
    private String emailFooter;

    @Override
    public void run(Map<String, Object> mailParams) {

        try {
            String to = parameterService.getString(Parameter.TEST_TIMEOUT_MAIL_TO);
            String cc = parameterService.getString(Parameter.TEST_TIMEOUT_MAIL_CC);
            String bcc = parameterService.getString(Parameter.TEST_TIMEOUT_MAIL_BCC);


            String mailSubject = (String) mailParams.get("subject");
            String mailMessage = (String) mailParams.get("message");
            String requestId = (String) mailParams.get("requestId");


            MailHtmlArgs textArgs = new MailHtmlArgs();
            textArgs.setTitle(emailTitle);
            textArgs.setSubtitle(mailSubject);
            textArgs.setSalute(emailSalute);
            textArgs.setRequestId(requestId);
            textArgs.setMessage(mailMessage);
            textArgs.setPreSignature(emailPresignature);
            textArgs.setSignature(emailSignature);
            textArgs.setFooter(emailFooter);
            //textArgs.setMessage(messageService.getMessage("prepaid.timeout.mail.message", 300)); // Aquí puse 300 segundos como el límite de tiempo

            MailSenderArgs sendArgs = new MailSenderArgs();
            sendArgs.setTo(mailService.splitAddresses(to));
            sendArgs.setCc(mailService.splitAddresses(cc));
            sendArgs.setBcc(mailService.splitAddresses(bcc));
            sendArgs.setSubject(mailSubject);
            //sendArgs.setSubject(messageService.getMessage("prepaid.timeout.mail.title"));
            sendArgs.setText(mailService.getMailHtml(textArgs));
            boolean b = mailService.send(sendArgs);
            b = !b;
        } catch (Exception e) {
            String er = e.getMessage();
        }

    }
}

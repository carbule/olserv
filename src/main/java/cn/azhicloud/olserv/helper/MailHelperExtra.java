package cn.azhicloud.olserv.helper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/17 22:06
 */
@Slf4j
@Component
public class MailHelperExtra extends cn.azhicloud.infra.base.helper.MailHelper {
    public MailHelperExtra(JavaMailSender mailSender) {
        super(mailSender);
    }

    /**
     * 发送订阅通知邮件
     *
     * @param to      收件人（订阅通知<notify@azhicloud.cn>）
     * @param subject 主题（subject (nanoid)）
     * @param content 内容
     */
    public void sendSubscribeNotice(String to, String subject, String content) {
        log.info("Send mail to [{}] with subject [{}}] and content [{}].", to, subject, content);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("订阅通知" + "<" + mailFrom + ">");
        message.setTo(to);
        message.setSubject(beautifulSubject(subject));
        message.setText(content);
        mailSender.send(message);
    }
}

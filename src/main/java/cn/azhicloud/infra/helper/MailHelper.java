package cn.azhicloud.infra.helper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/17 22:06
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class MailHelper {

    @Value("${spring.mail.from}")
    private String mailFrom;

    @Value("${alarm.mail.receiver}")
    private String alarmMailReceiver;

    private final JavaMailSender mailSender;

    /**
     * 发送普通邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param content 内容
     */
    public void sendSimpleMail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailFrom);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }

    /**
     * 发送预警邮件
     *
     * @param subject 主题
     * @param content 内容
     */
    public void sendAlarmMail(String subject, String content) {
        try {
            sendSimpleMail(alarmMailReceiver, subject, content);
        } catch (Exception e) {
            log.error("send alarm mail failed", e);
        }
    }

}

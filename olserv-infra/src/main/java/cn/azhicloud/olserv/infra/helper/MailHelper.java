package cn.azhicloud.olserv.infra.helper;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
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
        message.setSubject(subject + " (" + NanoIdUtils.randomNanoId() + ")");
        message.setText(content);
        mailSender.send(message);
    }

    /**
     * 发送系统预警邮件
     *
     * @param subject 主题
     * @param content 内容
     */
    public void sendAlarmMail(String subject, String content) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("系统预警" + "<" + mailFrom + ">");
            message.setTo(alarmMailReceiver);
            message.setSubject(subject + " (" + NanoIdUtils.randomNanoId() + ")");
            message.setText(content);
            mailSender.send(message);
        } catch (Exception e) {
            log.error("send alarm mail failed", e);
        }
    }

}

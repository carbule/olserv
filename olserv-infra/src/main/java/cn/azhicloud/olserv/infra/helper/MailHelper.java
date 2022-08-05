package cn.azhicloud.olserv.infra.helper;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
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

    @Value("${spring.mail.generate-subject-no}")
    private Boolean generateSubjectNo;

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
        message.setSubject(beautifulSubject(subject));
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
            message.setSubject(beautifulSubject(subject));
            message.setText(content);
            mailSender.send(message);
        } catch (Exception e) {
            log.error("send alarm mail failed", e);
        }
    }

    /**
     * 包装邮件主题
     * 1.主题编号的生成
     *
     * @param subject 主题
     * @return 包装后的邮件主题
     */
    private String beautifulSubject(String subject) {
        return subject + (BooleanUtils.isTrue(generateSubjectNo) ?
                " (" + NanoIdUtils.randomNanoId() + ")" : "");
    }
}

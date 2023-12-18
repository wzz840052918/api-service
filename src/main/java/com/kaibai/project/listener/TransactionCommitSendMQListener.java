package com.kaibai.project.listener;

import com.kaibai.project.event.TransactionCommitSendMQEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 事务监听器
 *
 * @author kaibai
 * @date 2023-12-18 21:47
 **/
@Component
@Slf4j
public class TransactionCommitSendMQListener {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void send(TransactionCommitSendMQEvent event) {
        String topic = event.getTopic() + ":" + event.getTag();
        rocketMQTemplate.asyncSend(topic, event.getMessage(), new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                if (sendResult.getSendStatus() != SendStatus.SEND_OK) {
                    log.error("Consumer replies message failed. SendStatus: {}", sendResult.getSendStatus());
                } else {
                    log.debug("Consumer replies message success.");
                }
            }

            @Override
            public void onException(Throwable e) {
                log.error("Consumer replies message failed. error: {}", e.getLocalizedMessage());
            }
        });
    }
}

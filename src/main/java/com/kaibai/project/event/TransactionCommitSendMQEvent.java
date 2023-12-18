package com.kaibai.project.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author kaibai
 * @date 2023-12-18 21:48
 **/
public class TransactionCommitSendMQEvent extends ApplicationEvent {

    @Getter
    private final String topic;

    @Getter
    private final String tag;

    @Getter
    private final Object message;

    public TransactionCommitSendMQEvent(Object source, String topic, String tag, Object message) {
        super(source);
        this.topic = topic;
        this.tag = tag;
        this.message = message;
    }
}

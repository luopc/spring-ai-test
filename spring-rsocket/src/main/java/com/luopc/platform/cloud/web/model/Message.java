package com.luopc.platform.cloud.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    private String sender; // 发送者名称
    private String content; // 消息内容
    private Long timestamp;

    private MessageType type;

    public enum MessageType {
        CHAT, JOIN, LEAVE
    }
}

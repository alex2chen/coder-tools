package com.github.common;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.junit.Test;

/**
 * @author alex.chen
 * @Description:
 * @date 2016/6/6
 */
public class EventBus_unit {
    @Test
    public void simply() {
        EventBus eventBus = new EventBus("test");
        MsgEventListener listener = new MsgEventListener();
        eventBus.register(listener);

        eventBus.post(new MsgEvent("你好，你的验证码是1234", MsgEvent.SMS));
        eventBus.post(new MsgEvent("请点击链接验证邮箱", MsgEvent.EMAIL));
        eventBus.post(new MsgEvent());

        eventBus.register(new Object() {
            @Subscribe
            public void lister(Integer number) {
                System.out.println("lister integer:" + number);
            }

            @Subscribe
            public void lister(Number number) {
                System.out.println("lister number:" + number);
            }

            @Subscribe
            public void lister(Long number) {
                System.out.println("lister long:" + number);
            }
        });
        eventBus.post(1);
        eventBus.post(1L);
        System.out.println(eventBus.identifier());
    }

    public static class MsgEvent {
        public static final byte SMS = 1;
        public static final byte EMAIL = 2;
        private String message;
        private byte msgType;

        public MsgEvent() {
        }

        public MsgEvent(String message, byte msgType) {
            this.message = message;
            this.msgType = msgType;
        }

        public byte getMsgType() {
            return msgType;
        }

        public void setMsgType(byte msgType) {
            this.msgType = msgType;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public static class MsgEventListener {
        @Subscribe
        public void listen(MsgEvent event) {
            switch (event.getMsgType()) {
                case MsgEvent.SMS:
                    System.out.println("发送短信:" + event.getMessage());
                    break;
                case MsgEvent.EMAIL:
                    System.out.println("发送email:" + event.getMessage());
                    break;
                default:
                    System.out.println("发现？消息:" + event.getMessage());
                    break;
            }
        }
    }
}

package com.github.novotnyr.rabbitmqadmin.command;

public class GetMessageRequest {
    private int count = 1;

    private AckMode ackMode = AckMode.ACK;

    private Encoding responseEncoding = Encoding.AUTO;

    private long payloadSizeLimit = -1;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public AckMode getAckMode() {
        return ackMode;
    }

    public void setAckMode(AckMode ackMode) {
        this.ackMode = ackMode;
    }

    public Encoding getResponseEncoding() {
        return responseEncoding;
    }

    public void setResponseEncoding(Encoding responseEncoding) {
        this.responseEncoding = responseEncoding;
    }

    public long getPayloadSizeLimit() {
        return payloadSizeLimit;
    }

    public void setPayloadSizeLimit(long payloadSizeLimit) {
        this.payloadSizeLimit = payloadSizeLimit;
    }

    public enum AckMode {
        ACK_REQUEUE("ack_requeue_true"),
        REJECT_REQUEUE("reject_requeue"),
        ACK("ack_requeue_false"),
        REJECT("reject_requeue_false");

        private final String code;

        AckMode(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        @Override
        public String toString() {
            return this.getCode();
        }
    }

    public enum Encoding {
        AUTO("auto"),
        BASE64("base64");

        private final String code;

        Encoding(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        @Override
        public String toString() {
            return getCode();
        }
    }
}

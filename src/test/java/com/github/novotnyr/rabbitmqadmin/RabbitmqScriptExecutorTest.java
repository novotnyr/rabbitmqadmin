package com.github.novotnyr.rabbitmqadmin;

import org.junit.Test;

public class RabbitmqScriptExecutorTest {
    @Test
    public void test() {
        RabbitmqAdmin.main(new String[] { "exec", "-f", "src/test/resources/example.rabbitmq" });
    }

    @Test
    public void testGet() {
        RabbitmqAdmin.main(new String[] { "exec", "-f", "src/test/resources/example.get.rabbitmq" });
    }
}
package com.github.novotnyr.rabbitmqadmin;

import org.junit.Test;

import static org.junit.Assert.*;

public class RabbitmqScriptExecutorTest {
    @Test
    public void test() {
        RabbitmqAdmin.main(new String[] { "exec", "-f", "src/test/resources/example.rabbitmq" });
    }
}
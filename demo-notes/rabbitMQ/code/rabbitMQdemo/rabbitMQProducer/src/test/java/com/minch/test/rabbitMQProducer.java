package com.minch.test;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.sun.corba.se.impl.orbutil.threadpool.TimeoutException;

import java.io.IOException;

public class rabbitMQProducer {
    public static void main(String[] args) throws IOException {

        String QUEUE = "channelName";
        //通过连接工厂创建新的连接
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
        // 设置虚拟机
        factory.setVirtualHost("/");
        // 创建与rabbitMQ的TCP连接
        Connection connection = null;
        Channel channel = null;
        try {
            connection = factory.newConnection();
            // 创建与Exchange的通道，每个连接可以创建对个通道，每个通道代表一个会话任务
            channel = connection.createChannel();
            // 声明队列
            /**
             * 声明队列，如果Rabbit中没有此队列将自动创建
             * param1:队列名称
             * param2:是否持久化
             * param3:队列是否独占此连接
             * param4:队列不再使用时是否自动删除此队列
             * param5:队列参数
             */
            channel.queueDeclare(QUEUE, true, false, false, null);
            String message = "hello! rabbitMQ---producer";
            /**
             * 消息发布方法
             * param1：Exchange的名称，如果没有指定，则使用Default Exchange
             * param2:routingKey,消息的路由Key，是用于Exchange（交换机）将消息转发到指定的消息队列
             * param3:消息包含的属性
             * param4：消息体
             */
            channel.basicPublish("", QUEUE, null, message.getBytes());
            System.out.println("消息发送完毕。。。");

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //关闭连接
            //先关闭通道
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}

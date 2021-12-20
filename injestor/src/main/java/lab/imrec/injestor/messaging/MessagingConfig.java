package lab.imrec.injestor.messaging;

import lab.imrec.injestor.Modes;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

public class MessagingConfig {
    @Value("${messaging.processing_queue}")
    private String processingQueue;
    @Value("${messaging.result_queue}")
    private String resultQueue;

    @Bean
    public Queue processing(){
        return new Queue(this.processingQueue);
    }

    @Bean
    public Queue result(){
        return new Queue(this.resultQueue);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        var rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}

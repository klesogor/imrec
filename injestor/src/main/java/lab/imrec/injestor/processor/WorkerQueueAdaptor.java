package lab.imrec.injestor.processor;

import lab.imrec.injestor.Modes;
import lab.imrec.injestor.images.dto.RecognitionInput;
import lab.imrec.injestor.images.dto.RecognitionResult;
import lab.imrec.injestor.processor.contract.FsReadDriver;
import lab.imrec.injestor.processor.utlis.DhashCalculator;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@Profile(Modes.Worker)
@Service
@RabbitListener(queues = "${messaging.processing_queue}")
public class WorkerQueueAdaptor {

    private RabbitTemplate template;
    private FsReadDriver readDriver;

    @Value("${messaging.result_queue}")
    private String resultQueue;

    private final SingleThreadedWorker worker = new SingleThreadedWorker(new DhashCalculator());

    @Autowired
    public WorkerQueueAdaptor(RabbitTemplate template, FsReadDriver driver) {
        this.template = template;
        this.readDriver = driver;
    }

    @RabbitHandler()
    public void handleImage(RecognitionInput input) throws IOException {
        var start = System.nanoTime();
        this.processImage(input);
        var end = System.nanoTime();

        var diff = (end - start) / 1000000;
        System.out.println("Processed image in " + diff + "ms");
    }

    private void processImage(RecognitionInput input) throws IOException {
        var imageBuffer = this.readDriver.readFile(input.getLocalReference());
        var image = ImageIO.read(new ByteArrayInputStream(imageBuffer));
        var hashes = worker.processImage(image);
        var result = new RecognitionResult(input.getLocalReference(), input.getName(), hashes.get(0), hashes);
        this.template.convertAndSend(resultQueue, result);
    }

}

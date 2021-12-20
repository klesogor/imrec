package lab.imrec.injestor.images.listeners;

import lab.imrec.injestor.Modes;
import lab.imrec.injestor.images.dto.RecognitionResult;
import lab.imrec.injestor.images.entity.Image;
import lab.imrec.injestor.images.entity.ImageFeature;
import lab.imrec.injestor.images.repository.ImageRepository;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;
import java.util.stream.Collectors;

@Profile(Modes.Injestor)
@RabbitListener(queues = "${messaging.result_queue}")
@Service
public class ImageSaver {
    private ImageRepository repository;

    @Autowired
    public ImageSaver(ImageRepository repository) {
        this.repository = repository;
    }

    @RabbitHandler
    @Transactional
    void saveImage(RecognitionResult result){
        var start = System.nanoTime();
        this.processImage(result);
        var end = System.nanoTime();
        var diff = (end - start) / 1000000;
        System.out.println("Saved image in DB in " + diff + "ms");
    }

    private void processImage(RecognitionResult result){
        var imageFeatures = result.getHashes()
                .stream()
                .map(hash -> ImageFeature.builder().hash(hash).build())
                .collect(Collectors.toList());
        var image = Image.builder()
                .name(result.getName())
                .reference(result.getLocalReference())
                .features(imageFeatures)
                .build();
        this.repository.save(image);
    }
}

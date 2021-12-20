package lab.imrec.injestor.images.service;

import lab.imrec.injestor.Modes;
import lab.imrec.injestor.images.dto.ImageData;
import lab.imrec.injestor.images.dto.RecognitionInput;
import lab.imrec.injestor.images.service.contract.FsDriver;
import lab.imrec.injestor.images.service.contract.ImageValidator;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile(Modes.Injestor)
public class ImageUploadService {
    private final ImageValidator validator;
    private final FsDriver fs;
    private final ImageResolver resolver;
    private RabbitTemplate template;

    @Value("${messaging.processing_queue}")
    private String queue;

    public ImageUploadService(ImageValidator validator, FsDriver fs, ImageResolver resolver, RabbitTemplate template) {
        this.validator = validator;
        this.fs = fs;
        this.resolver = resolver;
        this.template = template;
    }

    public void injestImageUpload(ImageData image){
        var rawData = image.match(resolver);
        if(!validator.isValidImage(rawData)){
            throw new RuntimeException("Image check failed, please upload a valid image");
        }
        // Unique local reference
        var localReference = this.fs.saveImage(image.getName(), rawData);
        template.convertAndSend(this.queue, new RecognitionInput(localReference, image.getName()));
    }
}

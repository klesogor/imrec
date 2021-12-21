package lab.imrec.injestor.images.service;

import lab.imrec.injestor.images.dto.ImageLookupResult;
import lab.imrec.injestor.images.repository.ImageRepository;
import lab.imrec.injestor.processor.utlis.DhashCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ImageLookupService {
    private DhashCalculator calc = new DhashCalculator();
    private ImageRepository repository;

    @Autowired
    public ImageLookupService(ImageRepository repository) {
        this.repository = repository;
    }

    public List<ImageLookupResult> searchImages(byte[] image) throws IOException {
        var hash = calc.calculateHash(ImageIO.read(new ByteArrayInputStream(image)));
        return this.repository.searchForImage(hash);
    }
}

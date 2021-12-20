package lab.imrec.injestor.processor;

import lab.imrec.injestor.processor.contract.Hasher;
import lab.imrec.injestor.processor.utlis.ImageRotator;
import lombok.AllArgsConstructor;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

@AllArgsConstructor
public class SingleThreadedWorker {
    private Hasher hasher;
    public ArrayList<Long> processImage(BufferedImage image) {
        try{
            var hashes = generateRotatedHashes(image);
            return hashes;
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    private ArrayList<Long> generateRotatedHashes(BufferedImage original) {
        var rotationDegree = 45;
        var hashCount = 8;
        var result = new ArrayList<Long>(hashCount);
        for(int i = 0; i < hashCount; i++){
            result.add(this.hasher.calculateHash(ImageRotator.rotateImage(original, rotationDegree * i)));
        }

        return result;
    }
}

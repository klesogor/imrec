package it.imrec.worker;

import it.imrec.core.data.ImageFeatures;
import it.imrec.worker.contract.Hasher;
import it.imrec.ingestor.contract.Worker;
import it.imrec.worker.utlis.ImageRotator;
import lombok.AllArgsConstructor;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

@AllArgsConstructor
public class SingleThrededWorker implements Worker {
    private Hasher hasher;

    @Override
    public ImageFeatures processImage(BufferedImage image) {
        try{
            var hashes = generateRotatedHashes(image);
            var primaryHash = hashes.get(0);

            return new ImageFeatures(primaryHash, hashes);
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    private ArrayList<Long> generateRotatedHashes(BufferedImage original) throws ExecutionException, InterruptedException {
        var rotationDegree = 45;
        var hashCount = 8;
        var result = new ArrayList<Long>(hashCount);
        for(int i = 0; i < hashCount; i++){
            result.add(this.hasher.calculateHash(ImageRotator.rotateImage(original, rotationDegree * i)));
        }

        return result;
    }
}

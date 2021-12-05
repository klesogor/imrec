package it.imrec.worker;

import it.imrec.core.data.ImageFeatures;
import it.imrec.worker.contract.Hasher;
import it.imrec.ingestor.contract.Worker;
import it.imrec.worker.utlis.ImageRotator;
import lombok.AllArgsConstructor;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@AllArgsConstructor
public class MultiThrededWorker implements Worker {
    private ExecutorService executorService;
    private Hasher hasher;

    @AllArgsConstructor
    private static class RotationTask implements Callable<Long> {
        private final double rotationDegree;
        private final Hasher hasher;
        private final BufferedImage image;
        @Override
        public Long call() throws Exception {
            return hasher.calculateHash(ImageRotator.rotateImage(image, rotationDegree));
        }
    }

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
        var tasks = new ArrayList<Future<Long>>(hashCount);
        for(int i = 0; i < hashCount; i++){
            tasks.add(executorService.submit(new RotationTask(i * rotationDegree, this.hasher, original)));
        }
        var result = new ArrayList<Long>(tasks.size());
        for(var task : tasks){
            result.add(task.get());
        }

        return result;
    }
}

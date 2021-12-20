package it.imrec.ingestor;

import it.imrec.core.data.ImageFeatures;
import it.imrec.ingestor.contract.WorkerFactory;
import lombok.AllArgsConstructor;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@AllArgsConstructor
public class LocalBufferedImageIngestor {
    private final ExecutorService pool;
    private final WorkerFactory factory;

    public Future<ImageFeatures> ingestImageAsync(BufferedImage image){
        return pool.submit(() -> factory.makeWorker().processImage(image));
    }

    public ArrayList<ImageFeatures> bulkIngestImages(Collection<BufferedImage> images) {
        try {
            var tasks = new ArrayList<Future<ImageFeatures>>(images.size());
            for (var image : images) {
                tasks.add(this.ingestImageAsync(image));
            }
            var res = new ArrayList<ImageFeatures>(images.size());
            for (var task : tasks) {
                res.add(task.get());
            }
            return res;
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }
}

package it.imrec.core;

import it.imrec.ingestor.LocalBufferedImageIngestor;
import it.imrec.worker.MultiThrededWorker;
import it.imrec.worker.SingleThrededWorker;
import it.imrec.worker.contract.Hasher;
import it.imrec.worker.utlis.DhashCalculator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.Executors;

public class PerformanceLocalIngestTest {
    private static ArrayList<BufferedImage> testData = new ArrayList<>();
    private static Hasher hasher = new DhashCalculator();

    @BeforeAll
    public static void loadTestImages() throws IOException {
        var basePath = System.getProperty("user.dir");
        System.out.println(basePath);
        var testDataPath = "TestData/images";
        var dir = Paths.get(basePath, testDataPath).toFile();
        //throw new RuntimeException(dir.getAbsolutePath());
        for(var file : dir.listFiles()) {
            //System.out.println(file);
            testData.add(ImageIO.read(file));
        }
    }

    @Test
    public void testPerformanceSingleThreadOneWorker(){
        var ingestor = new LocalBufferedImageIngestor(
                Executors.newFixedThreadPool(1),
                () -> new SingleThrededWorker(hasher)
        );
        testInjestorPerformance("Single thread, single worker", ingestor);
    }

    @Test
    public void testPerformanceSingleThreadEightWorkers(){
        var ingestor = new LocalBufferedImageIngestor(
                Executors.newFixedThreadPool(8),
                () -> new SingleThrededWorker(hasher)
        );
        testInjestorPerformance("Single thread, 8 workers", ingestor);
    }

    @Test
    public void testPerformanceSingleThreadSixteenWorkers(){
        var ingestor = new LocalBufferedImageIngestor(
                Executors.newFixedThreadPool(16),
                () -> new SingleThrededWorker(hasher)
        );
        testInjestorPerformance("Single thread, 16 workers", ingestor);
    }

    @Test
    public void testPerformanceMultiThreadOneWorker(){
        var ingestor = new LocalBufferedImageIngestor(
                Executors.newFixedThreadPool(1),
                () -> new MultiThrededWorker(Executors.newFixedThreadPool(4), hasher)
        );
        testInjestorPerformance("Multi thread(4), one worker", ingestor);
    }

    @Test
    public void testPerformanceMultiThreadWithOneThreadAndOneWorker(){
        var ingestor = new LocalBufferedImageIngestor(
                Executors.newFixedThreadPool(1),
                () -> new MultiThrededWorker(Executors.newFixedThreadPool(1), hasher)
        );
        testInjestorPerformance("Multi thread(1), one worker", ingestor);
    }

    @Test
    public void testPerformanceMultiThreadEightWorkers(){
        var ingestor = new LocalBufferedImageIngestor(
                Executors.newFixedThreadPool(8),
                () -> new MultiThrededWorker(Executors.newFixedThreadPool(4), hasher)
        );
        testInjestorPerformance("Multi thread(4), eight workers", ingestor);
    }

    @Test
    public void testPerformanceMultiTwoThreadFourWorkers(){
        var ingestor = new LocalBufferedImageIngestor(
                Executors.newFixedThreadPool(4),
                () -> new MultiThrededWorker(Executors.newFixedThreadPool(2), hasher)
        );
        testInjestorPerformance("Multi thread(2), four workers", ingestor);
    }
    @Test
    public void testPerformanceMultiFourThreadTwoWorkers(){
        var ingestor = new LocalBufferedImageIngestor(
                Executors.newFixedThreadPool(2),
                () -> new MultiThrededWorker(Executors.newFixedThreadPool(4), hasher)
        );
        testInjestorPerformance("Multi thread(4), two workers", ingestor);
    }


    private void testInjestorPerformance(String title, LocalBufferedImageIngestor ingestor){
        // Warm up JVM
        ingestor.bulkIngestImages(testData);

        long[] timing = new long[10];
        for(int i = 0; i < 9; i++){
            var now = System.nanoTime();
            ingestor.bulkIngestImages(testData);
            var after = System.nanoTime();
            timing[i] = (after - now) / 1000;
        }
        long best = Long.MAX_VALUE - 1, worst = 0, sum = 0;
        for(var t : timing){
            best = Math.min(best, t);
            worst = Math.max(worst, t);
            sum += t;
        }
        System.out.println(
                String.format(
                        "Test for %s:\n Best: %d ms;\n Worst: %d ms;\n Average: %d ms\n-----------------",
                        title,
                        best,
                        worst,
                        sum / timing.length
                )
        );
    }
}

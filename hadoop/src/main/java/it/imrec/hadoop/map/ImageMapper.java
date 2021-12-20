package it.imrec.hadoop.map;

import it.imrec.hadoop.model.LongArrayWritable;
import it.imrec.hadoop.util.DhashCalculator;
import it.imrec.hadoop.util.ImageRotator;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

@Slf4j
public class ImageMapper implements Mapper<Text, BytesWritable, Text, LongArrayWritable> {

  private final DhashCalculator calculator = new DhashCalculator();

  @Override
  public void map(Text key, BytesWritable file, OutputCollector<Text, LongArrayWritable> outputCollector,
      Reporter reporter) throws IOException {
    log.info("Got key [{}] and value size [{}]", key, file.getLength());
    final byte[] imageBytes = file.getBytes();
    final BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
    log.info("Image height: {}, width: {}", image.getHeight(), image.getWidth());
    try {
      final LongArrayWritable value = generateRotatedHashes(image);
      log.info("GGGG Sending to collect key:{}, value: {}", key, value);
      outputCollector.collect(key, value);
    } catch (Exception e) {
      System.err.println("Mapping error: " + e.getMessage());
    }
  }

  private LongArrayWritable generateRotatedHashes(BufferedImage original) {
    var rotationDegree = 45;
    var hashCount = 8;
    var result = new Long[8];
    for (int i = 0; i < hashCount; i++) {
      result[i] = calculator.calculateHash(ImageRotator.rotateImage(original, rotationDegree * i));
    }
    return new LongArrayWritable(result);
  }

  @Override
  public void close() throws IOException {

  }

  @Override
  public void configure(JobConf job) {

  }
}

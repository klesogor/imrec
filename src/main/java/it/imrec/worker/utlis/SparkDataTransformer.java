package it.imrec.worker.utlis;

import java.util.Iterator;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.BZip2Codec;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.input.PortableDataStream;
import org.apache.spark.launcher.SparkLauncher;
import scala.Tuple2;

public class SparkDataTransformer {

  private static final String DOT = ".";
  private static final String SEP_KEY = "-";
  private static final String SEP_PATH = "/";
  private static final JavaSparkContext jsc;

  static {
    SparkConf conf = new SparkConf();
    conf.setAppName("Imrec Parser");
    conf.set(SparkLauncher.EXECUTOR_EXTRA_JAVA_OPTIONS, "-Xss256M");
    conf.set(SparkLauncher.DRIVER_EXTRA_JAVA_OPTIONS, "-Xss256M");
    conf.set("spark.default.parallelism", "8");
    conf.set("spark.master", "local");
    conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());

    jsc = new JavaSparkContext(conf);
  }

  public static void prepareData(String basePath, String dataPathSuffix) {
    JavaPairRDD<String, PortableDataStream> imageByteRDD = jsc.binaryFiles(basePath + dataPathSuffix);
    if (!imageByteRDD.isEmpty()) {
      imageByteRDD.foreachPartition((VoidFunction<Iterator<Tuple2<String, PortableDataStream>>>) arg0 -> {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", basePath);
        SequenceFile.Writer writer = SequenceFile.createWriter(
            conf,
            SequenceFile.Writer.file(
                new Path(
                    basePath + "/result/image_seq" + SEP_KEY + System.currentTimeMillis() + ".seq")),
            SequenceFile.Writer.compression(SequenceFile.CompressionType.RECORD, new BZip2Codec()),
            SequenceFile.Writer.keyClass(Text.class), SequenceFile.Writer.valueClass(BytesWritable.class));

        while (arg0.hasNext()) {
          Tuple2<String, PortableDataStream> fileTuple = arg0.next();
          Text key = new Text(fileTuple._1());
          final String[] keySplit = key.toString().split(SEP_PATH);
          final String fullFileName = keySplit[keySplit.length - 1];
          final int dotIndex = fullFileName.lastIndexOf(DOT);
          String fileName = fullFileName.substring(0, dotIndex);
          String fileExtension = fullFileName.substring(dotIndex + 1);

          BytesWritable value = new BytesWritable(fileTuple._2().toArray());
          key = new Text(keySplit[keySplit.length - 2] + SEP_KEY + fileName + SEP_KEY + fileExtension);
          writer.append(key, value);
        }

        IOUtils.closeStream(writer);
      });
    }
  }
}

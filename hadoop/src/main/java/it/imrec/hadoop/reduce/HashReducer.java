package it.imrec.hadoop.reduce;

import it.imrec.hadoop.model.LongArrayWritable;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

@Slf4j
public class HashReducer implements Reducer<Text, LongArrayWritable, Text, Text> {

  @Override
  public void reduce(Text key, Iterator<LongArrayWritable> values,
      OutputCollector<Text, Text> output,
      Reporter reporter) throws IOException {
    Iterable<LongArrayWritable> iterable = () -> values;
    final String value = StreamSupport.stream(iterable.spliterator(), false)
        .flatMap(arr -> Arrays.stream(arr.get()))
        .map(LongWritable::toString)
        .collect(Collectors.joining(",", "[", "]"));
    log.info("Reducing key: [{}], value: [{}]", key, value);
    output.collect(key,new Text(value));
  }

  @Override
  public void close() throws IOException {
    log.info("GGGG Closed reduce");
  }

  @Override
  public void configure(JobConf job) {
    log.info("GGGG Configured reduce");
  }
}

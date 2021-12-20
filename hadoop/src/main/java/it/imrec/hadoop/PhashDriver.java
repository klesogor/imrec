package it.imrec.hadoop;

import it.imrec.hadoop.map.ImageMapper;
import it.imrec.hadoop.model.LongArrayWritable;
import it.imrec.hadoop.reduce.HashReducer;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.SequenceFileInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;

public class PhashDriver {

  public static void main(String[] args) {
    JobClient my_client = new JobClient();
    // Create a configuration object for the job
    JobConf job_conf = new JobConf(PhashDriver.class);

    // Set a name of the Job
    job_conf.setJobName("PhashPerImage");

    // Specify data type of output key and value
    job_conf.setOutputKeyClass(Text.class);
    job_conf.setOutputValueClass(Text.class);
    job_conf.setMapOutputValueClass(LongArrayWritable.class);

    // Specify names of Mapper and Reducer Class
    job_conf.setMapperClass(ImageMapper.class);
    job_conf.setReducerClass(HashReducer.class);

    // Specify formats of the data type of Input and output
    job_conf.setInputFormat(SequenceFileInputFormat.class);
    job_conf.setOutputFormat(TextOutputFormat.class);

    // Set input and output directories using command line arguments,
    //arg[0] = name of input directory on HDFS, and arg[1] =  name of output directory to be created to store the output file.

    FileInputFormat.setInputPaths(job_conf, new Path(args[0]));
    FileOutputFormat.setOutputPath(job_conf, new Path(args[1]));

    my_client.setConf(job_conf);
    try {
      // Run the job
      JobClient.runJob(job_conf);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}

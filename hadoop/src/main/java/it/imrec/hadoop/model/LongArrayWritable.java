package it.imrec.hadoop.model;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableFactories;

public class LongArrayWritable implements Writable {

  private LongWritable[] values;

  public LongArrayWritable() {
  }

  public LongArrayWritable(Long[] values) {
    LongWritable[] longs = new LongWritable[values.length];
    for (int i = 0; i < values.length; i++) {
      longs[i] = new LongWritable(values[i]);
    }
    set(longs);
  }

  public LongWritable[] get() {
    return values;
  }

  public void set(LongWritable[] values) {
    this.values = values;
  }

  @Override
  public void readFields(DataInput in) throws IOException {
    values = new LongWritable[in.readInt()];
    for (int i = 0; i < values.length; i++) {
      LongWritable value = (LongWritable) WritableFactories.newInstance(LongWritable.class);
      value.readFields(in);
      values[i] = value;
    }
  }

  @Override
  public void write(DataOutput out) throws IOException {
    out.writeInt(values.length);
    for (int i = 0; i < values.length; i++) {
      values[i].write(out);
    }
  }

  @Override
  public String toString() {
    return "LongArrayWritable [values=" + Arrays.toString(values) + "]";
  }
}
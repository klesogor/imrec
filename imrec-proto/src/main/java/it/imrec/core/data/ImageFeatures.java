package it.imrec.core.data;

import lombok.Value;

import java.util.ArrayList;

@Value
public class ImageFeatures {
    public long originalHash;
    public ArrayList<Long> features;
}

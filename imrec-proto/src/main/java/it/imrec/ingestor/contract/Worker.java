package it.imrec.ingestor.contract;

import it.imrec.core.data.ImageFeatures;

import java.awt.image.BufferedImage;

public interface Worker {
    ImageFeatures processImage(BufferedImage image);
}

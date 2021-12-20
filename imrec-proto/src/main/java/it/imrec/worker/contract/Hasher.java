package it.imrec.worker.contract;

import java.awt.image.BufferedImage;

public interface Hasher {
    long calculateHash(BufferedImage image);
}

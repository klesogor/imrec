package lab.imrec.injestor.processor.contract;

import java.awt.image.BufferedImage;

public interface Hasher {
    long calculateHash(BufferedImage image);
}

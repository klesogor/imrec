package lab.imrec.injestor.processor.utlis;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class ImageRotator {
    public static BufferedImage rotateImage(BufferedImage image, double angle){
        AffineTransform tx = new AffineTransform();
        tx.rotate(Math.toRadians(angle), image.getWidth() / 2f, image.getHeight() / 2f);

        AffineTransformOp op = new AffineTransformOp(tx,
                AffineTransformOp.TYPE_BILINEAR);
        return op.filter(image, null);
    }
}

package it.imrec.core.data;

import lombok.EqualsAndHashCode;
import lombok.Value;

public abstract class ImageData {
    public interface ImageVisitor<T>{
        T handleEmbeddedImage(EmbeddedImage img);
        T handleRemoteImage(RemoteImage img);
    }
    public abstract  <T> T match(ImageVisitor<T> visitor);

    @EqualsAndHashCode(callSuper = false)
    @Value
    public static class EmbeddedImage extends ImageData{
        public String rawImageData;

        @Override
        public <T> T match(ImageVisitor<T> visitor) {
            return visitor.handleEmbeddedImage(this);
        }
    }

    @EqualsAndHashCode(callSuper = false)
    @Value
    public static class RemoteImage extends ImageData{
        public String imageUrl;

        @Override
        public <T> T match(ImageVisitor<T> visitor) {
            return visitor.handleRemoteImage(this);
        }
    }
}

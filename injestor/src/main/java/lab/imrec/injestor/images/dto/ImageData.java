package lab.imrec.injestor.images.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;

@AllArgsConstructor
public abstract class ImageData {
    @Getter
    private final String name;


    public interface ImageVisitor<T>{
        T handleEmbeddedImage(EmbeddedImage img);
        T handleRemoteImage(RemoteImage img);
    }
    public abstract  <T> T match(ImageVisitor<T> visitor);

    @EqualsAndHashCode(callSuper = false)
    @Value
    public static class EmbeddedImage extends ImageData{
        private final byte[] rawImageData;

        public EmbeddedImage(String name, byte[] rawImageData) {
            super(name);
            this.rawImageData = rawImageData;
        }

        @Override
        public <T> T match(ImageVisitor<T> visitor) {
            return visitor.handleEmbeddedImage(this);
        }
    }

    @EqualsAndHashCode(callSuper = false)
    @Value
    public static class RemoteImage extends ImageData{
        private final String imageUrl;

        public RemoteImage(String name, String imageUrl) {
            super(name);
            this.imageUrl = imageUrl;
        }

        @Override
        public <T> T match(ImageVisitor<T> visitor) {
            return visitor.handleRemoteImage(this);
        }
    }
}
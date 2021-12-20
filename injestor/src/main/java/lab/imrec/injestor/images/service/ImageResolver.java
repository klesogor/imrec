package lab.imrec.injestor.images.service;

import lab.imrec.injestor.images.dto.ImageData;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service
public class ImageResolver implements ImageData.ImageVisitor<byte[]> {

    private final HttpClient client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .followRedirects(HttpClient.Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(20))
            .build();

    @Override
    public byte[] handleEmbeddedImage(ImageData.EmbeddedImage img) {
        return img.getRawImageData();
    }

    @Override
    public byte[] handleRemoteImage(ImageData.RemoteImage img) {
        try {
            var request = HttpRequest.newBuilder().GET().uri(URI.create(img.getImageUrl())).build();
            var response = this.client.send(request, HttpResponse.BodyHandlers.ofByteArray());
            if(response.statusCode() != 200){
                //Cryptic error messages go brrr
                throw new RuntimeException("Failed to download image!");
            }
            return response.body();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

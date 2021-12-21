package lab.imrec.injestor.images.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String reference;
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "image")
    private List<ImageFeature> features;

    public static Image fromFeatures(String name, String localReference, Collection<Long> hashes){
        var image = Image.builder()
                .id(UUID.randomUUID())
                .name(name)
                .reference(localReference)
                .build();
        var features = hashes.stream()
                .map(hash -> ImageFeature.builder()
                        .image(image)
                        .hash(hash)
                        .build()
                )
                .collect(Collectors.toList());

        image.setFeatures(features);
        return image;
    }
}

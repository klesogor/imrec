package lab.imrec.injestor.images.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageFeature {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private long hash;
    @ManyToOne
    private Image image;
}

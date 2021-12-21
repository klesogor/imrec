package lab.imrec.injestor.images.repository;

import lab.imrec.injestor.images.dto.ImageLookupResult;
import lab.imrec.injestor.images.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Tuple;
import java.util.List;
import java.util.UUID;

@Repository
public interface ImageRepository extends JpaRepository<Image, UUID> {

    @Query(value = "select lookup.name, lookup.reference, lookup.hash, lookup.\"match\" from (select i.id, i.\"name\", i.reference, imgf.hash, 1 - (bit_count(imgf.hash\\:\\:bit(64) # ?1\\:\\:bit(64)))\\:\\:FLOAT / 64 as \"match\" from image i inner join image_feature imgf on i.id = imgf.image_id) as lookup where lookup.\"match\" >= 0.8;", nativeQuery = true)
    public List<ImageLookupResult> searchForImage(long hash);
}

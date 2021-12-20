package lab.imrec.injestor.images.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class RecognitionResult implements Serializable {
    public RecognitionResult(String localReference, String name, Long primaryHash, List<Long> hashes) {
        this.localReference = localReference;
        this.name = name;
        this.primaryHash = primaryHash;
        this.hashes = hashes;
    }

    private String localReference;
    private String name;
    private Long primaryHash;
    private List<Long> hashes;
}

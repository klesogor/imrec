package lab.imrec.injestor.images.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecognitionInput implements Serializable {
    private String localReference;
    private String name;
}

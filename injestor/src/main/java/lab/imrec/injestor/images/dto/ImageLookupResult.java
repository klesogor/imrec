package lab.imrec.injestor.images.dto;

public interface ImageLookupResult {
    String getName();
    String getReference();
    long getHash();
    float getMatch();
}

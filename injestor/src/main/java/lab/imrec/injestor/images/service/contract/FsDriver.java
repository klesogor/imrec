package lab.imrec.injestor.images.service.contract;

public interface FsDriver {
    String saveImage(String name, byte[] bytes);
}

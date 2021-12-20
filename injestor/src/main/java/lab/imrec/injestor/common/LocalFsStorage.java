package lab.imrec.injestor.common;

import lab.imrec.injestor.images.service.contract.FsDriver;
import lab.imrec.injestor.processor.contract.FsReadDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class LocalFsStorage implements FsDriver, FsReadDriver {
    @Value("${localstorage.dir}")
    private String path;
    @Override
    public String saveImage(String name, byte[] bytes) {
        try {
            var path = Paths.get(this.path, name);
            if(Files.exists(path)){
                throw new RuntimeException("File with such name already exists");
            }
            Files.write(path, bytes);
            return name;
        } catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public byte[] readFile(String localToken) {
        try{
            return Files.readAllBytes(Paths.get(path, localToken));
        } catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }
}

package lab.imrec.injestor.images.service;

import lab.imrec.injestor.images.service.contract.ImageValidator;
import org.springframework.stereotype.Service;

@Service
public class ImageValidatorImpl implements ImageValidator {
    @Override
    public boolean isValidImage(byte[] data) {
        return true;
        /*if(data.length < 2){
            return false;
        }

        return isJPEG(data) || isPNG(data);*/
    }


    private static boolean isJPEG(byte[] data){
        System.out.println(data[0] + " " + data[1]);
        //check format identifier
        return data[0] == 255 && data[1] == 216;
    }

    private static boolean isPNG(byte[] data){
        return data[0] == 137 && data[1] == 80;
    }
}

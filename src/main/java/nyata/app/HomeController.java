package nyata.app;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

@Controller
public class HomeController {
    @GetMapping(value = "/")
    public String get() {
        return "index";
    }

    @PostMapping(value = "/")
    public String post() {
        final String BUCKET_NAME = System.getenv("AWS_S3_BUCKETNAME");
        final String FILE_NAME = System.getenv("AWS_S3_FILENAME");

        // 認証情報を用意
        AWSCredentials credentials = new BasicAWSCredentials("アクセスキー", "シークレットキー");

        // クライアントを生成
        AmazonS3 s3 = AmazonS3ClientBuilder
                .standard()
                // 認証情報を設定
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                // リージョンを AP_NORTHEAST_1に設定
                .withRegion(Regions.AP_NORTHEAST_1)
                .build();
        try {
            S3Object o = s3.getObject(BUCKET_NAME, FILE_NAME);
            S3ObjectInputStream s3is = o.getObjectContent();
            FileOutputStream fos = new FileOutputStream(new File(FILE_NAME));
            byte[] read_buf = new byte[1024];
            int read_len = 0;
            while((read_len = s3is.read(read_buf)) > 0) {
                fos.write(read_buf, 0, read_len);
            }
            s3is.close();
            fos.close();
        }catch(AmazonServiceException | IOException e){
            e.printStackTrace();
            System.exit(1);
        }
        return "index";
    }
}
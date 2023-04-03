package edu.byu.cs.tweeter.server.dao.dynamo;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;


import java.io.ByteArrayInputStream;
import java.util.Base64;

import edu.byu.cs.tweeter.server.dao.ImageDAO;

public class DynamoImageDAO implements ImageDAO {

    public DynamoImageDAO() {
    }

    private static AmazonS3 s3 = AmazonS3ClientBuilder.standard()
            .withRegion(Regions.US_WEST_2)
            .build();

    @Override
    public String addImage(String username, String image) {
        byte[] byteArray = Base64.getDecoder().decode(image);

        ObjectMetadata data = new ObjectMetadata();

        data.setContentLength(byteArray.length);

        data.setContentType("image/jpeg");

        PutObjectRequest request = new PutObjectRequest("image-tweeter-bucket", username, new ByteArrayInputStream(byteArray), data).withCannedAcl(CannedAccessControlList.PublicRead);

        s3.putObject(request);

        String link = "https://image-tweeter-bucket.s3.us-west-2.amazonaws.com/" + username;
        return link;
    }

}

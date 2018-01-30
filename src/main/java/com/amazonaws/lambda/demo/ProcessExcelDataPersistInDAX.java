package com.amazonaws.lambda.demo;

import java.util.Map;

import com.amazonaws.lambda.processexcel.service.ReadExcelFromS3;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

public class ProcessExcelDataPersistInDAX implements RequestHandler<S3Event, String> {

    private AmazonS3 s3 = AmazonS3ClientBuilder.standard().build();
    public ProcessExcelDataPersistInDAX() {}

    // Test purpose only.
    ProcessExcelDataPersistInDAX(AmazonS3 s3) {
        this.s3 = s3;
    }

    @Override
    public String handleRequest(S3Event event, Context context) {
        context.getLogger().log("Received event: " + event);

        // Get the object from the event and show its content type
        String bucket = event.getRecords().get(0).getS3().getBucket().getName();
        context.getLogger().log("Bucket Name " + bucket);
        String key = event.getRecords().get(0).getS3().getObject().getKey();
        context.getLogger().log("File Name " + key);
        try {
            S3Object response = s3.getObject(new GetObjectRequest(bucket, key));
            
            Map<String, String> excelData = ReadExcelFromS3.processsExcelRead(response.getObjectContent());
            context.getLogger().log("ExcelData " + excelData);
        } catch (Exception e) {
            e.printStackTrace();
            context.getLogger().log(String.format(
                "Error getting object %s from bucket %s. Make sure they exist and"
                + " your bucket is in the same region as this function.", key, bucket));
        }
        return "Success";
    }
}
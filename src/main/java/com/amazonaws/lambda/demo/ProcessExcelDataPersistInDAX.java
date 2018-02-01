package com.amazonaws.lambda.demo;

import java.util.Map;

import com.amazonaws.lambda.dynamodb.DaxHelper;
import com.amazonaws.lambda.processexcel.service.ReadExcelFromS3;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

public class ProcessExcelDataPersistInDAX implements RequestHandler<S3Event, String> {

	private AmazonS3 s3 = AmazonS3ClientBuilder.standard().build();

	public ProcessExcelDataPersistInDAX() {
	}
	
	private String DAX_ENDPOINT = "javahome-dax.kiwtxk.clustercfg.dax.usw2.cache.amazonaws.com:8111";
	private String DYBANO_TABLE = "DEMO";

	@Override
	public String handleRequest(S3Event event, Context context) {
		context.getLogger().log("Received event: " + event);

		// Get the object from the event and show its content type
		String bucket = event.getRecords().get(0).getS3().getBucket().getName();
		context.getLogger().log("Bucket Name " + bucket);
		String key = event.getRecords().get(0).getS3().getObject().getKey();
		context.getLogger().log("File Name " + key);
		S3Object response = s3.getObject(new GetObjectRequest(bucket, key));

		Map<String, String> countryMap = ReadExcelFromS3.processsExcelRead(response.getObjectContent());
		DaxHelper helper = new DaxHelper();
		DynamoDB daxClient = helper.getDaxClient(DAX_ENDPOINT);
		helper.writeData(DYBANO_TABLE, daxClient, countryMap);
		context.getLogger().log("ExcelData " + countryMap);
		return "Success";

	}
}
package com.amazonaws.lambda.dynamodb;

import java.util.Map;
import java.util.Set;

import com.amazon.dax.client.dynamodbv2.AmazonDaxClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;

public class DaxHelper {
	private static String AWS_REGION = "us-west-2";
	public DynamoDB getDaxClient(String daxEndpoint) {
        System.out.println("Creating a DAX client with cluster endpoint " + daxEndpoint);
        AmazonDaxClientBuilder daxClientBuilder = AmazonDaxClientBuilder.standard();
        daxClientBuilder.withRegion(AWS_REGION).withEndpointConfiguration(daxEndpoint);
        AmazonDynamoDB client = daxClientBuilder.build();
        return new DynamoDB(client);
     }

	public void writeData(String tableName, DynamoDB client, Map<String, String> countryMap) {
		Table table = client.getTable(tableName);
		System.out.println("Writing data to the table...");
		Set<String> keySet = countryMap.keySet();
		try {
			for (String key: keySet) {
					table.putItem(new Item().withPrimaryKey("CountryCode",key).withString("CurrencyCode", countryMap.get(key)));
			}
		} catch (Exception e) {
			System.err.println("Unable to write item:");
			e.printStackTrace();
		}
	}
}

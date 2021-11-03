# What is this?
Image hosting website created using Spring Boot, AWS S3 and DynamoDB for educational purposes. S3 is used for storing
files and DynamoDb is used for storing view counts.

# Requirements
* Java 8+ (Tested on 17.0.1)
* AWS account

# Getting started
* Create DynamoDB table with primary key `id`
* Create S3 bucket
* Specify properties in  `src\main\resources\application.properties`
  * AWS access key
  * AWS secret key
  * AWS region
  * DynamoDB table name
  * S3 bucket name
* Start application (`ImageHostingApplication::main`)
* Visit [http://localhost:8080](http://localhost:8080)

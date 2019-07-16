# High level solution design 
![High level solution design](https://github.com/MahiSharew/SEChallenge/blob/master/SystemDesign/SystemDesign.png)
> High level solution design 
---
The following technologies are used in system design
- **HAProxy**: For load balancing and routing HTTP traffic to services
- **Træfɪk**:Load Balancer for Microservices
- **Consul**: For service discovery
- **Docker**: For running the docker containers of the microservices
- **Autoscaling**
- **Apache Kafka**: For real-time data streaming
- **Lambda function**: For sending request to CloudWatch and DyanomoDB 
- **Amazon CloudWatch Metrics**: For real time data visualizing 
- **DyanomoDB Ondemand**: NoSQL database for data storage. 
- **Dynamodb DAX**: in-memory cache for high performance.
- **Amazon CloudFront**: For high access speed (performance)
---
In this project, two architectural approaches are considered:
1. **Real-time architecture** which provides metrics to customers with at most one hour delay. 
 _Green arrow is used to show the data flow in the system design diagram._  
2. **For reprocessing historical data in case of bugs**. 
_Red arrow is used to show the Data flow in the system design diagram._ 
---
### 1. Real-time Component Architecture
- A number of request from customer are connected to one of the available instance endpoint via **HAProxy**. HAProxy will use a reverse proxy to forward the request (XHR REQUEST) to the **Træfɪk** .
-The **HAProxy** forwards the requests (such as read, write or number of vistors of a website) to **Træfɪk**.Then the **Taefik** forwards the requests to **_appropriate_** micro-service.
- **Autoscaling group** is used for deploying appropriate number micro-services across the available pool of resources. Here, adding or removing micro-service is based on the number of requests received by the system. 
 -  **local consult clients** is responsible for registering micro-services with the **consul server** and to notify the **HAProxy** with the new registerd service. Therefore, with the use of **consul** and **HAProxy** a reliable service discovery and routing can be achieved.
- The **micro-services**(producers) that are running in the Docker containers are responsible for preprocessing and filtering records and send to **Kafka broker** (**Amazon Kinesis Data Streams**  can also be used as an alternative).
- **Apache Kafka** can ingest more than 1 billion records a day as it makes it  perfect suit for this application. **Apache Kafka** store record that send from  **micro-services**  in a topic  for set amount of time.since topic can get big ,  they get split into portions of smaller size for good performance . 
##### we have 2 consumer  **lambda function**,**micro-services** as it shown in the digram 
#### **micro-services** as consumer 
- The micro-services subscribe to the topic to receive new records. micro-services read  records and  build the dashboard based 
-  We provide dashboard creating by  **_microservices_** using the same technology that we used to receive the request from the customer as illustrated on the  System desing diagram (**_microservices, HAProxy and consult_**).
- **CDN services** is utilized for fast content delivery.  When a user requests a content, it is routed to the edge location that provides the lowest latency so that content is delivered with the best possible performance. If the content is already in the edge location with the lowest latency, CloudFront delivers it immediately.
#### **lambda function** as consumer 
- we can use a **lambda function** to process the records and damp the event to **CloudWatch**. **Cloudwatch** is utilized to visualized and build a real-time dashboard as it let us to publish and store metrics. We also can see the data points with a period of fewer than 1 minute by choosing high-resolution custom metrics. 
- We provide dashboard to our customer 
---
### 2. Reprocessing historical data component of architecture
The architecture follows the same approch as the **Real-time architecture** , **Apache Kafka**  store record in a topic  for set amount of time
- lambda function  subscribe to the topic to receive new records ,  lambda function to do a batch reading and store the batches into **Dynamobdb**. Since DynamoDB on-demand is utlized in this project, we can serve thousands of requests per second and we only pay for what we use (pay-per-request).
- For high reading and writing performance, we utlized Dynamodb DAX in-memory cache in which the micro-services read the data from DAX if the data already cache. If the data are not in the cache,we are going to have cache penalty (write the data first into DAX and send the data to microservice).
- The micro-services will build the dashboard  this time it is from  **DAX**. 
- We provide dashboard creating by  **_microservices_** using the same technology that we used to receive the request from the customer as illustrated on the  System desing diagram (**_microservices, HAProxy and consult_**).
- **CDN services** is utilized for fast content delivery.  When a user requests a content, it is routed to the edge location that provides the lowest latency so that content is delivered with the best possible performance. If the content is already in the edge location with the lowest latency, CloudFront delivers it immediately.
---

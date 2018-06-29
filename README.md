# Event-Driven Microservices Orchestration using Camunda, Spring Boot, and RabbitMQ

This is an example project of my article: ["Event-driven Orchestration: An Effective Microservices Integration using BPMN and AMQP"](https://dzone.com/articles/event-driven-orchestration-an-effective-microservi).

## Overview
This sample consists of three sub-projects:
* __amqp-adapter__

	A pre-built common adapter hiding the details of RabbitMQ/AMQP configurations.
	
* __shoppingcart-service__

    A sample orchestration service demonstrating an end-to-end shopping cart fulfillment flow.
    
* __service-stubs__

    A project simulating the integrated services that communicate with the shopping cart service in either a synchronous (i.e. an RPC client) or an asynchronous (i.e. a message subscriber)  way.

## How to Use
### Prerequisite

* [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [RabbitMQ](https://www.rabbitmq.com/)
*  [Camunda BPM](https://camunda.com/)
* [curl](https://curl.haxx.se/) or [postman](https://www.getpostman.com/)

### Build
* Clone or download project from [github](https://github.com/colinlucs/microservices-orchestration-amqp-example.git)
* Build the three projects respectively. For example:
```
$ cd microservices-orchestration-amqp-example
$ cd amqp-adapter
$ mvn clean install
$ cd ../shoppingcart-service
$ mvn clean install
$ cd ../service-stubs
$ mvn clean install
```
### Launch Services

#### Launch the Stub Services
The following services will be launched as the integrated participants in the shopping cart fulfillment flow:
* Location Service: validate customer's shipping address
* Payment Service: process payment for the shopping cart
* Inventory Service: allocate product from inventory
* Order Service: fulfill the customer order
* Customer Service: notify the customer about the shopping fulfillment state
* Back Office Service: handle the exception if  transaction rollback is not possible

Launch the service simulator respectively as below:
```
$ cd microservices-orchestration-amqp-example
$ cd service-stubs/target
$ java -jar microservice-orchestration-using-camunda-amqp-stub-0.0.1-SNAPSHOT.jar --spring.profiles.active=amqp-consumer,location
$ java -jar microservice-orchestration-using-camunda-amqp-stub-0.0.1-SNAPSHOT.jar --spring.profiles.active=amqp-consumer,payment
$ java -jar microservice-orchestration-using-camunda-amqp-stub-0.0.1-SNAPSHOT.jar --spring.profiles.active=amqp-consumer,inventory
$ java -jar microservice-orchestration-using-camunda-amqp-stub-0.0.1-SNAPSHOT.jar --spring.profiles.active=amqp-consumer,order
$ java -jar microservice-orchestration-using-camunda-amqp-stub-0.0.1-SNAPSHOT.jar --spring.profiles.active=amqp-consumer,customer
$ java -jar microservice-orchestration-using-camunda-amqp-stub-0.0.1-SNAPSHOT.jar --spring.profiles.active=amqp-consumer,backoffice
```
#### Launch the Main Orchestration Service -  the Shopping Cart Service
```
$ cd microservices-orchestration-amqp-example
$ cd shoppingcart-service/target
$ java -jar microservice-orchestration-using-camunda-amqp-0.0.1-SNAPSHOT.jar --spring.profiles.active=amqp-producer
```
### Test Shopping Cart Fulfillment Flow
#### Scenario 1: Submit Shopping Cart Successfully - Transaction Committed

* Request to Submit a Shopping Cart:
```
    POST http://localhost:8080/shoppingCart/1c93e02a-e7c6-418b-ae01-c047cefe0001/submit

```
	
* Expected Response:

  * Submit Shopping Cart is successful; the shopping cart is CLOSED
  * HTTP status code: 200


```json
{
  "id": "1c93e02a-e7c6-418b-ae01-c047cefe0001",
  "entityType": "SHOPPINGCART",
  "entitySpecification": "consumerSC",
  "name": "MySchoppingCart_1c93e02a-e7c6-418b-ae01-c047cefe0001",
  "status": "CLOSED",
  "relatedEntities": [
    {
      "id": "2ed5ef8f-1ea6-4851-8541-bf7eefbffeb4",
      "entityType": "LOCATION",
      "entitySpecification": "shippingAddr",
      "name": "MyShippingAddress_1c93e02a-e7c6-418b-ae01-c047cefe0001",
      "relatedEntities": []
    },
    {
      "id": "e7af5e98-d73a-44b8-9021-e98aa448b086",
      "entityType": "PAYMENT",
      "entitySpecification": "creditCartPayment",
      "name": "MyPayment_1c93e02a-e7c6-418b-ae01-c047cefe0001",
      "relatedEntities": []
    },
    {
      "id": "7a5520fc-1287-4d82-a483-c4c1e7c39e50",
      "entityType": "PRODUCT",
      "entitySpecification": "iphoneX_Gold_128G",
      "name": "MyProduct_1c93e02a-e7c6-418b-ae01-c047cefe0001",
      "relatedEntities": []
    },
    {
      "id": "27f9de5c-e384-486c-9252-600381554310",
      "entityType": "PRODUCT",
      "entitySpecification": "iphoneX_Case",
      "name": "MyProduct_1c93e02a-e7c6-418b-ae01-c047cefe0001",
      "relatedEntities": []
    }
  ]
}
```
* Executed Services
As shown in the service log, the fulfillment flow has executed all steps end-to-end:
```
2018-06-27 14:05:12.099  INFO 14672 --- [nio-8080-exec-1] c.m.o.d.b.RetrieveShoppingCartActivity   : execute ShoppingCartService - retrieve
2018-06-27 14:05:12.245  INFO 14672 --- [nio-8080-exec-1] c.m.o.demo.bpm.ValidateAddressActivity   : execute LocationService - validate
2018-06-27 14:05:13.066  INFO 14672 --- [nio-8080-exec-1] c.m.o.demo.bpm.ReservePaymentActivity    : execute PaymentService - reserve
2018-06-27 14:05:13.280  INFO 14672 --- [nio-8080-exec-1] c.m.o.d.bpm.AllocateInventoryActivity    : execute InventoryService - allocate
2018-06-27 14:05:13.550  INFO 14672 --- [nio-8080-exec-1] c.m.o.demo.bpm.PlaceOrderActivity        : execute OrderService - initiate
2018-06-27 14:05:13.864  INFO 14672 --- [nio-8080-exec-1] c.m.o.d.bpm.CloseShoppingCartActivity    : execute ShoppingCartService - close
2018-06-27 14:05:13.889  INFO 14672 --- [nio-8080-exec-1] c.m.o.demo.bpm.NotifyCustomerActivity    : execute CustomerService - notify
```

#### Scenario 2: Submit Shopping Cart Failed Due to Order Service Issue  - Transaction Rolled Back


* Request to Submit a Shopping Cart:
```
    POST http://localhost:8080/shoppingCart/invalid-OrderService/submit

```
	
* Expected Response:

  * Submit Shopping Cart is failed due to the processing error in placing the order to the Order Service
  * HTTP status code: 403

```json

{
    "code": "ERR_ORDER_FAILURE",
    "message": "Unable to process order.",
    "details": "Internal Error: Unable to process order, please contact the system administrator."
}
```
* Executed Services
As shown in the service log, the compensation flow was executed to release the reserved payment and the allocated inventory. The transaction has been rolled back successfully.
```
2018-06-27 14:12:02.760  INFO 14672 --- [nio-8080-exec-4] c.m.o.d.b.RetrieveShoppingCartActivity   : execute ShoppingCartService - retrieve
2018-06-27 14:12:02.766  INFO 14672 --- [nio-8080-exec-4] c.m.o.demo.bpm.ValidateAddressActivity   : execute LocationService - validate
2018-06-27 14:12:02.777  INFO 14672 --- [nio-8080-exec-4] c.m.o.demo.bpm.ReservePaymentActivity    : execute PaymentService - reserve
2018-06-27 14:12:02.784  INFO 14672 --- [nio-8080-exec-4] c.m.o.d.bpm.AllocateInventoryActivity    : execute InventoryService - allocate
2018-06-27 14:12:02.817  INFO 14672 --- [nio-8080-exec-4] c.m.o.demo.bpm.PlaceOrderActivity        : execute OrderService - initiate
2018-06-27 14:12:02.909  INFO 14672 --- [nio-8080-exec-4] c.m.o.demo.bpm.ReleasePaymentActivity    : execute PaymentService - release
2018-06-27 14:12:02.909  INFO 14672 --- [nio-8080-exec-4] c.m.o.demo.bpm.ReleaseInventoryActivity  : execute InventoryService - release
```

#### Scenario 3: Submit Shopping Cart Failed when Location Service is down  - Transaction Aborted
* Prerequisite
  Shutdown the location service by killing the process.
  
* Request to Submit a Shopping Cart:
```
    POST http://localhost:8080/shoppingCart/1c93e02a-e7c6-418b-ae01-c047cefe0002/submit

```
	
* Expected Response:

  * Submit Shopping Cart is failed due to the unavailability of the Location Service
  * HTTP status code: 403

```json

{
    "code": "ERR_SERVICE_UNAVAIL",
    "message": "Service Unavaialble",
    "details": "Internal Error: we are sorry, the LocationService is not reachable. Please try again later."
}
```
* Executed Services
As shown in the service log, the flow was stopped due to the timeout in invoking the location service. Rollback is not required in this case.
```
2018-06-27 14:18:49.266  INFO 14672 --- [nio-8080-exec-7] c.m.o.d.b.RetrieveShoppingCartActivity   : execute ShoppingCartService - retrieve
2018-06-27 14:18:49.282  INFO 14672 --- [nio-8080-exec-7] c.m.o.demo.bpm.ValidateAddressActivity   : execute LocationService - validate
```
## What's Next
In order to keep the sample project simple, the advanced topics of event handling are not addressed, such as RPC timeout, message TTL, clustered services, etc. 
But I hope this sample would help as a start point.

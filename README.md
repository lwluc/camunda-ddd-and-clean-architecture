# Camunda DDD and Clean Architecture

An example to show how you could use clean architecture and DDD and their advantages with Camunda.

## 🚀Features

The [BPMN process](assets/processes/loan_agreement.png) which start a [second process](assets/processes/cross_selling_recommendation.png) via message correlation should represent a tiny business process just to demonstrate the architecture.

### 🛫Start the process

With the following POST request, you could start the process:

```curl
curl --request POST \
  --url http://localhost:8080/loan/agreement/1 \
  --header 'Content-Type: application/json' \
  --data '
  "customerNumber": "A-11",
  "name": "Tester",
  "mailAddress": "tester@web.io",
  "amount": 1100
}'
```

Using the admin user (`username: admin` and `password: pw`) you could log in to the Camunda Cockpit.

## 🏗Architecture

The following sections contain some small aspects explaining the advantages of Domain-driven Design (DDD) and clean architecture.

![DDD-Clean-Architecture](assets/architecture/camunda-ddd-and-clean-architecture-rings.png)

### Clean Architecture

One main advantage of clean architecture is the independence of any framework. Due to this fact the architecture allows (and by using the [Dependency Inversion Principle](https://en.wikipedia.org/wiki/Dependency_inversion_principle)), compared to the conventional layer-architecture, exchange for example your Camunda Framework. So you could migrate to Camunda 8 without even touching your business code.

Flexibility around your domain (e.g., switching from Camunda 7 to Camunda 8) is the main focus I want to show you in this little example.

Using clean architecture as architecture style combines perfectly with Domain-driven Design because we completely focus on our domain, as shown in the image by placing it in the center.

[The origin of clean architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html).

### DDD

Using Domain-driven Design you focus on your core business domain and the domain logic. With this fact in mind, you build your complex design around your domain. Keeping your domain as close to the real business as possible, you need to collaborate constantly with your business experts.

Besides that, the focus in *immutability* and building object that know all about their *invariants* helps you to structure your code. Such DDD Elements can be found in our [domain-primitives](https://github.com/domain-primitives/domain-primitives-java) library.

Structuring your code functional and brining more context to your object with, e.g. Value Object does not only help you to keep your code expressive, it also helps keeping it close to your business as your BPMN model.

## 🙏🏼Credits

Thanks to [Matthias Eschhold](https://github.com/MatthiasEschhold) for the passionate discussion around DDD and clean architecture. 
Matthias published a nice blog series: [Clean Architecture and flexibility patterns](https://github.com/MatthiasEschhold/clean-architecture-and-flexibility-patterns)

## 📨Contact

If you have any questions or ideas, feel free to contact me or create an [issue](https://github.com/lwluc/camunda-ddd-and-clean-architecture/issues).
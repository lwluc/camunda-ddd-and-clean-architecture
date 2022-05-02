# Camunda DDD and Clean Architecture

An example to show how you could use clean architecture and DDD elements with Camunda.

## 🚀Features

The [BPMN process](./assets/loan_agreement.png) is a tiny process just to demonstrate the architecture.

With the following POST request you could start the process:

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

In the following sections contains some small aspects explaining the advantages of Domain-driven Design (DDD) and clean architecture.

![DDD-Clean-Architecture](./assets/camunda-ddd-and-clean-architecture-rings.png)

### DDD

Using Domain-driven Design or to be more precise tactical DDD you could be way more expressive and closer to your business domain. Beside that the focus in *immutability* and building object that know all about their *invariants* helps you to structure your code. Such DDD Elements can be found in our [domain-primitives](https://github.com/domain-primitives/domain-primitives-java) library.

Structuring your code functional and brining more context to your object with, e.g. Value Object does not only help you to keep your code expressive, it also helps keeping it close to you business as your BPMN model.

### Clean

Using clean architecture as architecture style combines perfectly with Domain-driven Design, because we completely focus on our domain, by using the [Dependency Inversion Principle](https://en.wikipedia.org/wiki/Dependency_inversion_principle).

Flexibility around your domain is the main focus I want to show you in this little example. 

The main advantage is the independence of any framework. Due to this fact the architecture allows, compared to the conventional layer-architecture, exchange for example you Camunda Framework. So you could migrate to Camunda 8 without even touching your business code.

[The origin of clean architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html).

## 🙏🏼Credits

Thanks to [Matthias Eschhold](https://github.com/MatthiasEschhold) for the passionate discussion around DDD and clean architecture.

## 📨Contact

If you have any questions or ideas feel free to contact me or create an [issue](https://github.com/lwluc/camunda-ddd-and-clean-architecture/issues).
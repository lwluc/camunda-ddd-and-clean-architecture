# Camunda Custom Connector

A custom Camunda 8 Connector (OutboundConnecotor) which runs on the connector 
runtime. 

## 🛫Build the Container 🐳 

First you need to configure the [env.txt](./env.txt) file, so that the connector could
connect to your cluster.

```bash
# Build it
docker build lwluc/camunda-ddd-and-clean-architecture-custom-notification-connector-camunda-8 .

# Run it
docker run --rm --name=custom-notification-connector --env-file env.txt lwluc/camunda-ddd-and-clean-architecture-custom-notification-connector-camunda-8
```
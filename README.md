# Payment-Gateway

## Initialize Payment Gateway Variables
### Add this to the ~/.zshrc  or ~/.bashrc file (Depending on the System)

```

export PG_DB_USER=<db-user>
export PG_DB_PASSWORD=<db-password>
export PG_DB_URL=<db-url>
export PG_DB_PORT=<db-port>
export PG_DB_DATABASE=<database-schema-name>
export PG_STRIPE_SK_KEY=<stripe-secret-key>
export PG_STRIPE_PUB_KEY=<stripe-public-key>
export PG_STRIPE_REDIRECT_URL=<server-base-url>

```


## Gateway APIs

```declarative
URL: localhost:9090/api/v1/payment/pay

Method : POST

Request:
{
    "source":"stripe" //Optional
    "industry":"Softrobotics",
    "country":"US",
    "currency":"USD",
    "amount":23
}
Response
{
    "success": true,
    "status": "open",
    "message": null,
    "gatewayTxnId": "29b4f531-c977-40f2-a09b-758deb796417",
    "providerTxnId": "cs_test_a1BFdLev85QMDgQ28aGNbYKzniCJGoxjxlPWTIP9iyWysr33zvrffZj5fr",
    "providerTxnURL": "https://checkout.stripe.com/c/pay/cs_test_a1BFdLev85QMDgQ28aGNbYKzniCJGoxjxlPWTIP9iyWysr33zvrffZj5fr#fidkdWxOYHwnPyd1blpxYHZxWjA0V0hxNExBR0hDQV9gRkx3b31CM25OQmJJNFFfT19BY1R3YVxXRnVCVDRGXzVoUX9kdT0xZ01VdDZ3VHJQPUp0MlJSZkpiRnxVUHA0NmZJNGZfR29CSEBCNTVjVGxdd2wyRycpJ2N3amhWYHdzYHcnP3F3cGApJ2lkfGpwcVF8dWAnPyd2bGtiaWBabHFgaCcpJ2BrZGdpYFVpZGZgbWppYWB3dic%2FcXdwYHgl"
}

```

```declarative
URL: localhost:9090/api/v1/payment/stripe/success?gatewayTxnId=<gatewayTxnId>

Method : GET

Response:
{
    "success": true,
    "status": "SUCCESS",
    "message": "Payment Successful",
    "gatewayTxnId": "4bbfa592-1bf1-4c8e-9493-da05e6689c4d",
    "providerTxnId": "pi_3RNCtyDBMFDZeCIr19LDnNyc",
    "providerTxnURL": null
}
```

```declarative
URL: localhost:9090/api/v1/payment/stripe/cancel?gatewayTxnId=<gatewayTxnId>

Method: GET
{
    "success": false,
    "status": "CANCELED",
    "message": "Payment failed",
    "gatewayTxnId": "4bbfa592-1bf1-4c8e-9493-da05e6689c4d",
    "providerTxnId": null,
    "providerTxnURL": null
}


```



```declarative
URL : localhost:9090/api/v1/payment/history/{txnId}

Method : GET

Response
    {
        "gatewayStatus": "SUCCESS",
        "providerStatus": "requires_payment_method",
        "gatewayTxnId": "a19dc325-3697-49be-a5c1-2d0df842c452",
        "providerTxnId": "pi_3RN9kkDBMFDZeCIr0RkFHDqd",
        "currency": "USD",
        "country": "US",
        "industry": null,
        "source": null,
        "amount": 4.00,
        "provider": "Stripe",
        "txnTime": "2025-05-10T15:31:29.532419"
    }

```

```declarative
URL: localhost:9090/api/v1/payment/history?pageNo=1&rowSize=10

Method : GET

Response
    {
        "content": [
        {
            "gatewayStatus": "SUCCESS",
            "providerStatus": "requires_payment_method",
            "gatewayTxnId": "a19dc325-3697-49be-a5c1-2d0df842c452",
            "providerTxnId": "pi_3RN9kkDBMFDZeCIr0RkFHDqd",
            "currency": "USD",
            "country": "US",
            "industry": null,
            "source": null,
            "amount": 4.00,
            "provider": "Stripe",
            "txnTime": "2025-05-10T15:31:29.532419"
        }
    ],
        "pageNo": 0,
        "pageSize": 10,
        "totalPage": 1,
        "totalCount": 1
    }
```

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:9090/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/payment-gateway-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.

## Related Guides

- REST ([guide](https://quarkus.io/guides/rest)): A Jakarta REST implementation utilizing build time processing and Vert.x. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it.
- REST Jackson ([guide](https://quarkus.io/guides/rest#json-serialisation)): Jackson serialization support for Quarkus REST. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it
- Hibernate ORM with Panache ([guide](https://quarkus.io/guides/hibernate-orm-panache)): Simplify your persistence code for Hibernate ORM via the active record or the repository pattern
- JDBC Driver - PostgreSQL ([guide](https://quarkus.io/guides/datasource)): Connect to the PostgreSQL database via JDBC

# gn-api-sdk-android

> An Android SDK for easy integration of your mobile apps with the payment methods
provided by [Gerencianet](http://gerencianet.com.br).

:warning: **This module is under development and is based on the new API that Gerencianet is about to release. It won't work in production by now.**

[![Build Status](https://travis-ci.org/gerencianet/gn-api-sdk-android.svg?branch=master)](https://travis-ci.org/gerencianet/gn-api-sdk-android)
[![Coverage Status](https://coveralls.io/repos/gerencianet/gn-api-sdk-android/badge.svg)](https://coveralls.io/r/gerencianet/gn-api-sdk-android)
[![Maven Central](https://img.shields.io/maven-central/v/br.com.gerencianet.mobile/gn-api-sdk-android.svg)](https://search.maven.org/#search|ga|1|g:"br.com.gerencianet.mobile" AND a:"gn-api-sdk-android")

*According to Gerencianet API Docs, a `payment_token` represents a credit card number in their context. With a payment
token acquired you can process/confirm payments. The purpose of this SDK is to provide a simple way to get these tokens so that
you can send them to your backend from your Android mobile apps.*

## Requirements

- Android 2.2+

## Installation

**Via gradle:**

```gradle
compile 'br.com.gerencianet.mobile:gn-api-sdk-android:0.1'
```

**Via maven:**

```xml
<dependency>
  <groupId>br.com.gerencianet.mobile</groupId>
  <artifactId>gn-api-sdk-android</artifactId>
  <version>0.1</version>
</dependency>
```

## Usage

Instantiate a `Config` object, set your credentials and whether or not you're using sandbox environment:

```java
Config config = new Config();
config.setClientId("clientId");
config.setClientSecret("clientSecret");
config.setSandbox(true);
```

Create a `CreditCard`:

```java
CreditCard creditCard = new CreditCard();
creditCard.setBrand("visa");
creditCard.setCvv("123");
creditCard.setNumber("40120010384433");
creditCard.setExpirationMonth("05");
creditCard.setExpirationYear("2018");
```

Create an `Endpoints` instance:

```java
Endpoints gnEndpoints = new Endpoints(config, new IGnListener() {
    @Override
    public void onPaymentDataFetched(PaymentData paymentData) {
        Log.d(Constants.TAG, new Gson().toJson(paymentData));
    }

    @Override
    public void onPaymentTokenFetched(PaymentToken paymentToken) {
        Log.d(Constants.TAG, paymentToken.getHash());
    }

    @Override
    public void onError(Error error) {
        Log.d(Constants.TAG, new Gson().toJson(error));
    }
});
```

Get a `PaymentToken`:

```java
gnEndpoints.getPaymentToken(creditCard);
```

Observe that `getPaymentToken` is an asynchronous call. When the response arrives, `onPaymentTokenFetched` will be triggered.

*Log.d* output:

```json
{
  "hash": "5fcf90da4830edcacf96e96814c66f5ec1bf28de"
}
```

In addition to getting payment tokens, sometimes you'll need to get payment information to display it for the users. For this, first create a `PaymentType`:

```java
PaymentType paymentType = new PaymentType();
paymentType.setName("visa");
paymentType.setTotal(10000);

gnClient.getPaymentData(paymentType);
```

This time, `onPaymentDataFetched` will be triggered if the call succeeds.

*Log.d* output:

```json
{
  "installments": [{
    "currency": "101,50",
    "hasInterest": false,
    "parcels": 1,
    "value": 10150
  }, {
    "currency": "52,79",
    "hasInterest": true,
    "parcels": 2,
    "value": 5279
  }, {
    "currency": "35,89",
    "hasInterest": true,
    "parcels": 3,
    "value": 3589
  }, {
    "currency": "27,46",
    "hasInterest": true,
    "parcels": 4,
    "value": 2746
  }, {
    "currency": "22,40",
    "hasInterest": true,
    "parcels": 5,
    "value": 2240
  }, {
    "currency": "19,04",
    "hasInterest": true,
    "parcels": 6,
    "value": 1904
  }, {
    "currency": "16,64",
    "hasInterest": true,
    "parcels": 7,
    "value": 1664
  }, {
    "currency": "14,85",
    "hasInterest": true,
    "parcels": 8,
    "value": 1485
  }, {
    "currency": "13,47",
    "hasInterest": true,
    "parcels": 9,
    "value": 1347
  }, {
    "currency": "12,36",
    "hasInterest": true,
    "parcels": 10,
    "value": 1236
  }, {
    "currency": "11,46",
    "hasInterest": true,
    "parcels": 11,
    "value": 1146
  }, {
    "currency": "10,72",
    "hasInterest": true,
    "parcels": 12,
    "value": 1072
  }],
  "interestPercentage": 0,
  "name": "visa",
  "rate": 150
}
```

As you may have already suspected, `onError` will be called in case of any error.

[Here](https://github.com/gerencianet/gn-api-sdk-android-sample) is a sample app with a checkout page using this SDK.

If you're wondering what to do in your backend code, here are the SDK's that will help:

- [NodeJS](https://github.com/gerencianet/gn-api-sdk-node)
- [PHP](https://github.com/gerencianet/gn-api-sdk-php)

If you're looking for this same code but for iOS, here it goes:

- [iOS SDK](https://github.com/gerencianet/gn-api-sdk-ios)

## Tests

To run the test suite with coverage:

```
./gradlew jacocoTestReport
```

The output files will be created in `app/build/reports`.

## Changelog

[CHANGELOG](https://github.com/gerencianet/gn-api-sdk-android/tree/master/CHANGELOG.md)

## License

[MIT](LICENSE)
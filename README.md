# CarConnect Android SDK

CarConnect is build to power your company to locate, unlock, and get information from cars across brands with one single API. The library provides a secure and fast way to authenticate a car to your service.

## Installation

### build.gradle

```groovy
implementation 'com.github.Jesse-Appwise:Carconnect-SDK-Android:ab91c59675'
```

## Usage

### Initialize the SDK

Before accessing any functions of the SDK you must register a clientID

```kotlin
CarConnect.register("your_client_id_here")
```

### Authenticating

To start the process of grating access to a car

```kotlin
val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
    if (result.resultCode == Activity.RESULT_OK) {
        val intent = result.data
        val tokens = intent?.getStringExtra(Authentication.RESULT_TOKENS)
    }
}
```

```kotlin
CarConnect.getInstance().authenticationIntent(this, AuthenticationOptions("username", "optional_brand"))
startForResult.launch(intent)
```

### Brands

Get a list of available brands

```kotlin
CarConnect.getInstance().brands { result ->
    when(result){
        is CarConnectResult.Success -> Log.d("CarConnect", result.value.toString())
        is CarConnectResult.Failure -> Log.e("CarConnect", result.message ?: "-", result.throwable)
    }
}
```
# My Giphy Test App <a href="https://github.com/scerdan"><img alt="License" src="https://img.shields.io/static/v1?label=GitHub&message=sCerdan&color=00ff4c"/></a>

This project is an Android application written in **Kotlin** using **Jetpack Compose** as the primary tool for the user interface. It is designed to manage and display gifs from the api of Giphy Service.

## Features

- **Jetpack Compose** for UI development.
- **Hilt** for dependency injection.
- **Retrofit** for communication with REST APIs.
- **Navigation Component** for navigating between screens.
- **Coil** for efficient image loading.

## Screenshots

| Home Screen | Detail Screen | Preview |
|-------------------|----------------|----------------|
| <img src="https://github.com/scerdan/giphy-test/blob/develop/media/home.jpg" width="250"/> | <img src="https://github.com/scerdan/giphy-test/blob/develop/media/detail.jpg" width="250"/> | <p align="center"> <img src="https://github.com/scerdan/giphy-test/blob/develop/media/video.gif" width="250" height="570" /></p>|

# States

| Empty Home | Network Error | Token Errors |
|-------------------|----------------|----------------|
| <img src="https://github.com/scerdan/giphy-test/blob/develop/media/empty.jpg" width="250"/> | <img src="https://github.com/scerdan/giphy-test/blob/develop/media/network_error.jpg" width="250"/> | <img src="https://github.com/scerdan/giphy-test/blob/develop/media/429.jpg" width="250"/> |


## Installation
Follow these steps to clone the project and set up dependencies:

1. Clone this repository:
    ```bash
    https://github.com/scerdan/giphy-test.git
    ```
2. Open the project in **Android Studio**.
3. Sync the project to install the dependencies.
4. Run
5. Or, you can download the apk from [Apk 🛜](https://github.com/scerdan/giphy-test/tree/develop/apk)

## 🔻Important data to use the app‼️
In order to use the app, you will require a personal access token.  
You can create it by logging into Giphy and accessing:  
[Create Personal Access Token Here!](https://developers.giphy.com/docs/api/#quick-start-guide)

Otherwise you will navigate to the token error screen.

<img src="https://github.com/scerdan/giphy-test/blob/develop/media/429.jpg" width="250"/>

## Architecture MVVM
<img src="https://github.com/scerdan/my-new-challenge/blob/master/pictures/figure0.png" width="500"/>

- This app is based on the MVVM architecture and the Repository pattern, which follows the [Google's official architecture guidance](https://developer.android.com/topic/architecture).

### Architecture Overview
<img src="https://github.com/scerdan/my-new-challenge/blob/master/pictures/figure1.png" width="500"/>

- Each layer follows [unidirectional event/data flow](https://developer.android.com/topic/architecture/ui-layer#udf); the UI layer emits user events to the data layer, and the data layer exposes data as a stream to other layers.
- The data layer is designed to work independently from other layers and must be pure, which means it doesn't have any dependencies on the other layers.

With this loosely coupled architecture, you can increase the reusability of components and scalability of your app.

### UI Layer

<img src="https://github.com/scerdan/my-new-challenge/blob/master/pictures/figure2.png" width="500"/>

The UI layer consists of UI elements to configure screens that could interact with users and [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) that holds app states and restores data when configuration changes.
- UI elements observe the data flow via [DataBinding](https://developer.android.com/topic/libraries/data-binding), which is the most essential part of the MVVM architecture. 
- With [Bindables](https://github.com/skydoves/bindables), which is an Android DataBinding kit for notifying data changes, you can implement two-way binding, and data observation in XML very clean.

### Data Layer

<img src="https://github.com/scerdan/my-new-challenge/blob/master/pictures/figure3.png" width="500"/>

The data Layer consists of repositories, which include business logic, such as querying data from the local database and requesting remote data from the network. It is implemented as an offline-first source of business logic and follows the [single source of truth](https://en.wikipedia.org/wiki/Single_source_of_truth) principle.<br>


# Giphy API Integration

This project integrates the Giphy API to search and retrieve stickers based on user-provided search terms. The following endpoint is used:

## Endpoint Used
```bash
GET https://api.giphy.com/v1/gifs/search
```
[Giphy Documentation](https://developers.giphy.com/docs/api)

# Unit Test

This test class verifies the functionality of the `NetworkModule`, ensuring the proper creation and behavior of `OkHttpClient`, `Retrofit`, and `ApiService`.

- **testProvideOkHttpClient:** Ensures that the `OkHttpClient` instance is created correctly and is not null.  
- **testProvideRetrofit:** Verifies that the `Retrofit` instance is properly created and that its base URL matches the one provided by the `MockWebServer`.  
- **testApiKeyValid:** Simulates a valid API key scenario by enqueueing a mock response with a 200 status code. Checks that the API call succeeds and returns the expected status code.  
- **testApiKeyInvalidOrRateLimitExceeded:** Simulates an invalid API key or rate limit exceeded scenario by enqueueing a mock response with a 429 status code. Ensures that the API call fails and returns the expected status code.  

### Test Setup

- **MockWebServer:** Used to simulate API responses without making actual network requests.  
- **Interceptors:** Two interceptors are added to `OkHttpClient`:  
  - *Logging Interceptor:* Logs request and response details, such as method, URL, headers, and response body.  
  - *API Key Interceptor:* Automatically appends the API key as a query parameter to every request.  

This setup ensures isolated testing of the networking layer, making it independent of the actual API and network conditions.


## Optimizations
What could we optimize in this project?
A lot of things!
For starters, it was done in a few days, so it has a lot of room for optimization. Next I'll go on to optimize some of the edges that we could attack in future features:
- Firstly, we could include tests for the whole app, since we have only tested only the network module.
- As a future feature it occurs to me that we could add a screen to introduce a new token when we have a token error.
- We can improve the designs, add functionality for downloading GIFs and take advantage of the sticker endpoint to expand the options available.

As we can see, we have a lot of room for improvement!

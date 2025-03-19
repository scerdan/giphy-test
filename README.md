# My Giphy Test App
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
| <img src="https://github.com/scerdan/giphy-test/blob/develop/media/home.jpg" width="250"/> | <img src="https://github.com/scerdan/giphy-test/blob/develop/media/detail.jpg" width="250"/> | <img src="https://github.com/scerdan/giphy-test/blob/develop/media/video.gif" width="250"/> |

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



## Optimizations
What could we optimize in this project?
A lot of things!
For starters, it was done in a few days, so it has a lot of room for optimization. Next I'll go on to optimize some of the edges that we could attack in future features.


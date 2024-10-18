# Movie App MVVM Architecture with Hilt

## Major Highlights
- MVVM Architecture
- Dagger Hilt
- Retrofit
- Coroutines
- Flows
- Stateflow
- Viewbinding

![image](https://github.com/user-attachments/assets/3e6a2500-283e-4d71-a0c4-121892a4f73f)

## Features Implemented
- Fetching Movies list
- Movie details
- Instant Search using Flows Operator
  - Debounce
  - Filter
  - DistinctUntilChanged
  - FlatMapLatest

## Dependency Used
- Retrofit for networking
```
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
```

- Android Lifecycle aware component
```
implementation 'android.arch.lifecycle:extensions:1.1.1'
implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1'
implementation 'androidx.lifecycle:lifecycle-runtime-ktx:2.5.1'
```

- Dagger Hilt for dependency Injection
```
implementation "com.google.dagger:hilt-android:2.44"
kapt "com.google.dagger:hilt-compiler:2.44"
```

## How to Run the Project
- Clone the Repository
```
https://github.com/ajinkya-iam/Movie-App.git
```

Visit themoviedb.org and sign up for an API key, Copy the API key provided
Open the `gradle.properties` file in the app module. Find the following line

```
android.nonTransitiveRClass=true
kotlin.code.style=official
org.gradle.jvmargs=-Xmx2048m -Dfile.encoding\=UTF-8
android.useAndroidX=true
android.enableJetifier=true
API_KEY=paste_your_api_key
```

Replace "Add your API Key" with the API key you obtained
Build and run the Project

## Complete Project Structure
```
            com.ajinkyashinde.movieapp
            ├───data
            │   ├───api
            │   ├───model
            │   └───repository
            ├───di
            │   └───module
            ├───ui
            │   ├───base
            │   ├───moviedetails
            │   ├───movielist
            │   └───searchmovie
            └───utils
                MovieApplication.kt

```


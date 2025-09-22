# SPLA (Secondary Product Legitimacy Application)

This is an Android application developed as a preliminary self-assessment tool for authenticating luxury goods.

## Backend Setup

The backend is powered by a serverless Google Firebase architecture.

### Firestore

Firestore is used to store the authentication knowledge base. The data is stored in the form of JSON checklists.

### Firebase Storage

Firebase Storage is used to host high-resolution reference imagery.

## Getting Started

To get started with the project, you need to set up your Firebase project and add the `google-services.json` file to the project.

### 1. Create a Firebase project

If you don't have a Firebase project yet, create one in the [Firebase console](https://console.firebase.google.com/).

### 2. Add your Android app to your Firebase project

In the Firebase console, add your Android app to your Firebase project. Make sure to use the package name `com.example.spla`.

### 3. Download the `google-services.json` file

After adding your app, download the `google-services.json` file from the Firebase console.

### 4. Add the `google-services.json` file to your project

Place the downloaded `google-services.json` file in the `app/` directory of your project.

## Building the project

Once you have added the `google-services.json` file, you can build the project using Android Studio or the command line.

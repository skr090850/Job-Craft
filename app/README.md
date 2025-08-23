# JobCraft - Android Job Finder App 🚀

<p align="center">
  <img src="https://placehold.co/600x300/7c3aed/ffffff?text=JobCraft&font=raleway" alt="JobCraft App Banner">
</p>

JobCraft is a dynamic Android job finder app 📱 designed to connect job seekers with their next career opportunity. Built with **Java** and **XML** for a native experience, it uses **Firebase** for real-time data storage of job listings and user profiles. This project showcases a complete, data-driven mobile application.

---

## ✨ Key Features

* **User Authentication:** Secure sign-up and login for job seekers and recruiters using Firebase Authentication.
* **Job Browsing & Searching:** A clean, intuitive interface to browse, search, and filter job listings.
* **Detailed Job Descriptions:** View comprehensive details for each job, including company info, responsibilities, and required skills.
* **User Profile Management:** Users can create and update their professional profiles with their details, resume, and skills.
* **Real-time Database:** All job listings and user data are stored and synced in real-time using Firebase Firestore.
* **Simple Application Process:** (Optional Feature) A straightforward "Apply Now" button to connect users with recruiters.

---

## 🛠️ Tech Stack & Architecture

* **Language:** **Java**
* **UI/Layout:** **XML** (Extensible Markup Language)
* **Database:** **Firebase Firestore** (for real-time, NoSQL data storage)
* **Authentication:** **Firebase Authentication**
* **Architecture:** Model-View-ViewModel (MVVM) - *[Optional, mention if you used it]*
* **IDE:** Android Studio

---

## Firebase Setup Instructions

To run this project, you need to connect it to your own Firebase project.

1.  **Create a Firebase Project:**
    * Go to the [Firebase Console](https://console.firebase.google.com/).
    * Click on **"Add project"** and follow the on-screen instructions.

2.  **Add an Android App to your Project:**
    * In your project's dashboard, click the Android icon to add a new Android app.
    * Enter the **Package Name** from your app's `build.gradle` file (usually something like `com.example.jobcraft`).
    * You can skip the "Add SHA-1" step for now.

3.  **Download `google-services.json`:**
    * Firebase will provide a `google-services.json` file to download.
    * Download this file and place it in the **`app/`** directory of your Android Studio project.

4.  **Enable Firebase Services:**
    * In the Firebase Console, go to the **"Authentication"** section from the left menu and **enable "Email/Password"** sign-in method.
    * Go to the **"Firestore Database"** section, click **"Create database"**, start in **test mode** for easy setup, and choose a location.

---

## 🚀 Getting Started

Follow these steps to get the project up and running on your local machine.

### Prerequisites

* Android Studio (latest version recommended)
* A Google account for Firebase setup

### Installation

1.  **Clone the repository:**
    ```sh
    git clone [https://github.com/skr090850/Job-Craft.git](https://github.com/skr090850/Job-Craft.git)
    ```

2.  **Open in Android Studio:**
    * Open Android Studio.
    * Select `File > Open` and navigate to the cloned project directory.

3.  **Connect to Firebase:**
    * Follow the **Firebase Setup Instructions** above to add your `google-services.json` file to the `app/` folder.

4.  **Sync & Run:**
    * Let Android Studio sync the Gradle files.
    * Click the **"Run"** button to build and install the app on an emulator or a physical device.

---
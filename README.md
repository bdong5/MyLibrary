# Library App

The Library App is an **Android** application designed to facilitate the management of personal reading lists. Users can organize books into various categories. The application leverages **Firebase Firestore** for secure and reliable data storage.

## Features

- **Comprehensive Book Management**: Allows users to add, remove, and view detailed information about books in their collection.
- **Categorized Reading Lists**: Supports the organization of books into specific categories for better tracking and management.
- **Firebase Firestore Integration**: Ensures secure storage and retrieval of book data.
- **User Interface**: Designed for ease of use, providing an intuitive experience for managing personal libraries.
- **Error Handling**: Implements robust error handling to manage issues during data retrieval from Firestore.

## Getting Started

### Prerequisites

- **Android Studio**: Ensure you have the latest version installed.
- **Firebase Account**: Required for setting up Firebase Firestore for backend data storage.

### Installation

1. **Clone the Repository**:
    ```bash
    git clone https://github.com/bdong5/library-app.git
    ```

2. **Open the Project** in Android Studio.

3. **Configure Firebase**:
    - Navigate to the Firebase console and create a new project.
    - Register your Android app within the Firebase project and download the `google-services.json` file.
    - Place the `google-services.json` file in the `app/` directory of your project.

4. **Build and Run**:
    - Sync the project with Gradle files.
    - Build and run the application on an Android device or emulator.

## Usage

Once installed, users can begin adding books to their lists, view details of each book, and organize them based on their reading status. The application will automatically synchronize data with Firebase Firestore.

## License

Who needs one?

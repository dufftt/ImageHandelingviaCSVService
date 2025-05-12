# Image Handling via CSV Service

This project demonstrates a microservices architecture for processing product image URLs from a CSV file, downloading the images, compressing them, and then providing an updated CSV with links to the processed images.

## Overview

The system consists of two main microservices:

1.  **CSV Handling Service (`csv_handeling`)**: Responsible for parsing uploaded CSV files, managing product data, and coordinating with the Image Download Service.
2.  **Image Download Service (`image_download_service`)**: Responsible for fetching image URLs, downloading and compressing images, and storing their new paths.

## Features

*   Upload product data via CSV.
*   Asynchronous image downloading and compression.
*   Tracking of processing status via a unique `trackID`.
*   Generation of an output CSV file with original data and new image URLs.
*   RESTful APIs for interaction.
*   Inter-service communication using Spring Cloud OpenFeign.

## Architecture

The user interacts with the **CSV Handling Service** to upload a CSV file. This service then communicates with the **Image Download Service** to process the images. Once processing is complete, the user can download the updated CSV from the **CSV Handling Service**.

*(You can embed your `diagram.png` here if you wish, or link to `csv_handeling/diagram.drawio.svg`)*

[View Project Diagram](csv_handeling/diagram.drawio.svg)

## Modules

### 1. CSV Handling Service (`csv_handeling`)

*   **Purpose**: Manages the CSV data, initiates image processing, and provides the final output CSV.
*   **Port**: `9092` (configurable in [csv_handeling/src/main/resources/application.properties](csv_handeling/src/main/resources/application.properties))
*   **Key Endpoints**:
    *   `POST /uploadCsv`: Uploads a CSV file. Expects a `MultipartFile` named `file`.
        *   Example: `curl -X POST -F "file=@/path/to/your/input.csv" http://localhost:9092/uploadCsv`
    *   `GET /fetchByTrackID/{trackID}`: (Internal) Called by the Image Download Service to fetch product URLs for a given `trackID`.
    *   `POST /sendOutputData`: (Internal) Called by the Image Download Service to send back processed image data.
    *   `GET /getcsv/{trackID}`: Downloads the processed CSV file containing original data and the new output image URLs.
*   **Database Entities**:
    *   [`csvProduct`](csv_handeling/src/main/java/com/duft/csv_handeling/Entity/csvProduct.java): Stores product information (serial number, name, track ID).
    *   [`csvProductUrls`](csv_handeling/src/main/java/com/duft/csv_handeling/Entity/csvProductUrls.java): Stores individual product image URLs from the input CSV, linked to `csvProduct`. Includes a `processedInd` flag.
    *   [`outputProductUrls`](csv_handeling/src/main/java/com/duft/csv_handeling/Entity/outputProductUrls.java): Stores the paths to the downloaded and compressed images, linked to `csvProductUrls`.

### 2. Image Download Service (`image_download_service`)

*   **Purpose**: Downloads images from URLs provided by the CSV Handling Service, compresses them, and stores them.
*   **Port**: `9091` (configurable in [image_download_service/src/main/resources/application.properties](image_download_service/src/main/resources/application.properties))
*   **Key Endpoints**:
    *   `GET /fetchByTrackID/{trackID}`: Called by the CSV Handling Service to initiate the image download and processing for a given `trackID`.
    *   `GET /retry`: Triggers a retry mechanism for processing image URLs that were not successfully processed previously.
*   **Image Storage**: Images are downloaded and compressed to a local directory (configured as `UPLOAD_DIR` in [`com.duft.image_download_service.Utility.Utility`](image_download_service/src/main/java/com/duft/image_download_service/Utility/Utility.java), currently `/Users/souravsharma/Downloads/images`).
*   **Database Entities**:
    *   [`csvProductUrls`](image_download_service/src/main/java/com/duft/image_download_service/Entities/csvProductUrls.java): Stores a copy of the image URLs to be processed.
    *   [`outputProductUrls`](image_download_service/src/main/java/com/duft/image_download_service/Entities/outputProductUrls.java): Stores the local file paths of the compressed images.

## Setup and Running

### Prerequisites

*   Java 17 or higher
*   Apache Maven

### Running the Services

1.  **Image Download Service**:
    *   Navigate to the `image_download_service` directory.
    *   Run the application:
        ```sh
        ./mvnw spring-boot:run
        ```
    *   The service will start on `http://localhost:9091`.

2.  **CSV Handling Service**:
    *   Navigate to the `csv_handeling` directory.
    *   Run the application:
        ```sh
        ./mvnw spring-boot:run
        ```
    *   The service will start on `http://localhost:9092`.

Ensure the `image_download_service` is running before the `csv_handeling` service attempts to communicate with it.

## API Usage / Workflow

1.  **Upload CSV**:
    *   Prepare a CSV file with columns: `product_id`, `name`, `productUrl`. The `productUrl` column can contain comma-separated image URLs.
    *   Send a POST request to `http://localhost:9092/uploadCsv` with the CSV file.
        ```bash
        curl -X POST -F "file=@/path/to/your/input.csv" http://localhost:9092/uploadCsv
        ```
    *   The response will include a `trackID` and the initial processing status.

2.  **Image Processing**:
    *   The CSV Handling Service will trigger the Image Download Service using the `trackID`.
    *   The Image Download Service will fetch the image URLs, download, compress, and save them. This process is asynchronous.
    *   It will then send the updated information (local image paths) back to the CSV Handling Service.

3.  **Download Processed CSV**:
    *   Once processing is complete (or if you want to check status), send a GET request to `http://localhost:9092/getcsv/{trackID}` (replace `{trackID}` with the ID received in step 1).
    *   If processing is still ongoing for some images, the service might respond with a message to retry later. The `image_download_service` has a `/retry` endpoint that can be triggered if needed, or the `csv_handeling` service might trigger it automatically if `checkProcessedInd` finds unprocessed items.
    *   If complete, this will download a new CSV file. This CSV will include the original data plus a new column (e.g., `OutputImageUrls`) containing the local file paths of the compressed images.

## Configuration

*   **Application Properties**:
    *   Service ports, database configurations (H2 in-memory), and Feign client URLs are defined in the `application.properties` file within each service's `src/main/resources` directory.
        *   [csv_handeling/src/main/resources/application.properties](csv_handeling/src/main/resources/application.properties)
        *   [image_download_service/src/main/resources/application.properties](image_download_service/src/main/resources/application.properties)
*   **Image Upload Directory**: The directory where downloaded and compressed images are stored is configured in [`com.duft.image_download_service.Utility.Utility`](image_download_service/src/main/java/com/duft/image_download_service/Utility/Utility.java) (`UPLOAD_DIR`).

```
This project demonstrates a microservices architecture for processing product image URLs from a CSV file, downloading the images, compressing them, and then providing an updated CSV with links to the processed images.

## Overview

The system consists of two main microservices:

1.  **CSV Handling Service (`csv_handeling`)**: Responsible for parsing uploaded CSV files, managing product data, and coordinating with the Image Download Service.
2.  **Image Download Service (`image_download_service`)**: Responsible for fetching image URLs, downloading and compressing images, and storing their new paths.

## Features

*   Upload product data via CSV.
*   Asynchronous image downloading and compression.
*   Tracking of processing status via a unique `trackID`.
*   Generation of an output CSV file with original data and new image URLs.
*   RESTful APIs for interaction.
*   Inter-service communication using Spring Cloud OpenFeign.

## Architecture

The user interacts with the **CSV Handling Service** to upload a CSV file. This service then communicates with the **Image Download Service** to process the images. Once processing is complete, the user can download the updated CSV from the **CSV Handling Service**.


[View Project Diagram](csv_handeling/diagram.drawio.svg)

## Modules

### 1. CSV Handling Service (`csv_handeling`)

*   **Purpose**: Manages the CSV data, initiates image processing, and provides the final output CSV.
*   **Port**: `9092` (configurable in [csv_handeling/src/main/resources/application.properties](csv_handeling/src/main/resources/application.properties))
*   **Key Endpoints**:
    *   `POST /uploadCsv`: Uploads a CSV file. Expects a `MultipartFile` named `file`.
        *   Example: `curl -X POST -F "file=@/path/to/your/input.csv" http://localhost:9092/uploadCsv`
    *   `GET /fetchByTrackID/{trackID}`: (Internal) Called by the Image Download Service to fetch product URLs for a given `trackID`.
    *   `POST /sendOutputData`: (Internal) Called by the Image Download Service to send back processed image data.
    *   `GET /getcsv/{trackID}`: Downloads the processed CSV file containing original data and the new output image URLs.
*   **Database Entities**:
    *   [`csvProduct`](csv_handeling/src/main/java/com/duft/csv_handeling/Entity/csvProduct.java): Stores product information (serial number, name, track ID).
    *   [`csvProductUrls`](csv_handeling/src/main/java/com/duft/csv_handeling/Entity/csvProductUrls.java): Stores individual product image URLs from the input CSV, linked to `csvProduct`. Includes a `processedInd` flag.
    *   [`outputProductUrls`](csv_handeling/src/main/java/com/duft/csv_handeling/Entity/outputProductUrls.java): Stores the paths to the downloaded and compressed images, linked to `csvProductUrls`.

### 2. Image Download Service (`image_download_service`)

*   **Purpose**: Downloads images from URLs provided by the CSV Handling Service, compresses them, and stores them.
*   **Port**: `9091` (configurable in [image_download_service/src/main/resources/application.properties](image_download_service/src/main/resources/application.properties))
*   **Key Endpoints**:
    *   `GET /fetchByTrackID/{trackID}`: Called by the CSV Handling Service to initiate the image download and processing for a given `trackID`.
    *   `GET /retry`: Triggers a retry mechanism for processing image URLs that were not successfully processed previously.
*   **Image Storage**: Images are downloaded and compressed to a local directory (configured as `UPLOAD_DIR` in [`com.duft.image_download_service.Utility.Utility`](image_download_service/src/main/java/com/duft/image_download_service/Utility/Utility.java), currently `/Users/souravsharma/Downloads/images`).
*   **Database Entities**:
    *   [`csvProductUrls`](image_download_service/src/main/java/com/duft/image_download_service/Entities/csvProductUrls.java): Stores a copy of the image URLs to be processed.
    *   [`outputProductUrls`](image_download_service/src/main/java/com/duft/image_download_service/Entities/outputProductUrls.java): Stores the local file paths of the compressed images.

## Setup and Running

### Prerequisites

*   Java 17 or higher
*   Apache Maven

### Running the Services

1.  **Image Download Service**:
    *   Navigate to the `image_download_service` directory.
    *   Run the application:
        ```sh
        ./mvnw spring-boot:run
        ```
    *   The service will start on `http://localhost:9091`.

2.  **CSV Handling Service**:
    *   Navigate to the `csv_handeling` directory.
    *   Run the application:
        ```sh
        ./mvnw spring-boot:run
        ```
    *   The service will start on `http://localhost:9092`.

Ensure the `image_download_service` is running before the `csv_handeling` service attempts to communicate with it.

## API Usage / Workflow

1.  **Upload CSV**:
    *   Prepare a CSV file with columns: `product_id`, `name`, `productUrl`. The `productUrl` column can contain comma-separated image URLs.
    *   Send a POST request to `http://localhost:9092/uploadCsv` with the CSV file.
        ```bash
        curl -X POST -F "file=@/path/to/your/input.csv" http://localhost:9092/uploadCsv
        ```
    *   The response will include a `trackID` and the initial processing status.

2.  **Image Processing**:
    *   The CSV Handling Service will trigger the Image Download Service using the `trackID`.
    *   The Image Download Service will fetch the image URLs, download, compress, and save them. This process is asynchronous.
    *   It will then send the updated information (local image paths) back to the CSV Handling Service.

3.  **Download Processed CSV**:
    *   Once processing is complete (or if you want to check status), send a GET request to `http://localhost:9092/getcsv/{trackID}` (replace `{trackID}` with the ID received in step 1).
    *   If processing is still ongoing for some images, the service might respond with a message to retry later. The `image_download_service` has a `/retry` endpoint that can be triggered if needed, or the `csv_handeling` service might trigger it automatically if `checkProcessedInd` finds unprocessed items.
    *   If complete, this will download a new CSV file. This CSV will include the original data plus a new column (e.g., `OutputImageUrls`) containing the local file paths of the compressed images.

## Configuration

*   **Application Properties**:
    *   Service ports, database configurations (H2 in-memory), and Feign client URLs are defined in the `application.properties` file within each service's `src/main/resources` directory.
        *   [csv_handeling/src/main/resources/application.properties](csv_handeling/src/main/resources/application.properties)
        *   [image_download_service/src/main/resources/application.properties](image_download_service/src/main/resources/application.properties)
*   **Image Upload Directory**: The directory where downloaded and compressed images are stored is configured in [`com.duft.image_download_service.Utility.Utility`](image_download_service/src/main/java/com/duft/image_download_service/Utility/Utility.java) (`UPLOAD_DIR`).

```
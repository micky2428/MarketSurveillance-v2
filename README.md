# Market Surveillance Android App

This Android application, named "Market Surveillance," is designed to streamline the workflow of market inspections. The app, written in Kotlin, facilitates the process of documenting and recording information about products inspected in markets.

## Project Overview

The primary functionalities of the app include:

1. **Image Text Recognition**: Users can capture photos of products and use ML Kit Text Recognition v2 to extract text information. This data is then populated into fields such as product name, origin, etc., and can be uploaded to a Google Sheet for archival purposes.

2. **Google Drive Integration**: The app integrates with Firebase to upload captured images directly to Google Drive for storage.

![Text recognition](https://github.com/micky2428/MarketSurveillance-v2/assets/99666209/cfa26a3a-651b-4c66-8e47-4d858c240c74)
![Fill with spaces](https://github.com/micky2428/MarketSurveillance-v2/assets/99666209/85b672d8-5d28-4eb6-9846-8db6dbe6d3a4)
![Upload photos to google drive](https://github.com/micky2428/MarketSurveillance-v2/assets/99666209/02a6f93d-cb3b-41ce-b69b-a534f2f3d38b)

## Purpose

The motivation behind developing this app stems from the need to streamline the inspection process. With approximately 6000 products inspected annually, automating data entry through image recognition significantly reduces manual transcription time. Additionally, storing data in cloud-based solutions ensures accessibility and facilitates further analysis.

## File Descriptions

1. **MainActivity.kt**: This serves as the home page of the app, featuring buttons for "Market Inspection" and "Upload Photos to Cloud." The former redirects to `ProductInfo.kt`, where users can manually input data or utilize the image recognition feature. Selecting "Take Photo" leads to `NextActivity.kt`.

2. **ProductInfo.kt**: This activity displays fields for comprehensive product information such as name, manufacturer/importer details, and address.

3. **NextActivity.kt**: Utilizes Google ML Kit for text recognition. Extracted text is populated into predefined fields, and a "Submit" button uploads the data to Google Sheets.

4. **TextRecognitionProcessor**: Implements the logic for text recognition using Google ML Kit.

5. **UploadActivity**: Implements the logic for uploading files to Google Drive.

## Design Considerations

1. **Data Storage**: Choosing between Firebase and Google Sheets for data storage was deliberated. Google Sheets was selected due to its compatibility with the existing database system utilized by the organization.

2. **Google Drive Integration**: Initially, attempts were made to directly integrate with Google Drive API. However, due to stricter authentication requirements for native apps, Firebase was adopted as an intermediary for successful integration.

3. **Text Recognition Model**: ML Kit was chosen for its ease of integration and suitability for mobile applications. Besides, it can recognize text in any Chinese, Devanagari, Japanese, Korean and Latin character set. Although alternatives like Google Docs offer text recognition capabilities, ML Kit's dedicated SDK for mobile development provided a more streamlined solution.

## References

- [Integrate Google Drive For Backup data on android Kotlin Jetpack Compose](https://medium.com/@salman.alamoudi95/integrate-google-drive-for-backup-data-on-android-kotlin-jetpack-compose-e92cff32f71f)
- [ML Kit Quickstart](https://github.com/googlesamples/mlkit)
- ChatGPT

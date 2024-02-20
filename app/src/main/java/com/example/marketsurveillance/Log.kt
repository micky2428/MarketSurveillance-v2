package com.example.marketsurveillance

//google網站api串接-error 400-待找尋解方

//import com.google.api.client.auth.oauth2.Credential
//import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
//import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
//import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
//import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
//import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
//import com.google.api.client.http.javanet.NetHttpTransport
//import com.google.api.client.json.JsonFactory
//import com.google.api.client.json.gson.GsonFactory
//import com.google.api.client.util.store.FileDataStoreFactory
//import com.google.api.services.drive.Drive
//import com.google.api.services.drive.DriveScopes
//import java.io.File
//import java.io.FileInputStream
//import java.io.FileNotFoundException
//import java.io.IOException
//import java.io.InputStreamReader
//import java.security.GeneralSecurityException
//
//
///* class to demonstrate use of Drive files list API */
//object DriveQuickstart {
//    /**
//     * Application name.
//     */
//    private const val APPLICATION_NAME = "MarketSurveillance"
//
//    /**
//     * Global instance of the JSON factory.
//     */
//    private val JSON_FACTORY: JsonFactory = GsonFactory.getDefaultInstance()
//
//    /**
//     * Directory to store authorization tokens for this application.
//     */
//    private const val TOKENS_DIRECTORY_PATH = "tokens"
//
//    /**
//     * Global instance of the scopes required by this quickstart.
//     * If modifying these scopes, delete your previously saved tokens/ folder.
//     */
//    private val SCOPES = listOf(DriveScopes.DRIVE_METADATA_READONLY)
//    private const val CREDENTIALS_FILE_PATH = "D:/Android/MarketSurveillance/app/src/main/java/com/example/marketsurveillance/credentials.json"
//    //和官方文件不同，使用了絕對路徑
//    /**
//     * Creates an authorized Credential object.
//     *
//     * @param HTTP_TRANSPORT The network HTTP Transport.
//     * @return An authorized Credential object.
//     * @throws IOException If the credentials.json file cannot be found.
//     */
//    @Throws(IOException::class)
////    private fun getCredentials(HTTP_TRANSPORT: NetHttpTransport): Credential {
////        // Load client secrets.
////        val `in` =
////            DriveQuickstart::class.java.getResourceAsStream(CREDENTIALS_FILE_PATH)
////                ?: throw FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH)
////        val clientSecrets =
////            GoogleClientSecrets.load(
////                JSON_FACTORY,
////                InputStreamReader(`in`)
////            )
//    private fun getCredentials(HTTP_TRANSPORT: NetHttpTransport): Credential { //和官方文件不同
//        // Load client secrets.
//        val credentialsFilePath = "$CREDENTIALS_FILE_PATH"
//        try {
//            val inputStream = FileInputStream(credentialsFilePath)
//            val clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, InputStreamReader(inputStream))
//            // 在这里可以继续处理 clientSecrets
//
//            // Build flow and trigger user authorization request.
//            val flow = GoogleAuthorizationCodeFlow.Builder(
//                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES
//            )
//                .setDataStoreFactory(
//                    FileDataStoreFactory(
//                        File(
//                            TOKENS_DIRECTORY_PATH
//                        )
//                    )
//                )
//                .setAccessType("offline")
//                .build()
//            val receiver = LocalServerReceiver.Builder().setPort(8888).build()
//            //returns an authorized Credential object.
//            return AuthorizationCodeInstalledApp(flow, receiver).authorize("user")
//        } catch (e: FileNotFoundException) {
//            e.printStackTrace()
//            throw IOException("Credentials file not found: $credentialsFilePath")
//        } catch (e: IOException) {
//            e.printStackTrace()
//            throw IOException("Error reading credentials file: $credentialsFilePath")
//        }
//    }
//
//    @Throws(IOException::class, GeneralSecurityException::class)
//    @JvmStatic
//    fun main(args: Array<String>) {
//        // Build a new authorized API client service.
//        val HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport()
//        val service = Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
//            .setApplicationName(APPLICATION_NAME)
//            .build()
//
//        // Print the names and IDs for up to 10 files.
//        val result = service.files().list()
//            .setPageSize(10)
//            .setFields("nextPageToken, files(id, name)")
//            .execute()
//        val files = result.files
//        if (files == null || files.isEmpty()) {
//            println("No files found.")
//        } else {
//            println("Files:")
//            for (file in files) {
//                System.out.printf("%s (%s)\n", file.name, file.id)
//            }
//        }
//    }
//}
//

//1130217

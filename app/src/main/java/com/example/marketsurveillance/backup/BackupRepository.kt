package com.example.marketsurveillance.backup


interface BackupRepository<D> {
    suspend fun backup(data:D)
    suspend fun getFiles():List<DriveFileInfo>
    suspend fun restore(fileId:String)
}
package com.example.marketsurveillance.login

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marketsurveillance.auth.AuthRepository
import com.example.marketsurveillance.backup.DriveBackupImage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel  //由 Hilt 管理的 ViewModel，這表示 Hilt 將會處理這個 ViewModel 的依賴注入
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository,   //處理身份驗證相關的邏輯
    @ApplicationContext private val context: Context
):ViewModel() {
//負責處理應用程式的主要邏輯，包括身份驗證、Google Drive 的操作以及處理與使用者互動相關的事件
    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    private val _effect = MutableStateFlow<MainEffect?>(null)
    val effect = _effect.asStateFlow()

    private val driveBackup = DriveBackupImage(context)
    init {
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.observeUserStatus().collect{user->
                if (user!=null){
                    driveBackup.drive = authRepository.getGoogleDrive()
                }
                _state.update {
                    it.copy(
                        user?.email
                    )
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun onEvent(mainEvent: MainEvent){
        when(mainEvent){
            is MainEvent.Backup -> {
                viewModelScope.launch(Dispatchers.IO) {
                    driveBackup.backup(mainEvent.imageUri)
                }
            }
            is MainEvent.OnAuthorize -> {
                viewModelScope.launch(Dispatchers.IO) {
                    authRepository.authorizeGoogleDriveResult(mainEvent.intent)
                }
            }
            is MainEvent.OnSignInResult -> {
                viewModelScope.launch (Dispatchers.IO){
                    onSignInResult(mainEvent.intent)
                }
            }
            MainEvent.GetFiles -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val getFiles = driveBackup.getFiles()
                    _state.update {
                        it.copy(
                            restoreFiles = getFiles
                        )
                    }
                }
            }

            MainEvent.SignInGoogle -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val getGoogleSignIn = authRepository.signInGoogle()
                    _effect.update {
                        MainEffect.SignIn(getGoogleSignIn)
                    }
                }
            }
            MainEvent.SignOut -> {
                viewModelScope.launch(Dispatchers.IO) {
                    authRepository.signOut()

                }
            }

            is MainEvent.Restore -> {
                viewModelScope.launch (Dispatchers.IO){
                    driveBackup.restore(mainEvent.fileId)
                }
            }
        }
    }

    private suspend fun onSignInResult(intent:Intent){
        val getResult = authRepository.getSignInResult(intent)
        _state.update {
            it.copy(
                email = getResult.email
            )
        }
        val authorizeGoogleDrive = authRepository.authorizeGoogleDrive()
        if (authorizeGoogleDrive.hasResolution()){
            _effect.update {
                MainEffect.Authorize(authorizeGoogleDrive.pendingIntent!!.intentSender)
            }
        }
    }

}

sealed class MainEffect{
    data class SignIn(val intentSender: IntentSender):MainEffect()
    data class Authorize(val intentSender: IntentSender):MainEffect()
}
sealed class MainEvent{
    data object SignInGoogle:MainEvent()
    data object SignOut:MainEvent()

    data class Backup(val imageUri:Uri):MainEvent()

    data class OnSignInResult(val intent:Intent):MainEvent()
    data class OnAuthorize(val intent:Intent):MainEvent()
    data class  Restore(val fileId:String):MainEvent()

    data object GetFiles:MainEvent()
}
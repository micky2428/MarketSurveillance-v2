#用于 Android 应用程序的优化和安全性增强。
#在 Android 开发中，proguard-rules.pro 文件通常用于配置 ProGuard 规则，告诉 ProGuard 哪些类、方法或字段需要保留，哪些可以被混淆或移除
#參考:https://jamesqi.medium.com/proguard-d8cc2e67211
# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#google最新認證方式:credential manager
#-if class androidx.credentials.CredentialManager
#-keep class androidx.credentials.playservices.** {
#  *;
#}
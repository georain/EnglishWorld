@echo off
chcp 65001 >nul 2>&1
echo ============================================
echo   英语天地 EnglishWorld APK 打包指南
echo ============================================
echo.

REM 检测 local.properties
if not exist "local.properties" (
    echo [错误] 未找到 local.properties 文件！
    echo.
    echo 请按以下步骤操作：
    echo.
    echo -------------------------------------------
    echo 方法一：通过 Android Studio 打包（推荐）
    echo -------------------------------------------
    echo   1. 打开 Android Studio
    echo   2. File -^> Open，选择本项目文件夹
    echo   3. 等待 Gradle Sync 完成
    echo   4. Build -^> Build Bundle(s) / APK(s) -^> Build APK(s)
    echo   5. 编译完成后在 app/build/outputs/apk/debug/ 下查看
    echo.
    echo -------------------------------------------
    echo 方法二：命令行手动打包
    echo -------------------------------------------
    echo   1. 找到本机 Android SDK 路径：
    echo      Android Studio -^> Settings -^> Android SDK -^> 复制路径
    echo.
    echo   2. 复制 local.properties.template 为 local.properties
    echo      修改 sdk.dir 为你实际的 SDK 路径，例如：
    echo      sdk.dir=C\:\\Users\\YourName\\AppData\\Local\\Android\\Sdk
    echo.
    echo   3. 再次运行 build_apk.bat 选择打包选项
    echo.
    pause
    exit /b 1
)

REM 调用正式的打包脚本
call build_apk.bat

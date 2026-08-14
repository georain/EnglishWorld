@echo off
chcp 65001 >nul 2>&1
echo ========================================
echo       EnglishWorld Build Tool
echo ========================================
echo.

if not exist "app\build.gradle" (
    echo Error: Please run in EnglishWorld folder
    pause
    exit /b 1
)

echo Select option:
echo   1. Build Debug APK
echo   2. Build Release APK
echo   3. Clean Project
echo   4. Generate Keystore
echo   0. Exit
echo.

set /p choice="Enter choice (0-4): "

if "%choice%"=="1" goto build_debug
if "%choice%"=="2" goto build_release
if "%choice%"=="3" goto clean
if "%choice%"=="4" goto generate_key
if "%choice%"=="0" goto end

echo Invalid choice
pause
goto end

:build_debug
echo.
echo [Building Debug APK...]
call gradlew assembleDebug
if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo   Build Success!
    echo ========================================
    echo APK: app\build\outputs\apk\debug\app-debug.apk
    echo.
    explorer app\build\outputs\apk\debug\
) else (
    echo Build failed
)
goto end

:build_release
echo.
echo [Building Release APK...]
call gradlew assembleRelease
if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo   Build Success!
    echo ========================================
    echo APK: app\build\outputs\apk\release\app-release.apk
    echo.
    explorer app\build\outputs\apk\release\
) else (
    echo Build failed
)
goto end

:clean
echo.
echo [Cleaning...]
call gradlew clean
echo Done!
goto end

:generate_key
echo.
set /p keystore_name="Keystore name (englishworld.jks): "
if "%keystore_name%"=="" set keystore_name=englishworld.jks

set /p alias="Key alias (englishworld): "
if "%alias%"=="" set alias=englishworld

echo.
echo Generating keystore...
echo Please enter password when prompted
echo.

keytool -genkey -v -keystore %keystore_name% -keyalg RSA -keysize 2048 -validity 10000 -alias %alias%

if %ERRORLEVEL% EQU 0 (
    echo.
    echo Keystore created: %keystore_name%
    echo IMPORTANT: Backup this file and remember the password!
) else (
    echo Failed to create keystore
)
goto end

:end
echo.
pause

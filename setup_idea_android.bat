@echo This script will setup your local.properties file and generate IntelliJ Project File (.ipr).
@echo.

@set /p sdk=Enter your location of Android SDK (or leave empty to skip): 
@if not defined sdk goto idea

@set sdk=%sdk:\=/%
@>local.properties (
	echo # Location of the Android SDK
	echo sdk.dir=%sdk%
)

:idea
gradlew idea
@pause
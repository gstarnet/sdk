# SDK
Recon SDK related files

First time only to get the SDK Add-On:
- Open the Android SDK Manager
- Click on Tools
- Select Manage Add-Ons site
- Select User Defined Sites
- Select New and add: http://www.reconinstruments.com/wp-content/recon-sdk/repository.xml
- Click close
- Scroll Down to API-16 and check "Recon Instruments"
- Select to download the add-on

To add it to your Gradle Project:
- In the build.gradle, set this:
  compileSdkVersion 'Recon Instruments:Recon Instruments SDK Add-On:16'

To add it to your Eclipse Project:
- In the project properties, under Android, select "Recon Instruments SDK"



# Car Locator System

Car Locator System is a internet based system that tracks your car in real time and shows its current location on the map.

### Used APIS & Libraries
  - Google Maps Api
  - Volley Networking Library (for Android)
  - CircleImageView (https://github.com/hdodenhof/CircleImageView)
  - PinView (https://github.com/ChaosLeong/PinView)


### Tech

This Project uses:

* [PHP](http://php.net/) - PHP handles the linking of database and online servers with the android apps.
* [Android Studio](https://developer.android.com/studio/) - Apps for the android is created using android studio.
* [Server 000webhost](https://www.000webhost.com/) - Its a free server that has been used by us to host php scripts and databse online.
* [Java](https://www.java.com/) - It has been used in android studio for building backend of the the android app.
* [xml](https://www.xml.com/) - Design UI and animations of android apps.
* [html](https://www.w3schools.com/html/) - For designing email containing otp.


### Installation

To install the Car Tracker app copy the "Car Tracker.apk" under CarLocator4L folder to your phone storage and install it.

To install the Car Locator app copy the "Car Locator.apk" under CarLocatorUserSide1M folder to your phone storage and install it.

Open app info for both the apps and grant all the permissions for both the apps. 




### Details about each and every app and web is given under their respctive folders.

## VERY IMPORTANT:

In the apps java files or string.xml where ever there is "https://car-locator-javalab-proj.000webhostapp.com/" replace it by your website name where the php files have been uploaded.

Ex: In this repository inside "Car-Locator-System/CarLocator4L/app/src/main/res/values/strings.xml" there are links like:

**"https://car-locator-javalab-proj.000webhostapp.com/loginA.php"**

make it:

**"https://your-example-site.com/loginA.php"**

### BUGS:
- Apps crashing when requesting permission/s.
- Map not zooming in Car Locator app.
- Online status is not changing in database if Car Tracker app is closed abruptly like when phone is rebooted not in proper way (like by removing barttery or force stopping emulator etc.)






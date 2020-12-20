# Restaurant App

University project: Android application for retrieving and displaying informations about restaurants, using a free API.

The application contains 2 activities and 8 fragments.

## Activities:

### 1. Main Activity

Contains a fragment container and a bottom navigation bar.

### 2. Maps activity

Displays Google Maps with the current restaurant's location and name. 

<img src="images/maps.jpg" alt="Maps Activity" height="500" width="270"/>


## Fragments:

### 1. Splash fragment

Where data is initialised, based on the profile section and
when the user gets from here to List Fragment, the data is already loaded.

<img src="images/splash.jpg" alt="Splash Fragment" height="500" width="270"/>

### List fragment

Here we have the main list with the restaurants. You can do text search or
filtering by Favorited data, Country list, City list, Price options. Each item has: Title, Address, Image (placeholder or image uploaded by user),
Price, Favourite icon (with this you can favourite it in/out).

<img src="images/restaurants.jpg" alt="List Fragment" height="500" width="270"/>|
<img src="images/city-filter.jpg" alt="List Fragment" height="500" width="270"/>

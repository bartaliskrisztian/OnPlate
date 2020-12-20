# OnPlate

University project: Android application for retrieving and displaying informations about restaurants, using a free API.

The application contains 2 activities and 8 fragments.

## Activities:

### 1. Main Activity

Contains a fragment container and a bottom navigation bar.

### 2. Maps activity

Displays Google Maps with the current restaurant's location and name. 

<img src="images/maps.jpg" alt="Maps Activity" height="500" width="270"/>


## Fragments:

### 1. Register fragment

Here the user can register, each user must have a unique username and email address. All inputs are validated, passwords are hashed (sha-256).

<img src="images/register.jpg" alt="Splash Fragment" height="500" width="270"/>

### 2. Login fragment

Here the user can login or go to registration.


<img src="images/login.jpg" alt="Login Fragment" height="500" width="270"/>

### 3. Splash fragment

Where data is initialised, based on the profile section and
when the user gets from here to List Fragment, the data is already loaded.

<img src="images/splash.jpg" alt="Splash Fragment" height="500" width="270"/>

### 4. List fragment

Here we have the main list with the restaurants. You can do text search or
filtering by Favorited data, Country list, City list, Price options. Each item has: Title, Address, Image (placeholder or image uploaded by user),
Price, Favourite icon (with this you can favourite it in/out).

<img src="images/restaurants.jpg" alt="List Fragment" height="500" width="270"/>|
<img src="images/city-filter.jpg" alt="List Fragment" height="500" width="270"/>

### 5. Favorites fragment

Here we can see the list of favorite restaurants of a user. The items can be removed by long click.

<img src="images/favorites.jpg" alt="Favorites Fragment" height="500" width="270"/>

### 6. Remove favorite fragment

A dialog fragment where the user can confirm the removing of a favorite, or cancel it.

<img src="images/remove-favorite.jpg" alt="Favorites Fragment" height="500" width="270"/>

### 7. Detail fragment

Here you can view all the details of what the API returned and edit it according to
the possibilities. These possibilities are: Favourites, Add/Delete Images. Other things: open
Google Maps with the coordinates of the restaurant, call the place.
Regarding displaying the images (at all the options in the app), if you have added your own
ones, then they are prioritised and those are displayed instead of the ones from the API. In the
case when you have not added images by your own, image from the API is displayed or if not exists then a
placeholder.

<img src="images/details.jpg" alt="Details Fragment" height="500" width="270"/>

### 8. Profile fragment

Here you can see and manage your own profile and can log out.

<img src="images/profile.jpg" alt="Profile Fragment" height="500" width="270"/>

# PhotoAlbumSample

PhotoAlbumSample is an Android sample project for displaying images from placeholder API.

## Frameworks

- OkHttp
- Retrofit2
- Moshi
- Kotlin Coroutines
- Glide
- Lifecycle Components
- LiveData

## Restrictions

- Android API from 21 to 29.

## Known issues

1. List blinks after data update. 

 - Suggested fix: hide list behind progress bar and after data update show list with animation or animate alpha value.
2. Small inset for top left item of photos grid
- Suggested fix: update calculation for divider, so it will calculate all sides separately depending on its position.
3. Not fixed divider margins for different screen size
- Suggested fix: implement calculation for grid items, so they will calculate it's width and height depending on screen size.

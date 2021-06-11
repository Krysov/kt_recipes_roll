![gif](https://user-images.githubusercontent.com/12496282/121555948-b97b7f80-ca13-11eb-9685-b6cd0af54c44.gif)

# about
This displays a list of recipes which the user can browse and read in detail.
A fold out design is utilized to not break the UX flow and to not have to switch
to a separate view to display the recipe details and allow for further browsing.

# installation
- have Android Studio set up for kotlin projects
- set the values contentful.space and contentful.token in the local.properties
- for the instrument test disable animations on your test device

# known issues
- it is assumed that the list has no duplicate items (same id)
- large descriptions (currently not present with the data) cause issues with the item view masking

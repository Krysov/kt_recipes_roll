![gif](https://j.gifs.com/lROk8J.gif)

# about
This displays a list of recipies which the user can browse and read in detail.
A fold out design is utilized to not break the UX flow and to not have to switch
to a separate view to display the recipe details and allow for further browsing.

# installation
- have Android Studio set up for kotlin projects
- for the instrument test disable animations on your test device
- for the view related unit tests JDK9 is required

# known issues
- it is assumed that the list has no duplicate items (same id)
- large descriptions (currently not present with the data) cause issues with the item view masking

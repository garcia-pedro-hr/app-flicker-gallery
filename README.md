Flickr Gallery App
======

This app has a screen to list a set of images fetched from Flickr REST API, where the images are shown in a grid layout.
Upon tapping on an image, it is opened and shown in full screen.

On a first version, you can use the parameter tags hardcoded to kitten.
The image tiles should use the images with label Large Square

Features contemplated by this app
------
- Pagination
- Infinite scrolling
- Rotation
- Animations
- Caching (response data is cached to avoid hitting the network every time)
- Offline mode (any existent images are used from previous runs if no network is available)

Features to be implemented
------
- Unit tests
- Instrumented tests

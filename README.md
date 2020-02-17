# StackOverflow Browser

![Search](https://github.com/ZieIony/StackOverflowBrowser/blob/master/images/search.png)
![Questions](https://github.com/ZieIony/StackOverflowBrowser/blob/master/images/question.png)

### The idea
It's a simple app, but still it's not that easy to make it fool-proof, nice-looking and well-coded. My goals were:

 - It shouldn't be overengineered. The app is simple and the code should be simple as well.
 - It should handle connection problems, orientation changes, RTL, etc. Basically, it shouldn't crash and behave well on all devices.
 - It should feel like a real app, not like an API test. The application should offer refreshing, paging and should show some useful data (like avatars or scores).
 - Material Design.
 - The app has to work on my Galaxy S3 mini with Android 4, so I can actually run it.

### Custom code
This app makes good use of some of my libraries. There's no point in reinventing the wheel, but it might be hard to track what libraries were actually used for networking, animations or navigation. Here's the list:

##### Carbon
https://github.com/ZieIony/Carbon

Material Design utility and backport. Helps me deal with UI-related irritating details. It's based on official android.support/android.support.design libraries with a bit of data binding, custom vector loading and animation helpers.

##### Base
https://github.com/ZieIony/Base

Base code for all my current Android application projects. Contains navigation setup for activities and fragments, and some view model-related stuff.

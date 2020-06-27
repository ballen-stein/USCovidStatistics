# USCovidStatistics

US Covid Statistics is more of a Global Covid Statistics app at this point by accessing repputable data from places such as John Hopkins University to gather and display daily information about Covid-19. Data is fetched using OkHttp and RxJava, ViewBinding for easier user interface changes, and is using Model-View-Presenter/MVP architecture.

## Dynamic Homepage
The Homepage by default displays only Global data. When viewing other countries, the favorite button can be clicked to add that country to the homepage. The hompage will dynamically change based on the saved countries, allowing new ones to immediately appear when returning to the hompage...

![](save_country.gif)

and immediately removing ones that are unfavorited.

![](remove_country.gif)

Both actions are saved to a Shared Preference setting, taking up minimal storage and allowing the app to display all the favorited countries on startup.


## Auto-Incremental Settings
Each setting is a Shared Preference with its own purpose, with the notifications having a slight twist. Instead of spamming notifications, a base metric is created when an option is selected, with options of Cases, Recoveries, and Deaths. 

###

When that metric is met, a push notification will be sent to the phone. Normally, that'd be the end of it; instead, the app will check to see if a notification was sent and, once confirmed that a notification was indeed sent, will auto-increment the metric for that specific notification, such as Cases, and save the new value. 

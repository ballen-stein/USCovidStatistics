# USCovidStatistics

US Covid Statistics was a temp name that stuck; the app is entirely a Global Covid Statistics app at this point. The app utilizes OkHttp and RxJava to make network requests, ViewBinding for easier user interface changes, Dark Theme as a base, and is using Model-View-Presenter/MVP architecture. Dagger2 was going to be used but the project was too far along by the time I thought about transitioning to it that I left the app as-is.

## Reliable Data
All data is taken from open-source APIs that compile a majority of their data from John Hopkin's University among other reputable sources. These APIs update every 10-15 minutes with the app making a request every 5 minutes, with a setting to increase or decrease that frequency. 

Every country will display it's provinces (or States for the US), territories, and Others (cruises, mainly) where applicable. Additionally, to add some flare, each country page will also display the country's flag when it's available, showing the United Nations flag when one is not found.

## Dynamic Homepage
The Homepage by default displays only Global data. When viewing other countries, the favorite button can be clicked to add that country to the homepage. The hompage will dynamically change based on the saved countries, allowing new ones to immediately appear when returning to the hompage...

![](save_country.gif)

and immediately removing ones that are unfavorited.

![](remove_country.gif)

Both actions are saved to a Shared Preference setting, taking up minimal storage and allowing the app to display all the favorited countries on startup.

## Auto-Incremental Settings
Each setting is saved as a Shared Preference with a few being co-dependent. The most expansive and diverse setting is the notifications option. Instead of spamming notifications or having them timed, a base metric is created when one of the three options is selected (Cases, Recoveries, or Deaths). 

When notifications are enabled, a service will run in the background, even when the app is closed. When that metric is met, a push notification will be sent to the phone. Normally, that'd be the end of it; instead, the app will check to see if a notification was sent and, once confirmed that a notification was indeed sent, will auto-increment the metric for that specific notification, such as Cases, and save the new value. 

![](settings.gif)

Essentially, if Cases has a trigger at 1,000 and Recoveries at 500 and a request to the API is made and Cases surpassed 1000 but Recoveries didn't, Cases will be marked as having made a notification, the metric will be incremented, and Cases will be marked eligible for another notification. Recoveries, not meeting its metric, will not receive a new metric.


[The app can be downloaded here (will require/requested external downloads to be enabled)](https://github.com/bma33/USCovidStatistics/releases/tag/v1.0)

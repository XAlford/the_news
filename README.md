# The news

**The news** is an app that takes articles from New York Times API and displays them in sections. The articles are displayed by categories and by top stories news.

This app was built as a group project for **General Assembly’s Android development Bootcamp**.

## Project Details

To be able to use New York Times API, 3 different end points had to be used. "News Wire", "Top Stories" and "Search". News Wire gives the most recent articles and Top Stories gives the top articles. Because those 2 end points only provided title, category and image url, the search end point had to be used to get a small paragraph for each article. Because these 3 end points were using the same API KEY, the search end point is only used when detail activity loads data for the article, if the requested article doesn't have a paragraph stored in the database the search end point is called and the paragraph stored in the database to prevent future calls for the same article.

Main activity has a view pager and tab layout to hold 3 different fragments. "All Stories", "Top Stories", and "Saved Articles".

All Stories includes a recycler view that holds each category title and another recycler view that holds a maximum of six items. The child recycler view has two different view types: the articles and a custom view that is always going to be at the last position allowing the user to see all the articles in each category.

Each activity (except search) has a navigation drawer that includes all the categories so the user can go directly to a category without having to go to all stories fragment.

When clicking on a article an Intent is created with the article ID and also a specific key to know which activity is calling startActivity. Detail Activity has a custom view pager to be able to swipe left and right to move to the previuos or next article. The second key is used to pull data from the database to create a list for the view pager. This list is only populated with the articles that correspond to the previous activity, meaning that if an item from top stories is clicked, when swiping left and right in detail view the next article that is going to be shown is going to be an article from top stories. If an item from a category is clicked, when swiping left and right the next article that is going to be shown is going to be an article for that category and so on for each activity that starts detail view.

Job Scheduler calls a Service to load new articles whenever the phone is connected to a WIFI network and charging. The service loads the articles to the database, if the limit of articles in the database is reached, the services removes the oldest article from the database before inserting a new one.



## Features include:

- Articles that are displayed by category.
- Ability to save articles to read later.
- Abitility the articles database.
- Swipe left and right in Detail view to read the next article.

#### Skills/languages/tools: Java, Android SDK, SQLite, Services, Job Scheduler, Retrofit, Volley, Rest API, JSON, GSON.

## Screenshots

![image](/screenshots/all_stories.jpg)
![image](/screenshots/category_view.jpg)
![image](/screenshots/detail_view.jpg)
![image](/screenshots/loading_page.jpg)
![image](/screenshots/navigation_drawer.jpg)
![image](/screenshots/saved_items.jpg)
![image](/screenshots/search.jpg)
![image](/screenshots/settings_page.jpg)
![image](/screenshots/share_using.jpg)
![image](/screenshots/top_stories.jpg)
![image](/screenshots/web_view.jpg)




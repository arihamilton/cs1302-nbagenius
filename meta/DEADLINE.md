# Deadline

Modify this file to satisfy a submission requirement related to the project
deadline. Please keep this file organized using Markdown. If you click on
this file in your GitHub repository website, then you will see that the
Markdown is transformed into nice looking HTML.

## Part 1: App Description

> Please provide a firendly description of your app, including the app
> category that you chose, and the primary functions available to users
> of the app.

* App Category
  - The app is an External API Tool. 

* App Functions
  - This app gets an NBA team based on the user's choice,
  - then finds songs that use one of that team's players as a lyric.
  - Upon opening the app, they will be able to pick an NBA team from a list of teams.
  - The app then processes that team using the NBA Api, Genius API, and ScraperMonkey API.
  - The NBA API returns a list of players that are on that team.
  - The Genius API searches for songs that use those players as lyrics.
  - Since the Genius API does not give us a song's lyrics, the ScraperMonkey API is used to scrape the lyrics off of the song's Genius page.
  - For each player on the roster, the app shows a list of songs that contain that player's full name.
  - Then, the player can choose to select another team and repeat the whole process. 
                

## Part 2: New

> What is something new and/or exciting that you learned from working
> on this project?

* What did I learn?
  - I learned about how to work with APIs! The gallery app was a good start, and I wanted to find out what else I could do with APIs.
  - With the gallery I was limited to one API, but now I've learned about how to find more (through sites like RapidApi) and how I can query them.
  - I've always wanted to make an app where you input a Spotify song and it gives you a list of similar songs, and now I think I can!

## Part 3: Retrospect

> If you could start the project over from scratch, what do
> you think might do differently and why?
        
* Retrospect
  - If I could start over, I would definitely spend more time researching APIs. The Genius API I have works, but it is very slow.
  - The Scraper API I use is decent, but it returns an unformatted version of a page's HTML, so I have to format it myself.
  - I would also research how to make my own API, so I can have complete control over what kind of data I receive from a query.
# Andrew Dunn Vintrace Challenge

## How to Run

You need to start both the back-end and frontend independently. Open two
terminals, navigate to this root directory and run the following commands:

### Terminal 1: Back-end
```bash
./gradlew run
```

### Terminal 2: Front-end
```bash
cd frontend
yarn start
```

You should then be able to access the front-end at http://localhost:3000

### Available wines
I only have the original three wines loaded. You can add more by dropping them
into the **src/main/resources/randy/vintrace/data** directory.

For convenience here are links to the three wines

- http://localhost:3000/wine/11YVCHAR001
- http://localhost:3000/wine/11YVCHAR002
- http://localhost:3000/wine/15MPPN002-VK

You should have loaded the frontend once before trying to access the wines.

### Dependencies
You will need to have the JDK, Node and Yarn installed.

## Notes
- I'd normally separate the REST endpoints into a separate project but didn't
  for this exercise to save time that would be better spent than setting up a
  multi-artifact build.
- The top-level package name is **randy**, a nickname I picked up early in my
  career which stuck.
- All of my tests are purely at the REST endpoint level, as that is the only
  layer where users will be interacting with the system. I generally avoid
  testing at the lower layers as it takes time that I would rather spend
  creating more tests at the layers the users actually interact with.
- For this exercise, I've implemented the API as specified in the challenge
  instructions, however if working with a real-world application, I'd probably
  change the API a bit to make it more RESTful.
  
  Something like:

  - **/api/v1/wine/{lotCode}/breakdown/year**
  - **/api/v1/wine/{lotCode}/breakdown/variety**
  - **/api/v1/wine/{lotCode}/breakdown/region**
  - **/api/v1/wine/{lotCode}/breakdown/year-variety**
  
  Would better communicate that the breakdown is a property of the wine, in my
  opinion.
- The frontend is currently built separately from the backend. I'd like to
  incorporate it into the gradle build, but don't have the time to do that.
- Originally I was only planning on doing a vertical slice of the **Year**
  breakdown, but when refactoring I was basically able to add all of them with
  little effort. The other breakdowns were not implemented with TDD though.
- I would have preferred to use TDD for the frontend, but due to time
  constraints I stopped once I was happy with the architecture of the code.
- I am guessing the original was developed in something like Material UI, but
  I decided to do the HTML and CSS myself to save time learning a new framework.
- I did not implement the edit or back buttons, or the avatar thing (I still 
  have no idea what it's for), or the down arrow next to the "Show More" button.
- My implementation actually made it so easy to add new breakdown types that I
  even added a bonus one: "Region & Variety".
- While I wrote the CSS to closely resemble the template, I did not aim for a
  pixel perfect implementation and did not use absolute/relative positioning.
- I have not tested the code on a mobile device.

## Assumptions
- I'm assuming that grape varieties are normalised to a single name per wine,
  eliminating the need to coalesce alternative names of the same grape during
  aggregation, e.g Syrah & Shiraz.
  - If I were to implement this, I'd create an index of grape varieties by
    their common names.
  - The most common name used in a wine by percentage would be presented to
    endpoint users.
- I'm also assuming that the input data will frequently have seemingly
  identical wine components that are frequently repeated. I'm guessing it's the
  output of another REST endpoint that is hiding unique information for the
  "duplicate" components (such as harvest date, vineyard, grape quality, etc.)
- I assumed that the percentages for each wine component should add up to 100%.
- I assumed that "null" values should be hidden from the user.
- Users will have access to the Internet and Google Fonts.
- Users will be using a desktop browser.

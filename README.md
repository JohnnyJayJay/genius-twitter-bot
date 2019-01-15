# Genius-Twitter Bot (BoBo Bot)
This is just a fun project I made to create [this](https://twitter.com/bobooftheday) Twitter account but 
I decided to make it public so that everybody is able to use it.

## Step by Step Tutorial/Set Up

1. Create a new Twitter account to use as the bot.
2. Log in and head over to [this page](https://developer.twitter.com/en/apps)
3. Create a developer account there
4. Click on "Create an app" (on the page I just linked)
5. Fill in the necessary information and click "Create"
6. Create a [genius](https://genius.com) account and [create an API client](https://genius.com/api-clients/new)
7. Now download the .jar-file [here](../master/build/lib) and put it in some folder
8. Download the [config-template.json](../master/config-template.json) file and put it in the same directory.
9. Edit the file using any editor, preferably something like [Notepad++](https://notepad-plus-plus.org/).
[Click here](../master/README.md#Configuration) to see how to configure the bot.
10. After you're done, save the file and rename it *config.json*
11. Create a new file in the same directory and call it *start.bat* (for Windows). You can edit it using any editor.
Write this in there:
```batch
java -jar NAME-OF-THE-JAR-FILE.jar
pause
```
Replace "NAME-OF-THE-JAR-FILE" with the name of the .jar-file (duh)

You're done! The bot should start as soon as you double click *start.bat*. 
If it says something like "The command "java" is unknown" in the console, 
you either need to install Java or you still have to 
[add it to your *Path* environment variable](https://kingluddite.com/tools/how-do-i-add-java-to-my-windows-path).

The bot will immediately send the first tweet after starting and will then continue periodically as configured.

## Configuration
There are several things to configure in the *config.json* file. Let's start with the Twitter and Genius credentials.

### Twitter
In the "twitter" section, you have to provide 4 things:
- a consumer API key
- a consumer API key secret
- an access token
- an access token secret

All of these can be found on the [apps page](https://developer.twitter.com/en/apps) where you've created your app before.
Just navigate to the page of your app -> *Keys and Tokens*. The consumer keys should already be visible, but you may have to
generate your access token + secret. Once you've done that, simply copy them and add set them in the *config.json* file.

### Genius
In the "genius" section, you have to provide an access token as well as the name of the artist you'd like to take lyrics from.
The access token can be found [here](https://genius.com/api-clients). You may have to click "Generate Access Token" first.

Now you need to specify the name of the artist, preferably as they are called on genius.com, as the app will query the API using name.

Setting `"double_check_artist"` to `true` ensures that only songs from the artist will be fetched. This was implemented because the 
genius API tends to throw in random songs from other artists when fetching the ones of a specific one in some cases. Most of the time, 
this should not happen, so you can set it to `false`. Though if it does, you know how to prevent it.

### Behaviour
The rest of the config specifies how the app should behave, i.e. when to post, how to post and so on.
These are the options available:
- `"posts_per_day"`: How many tweets should the bot make per day? A number is expected here, such as `4`. 
Note that you must take [Twitter's rules](https://help.twitter.com/en/rules-and-policies/twitter-automation) 
for post frequency into account.
- `"max_stash_size"`: How many songs should be stashed, i.e. how many songs to choose from when picking random 
lyrics. This should be a multiple of `50`, such as `100` or `50`. If the artist has less songs than 50, 
all songs will be taken into account. The songs are ordered using their popularity, i.e. providing `50` will use 
the 50 most popular songs of this artist. To apply changes to this setting, delete the *songs.genius* file in 
the directory. This will cause the app to re-fetch the songs, using the new stash size.
- `"max_lines"`: How many coherent lines to post at max. Less lines will be posted if the Twitter character 
limit applies. Otherwise, the bot will always try to post as many lines as specified here. This expects a number, 
such as `2`, `3` or `4`.
- `"post_source"`: Should the bot append the source of the quote to its posts? This can either be `true` (yes)
or `false` (no). If it is `true`, there will be a `- Artist, "Song Title"` below every lyrics quote.
- `"all_caps"`: Pretty self-explanatory. Should the lyric quotes be in all caps? Again, you can set this
to `true` or `false`.


 

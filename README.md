# chirpy

For the next few homework assignments, you will be incrementally building a Twitter-like service.  I've called my service Chirpy, but you should call yours something different.  Later in the semester, we'll get domain names for each group's service (e.g., chirpyposts.com).

Throughout this document, I'll be using the name Chirpy.  And yes, I know that Twitter is now called $\mathcal{X}$, but I'm just not gonna call it that.  And no, I won't rebrand Chirpy as $\mathcal{C}$.

## Assignment Goals

The goal of this assignment is to provide experiential learning in which students participate in a significant group project, working together to construct a sophisticated and complex software system.  Secondary goals include experience with the following:

* design documents;
* code documentation;
* adhering to style guidelines;
* test cases; and
* source code management, including branching and merging


# What is Chirpy?

Chirpy is a Twitter-like service in which users, who are called *chirpers*, can post short messages or *chirps*.  Chirps can contain #hashtags, which are identified as any word that begins with a # symbol.

After logging in, the main screen for Chirpy is the *timeline*, which constitutes the logged-in user's feed.  Populating the timeline are chirps from the logged-in user's *contacts*.  Contacts are fellow Chirpers.  

There is also a **search functionality**, where a Chirper can either:
1. type in the name of another Chirper and view that latter Chirper's chirps (unless that latter Chirper's profile is set to private; see note below); or 
2. search for posts that contain a given hashtag.

For example, a Chirper named `Micah1` can either search for Chirps from another Chirper, say, `DeGioia`, or for a hashtag, say, `#Georgetown`.



# Requirements

This project is purposefully left somewhat open ended, and I've tried to keep the requirements minimal.  They are:

* Your implementation must implement the logic described in the previous section (i.e., allow Chirps, searching, etc.).

* Anyone can register for an account on Chirpy.  Chirpers have a username and a password.

* After registering, a Chirper must log into the service using its correct username and password.

* A chirper's account is public by default, but can be set to private by that user, meaning that the chirper's chirps are only viewable by that chirper's contacts.

* You can specify a maximum length for Chirps, but it must be at least 128 characters.


# Extra Features

Additionally, your project should support at least one additional functionality:

* Moderation mode in which site operators can remove posts and suspend (and unsuspend) accounts.

* Support for uploading images or linking to videos in Chirps.

* Support for interacting with ChatGPT or other large language model chat bots.  (Talk to me if you are attempting this, since it might cost money, and I might be able to cover it.)

* Something clever that I haven't thought of.  This requires TA or instructor approval.


## A Brief Warning

Please don't blindly release your project to the world.  You should probably password protect it with a site-wide password.  (If you do that, you'll need to tell us what it is.)

Completely unmoderated social networks will quickly get overrun by horrible (and potentially illegal) content.  Running a real social network is expensive and difficult, and this course project isn't going to prepare you for that.


# Overall Structure

Chirpy is currently divided into three main packages:

* [data access object (DAO)](/src/main/java/edu/georgetown/dao/): contains classes that represent the various objects for Chirpy.  These will likely include Chirper (a user), Chirp (a post), AuthenticationCookie (used to maintain state after logging in), among others.

* [business logic layer (BLL)](/src/main/java/edu/georgetown/bll/): contains classes that implement the core logic of Chirpy.  This will likely include a UserRegistrationService, a UserAuthenticationService, a ChirpService, and a SearchService.

* [display logic (DL)](/src/main/java/edu/georgetown/dl/): contains classes that produce HTML for rendering the Chirpy website.

I have written skeleton code for each of these components.  I highly recommend that you start from this code, but you are free to deviate from it if you wish.



## Some helpful curl commands

To create a user:
```
curl -H 'Content-Type: application/json' -d '{"username" : "joe", "password" : "security123", "name" : "Joe Smoe" }' https://SITE/newuser
```

To list users:
```
curl -H 'Content-Type: application/json' https://SITE/listusers
```



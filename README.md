# Login-Portal
A Kotlin based login portal which is halfway done, and is currently in progress to be completed. Till now, a proper structure for login and logout is created, Room database is currently being used to save signed up users, then the database is accessed and the entry is checked in the login process. If the entry is present and email and password matches, the user is then logged in. Session login is also implemented, proper error handling is done, side navigation drawer is also locked on login and signup page, and stack is popped after logout so that user cannot go back to home screen after pressing back. One more feature which I have added is, all the users that are added to database are shown in a recycleview, and the user that is currently logged in is shown and the box for logged in user is turned green and box is red if the user is logged out.

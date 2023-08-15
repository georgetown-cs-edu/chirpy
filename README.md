# chirpy

A webservice that's totally not Twitter.

It's divided into business logic layer (BLL) and data access object (dao) components.


## Some helpful curl commands

To create a user:
```
curl -H 'Content-Type: application/json' -d '{"username" : "joe", "password" : "security123", "name" : "Joe Smoe" }' https://SITE/newuser
```

To list users:
```
curl -H 'Content-Type: application/json' https://SITE/listusers
```



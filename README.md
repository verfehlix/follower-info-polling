# follower-info-polling
java spring boot service that regularly polls a twitter profile &amp; saves the follower count & names of the followers

## How to use

- Configure ```twitter.userName```, ```spring.datasource.url```, ```spring.datasource.username``` and ```pring.datasource.password```

  - either in ```resources/application.properties``` 
  
  - or as command line parameter (e.g. ```--twitter.userName=it_meirl_bot```)

- Configure Twitter API access - create a ```twitter4j.properties``` file in the root directory and add the following properties:
```
oauth.consumerKey = [...]
oauth.consumerSecret = [...]
oauth.accessToken = [...]
oauth.accessTokenSecret = [...]
```

# Music Finder #


## service 
```
 mvn package
 mvn exec:exec

```
Starts a service on `localhost:8081`

to verify that the in app cache does cache requests set an environment variable to control the cache timeout, e.g.
```
CACHE_EXPIRY_SECONDS=5 mvn exec:exec
```
Cache default is 120 seconds.

 ### docker
 ```
docker build . -t musicfinder
docker run -p 8081:8081 musicfinder

```
or with cache control

```
docker run -e CACHE_EXPIRY_SECONDS=17 -p 8081:8081 musicfinder
```


To make the logs a bit more quiet set the `LOG_LEVEL` env variable to `info` or `warn`

 ##  api
 
(POST) http://localhost:8081/api/search  
(GET)  http://localhost:8081/api/artist/{mbId}  
(GET)  http://localhost:8081/health  
(GET)  http://localhost:8081/status    
     
To search for an artist POST a json body to `localhost:8081/api/search` , i.e.
 
 ```
{
	"artist": "al"
}

``` 

The search will return a list of results orderd by their individual `score` 

 ```
{
    "artists": [
        {
            "name": "Al Green",
            "id": "fb7272ba-f130-4f0a-934d-6eeea4c18c9a",
            "gender": "male",
            "score": 100,
            "url": "http://localhost:8081/api/artist/fb7272ba-f130-4f0a-934d-6eeea4c18c9a"
        },
        {
            "name": "Al Jarreau",
            "id": "3e54ba8b-f4cc-4192-9813-890d570e2b7a",
            "gender": "male",
            "score": 96,
            "url": "http://localhost:8081/api/artist/3e54ba8b-f4cc-4192-9813-890d570e2b7a"
        }, ...
      "count": 2061
]

```
 
 Via the  `url` property you can get the full artist details
 
 ```
{
    "name": "Al Green",
    "id": "fb7272ba-f130-4f0a-934d-6eeea4c18c9a",
    "gender": "Male",
    "bio": {
        "profile": "Soul/gospel singer and songwriter, born 13 April 1946 in Forrest City, Arkansas, USA.\r\n\r\nHe started his career as [b]Al Greene[/b] but has used the name Al Green since the late 1960s. Al Green has also been a pastor at Memphis' [i]Full Gospel Tabernacle[/i] since 1976.\r\n\r\nInducted into Rock And Roll Hall of Fame in 1995 (Performer).\r\nInducted into Songwriters Hall of Fame in 2004.\r\n",
        "releases": null,
        "uri": "https://www.discogs.com/artist/25261-Al-Green",
    },
    "albums": [
        {
            "id": "2b1c347c-2caf-305a-8675-f035f54e48d7",
            "title": "Back Up Train",
            "released": "1967",
            "images": [
                "http://coverartarchive.org/release/978c7b10-e8ab-4142-9855-fdab9a017d97/6636223681.jpg"
            ]
        }, ... 
      ]
}  
    
```

 
 


## Process / tradeoffs etc
As a starting point these are some of the design decisions along the way. 

Most importantly is that I generally try to optimise for development speed by keeping code as simple easy to adapt. I introduce complexity only when there is a need for it and only where it is needed .
Over time, a well-structured, easily modified application with low technical debt will outperform any complex solutions.

So you will find shortcuts, but for a real solution, with proper benchmarking and monitoring, the necessary adjustments will be easy to make.

### Benchmarking and monitoring
I'd add [Prometheus](https://github.com/prometheus/client_java) metrics on requests and general jvm performance, haven't had time to add that.

### Embedded jetty
The rational decision might have been spring-boot, but I could only see a need for a small number of components in the ecosystem (Cache, RestTemplate) and did not see the need to bring in the entire behemoth for just those. Also, I haven't built a 'clean' REST service in java in years, so I thought it would be fun to get stuck into the details.

A benefit of building from scratch is that the solution is more explicit; nothing is happening automatically behind the scenes.
As for performance; spring boot is also using embedded Jetty / tomcat so comparable configuration would be needed.  First optimisation would be to tweak the Thread pool to be adapted to the underlying OS and how much that can handle, but there are probably more, I havent read through the entire Jetty documentation.


### Logging
For a production environment I would add  slf4j / logback or similar but that would depend on the integration with the log management system (ELK / Splunk / ... ) in place and its connectors.
Simple apache-commons console logging, without formatting used here.

### Environment variables  
You might notice that there are no environment parameters or configuration other than cache control. For a production environment I'd also tweak at least 
* jetty server thread pool
* http client cache parameters
* Thread pool executor for the async handling of requests to coverArt 

for starters...

I prefer using env variables to system properties as it makes the startup script more generic. 

### Cache  
For a real production solution eh-cache, redis or similar solution would be used, but that depends on whether it should be distributed or not and the overall production architecture of the application.
Replacing the implementation behind the wrapper should be simple. (Even annotating for Spring-cache)

The need for httpCachingClient is almost invalidated by the implementation of the MusicBrainz API's, as it uses query params and is not sending any cache instructions, and I could not find any support for requesting that.
but the artwork and discogs clients do, so it catches a few requests at least (Still, there are far too many CACHE_MISS responses for my liking. The settings probably need a bit of tweaking to be optimal, but that requires a deeper dive into the client or other clients). 
The intention is there, there probably are better suited implementations or missing configurations, but with more research they can be easily dropped in.


### performance/ resilience etc  
While the application needs to perform on its own, it will probably be a part of a distributed system and need load balancing, so rather than build in complexity in the application this should make use of 
the orchestration platform (kubernetes, ECS, GKE, etc)  capabilities. Lifecycle events, loadbalancing, reverse-proxy, tls termination etc should all happen outside of the application. 
As such, there need to be criterias in the `Lifecycle` endpoints that determine if the application is healthy or not to instruct orchestration framework that they should  be recycled.

A prometheus Metrics enpoint would also be nice, but timeframes...

### choice of additional source  
Why did I choose discogs for additional data ? 
 - It had documentation in the spec and I preferred spending time on jetty / httpCaching / Async requests than reading more docs. (MusicBrainz docs killed my enthusiasm a bit)

### Async handling

I wanted to play with a comparable solution to `Promise.all` in node.js, as I haven't done that in Java before. The Fork-Join pool works without tweaking most of the time, but it has gotten in a twist on one or two occasions.
I'd monitor and load test that in a production like environment and make any necessary adjustments. Paging results would reduce the need for it, which is an attractive solution, as MultiThreading adds complexity that is hard to consistently replicate the behaviour of.

The ForkJoin pool also crashes the multi-stage docker build in the test phase, I confess to disabling the tests rather than going looking for a setting that works or mocking the tests. Had more interesting things to look at.
 
### testing
The unit tests are really integration tests, but there is very little logic in the application itself that needs testing. In a live situation it would be a team decision to choose to keep it this way or introduce mocking.
There are no tests for the Discogs client, becuase there is no logic there. There are other test that use HttpClients, but I also trust that its tested on its own.

### Docker
I wanted to make sure the application was java 10 compatible while not polluting the local environment too much. Running it in docker is a good option that also prepares for horizontal scaling.
Multi-stage builds are a good option to make it easily pluggable in any pipeline, but for a production pipeline the tests need to be enabled.


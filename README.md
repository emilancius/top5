
# Top 5  
  
Service provides capability to search for an artist(s) by name in iTunes.  
Single artist can be saved as a favourite to the user so user can fetch it's top 5 albums.  
  
Service API provides 3 endpoints.

## Endpoints
### Artist search endpoint
**HTTP GET /artists**

Request headers:
none

Query parameters:
|Query parameter|Is required  | Description |
|--|--|--|
| name | yes | Artist name |
| limit| no | Results limit in response |

Response codes:
|Response code| Description |
|--|--|
| 200 | Artists returned (result may be empty) |
| 404 | Bad request (query parameter "name" is missing) |
| 500 | Internal server error |

Example response body, given HTTP GET */artists?name=Rammstein* request was made:

    [
	    {
		    "id":408932,
		    "name":"Rammstein",
		    "genre":"Metal"
	    }
    ]
    
### Set user's favourite artist endpoint
**HTTP POST /artists**

Request headers:
none

Query parameters:
none

Response codes:
|Response code| Description |
|--|--|
| 201 | User's favourite artist is created / updated |
| 404 | Bad request (no request body provided) |
| 500 | Internal server error |


### User favourite artist's top 5 albums endpoint
**HTTP GET /artists/favourite/albums**

Request headers:
|Request header| Description |
|--|--|
| userId | User's ID |


Query parameters:
none

Response codes:
|Response code| Description |
|--|--|
| 200 | User favourite artist's top 5 albums returned |
| 400 | Bad request (request header "userId" is missing) |
| 404 | Favourite artist for user is not set |
| 500 | Internal server error |

Example response body, given *HTTP GET /artists/favourite/albums* request was made:

    [
	    {
		    "id":1390562159,
		    "artistId":408932,
		    "name":"Sehnsucht",
		    "genre":"Metal",
		    "released":"1997-08-22T07:00:00Z"
	    },
	    {
		    "id":1440734479,
		    "artistId":408932,
		    "name":"Reise, Reise",
		    "genre":"Metal",
		    "released":"2004-09-27T07:00:00Z"
	    },
		{
			"id":1440770702,
			"artistId":408932,
			"name":"Mutter",
			"genre":"Metal",
			"released":"2001-04-02T07:00:00Z"
		},
		{
			"id":1456789831,
			"artistId":408932,
			"name":"RAMMSTEIN",
			"genre":"Metal",
			"released":"2019-05-17T07:00:00Z"
		},
		{
			"id":1440657388,
			"artistId":408932,
			"name":"Herzeleid",
			"genre":"Metal",
			"released":"1995-09-24T07:00:00Z"
		}
	]


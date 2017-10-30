# adstxt-results

A library to pull down the latest file from [https://github.com/InteractiveAdvertisingBureau/adstxtcrawler/tree/adstxt-results](https://github.com/InteractiveAdvertisingBureau/adstxtcrawler/tree/adstxt-results).

## Background

Per Bjorke, Product Manager for Ad Traffic Quality at Google, puts out a list of domains which according to Google host Ads.txt files. He does this by adding a new file each week to a branch of the IAB's adstxtcrawler GitHub repo at [https://github.com/InteractiveAdvertisingBureau/adstxtcrawler/tree/adstxt-results](https://github.com/InteractiveAdvertisingBureau/adstxtcrawler/tree/adstxt-results).

The files are added with a naming convention which contains the date of the file. This site is the web interface to a library which was designed to look in the `adstxt-results` branch of the repo and extract the most recent file. Also, links a above let you gatyher the data from the file (as a text list or json) or as the original file.

There are four routes. The first returns the list of domains as a simple text list. The second is a route to the original file. The third returns the data from the file in JSON format. The last returns in JSON the name of the file and the number of domains. This last route is useful for application which may want to track the data and only ask for an update if one is available.

## API
    
### GET /api/list

### GET /api/file
       
### GET /api/json

### GET /api/info


## Example Site

A version of this project is running at [https://adstxt-results.herokuapp.com/](https://adstxt-results.herokuapp.com/).



## License

Copyright © 2017 Brad Lucas

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

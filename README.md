# adstxt-results

A library to pull down the latest file from https://github.com/InteractiveAdvertisingBureau/adstxtcrawler/tree/adstxt-results

## Background

Per Bjorke, Product Manager for Ad Traffic Quality at Google, puts out a list of domains which according to Google host Ads.txt files.

He does this by adding a new file each week to a branch of the IAB's adstxtcrawler GitHub repo at https://github.com/InteractiveAdvertisingBureau/adstxtcrawler/tree/adstxt-results.

The files have started to be added with a naming convention which contains the date of the file.

This project is designed to provide a library which will when exercised grab the latest file from this branch.

## Version 1

Run the library from the command line with `lein run` or build an uberjar and run without arguments. You will notice the branch downloaded to a zip file called `adstxt-results.zip` and it's latest file extracted. At the time of this writing the file was `adstxt_domains_2017-10-23.txt'. This will change depending on when you run your version.


## License

Copyright © 2017 Brad Lucas

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

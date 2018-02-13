#!/bin/bash
cd src/DonationAnalytics
# mvn clean package -Dmaven.test.skip=true # <- uncomment this line
java -cp ./target/donation-analytics-1.0-SNAPSHOT.jar com.khwu.analytics.Main ../../input/itcont.txt ../../input/percentile.txt ../../output/percentile_by_zip_and_year.txt

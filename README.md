# Table of Contents
1. [Summary](README.md#summary)
2. [Data Structures and Algorithms Analysis](README.md#data-structures-and-algorithms-analysis)
3. [Tests](README.md#tests)
4. [Diagrams](README.md#diagrams)

# Summary

This project is about solving [coding challeng](https://github.com/InsightDataScience/donation-analytics) from [Insight Data Engineering Program](http://insightdataengineering.com). My approach solving the problem relies heavily on Java 8 `stream`, `lambda` as well as other functional programming which are well suite for handling streaming data.

## Depedencies

* Java 8 (Required)
* Maven 3.5.2 (Required)
* lombok 1.16.18 (Added in pom.xml and will bed downloaded by Maven)
* JUnit 4.8.1 (Added in pom.xml and will be downloaded by Maven)

Java is relatively verbose comparing with other modern programming languages. [lombok](https://projectlombok.org) helps making the code compact by auto generating `Constructor`, `Getter`, `Setter`, `hashCode`, `equals` and other common methods using Java annotation. Notice that with `exclude` in `@EqualsAndHashCode`, we still have full control of the implementation of `hashCode` and `equals` method. It helps programmers focusing on most important part of the code. 

```java
@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode(exclude = "otherID")
public class Donor {
    private final String name;
    private final String zipCode;
    private final String otherID;
}
```

## How to run

The jar file for this project is checked in Github. Therefore, cloning this repository and executing `run.sh` should work just fine. In case, the binary file is corrupted or does not work on your computer. Please uncomment the second command in `run.sh` as indicated below. The `run.sh` will compile the jar file again. Or just execute `mvn clean package -Dmaven.test.skip=true` under `./src/DonationAnalytics`.

```shell
#!/bin/bash
cd src/DonationAnalytics
# mvn clean package -Dmaven.test.skip=true # <- uncomment this line
java -cp ./target/donation-analytics-1.0-SNAPSHOT.jar com.khwu.analytics.Main ../../input/itcont.txt ../../input/percentile.txt ../../output/repeat_donors.txt
```

## Project Structure

```shell
$ tree -I "target" ./src
./src
└── DonationAnalytics
    ├── DonationAnalytics.iml
    ├── pom.xml
    └── src
        ├── main
        │   ├── java
        │   │   └── com
        │   │       └── khwu
        │   │           └── analytics
        │   │               ├── Donation.java
        │   │               ├── DonationDB.java
        │   │               ├── Donor.java
        │   │               ├── Main.java
        │   │               ├── Percentile.java
        │   │               ├── Recipient.java
        │   │               ├── Statistic.java
        │   │               ├── StreamProcessor.java
        │   │               └── YearZipCode.java
        │   └── resources
        └── test
            └── java
                └── com
                    └── khwu
                        └── analytics
                            ├── DonationDBTest.java
                            ├── MainTest.java
                            ├── PercentileTest.java
                            ├── RecipientTest.java
                            └── StreamProcessorTest.java
```

# Data Structures and Algorithms Analysis

## tldr

* Average: O(log(n))
* Best: O(log(n))
* Worst: O(log^2(n))

This performance analysis is base on each piece of streaming data where `n` is amount of data already stored in memory. The bottleneck of the program is adding donation to `Percentile` which is O(log(n)). Whereas, the worst is O(log^2(n)) and is only theoretically possible. Two key analyses are discussed in detail below.

## Look up `Donor` with a given year:

### Time Complexity

* Average: O(1)
* Best: O(1)
* Worst: O(log^2(n))

### Space Complexity

* O(n)

### Key data strucuture

* HashSet
* HashMap

### Description:

For every successfully parsed data, we need to check the donor of a given donation is a repeated donor before we write to file. Therefore, we need an efficient way for quick way of looking up donors from previous year. I used `HashMap` along with `HashSet` creating lookup table like `HashMap<Integer, HashSet<Donor>>`. The `hashCode` and `equals` of `Donor` is comparing and hashing with `name` and `zipCode` so that donor with same `name` and `zipCode` would be identified as same donor effectively. Since Java 8, the HashMap has replace LinkedList with Binary Tree. This means under worst case scenario (all keys collides to same index), HashMap  still guarantees O(log(n)) performance. You can read it from [here](https://www.nagarro.com/de/perspectives/post/24/performance-improvement-for-hashmap-in-java-8). Additionally, HashSet is internally implemented by HashMap but without value and therefore guarentees O(1) performance on average but O(log(n)) in worst case.

Other quick lookups are implemented likewise (having same best and average performance but O(log(n)) in worst case since there is no addtional `HashSet` in the value of `HashMap`):

* Look up `Recipient` by its `cmteID` using `HashMap<String, Recipient>`.
* Look up `Statistic` object with a given `YearZipCode` (Not calculating the percentile) using `HashMap<YearZipCode, Statistic>`.

## Adding donation to `Percentile`:

### Time Complexity

* Average: O(log(n))
* Best: O(log(n))
* Worst: O(log(n))

### Space Complexity

* O(n)

### Key data strucuture

* PriorityQueue

### Description:

In practice, adding donations to `Percentile` is the bottleneck for this program. To minimize computational cost, I use two sets of `PriorityQueue` naming `smallTree` and `bigTree`. `smallTree` has maximum value at root; `bigTree` has minimum value at root. The kth percentile is maintained as the root of `smallTree`. First, if the incoming donation is smaller or equal to the value at the root of the `smallTree`, the value would be add to the `smallTree`. Then, if this would cause <sup>k</sup>th percentile to misplace to the `bigTree`, then the root is going to add to the `bigTree`. Similiar logic if the incoming donation is greater than the root of the `smallTree`. After adding value in the `Percentile`, the performance for retrieving <sup>k</sup>th percentile is only O(1) which is just `peek`ing the root of `smallTree`

## Benchmark

For benchmark, I downloaded 2017-2018 data files from [The Federal Election Commission](http://classic.fec.gov/finance/disclosure/ftpdet.shtml). As shown below, the size of file is approximately 1.23 GB. The program took about 15 seconds and 400 MB of memory usage to parse this data calculating its running 30th percentile. The experiment is conducted on my 2017 MacBook Pro which comes with 2.3 GHz Intel Core i5 and 8 GB of RAM.

```shell
$ ls -l
total 2583152
drwxr-xr-x@ 7 khwu  staff         224 Feb  6 23:00 by_date
-rw-r--r--@ 1 khwu  staff  1318899546 Feb  4 07:26 itcont.txt
$ ./run.sh
Used Memory before: 1 MB
=== Start parsing file ===
=== Complete parsing file ===
Used Memory after: 394 MB
Total time used: 14545 ms
```

# Tests

## Unit Tests

```shell
-------------------------------------------------------
 T E S T S
-------------------------------------------------------
Running com.khwu.analytics.RecipientTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.05 sec
Running com.khwu.analytics.DonationDBTest
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.006 sec
Running com.khwu.analytics.StreamProcessorTest
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.14 sec
Running com.khwu.analytics.PercentileTest
Tests run: 17, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.06 sec
Running com.khwu.analytics.MainTest
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.004 sec

Results :

Tests run: 27, Failures: 0, Errors: 0, Skipped: 0
```

## Integration Tests

```shell
$ ./run_tests.sh
[PASS]: test_1 repeat_donors.txt
[PASS]: test_2 repeat_donors.txt
[PASS]: test_3 repeat_donors.txt
[PASS]: test_4 repeat_donors.txt
[Sun Feb 11 18:12:22 PST 2018] 4 of 4 tests passed
```

# Diagrams

The best way to understand Object-oriented programming paradigm is looking at the UML class diagram and sequence diagram. The class diagram was generated by [astah](http://astah.net/) with slight  modifications; the sequence diagram was drawn manually.

## Class Diagram

![](./pic/class-diagram.png)

## Sequence Diagram

Sequence diagram is the sequence for each incoming valid donation data.

![](./pic/seq-diagram.png)

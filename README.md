![Github Actions](https://github.com/adilqayyum/rest-assured-project/actions/workflows/api-test.yml/badge.svg?branch=main)

# rest-assured-project
Sample framework for API automation using RestAssured with Java.

## Overview

This is a simple yet easily scalable API Automation Framework designed using RestAssured. The languages used is Java, alongside Extent Reports. The build tool used is Maven. The tests can easily be executed, modified or added without many code changes or complexities, as use of properties and API class segregation is maintained.

Languages Used: JAVA

Framework Used: JUnit

Reporting: Extent, JUnit

## Installation

To install all dependencies, run 

```console
$ mvn clean install
```

## Running tests from Terminal

```console
$ mvn test
```

## Running tests from Github Action

- While in the repository on Github, navigate to Actions.
- Under All workflows, select Run API tests.
- Click on Run Workflow button on the right, and click Run Workflow. (The variables currently are dummy, and can be later used to select between different tags or environments for execution)

![image](https://user-images.githubusercontent.com/35289892/201520193-85bb1a68-9a35-459c-8d04-84bf975f9b8b.png)

After execution, the extent report can be located under artifacts:
![image](https://user-images.githubusercontent.com/35289892/201521245-455d9b18-0a68-4534-9120-c6e1ddc530e6.png)

Inside the report, test details can be seen, alongside the Request and Response Headers and Body:
![image](https://user-images.githubusercontent.com/35289892/201521280-54d833aa-aaab-4b07-b36f-74a11f8cd69c.png)

---

## Support

Reach out to me at one of the following places!

- Linkedin at <a href="https://www.linkedin.com/in/madilqayyum/" target="_blank">`Adil Qayyum Linkedin`</a>
- Email at: adilqayyumk@gmail.com

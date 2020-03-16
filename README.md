# Stock Price

Stock Price provides stock related data of companies based on professional Quandl stock database.

## Getting Started

Check out the project

`cd stock-price-service`

##### Build the project

`./gradlew build`

##### Run the project 

`./gradlew bootRun`

## Querying close price

```
GET http://<host>/api/v2/<ticker>/closePrice?start-date=<startDate>&end-date=<endDate>
```

| Parameters  | Description | Value format | 
| -------------------| ----------- | ----- | 
| `ticker `          | Ticker symbol of requested data | Alphabet   | 
| `startDate `       | Start date of requested data   | yyyy-mm-dd | 
| `endDate`          | End date of requested data      | yyyy-mm-dd | 

##### Example

```
GET http://localhost:8080/api/v2/FB/closePrice?start-date=2018-03-01&end-date=2018-03-03
```

##### Response

```json
{
    "Prices": {
        "Ticker": "FB",
        "DateClose": [
            [
                "2018-03-02",
                "176.62"
            ],
            [
                "2018-03-01",
                "175.94"
            ]
        ]
    }
}
```

## Querying 200-day-moving average

```
GET http://<host>/api/v2/<ticker>/200dma?start-date=<startDate>
```

| Parameters  | Description | Value format | 
| -------------------| ----------- | ----- | 
| `ticker `          | Ticker symbol of requested data | Alphabet   | 
| `startDate `       | Start date of requested data   | yyyy-mm-dd | 

##### Example

```
GET http://localhost:8080/api/v2/FB/200dma?start-date=2014-05-18
```

##### Response

```json
{
    "200dma": {
        "Ticker": "FB",
        "Avg": "73.700025"
    }
}
```

## Querying a list of 200-day-moving averages

```
GET http://<host>/api/v2/multi200dma?tickers=<tickers>&start-date=<startDate>
```

| Parameters  | Description | Value format | 
| -------------------| ----------- | ----- | 
| `tickers`          | Ticker symbols of requested data| separated by comma | 
| `startDate `       | Start date of requested data.   | yyyy-mm-dd | 

##### Example

```
GET http://localhost:8080/api/v2/multi200dma?tickers=FB,GE&start-date=2012-06-01
```

##### Response

```json
{
    "200dma": [
        {
            "Ticker": "FB",
            "Avg": "25.453719500000002"
        },
        {
            "Ticker": "GE",
            "Avg": "21.389699999999998"
        }
    ]
}
```

## Exception handling

| Exception              | HTTP status code | Error code       | Description                           | 
|------------------------|------------------|------------------|---------------------------------------| 
| BadRequestException    | 400              | badRequest       | Bad request                           | 
| NotFoundException      | 404              | notFound         | Not Found Error                    | 
| WriterException        | 500              | writerError      | Internet Server Error                   | 

##### Example of Invalid Ticker

```
GET http://localhost:8080/api/v2/FBE/closePrice?start-date=2018-03-01&end-date=2018-03-03
```

##### Response

```json
{
    "timestamp": "2020-02-27T08:02:49.697+0000",
    "message": "Invalid ticker symbol",
    "details": "uri=/api/v2/FBE/closePrice"
}
```

##### Example of Invalid Start Date and End Date

```
GET http://localhost:8080/api/v2/FB/closePrice?start-date=2018-03-01&end-date=2017-03-03
```

##### Response

```json
{
    "timestamp": "2020-02-27T08:04:37.106+0000",
    "message": "Incorrect date, start_date should not exceed end_date.",
    "details": "uri=/api/v2/FB/closePrice"
}
```

## Caching

Using simple cache based on Spring Boot https://spring.io/guides/gs/caching/




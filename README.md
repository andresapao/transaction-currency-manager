# Transactions Currency Manager

## Overview

The Transactions Currency Manager is a Kotlin-based Spring Boot application designed to manage transactions and provide
currency exchange rate information.

All the rates are fetched from
an [external API](https://fiscaldata.treasury.gov/datasets/treasury-reporting-rates-exchange/treasury-reporting-rates-of-exchange),
and the application implements a retry mechanism to ensure resilience.

## Features

- **Retry Mechanism**: Ensures resilience when calling external APIs by retrying failed requests.
- **Caching**: Utilizes Caffeine Cache to store currency exchange rates for faster subsequent access.
- **Endpoints**: Provides RESTful endpoints for creating transactions, retrieving transactions with exchange rates, and
  fetching transactions by date range and individually by id.

## Endpoints

### 1. Create Transaction

**Purpose**: Save a new transaction to the database.

- **Method**: `POST`
- **Endpoint**: `/transactions`
- **Request Body**:
  ```json
  {
    "description": "string",
    "amount": 100.00,
    "creationDate": "yyyy-MM-dd"
  }
  ```
- **Response**: HTTP 201 Created with the created transaction details.

### 2. Get Single Transaction

**Purpose**: Retrieve a transaction by ID and convert its amount to a target currency.

- **Method**: `GET`
- **Endpoint**: `/transactions/{id}?targetCurrency={targetCurrency}`
- **Path Variables**:
    - `id`: Transaction ID
    - `targetCurrency`: A string containing country and currency (e.g., Brazil-Real, Canada-Dollar).
- **Response**:
  ```json
  {
    "id": 1,
    "description": "string",
    "amount": 100.00,
    "exchangeRate": 1.23,
    "convertedAmount": 123.00
  }
  ```

### 3. Get Transactions by Date Range

**Purpose**: Retrieve all transactions within a specified date range and convert their amounts to a target currency.

- **Method**: `GET`
- **Endpoint**: `/transactions?startDate={startDate}&endDate={endDate}&targetCurrency={targetCurrency}`
- **Query Parameters**:
    - `startDate`: Start date (yyyy-MM-dd)
    - `endDate`: End date (yyyy-MM-dd)
    - `targetCurrency`: A string containing country and currency (e.g., Brazil-Real, Canada-Dollar).
- **Response**: List of transactions with converted amounts.

## Technical Details

### Retry Mechanism

The application uses a retry mechanism when calling the `CurrencyGatewayImpl` to fetch exchange rates. This ensures that
transient failures in the external API do not disrupt the application.

### Caching

- **Library**: Caffeine Cache
- **Purpose**: Store currency exchange rates for 60 minutes to reduce the number of external API calls.
- **Implementation**: The `getCurrencyInfo` method in `TransactionService` checks the cache before making an API call.
  Key is a composite with transaction date and currency.
  If the data is not in the cache, it fetches the data from the API and stores it in the cache.

### Further considerations

- **Error Handling**: The application includes error handling for scenarios such as invalid input, API failures, and
  database errors.
- **Validation**: Input validation is implemented to ensure that the data being processed is correct and complete.
- **Testing**: Unit and integration tests are included to ensure the reliability and correctness of the application.
- **Circuit Breaker**: In addition to the retry mechanism, a circuit breaker pattern can be implemented to prevent
  overwhelming the external API during periods of failure.

### How to Run

#### Prerequisites

- Java 21 or higher
- Maven 3.8+
- Docker (optional, for running the database)

#### Steps

1. Clone the repository:
   ```bash
   git clone https://github.com/your-repo/transactions-currency-manager.git
   cd transactions-currency-manager
   ```
2. Build the project:
   ```bash
   ./mvnw clean package
   ```
3. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```
4. Access the application at `http://localhost:8080`.

#### Running with Docker

1. Start the database:
   ```bash
   docker-compose up -d
   ```
2. Run the application as described above.

## Notes

- External API [Documentation](https://fiscaldata.treasury.gov/api-documentation/)
- The application uses `application.yaml` for configuration. Update it as needed for your environment.

## License

This project is licensed under the MIT License.

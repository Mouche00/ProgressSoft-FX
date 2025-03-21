# Deal Management API

A RESTful API for managing deals, built with **Spring Boot**. Supports creating individual deals, bulk creation, and fetching all deals.

---

## Endpoints

### 1. Create a Single Deal
- ***POST*** `/api/v1/deals/create`
- Request Example:
    ```json
    {
        "id": "1",
        "sourceCurrency": "USD",
        "targetCurrency": "EUR",
        "amount": 100.0
    }
    ```

### 2. Create Deals in Bulk
- ***POST*** `/api/v1/deals/create/batch`
- Invalid deals are automatically filtered out, but are included in the final response for tracking purposes.
- Request Example:
    ```json
    [
        {
            "id": "2",
            "sourceCurrency": "USD",
            "targetCurrency": "EUR",
            "amount": 100.0
        },
        {
            "id": "3",
            "sourceCurrency": "EUA",
            "targetCurrency": "UFN",
            "amount": 200.0
        }
    ]
    ```

### 3. Fetch All Deals
- ***GET*** `/api/v1/deals/fetch/all`

---

## Validation Rules

### `DealRequestDTO`
- **`id`**: Required, not blank.
- **`sourceCurrency`**: Required, 3-letter uppercase code, must be ISO 4217 compliant, (e.g., `USD`).
- **`targetCurrency`**: Required, 3-letter uppercase code, must be ISO 4217 compliant.
- **`amount`**: Required, positive number.

---

## Setup

1. Clone the repo:
   ```bash
   git clone https://github.com/Mouche00/ProgressSoft-FX.git
   cd ProgressSoft-FX
   ```

2. Copy the `.env.example` file to `.env`:
   ```bash
   make setup
   # OR
   cp .env.example .env
   ```

2. Configure the newly created `.env` file:
   ```properties
    POSTGRES_DB=progresssoft-fx
    POSTGRES_USER=your-username
    POSTGRES_PASSWORD=your-password
    APP_PORT=8080
   ```

3. Build and run:
   ```bash
   make run
   # OR
   docker compose up -d --build
   ```

4. Access the API at `http://localhost:8080/api/v1/deals`.
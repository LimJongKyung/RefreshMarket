# RefreshMarket deployment

## Required environment variables

The application reads database and email credentials from environment variables.
Do not commit real values or Oracle Wallet files.

```bash
export DB_URL='jdbc:oracle:thin:@portfoliodb_low?TNS_ADMIN=/opt/refreshmarket/wallet'
export DB_USERNAME='REFRESHMARKET'
export DB_PASSWORD='your-database-password'
export MAIL_USERNAME='your-gmail-address'
export MAIL_PASSWORD='your-new-gmail-app-password'
```

Replace `portfoliodb_low` with the LOW TNS name from the downloaded wallet.
The `TNS_ADMIN` path must point to the directory containing `tnsnames.ora`.

## Wallet

1. Extract the instance wallet on the server.
2. Restrict access to the wallet directory.
3. Set `TNS_ADMIN` in `DB_URL` to that directory.
4. Never add the wallet or its ZIP file to Git.

## Build

```bash
./mvnw clean package -DskipTests
```

## Run

```bash
java -jar target/devProject-0.0.1-SNAPSHOT.jar
```

The Autonomous Database user must own all tables and sequences referenced by
the MyBatis mapper files before the application can serve database-backed pages.

## Initialize the database

Sign in to Database Actions as `REFRESHMARKET`, open SQL, and run these files in
order:

1. `database/schema.sql`
2. `database/sample-data.sql`

The sample login is only for demonstrating the portfolio:

```text
ID: portfolio
Password: portfolio1234
```

Replace or remove this account before using the project for anything beyond a
portfolio demonstration.

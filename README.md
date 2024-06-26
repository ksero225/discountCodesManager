# discountCodesManager
## Quick start

1. Clone the Repository:<br> 
`git clone https://github.com/ksero225/discountCodesManager.git`
2. Navigate to project directory:<br> 
`cd <project_directory>`
3. Build the project: <br> 
`mvn clean install`
4. If tests are failing for some reason, build the project with following command <br>
`mvn clean install -DskipTests`

## Running the application
1. Run the application: <br>
`mvn spring-boot:run`
2. Access the application:<br>
- Open a web browser <br>
- Enter the following URL "http://localhost:8080"

## Testing
1. Run tests: <br>
`mvn clean verify`

## Project structure
- src/main/com.discountCodesManager.discountCodesManager: Contains Java source files.
- src/test/java: Contains test files.
- src/main/resources: Contains application properties and configuration files.
- pom.xml: Contains project dependencies and configuration for maven.
  
## Additional information
- Ensure you have Java (version 21) and Maven installed on your system.
- This project uses Spring Boot for easy setup and development.
- Customize application properties in src/main/resources/application.properties.
- Refer to Spring Boot documentation for advanced configurations and features.
- Catalog postmanCollections contains postman collections with examples of use application.

## Additional maven information
`Java version: 21.0.1, vendor: Oracle Corporation, runtime: C:\Program Files\Java\jdk-21` <br>
`Default locale: pl_PL, platform encoding: UTF-8` <br>
`OS name: "windows 10", version: "10.0", arch: "amd64", family: "windows"`

## URL's list
1. Welcome controller URL
- `http:/localhost:8080/` GetMapping to check connection, welcome page.
2. Product controller URL's
- `/product` PostMapping for product.
- `/product/{productId}` GetMapping for product (get one).
- `/product` GetMapping for product (list products, get all).
- `/product/{productId}` PutMapping for product (full update).
- `/product/{productId}` PatchMapping for product (partial update).
- `/product/{productId}` DeleteMapping for product.
3. Promo code controller URL's
- `/promoCode` PostMapping for promo code.
- `/promoCode/{promoCode}` GetMapping for promo code (get one).
- `/promoCode` GetMapping for promo code (list promo codes, get all).
- `/promoCode/{promoCode}` PutMapping for promo code (full update).
- `/promoCode/{promoCode}` PatchMapping for promo code (partial update).
- `/promoCode/{promoCode}` DeleteMapping for promo code.
4. Purchase controller URL's
- `/discount?promoCode=` GetMapping to count discount, you need to provide existing promo code and product.
- `/purchase?promoCode=` GetMapping to simulate purchase, you need to provide existing promo code and product.

## Example bodies for DTO
```angular2html
1.ProductDto
{
    "productName": "Mouse",
    "productDescription": "Mouse for computer",
    "productPrice": 30.0,
    "productCurrency": "EUR"
}
```
2.PromoCodeDto
```angular2html
{
    "promoCode": "123A",
    "promoCodeExpirationDate": "2025-06-06",
    "promoCodeCurrency": "EUR",
    "promoCodeDiscountAmount": 10.5,
    "promoCodeAllowedUsagesNumber": 10
}
```
3.Example purchase simulation return PurchaseDto
```angular2html
{
"purchaseId": 1,
"purchaseDate": "2024-05-15",
"purchaseProductBasicPrice": 30.00,
"purchaseDiscountApplied": 10.00,
"purchaseFinalPrice": 20.00,
"product": {
    "productId": 1,
    "productName": "Mouse",
    "productDescription": "Mouse for computer",
    "productPrice": 30.00,
    "productCurrency": "EUR"
    }
}
```
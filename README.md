# discountCodesManager
## Quick start

1. Clone the Repository:<br> 
`git clone https://github.com/ksero225/discountCodesManager.git`
2. Navigate to project directory:<br> 
`cd <project_directory>`
3. Build the project: <br> 
`mvn clean install`

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
- `http:/localhost:8080/` Welcome page.
- `http:/localhost:8080/product` PostMapping for product.
- `http:/localhost:8080/product/{productId}` GetMapping for product (get one).
- `http:/localhost:8080/product` GetMapping for product (list products, get all).
- `http:/localhost:8080/product/{productId}` PutMapping for product (full update).
- `http:/localhost:8080/product/{productId}` PatchMapping for product (partial update).
- `http:/localhost:8080/product/{productId}` DeleteMapping for product.
3. Promo code controller URL's
- `http:/localhost:8080/promoCode` PostMapping for promo code.
- `http:/localhost:8080/promoCode/{promoCode}` GetMapping for promo code (get one).
- `http:/localhost:8080/promoCode` GetMapping for promo code (list promo codes, get all).
- `http:/localhost:8080/promoCode/{promoCode}` PutMapping for promo code (full update).
- `http:/localhost:8080/promoCode/{promoCode}` PatchMapping for promo code (partial update).
- `http:/localhost:8080/promoCode/{promoCode}` DeleteMapping for promo code.
4. Purchase controller URL's
- `http:/localhost:8080/discount?promoCode=` GetMapping to count discount, you need to provide existing promo code and product.
- `http:/localhost:8080/purchase?promoCode=` GetMapping to simulate purchase, you need to provide existing promo code and product.
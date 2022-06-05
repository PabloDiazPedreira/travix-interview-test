**Travix - Interview Test - Pablo Diaz Pedreira**

**Notes and comments for the test**

- Updated SpringBoot version to: 2.7.0
- Added Spring Actuator for quick health checks
- Added Spring WebFlux for reactive streams
- Added Cucumber for integration testing
- Took "domain" as the package for message definition. Classes necessary for web messaging will be added here.
- Added javax.constrains notations in domain package for validations
- Included in the project is a tiny Postman collection used for test the application. The call is bound to fail because the providers APIs do not actually exist. I use it to test but using fake data on modified code


**Assumptions**

- All the input values will are considered to be mandatory
- The external API providers, CrazyAir and ToughJet, are assumed to always return clean, correct data
- The external API providers will return a list of elements with the structure presented in the README file. This will make them mirror the behaviour of BusyFlights API
- The external API providers URLs are "http://crazyair.co.uk/flights" for CrazyFlights, and "http://toughjet.co.uk/flights" for ToughJet
- Because the instructions have defined ISO_LOCAL_DATE as date input and output, the time precision for the flights date checks will be the day. Because it will be impossible to know if the user can take the return flight the same day, if departure date and return date are the same, it will be considered invalid request.
- Discount price for ToughJet provider will be applied on the final fare price before applying the taxes price
- For ToughJet, the format upon discount and taxes are returned would be as decimals over the unit (e.g. 1.15, 1.07)
- The timezone of the times returned by ToughJet provider is "Europe/London"


**Improvements and things pending to do**
- I tried to develop the integration tests using Cucumber, but run out of time. That feature is half done with only static steps for a single scenario. Also need to mock the calls to provider APIs, using WireMock for example.
- Exception and error handling can be improved. For example, creating custom exceptions. No complex error system for exceptions is created
- The test's data is done in a very rough way. Refactor to make it more scalable in future scenarios
- Didn't use Reactor, although I mentioned the Spring WebFlux earlier.
- The IATA codes are only validated using a simple 3 letter pattern, not a proper list

**Travix - Interview Test - Pablo Diaz Pedreira**

**Notes and comments for the test**

- Updated SpringBoot version to latest: 2.7.0
- Added Spring Actuator for quick health checks
- Added Spring WebFlux for reactive streams
- Took ¨domain¨ as message definition package
- Added javax.constrains notations inn domain package for validations


**Assumptions**
- Because the system only has ISO_LOCAL_DATE as date input and output, the time precision for the flights date checks will be the day. Because it will be impossible to know if the user can take the return flight the same day, if departure date and return date are the same, it will be considered invalid request.
- No complex error system for exceptions is created
- Used generic exceptions. Room for improvement.
- Discount price for Tough Jet will be applied onn te before taxes price.

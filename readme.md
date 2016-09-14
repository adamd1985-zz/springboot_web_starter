# Authentication POC 

This is a developer "diary" with minor observations on what is required.


## Webjars

Springboot detects webjars with the resource resolver: *webjars-locator*.
reference the webjar library as follows: *<script src="/webjars/WEBJAR_LIBRARY/WEBJAR_LIBRARY_FILE"></script>*

Read more here: http://www.webjars.org/documentation#springmvc

## Basic Security

The moment you add: *spring-boot-starter-security* default _HTTP basic security_ will be implemented.
This will always have a default user called: *"user"* with a random password at application startup or you can set: *"security.user.password"* setting in your application.properties/yaml.

Use *WebSecurityConfigurerAdapter* to override the security mappings within the spring application.

Note the headers _"X-Requested-With" = 'XMLHttpRequest'_, this can be used to circumvent the standard browser Login dialog. Spring Security responds  not sending a _"WWW-Authenticate"_ header in a 401 response.
Angular used to do the above by default.

### Security with angular

Angular requires X-CSRF cookie to communicate across domains to a backend. Spring need to send a cookie whenever angular does a request, it has a *CsrfTokenRepository* class to deal with this.
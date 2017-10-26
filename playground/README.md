# Playground area


## Add to pom.xml for Persistence Support
```
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>
		<dependency>
			<groupId>com.h2database</groupId>
			<artifactId>h2</artifactId>
			<scope>runtime</scope>
		</dependency>
```

## Replace contents of application.yml in src/main/resources for h2-console support

```
spring:
  h2:
    console:
      enabled: true
      path: '/h2-console'
  datasource:
    url: jdbc:h2:mem:spring-app;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
    platform: h2
    username: sa
    password:
    driverClassName: org.h2.Driver
```


## Encountered Issues on Day 1

Solutions to all issues from day


#### 1. @JsonIgnore, @JsonBackReference and the @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) Problem from entities

Truth be said, I've looked up all my own code samples and project work with JPA and everywhere we used entites directly we simply just added @JsonIgnore on the "Many" Side


#### 2. Issue Regarding FetchType.LAZY and automatic loading of entites

This has to do with a relativly new property `spring.jpa.open-in-view: false` which causes an EntityManeger 'session' to be kept open for VIEW Requests, this means that only when someone interacts with the App via HTTP, the objects marked as lazy will load automatically on demand!

In my other sample from snippets/jpa we simply use a command-line-runner, no HTTP Request/Response!

#### 3. Issue Regarding

### Day 2.

### TODO Remaing from Day 1

1. show usecase of entity annotations like @PreRemove, @PostPersist and so forth
2. show update use-case for transaction, managed vs unmanaged entity

```
    @PostMapping("/{authorId}")
    @Transactional
    // Try the same code without the @Transactional annotation
    public Author updateBioInTransaction(@PathVariable("authorId") long authorId, @RequestParam("bio") String newBio) {
        Author author = authorRepo.findOne(authorId);
        author.setBio(newBio);
        return author;
    }

    @GetMapping("/{authorId}/books")
    // for this example set spring.jpa.open-in-view: false in your application management
    // test with @Transactional annotation and without
    public List<Book> getBooksByAuthor(@PathVariable("authorId") long authorId){

        List<Book> books =  authorRepo.findOne(authorId).getBooks();
        // this is how the proxy actually works, you can call get, it will return a proxied List, 
        // any future call on that list or its elements will load the list, NOT the call to getBooks() itsself
        books.size();
        return  books;
    }

```    


### Part 1, Security ( we'll push testing to the afternoon so we can test both with and without security )

Part 1, be amazed an win an awesome prize!

After, demo we're gonna need these snippets ( so you don't have to type it )


##### Snippet for Security Config, after demo
```

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // allow everything on swagger, the other ones are just resources swagger loads, nothing 'dangerous here'
                .antMatchers("/swagger-ui.html", "/webjars/**", "/swagger-resources/**", "/v2/**").permitAll()
                // Enable Http Basic
                .and().httpBasic()
                // Disable Form Login and CSRF ( just so you can test your Calls! )
                .and().formLogin().disable()
                .csrf().disable();

        // Custom Config follows ! we'll write this
        String[] API_PATHS = {"/api/**"};

    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // Tell Spring to Use in-memmory user database with 2 pre-set users, since we don't have a User Entity/Table yet!
        auth.inMemoryAuthentication()
                .withUser("user").password("user").roles("USER").and()
                .withUser("admin").password("admin").roles("ADMIN");
    }
}

```

For ant pattern, a pretty good sample explained in first answer here: https://stackoverflow.com/questions/18211196/confusion-with-ant-pattern-syntax-and-possible-variations


### Snippets to copy/paste for testing

First, assertJ for fluid asserts

Add to pom.xml!
```
        <!-- https://mvnrepository.com/artifact/org.assertj/assertj-core -->
        <dependency>
            <groupId>org.assertj</groupId>
            <artifactId>assertj-core</artifactId>
            <version>3.8.0</version>
            <scope>test</scope>
        </dependency>
```

#### Sample Test for MVC Testing

```
    @RunWith(SpringRunner.class)
    @Import(YourSecurityConfig.class) // <- change this
    @WebMvcTest(value = ControllerName.class) // <- change this
    public class ControllerNameTest { // <- change this

        @Autowired
        private MockMvc mockMvc;


        @Test
        public void testAMethod(){
          // <- your code here
        }

    }
```
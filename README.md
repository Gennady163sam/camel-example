# Apache camel example

This example shows how easy it is to use enterprise integration patterns in your projects. 
Application reads file from peopleStorage directory and save it in the database. 

## Getting Started

First of all you need to download a project.
All application properties are kept in app.properties file. You can change them, when necessary.

```
mvn camel:run
```

### Prerequisites

For correct work of the application, you need to install this system:

* [Maven](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html)
* [ActiveMQ](http://activemq.apache.org/getting-started.html#GettingStarted-InstallationProcedureforWindows)
* [PostgreSQL](https://www.postgresql.org/download/) or you can use your database (need change jdbc driver)

### Installing

First of all, you need to start your database.
For example:
```
su postgres -c 'pg_ctl start -D /usr/local/pgsql/data -l serverlog'
```

Now, you need to start activemq server

```
cd [activemq_install_dir]/bin
./activemq start
```

Configure your application with properties in app.properties file

```
   mail.use=true                    //whether to send mail
   smtp.server=smtp.gmail.com       //address smtp server (google by default)
   smtp.port=465                    //port smtp server
   mail.username=username@gmail.com //your username for autorization smtp server
   mail.password=yourpassword       // your password for autorization smtp server
   mail.from=addressfrom@gmail.com  // email address, which will be sending message 
   mail.to=addressto@gmail.com      // email address, which will receive messages
   concentration=20                 // concentration of poison for people
   activemq.url=localhost           // activemq host
   activemq.port=61616              // activemq port
   db.host=localhost                // database host
   db.port=5432                     // database port
   db.username=postgres             // database username
   db.password=1234                 // database password
   rest.host=localhost              // host, which will be located your REST-api
   rest.port=8089                   // port your REST-api
```

## Running the tests

For run test:

```
mvn install
```

### And coding style tests

You can extends *CamelSpringTestSupport* and override *createApplicationContext* method.
Specify directory with your camel context.

```
return new ClassPathXmlApplicationContext("/camel/camel-context.xml");
```

If you want remove part of your routes, you can remove its from route definitions.
For replace application properties, you can override *useOverridePropertiesWithPropertiesComponent*
method and put properties, which you want replace.

```
    @Override
    protected Properties useOverridePropertiesWithPropertiesComponent() {
        Properties properties = new Properties();
        properties.put("generator.use", "false");
        return properties;
    }
```


## Built With

* [Apache Camel](http://camel.apache.org) - Enterprise Integration Patterns framework used
* [Maven](https://maven.apache.org/) - Dependency Management
* [ActiveMQ](http://activemq.apache.org/) - Used to messaging
* [PostgreSQL](https://www.postgresql.org/) - Used to save information about people

## Contributing

Please read [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/your/project/tags). 

## Authors

* **Gennady Savinov** - *Initial work* - [Gennady163sam](https://gist.github.com/Gennady163sam)

See also the list of [contributors](https://github.com/your/project/contributors) who participated in this project.




# Purpose of this Fork

The updates in this fork run and test the Stormpath Shiro Web Sample with Arquillian.
An arbitrary sample user is created before all tests, and this user is deleted after all tests.

## Configuing API Keys

As with any Stormpath app, API Keys and application HREF must be obtained using your own Stormpath account.

The API Key is something you should keep private, and we ended up storing ours in the settings.xml file
as one way to avoid putting it in the git repository.

Modify your `~/.m2/settings.xml` file to provide the following information to the tests.

         <profiles>
            <profile>
              <id>sample-web-sample</id>
              <activation>
                 <activeByDefault>true</activeByDefault>
              </activation>
              <properties>
                    <arq.rest.username>anyoldusername</arq.rest.username>
                    <arq.rest.password>FADFFFDASF2r293479879ljlkajsfdasfasdfASF</arq.rest.password>
                    <stormpath.properties>${user.home}/.stormpath/apiKey.properties</stormpath.properties>
                    <stormpath.apikey.id>YOUR_API_KEY</stormpath.apikey.id>
                    <stormpath.apikey.secret>YOUR_API_KEY_SECRET</stormpath.apikey.secret>
                    <stormpath.application.href>https://api.stormpath.com/v1/applications/YOUR_APP_ID</stormpath.application.href>
              </properties>
            </profile>
         </profiles>

The `arq.rest.username` and `arq.rest.password` are used as the username/password to create in stormpath,
and these can be anything you like as it is only used for the duration of the tests.

## Running the tests

        mvn clean test

## Notes on tests

We would have preferred to use `htmlunit` for testing the JSP forms, however we encountered a runtime
error of some kind with the WebClient class is loaded, and this was more difficult to debug than
just switching to a simpler client with fewer dependencies.

# Stormpath Shiro Web Sample

Fork of the Apache Shiro Web sample application that uses Stormpath for User Management. 
This sample application uses the [Apache Shiro plugin for Stormpath](https://github.com/stormpath/stormpath-shiro) 
to demonstrate how to integrate Apache Shiro and Stormpath.

Stormpath is a User Management API that reduces development time with instant-on, scalable user infrastructure. 
Stormpath's intuitive API and expert support make it easy for developers to authenticate, manage, and secure users 
and roles in any application. The `stormpath-shiro` plugin allows a [Shiro](http://shiro.apache.org/)-enabled 
application to use [Stormpath](http://www.stormpath.com) for all authentication and access control needs.

## Documentation

Stormpath offers usage documentation and support for the Apache Shiro Plugin for Stormpath [in the wiki](https://github.com/stormpath/stormpath-shiro/wiki). Please email support@stormpath.com with any errors or issues with the documentation.

## Links

Below are some resources you might find useful!
- [The Apache Shiro Plugin for Stormpath](https://github.com/stormpath/stormpath-shiro)
- [User Permissions with Apache Shiro and Stormpath](https://stormpath.com/blog/user-permissions-apache-shiro-and-stormpath/)

**Stormpath Java Support**
- [Stormpath API Docs for Java](https://docs.stormpath.com/java/apidocs/)
- [Stormpath Java Product Guide](https://docs.stormpath.com/java/product-guide/)
- [Stormpath Java SDK](https://github.com/stormpath/stormpath-sdk-java)

## Contributing

Contributions, bug reports and issues are very welcome. Stormpath regularly maintains this repository, and are quick to review pull requests and accept changes!

You can make your own contributions by forking the develop branch of this
repository, making your changes, and issuing pull request on the develop branch.

## Build Instructions ##

This project requires Maven 3 to build. Run the following from a command prompt:

`mvn clean compile`

Release changes are viewable in the [change log](changelog.md)

## Copyright ##

Copyright &copy; 2013-2015 Stormpath, Inc. and contributors.

This project is open-source via the [Apache 2.0 License](http://www.apache.org/licenses/LICENSE-2.0).

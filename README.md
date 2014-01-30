easy-mail
=========
[![Build Status](https://travis-ci.org/JayStGelais/easy-mail.png?branch=master)](https://travis-ci.org/JayStGelais/easy-mail)

The **easy-mail** API provides a simple wrapper around the JavaMail APi and utilities for transforming HTML with CSS to
provide greater cross client support for html email message rendering.

**easy-mail** accepts HTML input and performs the following transformations on it before using it for the mail message
body.

1. Calculate effective styles of all elements ad write them to their style attribute.
2. Remove style declarations form head of document.
3. Remove class attributes form all elements.
4. Embed all images referneces from the HTML document as relative URLs. *(Fully qualified URLS will continue to
link to their remote image resource)*

For tips on writing effective HTML input for **easy-mail** please refer to this wiki page:
[Tips for Writing HTML Templates for Input to easy mail](https://github.com/JayStGelais/easy-mail/wiki/Tips-for-Writing-HTML-Templates-for-Input-to-easy-mail)

# Getting easy-mail
The latest **easy-mail** release can be downloaded directly from [bintray](https://bintray.com/jaystgelais/main/easy-mail/0.2.0/files)
or you can configure your build to fetch them like this (gradle example):

    repositories {
    	jcenter()
    }

    dependencies {
        compile group: 'com.github.jaystgelais', name: 'easy-mail', version: '0.2.0'
    }


# Using easy-mail
Suppose the following resources are available on your classpath:

    /mailcontent
       |--+ MyMailMessage.html
       |--+ /css
               |--+ mail-style.css
       |--+ /images
               |--+ company-logo.jpeg

The following code would send an email based on /mailcontent/MyMailMessage.html and it's linked content.

    import javax.mail.Session;
    import com.github.jaystgelais.easymail.EmailMessage;
    import com.github.jaystgelais.easymail.HtmlContentProvider;
    import com.github.jaystgelais.easymail.URLHtmlContentProvider;
    ...
    Session mailSession = <setup mail session>
    HtmlContentProvider contentProvider
        = new URLHtmlContentProvider(getClass().getResource("./EmbeddedImageTest.html"));
    EmailMessage message = new EmailMessage.Builder("myemail@mydomain.com",
            "Hey check out easy-mail!", contentProvider)
        .addTo("recipient@otherdomain.com")
        .build();
    message.send(mailSession);



# Extending easy-mail
The main extension point for **easy-mail** is the *HtmlContetProvider* interface. Implementing this interface
is you entry point to provided HTML content form your favorite HTML template framework.

# easy-mail Versions
This project's version scheme adheres to the guidelines of [Semantic Vesrioning 2.0.0](http://semver.org/spec/v2.0.0.html).

These guidelines state that, given a version number **MAJOR.MINOR.PATCH**, increment the:

1. **MAJOR** version when you make incompatible API changes,
2. **MINOR** version when you add functionality in a backwards-compatible manner, and
3. **PATCH** version when you make backwards-compatible bug fixes.

Additional labels for pre-release and build metadata are available as extensions to the MAJOR.MINOR.PATCH format.

The only exception to this scheme is for version before 1.0.0. 0.x.x versions are considered experimental and allow for
incompatible API changes between MINOR versions. Once version 1.0.0 is released, the rules above apply to future
versions.

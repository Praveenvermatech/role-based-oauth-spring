# role-based-oauth2
In this article, we will be securing REST APIs with role based OAUTH2 implementation. To do so, we will be creating two custom roles as ADMIN and USER and we will use &lt;code>@secured&lt;/code> annotation provided by spring security to secure our controller methods based on role. This article was posted by [Praveen](https://github.com/Praveenvermatech/role-based-oauth-spring). Below are some other articles on OAUTH2

# How to generate oauth token:
First we need to create # POST call in postman:
choose Authrozation type and fill the username & password 
username:"hcl-client";
password:"abc123";
grant_type:"password";

then we get the auth token

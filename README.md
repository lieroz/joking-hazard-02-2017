# JTest
## API DOCS
### signUp
#### Адрес
<code>/api/user/signup</code>
#### Запрос
<code>{
"passHash": "111", 
"userLogin":"211", 
"userMail":"1@1.ru"
}</code>
#### Ответ
<code>
{
"signUpResult": false,
"errorMsg": "Login is occupied"   
}
</code>
#### Адрес 
<code>/api/user/login</code>
#### Запрос
<code> {"passHash": "111", "userLogin":"211"} </code>
#### Ответ
<code> {
"loginResult": true
} </code>
#### Адрес 
<code>/api/user/changeMail</code>
#### Запрос
<code> {"strCont":"1@2.ru"} </code>
#### Ответ
<code>{
"result": true,
"errorMsg": "ok"
} </code>
#### Адрес 
<code>/api/user/changePass</code>
#### Запрос
<code> {"oldPassHash":"111", "newPassHash":"122"} </code>
#### Ответ
<code> {
"result": false,
"errorMsg": "Invalid password"
}</code>
#### Адрес 
<code>/api/who_i_am</code>
#### Ответ
<code> {"userMail":"1@1.ru","userLogin":"111"}</code>
#### Адрес 
<code>/api/logout</code>
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
<code>/api/user/login</code>
#### Запрос
<code> {"passHash": "111", "userLogin":"211"} </code>
#### Ответ
<code> {
"loginResult": true
} </code>


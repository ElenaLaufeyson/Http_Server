# Http_Server

Запуск: 
```
gradle startService
```
Тк проект без докера, а порт 80 занят, сервер запускается на порту 8080, соответственно и все запросы тоже на 8080.

Примеры:

$curl http://localhost:8080?year=1998
{"errorCode":200,"dataMessage":"13/09/98"}

$curl http://localhost:8080?year=2020
{"errorCode":200,"dataMessage":"14/09/20"}

$curl http://localhost:8080?ear=1998
{"errorCode":401,"dataMessage":"invalid parameter"}

 $curl http://localhost:8080?year=1u98
 {"errorCode":2,"dataMessage":"invalid year"}
 
 $curl http://localhost:8080?year=345
{"errorCode":200,"dataMessage":"13/09/45"}

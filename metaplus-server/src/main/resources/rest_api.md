
Hello:
- `GET` /hello
- `POST` /echo/{name}

Sync:
- `POST` /sync/one 
- `POST` /sync/one/{domain}
- `POST` /sync/bulk/{domain}

Meta :
- `PUT`    /meta/create/{fqmn}
- `POST`   /meta/update/{fqmn}
- `DELETE` /meta/delete/{fqmn}
- `HEAD`   /meta/exist/{fqmn}

Plus: 
- `POST`   /plus/update/{fqmn}

Doc:
- `GET` /doc/read/{fqmn}

Search:
- `GET` /search/simple/{domain}/{text}


Domain:
- `PUT` /domain/create/{domain}
- `POST` /domain/update/{domain}
- `DELETE` /domain/delete/{domain}
- `GET` /domain/read/{domain}
- `HEAD` /domain/exist/{domain}
- `GET` /domain/sample/{domain}
- `GET` /domain/list
- `GET` /domain/schema/{domain}
- `GET` /domain/health/{domain}




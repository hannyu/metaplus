Bus:

- `POST` /bus/one
- `POST` /bus/one/{domain}
- `POST` /bus/bulk/{domain}

Doc:

- `PUT` /doc/create/{fqmn}
- `POST` /doc/meta_update/{fqmn}
- `DELETE` /doc/meta_delete/{fqmn}
- `POST` /doc/plus_update/{fqmn}
- `GET` /doc/read/{fqmn}
- `HEAD` /doc/exist/{fqmn}

Domain:

- `PUT` /domain/create/{domain}
- `POST` /domain/update/{domain}
- `DELETE` /domain/delete/{domain}
- `HEAD` /domain/exist/{domain}
- `GET` /domain/read/{domain}
- `GET` /domain/schema/{domain}
- `GET` /domain/sample/{domain}
- `GET` /domain/health/{domain}

Search:

- `GET` /search/simple
- `GET` /search/_search



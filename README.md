## Zadanie rekrutacyjne

### Aplikacja wystawia jeden endpoint:

> ***/repo/{username}***

gdzie *username* jest nazwą użytkownika na Githubie.

Aplikacja przekierowuje request do API Githuba i zwraca wymagany przez zadanie model jako obiekt JSON:

```json
[
  {
    "repositoryName": "string",
    "ownerLogin": "string",
    "branches": [
      {
        "name": "string",
        "sha": "string"
      }
    ]
  }
]
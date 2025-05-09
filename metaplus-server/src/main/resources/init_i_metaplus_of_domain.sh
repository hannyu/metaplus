
# Config:
ES_BASE_URL="http://localhost:9200"
USERNAME=""
PASSWORD=""

# check if curl is available
curl --version >/dev/null 2>&1
if [ $? -ne 0 ]; then
  echo "Curl is not found, please install curl or initialize 'i_metaplus_of_domain' manually."
  exit 1;
fi

# create index 'i_metaplus_of_domain'
echo "=== Create index i_metaplus_of_domain"
curl -XPUT "${ES_BASE_URL}/i_metaplus_of_domain" -H 'Content-Type: application/json' -d '
{
  "settings": {},
  "mappings": {
    "dynamic": false,
    "properties": {
      "fqmn": {
        "properties": {
          "fqmn": {
            "type": "keyword"
          },
          "corp": {
            "type": "keyword"
          },
          "domain": {
            "type": "keyword"
          },
          "name": {
            "type": "keyword"
          },
          "_id": {
            "type": "alias",
            "path": "_id"
          }
        }
      },
      "sync": {
        "properties": {
          "createdBy": {
            "type": "keyword"
          },
          "createdAt": {
            "type": "date"
          },
          "createdFrom": {
            "type": "keyword"
          },
          "updatedBy": {
            "type": "keyword"
          },
          "updatedAt": {
            "type": "date"
          },
          "updatedFrom": {
            "type": "keyword"
          },
          "_version": {
            "type": "alias",
            "path": "_version"
          }
        }
      },
      "meta": {
        "properties": {
          "index": {
            "properties": {
              "name": {
                "type": "keyword"
              },
              "desc": {
                "type": "text"
              },
              "abstract": {
                "type": "boolean"
              },
              "extend": {
                "type": "keyword"
              },
              "created": {
                "type": "boolean"
              }
            }
          },
          "schema": {
            "properties": {
              "mappings": {
                "type": "object"
              },
              "settings": {
                "type": "object"
              }
            }
          }
        }
      },
      "plus": {
        "type": "object"
      }
    }
  }
}
'
if [ $? -ne 0 ]; then
  echo "Create index i_metaplus_of_domain failed...exit"
  exit 2;
fi


# import meta of none
echo ""
echo ""
echo "=== Init ::domain::none"
#curl -XPUT "${ES_BASE_URL}/i_metaplus_of_domain/_create/metaplus::domain::none" -H 'Content-Type: application/json' -d '
curl -XPOST "${ES_BASE_URL}/i_metaplus_of_domain/_doc/" -H 'Content-Type: application/json' -d '

{
  "fqmn": {
    "fqmn": "::domain::none",
    "corp": "",
    "domain": "domain",
    "name": "none"
  },
  "sync": {
    "createdBy": "init_script",
    "createdAt": "'`date "+%Y-%m-%dT%H:%M:%S%z"`'"
  },
  "meta": {
    "index": {
      "name": "i_metaplus_of_none",
      "desc": "the base template of all domains",
      "abstract": true,
      "created": false
    },
    "schema": {
      "mappings": {
        "dynamic": false,
        "properties": {
          "fqmn": {
            "properties": {
              "fqmn": {
                "type": "keyword",
                "#expression": "fqmn.corp+\"::\"+fqmn.domain+\"::\"+fqmn.name",
                "#expressionOrder": 100
              },
              "corp": {
                "type": "keyword"
              },
              "domain": {
                "type": "keyword"
              },
              "name": {
                "type": "keyword"
              }
            }
          },
          "sync": {
            "properties": {
              "createdBy": {
                "type": "keyword",
                "#comment": "Who create this"
              },
              "createdAt": {
                "type": "date",
                "#comment": "Date format is yyyy-MM-dd'"'T'"'HH:mm:ss.SSSZ"
              },
              "createdFrom": {
                "type": "keyword",
                "#comment": "Through what tool or platform to create this"
              },
              "updatedBy": {
                "type": "keyword"
              },
              "updatedAt": {
                "type": "date"
              },
              "updatedFrom": {
                "type": "keyword"
              },
              "version": {
                "type": "alias",
                "path": "_version"
              }
            }
          },
          "meta": {
            "type": "object"
          },
          "plus": {
            "type": "object"
          }
        }
      },
      "settings": {}
    }
  }
}
'

# Get doc metaplus::domain::none
echo ""
echo ""
echo "=== Get ::domain::none"
curl -XGET "${ES_BASE_URL}/i_metaplus_of_domain/_search" -H 'Content-Type: application/json'


# import domain of domain
echo ""
echo ""
echo "=== Init ::domain::domain"
#curl -XPUT "${ES_BASE_URL}/i_metaplus_of_domain/_create/::domain::domain" -H 'Content-Type: application/json' -d '
curl -XPOST "${ES_BASE_URL}/i_metaplus_of_domain/_doc/" -H 'Content-Type: application/json' -d '
{
  "fqmn": {
    "fqmn": "::domain::domain",
    "corp": "",
    "domain": "domain",
    "name": "domain"
  },
  "sync": {
    "createdBy": "init_script",
    "createdAt": "'`date "+%Y-%m-%dT%H:%M:%S%z"`'"
  },
  "meta": {
    "index": {
      "name": "i_metaplus_of_domain",
      "desc": "domain of domain, defines this index",
      "abstract": false,
      "extend": "none",
      "created": true
    },
    "schema": {
      "mappings": {
        "properties": {
          "meta": {
            "properties": {
              "index": {
                "properties": {
                  "name": {
                    "type": "keyword",
                    "#comment": "The name of the index in Elasticsearch",
                    "#required": true,
                    "#samples": "i_metaplus_of_book"
                  },
                  "desc": {
                    "type": "text",
                    "#comment": "A brief description of the index"
                  },
                  "abstract": {
                    "type": "boolean",
                    "#comment": "Whether need to create a physical index",
                    "#default": false
                  },
                  "extend": {
                    "type": "keyword",
                    "#comment": "When creating the index, the settings and mappings will recursively be merged from all extend domains",
                    "#default": "none"
                  },
                  "created": {
                    "type": "boolean",
                    "#comment": "Whether the index has already been created",
                    "#default": false
                  }
                }
              },
              "schema": {
                "properties": {
                  "mappings": {
                    "type": "object",
                    "#required": true
                  },
                  "settings": {
                    "type": "object"
                  }
                }
              }
            }
          },
          "plus": {
            "type": "object"
          }
        }
      },
      "settings": {}
    }
  }
}
'

# Done
echo ""


package kmm.example.recipesroll.remote

import android.accounts.NetworkErrorException
import com.contentful.java.cda.CDAAsset
import com.google.gson.GsonBuilder
import kmm.example.recipesroll.model.ChefModel
import kmm.example.recipesroll.model.RecipeModel
import kmm.example.recipesroll.model.TagModel


object DummyRecipesApi : RecipesApi {
    private val gson = GsonBuilder().setPrettyPrinting().create()

    var recipesCount = 3
    var simulateConnectionError = false

    override fun fetchRecipes(
        onResult: (Collection<RecipeModel>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        try {
            if (simulateConnectionError) throw NetworkErrorException()

            onResult(IntRange(0, recipesCount - 1).map {
                getRecipeData(it % 4).copy(
                    id = "recipe_${it + 1}",
                    photo = getPhotoData(it % 4)
                )
            })
        } catch (e: Exception) {
            onError(e)
        }
    }

    private fun getRecipeData(i: Int): RecipeModel {
        return when (i) {
            1 -> RecipeModel(
                title = "Tofu Saag Paneer with Buttery Toasted Pita",
                description = "Saag paneer is a popular Indian dish with iron-rich spinach and cubes of paneer, an Indian cheese that is firm enough to retain it\u0027s shape, but silky-soft on the inside. We have reimagined Saag Paneer and replaced the \"paneer\" with crispy cubes of firm tofu, making this already delicious and nutritious vegetarian dish burst with protein. Toasted pita bread is served alongside as an ode to naan. Cook, relax, and enjoy! [VIDEO](https://www.youtube.com/watch?v\u003dRMzWWwfWdVs)",
                calories = 900.0,
            )
            2 -> RecipeModel(
                title = "Grilled Steak \u0026 Vegetables with Cilantro-Jalapeño Dressing",
                description = "Warmer weather means the ushering in of grill season and this recipe completely celebrates the grill (or grill pan)! Squash and green beans are nicely charred then take a bath in a zesty cilantro-jalapeño dressing. After the steaks are perfectly seared, the same dressing does double duty as a tasty finishing sauce. A fresh radish salad tops it all off for crunch and a burst of color. Cook, relax, and enjoy!",
                calories = 345.0,
                chef = ChefModel("Mark Zucchiniberg"),
            )
            3 -> RecipeModel(
                title = "Crispy Chicken and Rice\twith Peas \u0026 Arugula Salad",
                description = "Crispy chicken skin, tender meat, and rich, tomatoey sauce form a winning trifecta of delicious in this one-pot braise. We spoon it over rice and peas to soak up every last drop of goodness, and serve a tangy arugula salad alongside for a vibrant boost of flavor and color. Dinner is served! Cook, relax, and enjoy!",
                calories = 785.0,
                chef = ChefModel("Jony Chives"),
                tags = listOf(
                    TagModel("gluten free"),
                    TagModel("healthy"),
                ),
            )
            else -> RecipeModel(
                title = "White Cheddar Grilled Cheese with Cherry Preserves \u0026 Basil",
                description = "*Grilled Cheese 101*: Use delicious cheese and good quality bread; make crunchy on the outside and ooey gooey on the inside; add one or two ingredients for a flavor punch! In this case, cherry preserves serve as a sweet contrast to cheddar cheese, and basil adds a light, refreshing note. Use __mayonnaise__ on the outside of the bread to achieve the ultimate, crispy, golden-brown __grilled cheese__. Cook, relax, and enjoy!",
                calories = 788.0,
                tags = listOf(
                    TagModel("vegan"),
                    TagModel("fake cheese"),
                ),
            )
        }
    }

    private fun getPhotoData(i: Int): CDAAsset {
        val photoData = when (i) {
            1 -> """
            {
              "defaultLocale": "en-US",
              "fallbackLocaleMap": {},
              "fields": {
                "file": {
                  "en-US": {
                    "url": "//images.ctfassets.net/kk2bw5ojx476/48S44TRZN626y4Wy4CuOmA/9c0a510bc3d18dda9318c6bf49fac327/SKU1498_Hero_154__2_-adb6124909b48c69f869afecb78b6808-2.jpg",
                    "details": {
                      "size": 218803.0,
                      "image": {
                        "width": 1020.0,
                        "height": 680.0
                      }
                    },
                    "fileName": "SKU1498_Hero_154__2_-adb6124909b48c69f869afecb78b6808-2.jpg",
                    "contentType": "image/jpeg"
                  }
                },
                "title": {
                  "en-US": "SKU1498 Hero 154 2 -adb6124909b48c69f869afecb78b6808-2"
                }
              },
              "rawFields": {
                "file": {
                  "en-US": {
                    "url": "//images.ctfassets.net/kk2bw5ojx476/48S44TRZN626y4Wy4CuOmA/9c0a510bc3d18dda9318c6bf49fac327/SKU1498_Hero_154__2_-adb6124909b48c69f869afecb78b6808-2.jpg",
                    "details": {
                      "size": 218803.0,
                      "image": {
                        "width": 1020.0,
                        "height": 680.0
                      }
                    },
                    "fileName": "SKU1498_Hero_154__2_-adb6124909b48c69f869afecb78b6808-2.jpg",
                    "contentType": "image/jpeg"
                  }
                },
                "title": {
                  "en-US": "SKU1498 Hero 154 2 -adb6124909b48c69f869afecb78b6808-2"
                }
              },
              "sys": {
                "space": {
                  "sys": {
                    "type": "Link",
                    "linkType": "Space",
                    "id": "kk2bw5ojx476"
                  }
                },
                "id": "48S44TRZN626y4Wy4CuOmA",
                "type": "Asset",
                "createdAt": "2018-05-07T13:39:06.171Z",
                "updatedAt": "2018-05-07T13:39:06.171Z",
                "environment": {
                  "sys": {
                    "id": "master",
                    "type": "Link",
                    "linkType": "Environment"
                  }
                },
                "revision": 1.0,
                "locale": "en-US"
              }
            }
            """.trimIndent()

            2 -> """
            {
              "defaultLocale": "en-US",
              "fallbackLocaleMap": {},
              "fields": {
                "file": {
                  "en-US": {
                    "url": "//images.ctfassets.net/kk2bw5ojx476/3TJp6aDAcMw6yMiE82Oy0K/2a4cde3c1c7e374166dcce1e900cb3c1/SKU1503_Hero_102__1_-6add52eb4eec83f785974ddeac3316b7.jpg",
                    "details": {
                      "size": 216391.0,
                      "image": {
                        "width": 1020.0,
                        "height": 680.0
                      }
                    },
                    "fileName": "SKU1503_Hero_102__1_-6add52eb4eec83f785974ddeac3316b7.jpg",
                    "contentType": "image/jpeg"
                  }
                },
                "title": {
                  "en-US": "SKU1503 Hero 102 1 -6add52eb4eec83f785974ddeac3316b7"
                }
              },
              "rawFields": {
                "file": {
                  "en-US": {
                    "url": "//images.ctfassets.net/kk2bw5ojx476/3TJp6aDAcMw6yMiE82Oy0K/2a4cde3c1c7e374166dcce1e900cb3c1/SKU1503_Hero_102__1_-6add52eb4eec83f785974ddeac3316b7.jpg",
                    "details": {
                      "size": 216391.0,
                      "image": {
                        "width": 1020.0,
                        "height": 680.0
                      }
                    },
                    "fileName": "SKU1503_Hero_102__1_-6add52eb4eec83f785974ddeac3316b7.jpg",
                    "contentType": "image/jpeg"
                  }
                },
                "title": {
                  "en-US": "SKU1503 Hero 102 1 -6add52eb4eec83f785974ddeac3316b7"
                }
              },
              "sys": {
                "space": {
                  "sys": {
                    "type": "Link",
                    "linkType": "Space",
                    "id": "kk2bw5ojx476"
                  }
                },
                "id": "3TJp6aDAcMw6yMiE82Oy0K",
                "type": "Asset",
                "createdAt": "2018-05-07T13:30:06.967Z",
                "updatedAt": "2018-05-07T13:30:06.967Z",
                "environment": {
                  "sys": {
                    "id": "master",
                    "type": "Link",
                    "linkType": "Environment"
                  }
                },
                "revision": 1.0,
                "locale": "en-US"
              }
            }
            """.trimIndent()

            3 -> """
            {
              "defaultLocale": "en-US",
              "fallbackLocaleMap": {},
              "fields": {
                "file": {
                  "en-US": {
                    "url": "//images.ctfassets.net/kk2bw5ojx476/5mFyTozvSoyE0Mqseoos86/fb88f4302cfd184492e548cde11a2555/SKU1479_Hero_077-71d8a07ff8e79abcb0e6c0ebf0f3b69c.jpg",
                    "details": {
                      "size": 230068.0,
                      "image": {
                        "width": 1020.0,
                        "height": 680.0
                      }
                    },
                    "fileName": "SKU1479_Hero_077-71d8a07ff8e79abcb0e6c0ebf0f3b69c.jpg",
                    "contentType": "image/jpeg"
                  }
                },
                "title": {
                  "en-US": "SKU1479 Hero 077-71d8a07ff8e79abcb0e6c0ebf0f3b69c"
                }
              },
              "rawFields": {
                "file": {
                  "en-US": {
                    "url": "//images.ctfassets.net/kk2bw5ojx476/5mFyTozvSoyE0Mqseoos86/fb88f4302cfd184492e548cde11a2555/SKU1479_Hero_077-71d8a07ff8e79abcb0e6c0ebf0f3b69c.jpg",
                    "details": {
                      "size": 230068.0,
                      "image": {
                        "width": 1020.0,
                        "height": 680.0
                      }
                    },
                    "fileName": "SKU1479_Hero_077-71d8a07ff8e79abcb0e6c0ebf0f3b69c.jpg",
                    "contentType": "image/jpeg"
                  }
                },
                "title": {
                  "en-US": "SKU1479 Hero 077-71d8a07ff8e79abcb0e6c0ebf0f3b69c"
                }
              },
              "sys": {
                "space": {
                  "sys": {
                    "type": "Link",
                    "linkType": "Space",
                    "id": "kk2bw5ojx476"
                  }
                },
                "id": "5mFyTozvSoyE0Mqseoos86",
                "type": "Asset",
                "createdAt": "2018-05-07T13:31:45.501Z",
                "updatedAt": "2018-05-07T13:31:45.501Z",
                "environment": {
                  "sys": {
                    "id": "master",
                    "type": "Link",
                    "linkType": "Environment"
                  }
                },
                "revision": 1.0,
                "locale": "en-US"
              }
            }
            """.trimIndent()

            else -> """
            {
              "defaultLocale": "en-US",
              "fallbackLocaleMap": {},
              "fields": {
                "file": {
                  "en-US": {
                    "url": "//images.ctfassets.net/kk2bw5ojx476/61XHcqOBFYAYCGsKugoMYK/0009ec560684b37f7f7abadd66680179/SKU1240_hero-374f8cece3c71f5fcdc939039e00fb96.jpg",
                    "details": {
                      "size": 194737.0,
                      "image": {
                        "width": 1020.0,
                        "height": 680.0
                      }
                    },
                    "fileName": "SKU1240_hero-374f8cece3c71f5fcdc939039e00fb96.jpg",
                    "contentType": "image/jpeg"
                  }
                },
                "title": {
                  "en-US": "SKU1240 hero-374f8cece3c71f5fcdc939039e00fb96"
                }
              },
              "rawFields": {
                "file": {
                  "en-US": {
                    "url": "//images.ctfassets.net/kk2bw5ojx476/61XHcqOBFYAYCGsKugoMYK/0009ec560684b37f7f7abadd66680179/SKU1240_hero-374f8cece3c71f5fcdc939039e00fb96.jpg",
                    "details": {
                      "size": 194737.0,
                      "image": {
                        "width": 1020.0,
                        "height": 680.0
                      }
                    },
                    "fileName": "SKU1240_hero-374f8cece3c71f5fcdc939039e00fb96.jpg",
                    "contentType": "image/jpeg"
                  }
                },
                "title": {
                  "en-US": "SKU1240 hero-374f8cece3c71f5fcdc939039e00fb96"
                }
              },
              "sys": {
                "space": {
                  "sys": {
                    "type": "Link",
                    "linkType": "Space",
                    "id": "kk2bw5ojx476"
                  }
                },
                "id": "61XHcqOBFYAYCGsKugoMYK",
                "type": "Asset",
                "createdAt": "2018-05-07T13:37:53.784Z",
                "updatedAt": "2018-05-07T13:37:53.784Z",
                "environment": {
                  "sys": {
                    "id": "master",
                    "type": "Link",
                    "linkType": "Environment"
                  }
                },
                "revision": 1.0,
                "locale": "en-US"
              }
            }
            """.trimIndent()
        }
        return gson.fromJson(photoData, CDAAsset::class.java)
    }
}
@file:Suppress("UnstableApiUsage")

import java.net.URI

include(":mobile:feature-article:feature-article-api")


include(":mobile:feature-article")


include(":mobile:feature-filters:feature-filters-api")


include(":mobile:feature-filters")


pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = URI.create("https://jitpack.io") }
    }
}

rootProject.name = "NewsApp"

include(":app")

include(":core:database")
include(":core:database:database-api")

include(":core:newsapi")
include(":core:newsapi:newsapi-api")

include(":core:newsdata")
include(":core:newsdata:newsdata-api")

include(":mobile:mobile-common")

include(":mobile:navigation")
include(":mobile:navigation:navigation-api")

include(":mobile:feature-headlines")
include(":mobile:feature-headlines:feature-headlines-api")

include(":mobile:feature-saved")
include(":mobile:feature-saved:feature-saved-api")

include(":mobile:feature-sources")
include(":mobile:feature-sources:feature-sources-api")



















# yml4j
yml4J est une api ajoutant des fonctionnalités 
qui permettent de simplifie l'utilisation de 
[snakeYAML](https://bitbucket.org/asomov/snakeyaml) 
qui est une api de lecture de fichier [yaml](http://yaml.org/).


## Ajout à la dépendance du projet
`@VERSION@` => version voulue.   
Liste des versions [ici](https://github.com/redsarow/yml4j/releases) 
ou [la](https://github.com/redsarow/maven_repo/tree/master/fr/redsarow/yml4J).

### Maven
Dans le `pom.xml`:
```xml
<repositories>
    <repository>
        <id>yml4J-repo</id>
        <url>https://raw.github.com/redsarow/maven_repo/master</url>
    </repository>
</repositories>
 
<dependencies>
    <dependency>
        <groupId>fr.redsarow</groupId>
        <artifactId>yml4J</artifactId>
        <version>@VERSION@</version>
    </dependency>
</dependencies>
```

### Gradle
Dans le `build.gradle`:
```groovy
repositories {
  maven {
    url  "https://raw.github.com/redsarow/maven_repo/master"
  }
}
 
dependencies {
  compile "fr.redsarow:yml4J:@VERSION@"
}
```

### Manuellement
#### IntelliJ
`Module Settings > Dependencies > + > JARs or directories > Select your JAR file`

#### Eclipse
`Project Properties > Java Build Path > Add the jar file`

## Dépendance
#### Java 8

#### SnakeYAML 1.18
https://bitbucket.org/asomov/snakeyaml


## License
##### FR
[![Licence Creative Commons](https://i.creativecommons.org/l/by-nc-sa/4.0/88x31.png)](https://creativecommons.org/licenses/by-nc-sa/4.0/deed.fr)  
Ce dépôt est mis à disposition selon les termes de la [licence Creative Commons Attribution - Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 4.0 International](https://creativecommons.org/licenses/by-nc-sa/4.0/deed.fr).
##### EN
[![Licence Creative Commons](https://i.creativecommons.org/l/by-nc-sa/4.0/88x31.png)](http://creativecommons.org/licenses/by-nc-sa/4.0/)  
This deposit is licensed under a [Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License](http://creativecommons.org/licenses/by-nc-sa/4.0/).

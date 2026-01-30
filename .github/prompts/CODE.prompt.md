---
agent: agent
---

# Instructions pour le Codage du Plan de Test

## Règles obligatoires

Tout changement de code dans le répertoire `src/main` suite à un test échoué doit être répertorié dans un fichier `AI_changelog.md` avec la justification appropriée et un résumé en listant les fichiers modifiés.

Les exigences pour chaque fonctionnalité spécifiée dans le PRD doivent être respectées.

## Instructions spécifiques

[Ajouter instruction spécifique ici]

## Structure des répertoires de test

Organiser les tests selon la structure suivante :

```
src/test/
├── java/com/agora/monitoring/
│   ├── ui/                    # Tests Selenium WebDriver
│   ├── api/                   # Tests d'API REST
│   ├── acceptance/            # Tests Cucumber/Gherkin
│   ├── unit/                  # Tests JUnit unitaires
│   └── integration/           # Tests d'intégration TestNG
├── resources/
│   ├── features/              # Fichiers .feature Cucumber
│   ├── postman/               # Collections Postman
│   ├── robot/                 # Scripts Robot Framework
│   └── jmeter/                # Plans de test JMeter
```

## Tâche : Implémenter le plan de test

Pour le fichier PRD du dossier `Features` spécifié par nom (complet ou partiel) ou par numéro :

### Étape 1 : Analyser le PRD

1. Lire le fichier PRD spécifié dans le dossier `Features/`
2. Extraire la section "Mapping des tests" ou "Cas de test recommandés"
3. Identifier TOUS les cas de test listés avec leurs outils, types et fichiers à créer

### Étape 2 : Vérifier et configurer les outils

Pour CHAQUE outil mentionné dans le PRD, vérifier :

1. **JUnit/TestNG** :
   - Vérifier présence dans `pom.xml`
   - Si absent, ajouter les dépendances nécessaires

2. **Selenium WebDriver** :
   - Vérifier présence de `selenium-java` dans `pom.xml`
   - Vérifier présence de WebDriverManager ou drivers
   - Si absent, ajouter dépendances :
     ```xml
     <dependency>
       <groupId>org.seleniumhq.selenium</groupId>
       <artifactId>selenium-java</artifactId>
       <version>4.15.0</version>
       <scope>test</scope>
     </dependency>
     ```

3. **Cucumber/Gherkin** :
   - Vérifier présence de `cucumber-java` et `cucumber-junit-platform-engine` dans `pom.xml`
   - Si absent, ajouter dépendances Cucumber pour Java

4. **Robot Framework** :
   - Vérifier présence de `robotframework` dans les outils du projet
   - Créer configuration si nécessaire dans `src/test/resources/robot/`

5. **Postman** :
   - Vérifier présence de Newman (CLI Postman) ou collections exportées
   - Créer structure dans `src/test/resources/postman/`

6. **JMeter** :
   - Vérifier présence de plans de test `.jmx`
   - Créer structure dans `src/test/resources/jmeter/`

### Étape 3 : Créer TOUS les fichiers de test

Pour CHAQUE cas de test listé dans le PRD :

1. **Créer le fichier de test** au chemin exact spécifié dans la colonne "Fichier à créer"

2. **Implémenter le test complet** avec :
   - Imports nécessaires
   - Annotations appropriées (@Test, @Before, @After, etc.)
   - Implémentation des pré-conditions
   - Implémentation de TOUTES les étapes d'exécution listées
   - Assertions pour TOUS les résultats attendus
   - Gestion des post-conditions
   - Commentaires expliquant la logique

3. **Pour les tests Selenium** :
   - **JAMAIS utiliser @Disabled** - tous les tests doivent être exécutables
   - Utiliser `@SpringBootTest(classes = WebApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)` pour démarrer l'application automatiquement
   - Toujours spécifier la classe de configuration principale dans `classes = WebApplication.class`
   - Injecter `@LocalServerPort` pour obtenir le port du serveur démarré
   - Construire l'URL dynamiquement : `http://localhost:{port}/`
   - Configurer ChromeDriver en mode headless avec les options : `--headless`, `--no-sandbox`, `--disable-dev-shm-usage`
   - Utiliser Page Object Pattern pour la maintenabilité
   - Créer classes Page pour chaque page testée
   - Implémenter les locators (CSS, XPath)
   - Gérer le cycle de vie du WebDriver (setup/teardown) avec @BeforeEach/@AfterEach
   - Ajouter waits explicites et conditions d'attente (WebDriverWait)
   - Les tests doivent être complètement autonomes et ne jamais dépendre d'un serveur externe

4. **Pour les tests Cucumber** :
   - Créer le fichier `.feature` dans `src/test/resources/features/`
   - Implémenter les step definitions en Java
   - Créer le runner Cucumber

5. **Pour les tests Postman** :
   - Créer la collection JSON avec toutes les requêtes
   - Ajouter les assertions (tests) dans chaque requête
   - Documenter les variables d'environnement nécessaires

6. **Pour les tests Robot Framework** :
   - Créer les fichiers `.robot` avec keywords
   - Implémenter les test cases
   - Créer libraries custom si nécessaire

7. **Pour les tests JMeter** :
   - Créer le plan de test `.jmx`
   - Configurer les thread groups
   - Définir les assertions de performance

### Étape 4 : Données de test

Pour chaque fichier de test créé :
- Utiliser les données spécifiées dans la section "Données de test et valeurs limites" du PRD
- Créer des méthodes de test séparées pour :
  - Cas valides (happy path)
  - Cas invalides (validation d'erreurs)
  - Cas limites (boundary values)
  - Cas d'erreur (exception handling)

### Étape 5 : Exécuter les tests

1. Compiler le projet : `mvn clean compile`
2. Exécuter TOUS les tests créés : `mvn test`
3. Analyser les résultats (succès ET échecs)
4. Pour chaque test échoué, analyser la cause de l'échec
5. Corriger les bugs dans `src/main` si nécessaire pour que tous les tests passent

### Étape 6 : Corriger les bugs et documenter les changements

1. Pour chaque test échoué :
   - Analyser la cause de l'échec (bug dans `src/main`, test incorrect, etc.)
   - Si le bug est dans `src/main`, corriger le code
   - Documenter CHAQUE modification dans `AI_changelog.md`
   - Ré-exécuter les tests pour valider la correction

2. Structure du fichier `AI_changelog.md` :
   ```markdown
   # Changelog des modifications suite aux tests

   ## [Nom de la Feature] - [Date]

   ### Fichier modifié : `chemin/vers/fichier.java`

   **Test échoué** : `NomDuTest.methodeTest()`

   **Erreur** : Description de l'erreur rencontrée

   **Justification** : Explication de pourquoi la modification est nécessaire

   **Modification** : Description détaillée des changements apportés

   **Résultat** : Test passe maintenant ✓
   ```

3. Créer un fichier `TEST_EXECUTION_REPORT_[FeatureName].md` contenant :
   - Liste de tous les fichiers de test créés
   - Résultat final de compilation
   - Résultat final d'exécution (tous les tests doivent passer)
   - Statistiques : nombre total de tests, tous réussis
   - Référence au `AI_changelog.md` si des corrections ont été faites

### Étape 7 : Validation finale

Vérifier que :
- [ ] Tous les cas de test du PRD ont un fichier de test correspondant créé
- [ ] Tous les outils mentionnés dans le PRD sont configurés
- [ ] Tous les tests compilent sans erreur
- [ ] Tous les tests passent avec succès (100% de réussite)
- [ ] Le rapport de test est complet et précis
- [ ] `AI_changelog.md` documente toutes les modifications apportées à `src/main`
- [ ] Le code respecte les exigences du PRD

## Exemple de workflow complet

Pour la Feature "01_Feature1.md" :

1. Lire `Features/01_Feature1.md`
2. Identifier les cas de test :
   - Happy path WebSocket → Créer `src/test/java/com/agora/monitoring/ui/Feature1SeleniumTest.java`
   - Polling HTTP → Créer tests Postman + Selenium
   - Perte de connexion → Test d'intégration
   - Pas de duplication → Test Selenium avec assertions JS
3. Vérifier/ajouter dépendances Selenium et Postman dans `pom.xml`
4. Créer tous les fichiers de test avec implémentation complète
5. Exécuter `mvn test`
6. Corriger les bugs détectés dans `src/main` et documenter dans `AI_changelog.md`
7. Ré-exécuter les tests jusqu'à ce que tous passent
8. Générer `TEST_EXECUTION_REPORT_Feature1.md`

## Notes importantes

- **Ne jamais** sauter un cas de test listé dans le PRD
- **Ne jamais** utiliser @Disabled sur les tests - tous les tests doivent s'exécuter
- **Toujours** utiliser @SpringBootTest pour les tests Selenium/UI afin de démarrer l'application automatiquement
- **Toujours** créer le fichier au chemin exact spécifié
- **Toujours** implémenter toutes les assertions mentionnées
- **Toujours** corriger les bugs détectés par les tests pour atteindre 100% de réussite
- **Toujours** documenter chaque modification de `src/main` dans `AI_changelog.md`
- Si le PRD ne spécifie pas de chemin de fichier, suivre la structure standard des répertoires de test ci-dessus
- Itérer (test → correction → test) jusqu'à ce que tous les tests passent
- **Finaliser avec un état où tous les tests passent avec succès**

---
mode: agent
---
# Instructions pour le PRD du Plan de Test

Créer un plan de test compréhensif pour un logiciel avec une interface Web et des API Rest et Websocket.

## Outils de Test

Utiliser tous les application de test suivant:
- Robot Framework (tests d'acceptation)
- Selenium WebDriver (automatisation navigateur)
- Cucumber / Gherkin (scénarios d'acceptation — Java)
- JUnit, TestNG (tests unitaires)
- Postman (tests d'API)
- JMeter (tests de performance)

## Exigences du plan de test

Le plan de test doit couvrir les aspects suivants:
- test fonctionnels pour chaque fonctionnalité
- tester les cas limites et les scénarios d'erreur
- tester utilisation de valeurs non numérique pour des champs qui veux des valeurs numérique et vice versa.
- Utiliser le meilleur outil pour chaque type de test
- Tests d'interface utilisateur avec Selenium WebDriver et Robot Framework
- Tests d'API avec Postman
- Tests d'acceptation avec Cucumber/Gherkin
- L'utilisation des outils de test doit être justifiée dans le plan de test.

## Fonctionnalités à Tester obligatoirement

- Les données affichées devraient rafraichir à tous les 5 secondes:
  1. Les données de Températures doivent Rafraichir
  2. Les données de Fans devrait rafraichir

- Ajouter une valeur de seuil pour un Sensor ID dans la section des Température dans l'interface utilisateur:
  1. Entrer une valeur de Sensor ID qui ne fait pas parti de la liste dans le tableau devrait afficher une erreur à l'usager soit `Sensor ID not found`.
  2. Entrer une valeur de température en format non numérique devrait afficher une erreur
  3. Entrer une valeur valide devrait ajouter cette entrée dans le tableau à la bonne ligne soit celle correspondant au Sensor ID. Une entrée correspondant dans le fichier de configuration devrait aussi être créée.

- Ajouter une valeur de seuil pour un Fan ID dans la section des Fans dans l'interface utilisateur:
  1. Entrer une valeur de Fan ID  qui ne fait pas parti de la liste dans le tableau devrait afficher une erreur à l'usager soit `Fan ID not found`.
  2. Entrer une Valeur de Min RPM en format non numérique devrait afficher une erreur
  3. Entrer une valeur valide devrait ajouter cette entrée dans le tableau à la bonne ligne soit celle correspondant au Fan ID. Une entrée correspondant dans le fichier de configuration devrait aussi être créée.

- Click sur une ligne des tableau dans l'interface utilisateur doit insérer automatiqument la valeur de l'ID et du seuil dans les champ d'entrée correspondant au tableau.

- Lorsque il existe un seuil défini dans l'interface utilisateur et qu'il est rencontré:
  1. la ligne dans le tableau devrait surligné de couleur rouge
  2. ajouter une alerte correspondant dans la liste des alerte

- Lorsque l'on selectionne le bouton de Clear Alerts, toutes les alertes devraient être supprimées de la liste.

## Création du PRD

Créez un PRD pour ces fonctionnalités en Markdown dans le dossier `Features`.

Divisez chaque fonctionnalité dans son propre fichier prefixé par un numéro.

Chaque fichier doit être nommé avec le nom de la fonctionnalité et une courte description.

Ne pas créer un fichier sommaire, seulement les fichiers de fonctionnalités individuelles.

## Structure requise pour chaque fichier PRD

Chaque fichier PRD DOIT contenir les sections suivantes :
``` markdown
# [Titre de la fonctionnalité]

## 1. Objectif
Description claire de la fonctionnalité à tester.

## 2. Contexte
Explication technique de la fonctionnalité et de son environnement.

## 3. Critères d'acceptation
Liste précise des conditions de succès pour la fonctionnalité.

## 4. Cas de test recommandés
Pour CHAQUE cas de test, spécifier OBLIGATOIREMENT :
- **Nom du cas de test** (unique et descriptif)
- **Outil de test à utiliser** (Selenium, JUnit, TestNG, Cucumber, Postman, Robot Framework, JMeter)
- **Type de test** (unitaire, intégration, UI, API, performance, acceptation)
- **Fichier de test à créer** (chemin relatif depuis la racine du projet, ex: `src/test/java/com/agora/monitoring/ui/DataRefreshSeleniumTest.java`)
- **Pré-conditions** : état initial du système
- **Étapes d'exécution** : actions à effectuer (détaillées)
- **Données de test** : valeurs spécifiques à utiliser (valides, invalides, limites)
- **Résultat attendu** : comportement exact attendu avec assertions vérifiables
- **Post-conditions** : état final du système (si applicable)

## 5. Justification des outils
Expliquer pourquoi chaque outil a été choisi pour chaque type de test.

## 6. Données de test et valeurs limites
Liste exhaustive des valeurs à tester (cas normaux, cas limites, cas d'erreur).

## 7. Mapping des tests
Table récapitulative au format :

| Cas de test | Outil | Type | Fichier à créer | Priorité |
|-------------|-------|------|-----------------|----------|
| Exemple | Selenium | UI | src/test/java/.../ExampleTest.java | P0 |

Cette table permet au prompt CODE de savoir exactement quels fichiers créer.

## 8. Risques et considérations
Points d'attention particuliers pour l'implémentation des tests.

```

**IMPORTANT** : Les tests Selenium et UI doivent toujours être exécutables sans annotation `@Disabled`.
- Utiliser `@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)` pour démarrer l'application automatiquement
- Injecter `@LocalServerPort` pour obtenir le port dynamique
- Les tests doivent être complètement autonomes et ne jamais nécessiter de serveur externe

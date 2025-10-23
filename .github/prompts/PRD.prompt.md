---
mode: agent
---
# Instructions pour le PRD du Plan de Test

Créer un plan de test compréhensif pour un logiciel avec une interface Web et des API Rest et Websocket.

## Outils de Test

[Ajouter instruction spécifique ici]

## Exigences du plan de test

[Ajouter instruction spécifique ici]

## Fonctionnalités à Tester obligatoirement

[Ajouter instruction spécifique ici]

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

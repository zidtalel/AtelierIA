# Ajouter un seuil - Température

## 1. Objectif
Permettre à un utilisateur d'ajouter une valeur de seuil pour un `Sensor ID` via l'interface Température et s'assurer que les validations et la persistance sont correctes.

## 2. Contexte
La page Température contient un tableau listant les capteurs et leurs seuils. L'utilisateur peut saisir un `Sensor ID` et une valeur de seuil; le front-end doit valider le `Sensor ID` existe et que la valeur de seuil est numérique, puis appeler l'API pour persister la configuration.

## 3. Critères d'acceptation
- Entrer un `Sensor ID` qui n'existe pas affiche `Sensor ID not found`.
- Entrer une valeur non numérique pour le seuil affiche une erreur de validation.
- Entrer une valeur valide ajoute l'entrée dans le tableau et persiste la configuration côté serveur.

## 4. Cas de test recommandés
- **Nom du cas de test**: AddTemperatureThreshold_InvalidSensor
  - **Outil de test à utiliser**: Selenium WebDriver
  - **Type de test**: UI (intégration)
  - **Fichier de test à créer**: src/test/java/com/agora/monitoring/ui/TemperatureAddThresholdInvalidSensorSeleniumTest.java
  - **Pré-conditions**: Application démarrée; le tableau contient les Sensor IDs [101,102,103]
  - **Étapes d'exécution**:
    1. Naviguer vers la page Température.
    2. Saisir `Sensor ID` = 999 et `Threshold` = 50
    3. Cliquer sur Add
  - **Données de test**: Sensor ID invalide 999
  - **Résultat attendu**: Message `Sensor ID not found` affiché.

- **Nom du cas de test**: AddTemperatureThreshold_NonNumeric
  - **Outil de test à utiliser**: Selenium WebDriver
  - **Type de test**: UI (intégration)
  - **Fichier de test à créer**: src/test/java/com/agora/monitoring/ui/TemperatureAddThresholdNonNumericSeleniumTest.java
  - **Pré-conditions**: Application démarrée
  - **Étapes d'exécution**: Saisir Sensor ID valide 101 et Threshold = "abc", cliquer Add
  - **Données de test**: Threshold non numérique "abc"
  - **Résultat attendu**: Message d'erreur de validation affiché.

- **Nom du cas de test**: AddTemperatureThreshold_Valid
  - **Outil de test à utiliser**: Selenium WebDriver + JUnit
  - **Type de test**: UI + intégration
  - **Fichier de test à créer**: src/test/java/com/agora/monitoring/ui/TemperatureAddThresholdSeleniumTest.java
  - **Pré-conditions**: Application démarrée; Sensor ID 101 présent
  - **Étapes d'exécution**: Saisir Sensor ID 101, Threshold 55, cliquer Add, vérifier table et fichier de config
  - **Données de test**: Threshold 55
  - **Résultat attendu**: Nouvelle ligne insérée et configuration persistée.

## 5. Justification des outils
Selenium WebDriver permet d'exercer la validation côté client et d'observer les messages d'erreur. JUnit contrôle assertions côté serveur pour vérifier la persistance.

## 6. Données de test et valeurs limites
- Sensor ID valides: 100-199
- Threshold valides: entiers 0-100
- Cas invalides: Sensor ID inexistant, threshold non numérique, threshold négatif

## 7. Mapping des tests
| Cas de test | Outil | Type | Fichier à créer | Priorité |
|-------------|-------|------|-----------------|----------|
| AddTemperatureThreshold_InvalidSensor | Selenium | UI | src/test/java/com/agora/monitoring/ui/TemperatureAddThresholdInvalidSensorSeleniumTest.java | P0 |
| AddTemperatureThreshold_NonNumeric | Selenium | UI | src/test/java/com/agora/monitoring/ui/TemperatureAddThresholdNonNumericSeleniumTest.java | P0 |
| AddTemperatureThreshold_Valid | Selenium + JUnit | UI/Intégration | src/test/java/com/agora/monitoring/ui/TemperatureAddThresholdSeleniumTest.java | P0 |

## 8. Risques et considérations
- Tests doivent valider les messages d'erreur localisés (fr/en) si l'application supporte l'internationalisation.

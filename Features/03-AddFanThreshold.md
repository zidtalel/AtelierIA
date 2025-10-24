# Ajouter un seuil - Fan

## 1. Objectif
Permettre à un utilisateur d'ajouter une valeur minimale de RPM pour un `Fan ID` via l'interface Fans et s'assurer que les validations et la persistance sont correctes.

## 2. Contexte
La page Fans contient un tableau listant les ventilateurs et leurs seuils. L'utilisateur peut saisir un `Fan ID` et une valeur `Min RPM`; le front-end doit valider le `Fan ID` existe et que la valeur est numérique, puis appeler l'API pour persister la configuration.

## 3. Critères d'acceptation
- Entrer un `Fan ID` qui n'existe pas affiche `Fan ID not found`.
- Entrer une valeur non numérique pour le Min RPM affiche une erreur de validation.
- Entrer une valeur valide ajoute l'entrée dans le tableau et persiste la configuration côté serveur.

## 4. Cas de test recommandés
- **Nom du cas de test**: AddFanThreshold_InvalidFan
  - **Outil de test à utiliser**: Selenium WebDriver
  - **Type de test**: UI (intégration)
  - **Fichier de test à créer**: src/test/java/com/agora/monitoring/ui/FanAddThresholdInvalidFanSeleniumTest.java
  - **Pré-conditions**: Application démarrée; le tableau contient les Fan IDs [201,202]
  - **Étapes d'exécution**:
    1. Naviguer vers la page Fans.
    2. Saisir `Fan ID` = 999 et `Min RPM` = 1000
    3. Cliquer sur Add
  - **Données de test**: Fan ID invalide 999
  - **Résultat attendu**: Message `Fan ID not found` affiché.

- **Nom du cas de test**: AddFanThreshold_NonNumeric
  - **Outil de test à utiliser**: Selenium WebDriver
  - **Type de test**: UI (intégration)
  - **Fichier de test à créer**: src/test/java/com/agora/monitoring/ui/FanAddThresholdNonNumericSeleniumTest.java
  - **Pré-conditions**: Application démarrée
  - **Étapes d'exécution**: Saisir Fan ID valide 201 et Min RPM = "xyz", cliquer Add
  - **Données de test**: Min RPM non numérique "xyz"
  - **Résultat attendu**: Message d'erreur de validation affiché.

- **Nom du cas de test**: AddFanThreshold_Valid
  - **Outil de test à utiliser**: Selenium WebDriver + JUnit
  - **Type de test**: UI + intégration
  - **Fichier de test à créer**: src/test/java/com/agora/monitoring/ui/FanAddThresholdSeleniumTest.java
  - **Pré-conditions**: Application démarrée; Fan ID 201 présent
  - **Étapes d'exécution**: Saisir Fan ID 201, Min RPM 900, cliquer Add, vérifier table et fichier de config
  - **Données de test**: Min RPM 900
  - **Résultat attendu**: Nouvelle ligne insérée et configuration persistée.

## 5. Justification des outils
Selenium WebDriver est utilisé pour valider les comportements côté client; JUnit pour assertions de persistance côté serveur.

## 6. Données de test et valeurs limites
- Fan ID valides: 200-299
- Min RPM valides: entiers 0-5000
- Cas invalides: Fan ID inexistant, Min RPM non numérique, Min RPM négatif

## 7. Mapping des tests
| Cas de test | Outil | Type | Fichier à créer | Priorité |
|-------------|-------|------|-----------------|----------|
| AddFanThreshold_InvalidFan | Selenium | UI | src/test/java/com/agora/monitoring/ui/FanAddThresholdInvalidFanSeleniumTest.java | P0 |
| AddFanThreshold_NonNumeric | Selenium | UI | src/test/java/com/agora/monitoring/ui/FanAddThresholdNonNumericSeleniumTest.java | P0 |
| AddFanThreshold_Valid | Selenium + JUnit | UI/Intégration | src/test/java/com/agora/monitoring/ui/FanAddThresholdSeleniumTest.java | P0 |

## 8. Risques et considérations
- S'assurer que la mise à jour du fichier de configuration est atomique pour éviter les corruptions en cas d'échec.

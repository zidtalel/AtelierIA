# Table Row Click - Remplissage automatique des champs

## 1. Objectif
S'assurer qu'un clic sur une ligne de tableau insère automatiquement l'ID et la valeur de seuil correspondante dans les champs d'entrée.

## 2. Contexte
Les tableaux (Températures et Fans) permettent la sélection d'une ligne. Lorsqu'une ligne est cliquée, le front-end doit pré-remplir les champs `ID` et `Threshold/Min RPM` pour faciliter les modifications.

## 3. Critères d'acceptation
- Un clic sur une ligne remplit les champs d'entrée correspondants au `ID` et au seuil.
- Si la ligne n'a pas de seuil, les champs doivent rester vides.

## 4. Cas de test recommandés
- **Nom du cas de test**: TableRowClick_Temperature
  - **Outil de test à utiliser**: Selenium WebDriver
  - **Type de test**: UI
  - **Fichier de test à créer**: src/test/java/com/agora/monitoring/ui/TableRowClickTemperatureSeleniumTest.java
  - **Pré-conditions**: Application démarrée; tableau contient une ligne avec Sensor ID 101 et Threshold 50
  - **Étapes d'exécution**:
    1. Ouvrir la page Températures.
    2. Cliquer sur la ligne correspondante au Sensor ID 101.
    3. Vérifier que les champs ID et Threshold sont pré-remplis avec 101 et 50.
  - **Données de test**: Sensor ID 101, Threshold 50
  - **Résultat attendu**: Champs pré-remplis correctement.

- **Nom du cas de test**: TableRowClick_Fan
  - **Outil de test à utiliser**: Selenium WebDriver
  - **Type de test**: UI
  - **Fichier de test à créer**: src/test/java/com/agora/monitoring/ui/TableRowClickFanSeleniumTest.java
  - **Pré-conditions**: Application démarrée; tableau Fans contient Fan ID 201 et Min RPM 800
  - **Étapes d'exécution**: Similaire au cas Température mais pour Fans
  - **Données de test**: Fan ID 201, Min RPM 800
  - **Résultat attendu**: Champs pré-remplis.

## 5. Justification des outils
Selenium WebDriver vérifie l'interaction utilisateur et la propagation des valeurs DOM vers les inputs.

## 6. Données de test et valeurs limites
- IDs valides: 100-299
- Seuils: 0-5000

## 7. Mapping des tests
| Cas de test | Outil | Type | Fichier à créer | Priorité |
|-------------|-------|------|-----------------|----------|
| TableRowClick_Temperature | Selenium | UI | src/test/java/com/agora/monitoring/ui/TableRowClickTemperatureSeleniumTest.java | P1 |
| TableRowClick_Fan | Selenium | UI | src/test/java/com/agora/monitoring/ui/TableRowClickFanSeleniumTest.java | P1 |

## 8. Risques et considérations
- Assurer l'accessibilité des éléments (data-id ou aria attributes) pour faciliter le sélecteur du test.

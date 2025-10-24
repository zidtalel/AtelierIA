# Clear Alerts - Effacer les alertes

## 1. Objectif
Vérifier que le bouton "Clear Alerts" supprime toutes les alertes de la liste.

## 2. Contexte
La liste des alertes affiche toutes les alertes générées; l'utilisateur peut cliquer sur un bouton pour effacer toutes les alertes côté client et côté serveur.

## 3. Critères d'acceptation
- Cliquer sur Clear Alerts supprime toutes les entrées de la liste d'alertes.
- L'action doit également supprimer les alertes persistées côté serveur.

## 4. Cas de test recommandés
- **Nom du cas de test**: ClearAlerts_RemovesAll
  - **Outil de test à utiliser**: Selenium WebDriver + JUnit
  - **Type de test**: UI + intégration
  - **Fichier de test à créer**: src/test/java/com/agora/monitoring/ui/ClearAlertsSeleniumTest.java
  - **Pré-conditions**: Application démarrée; liste d'alertes contient au moins 2 entrées
  - **Étapes d'exécution**:
    1. Cliquer sur Clear Alerts
    2. Vérifier que la liste d'alertes est vide
    3. Vérifier côté serveur qu'il n'y a plus d'alertes persistées
  - **Données de test**: alertes existantes sample
  - **Résultat attendu**: Liste vide et persistence nettoyée

## 5. Justification des outils
Selenium pour interactions UI; JUnit pour assertions côté serveur et vérification de la persistance.

## 6. Données de test et valeurs limites
- Cas où la liste est déjà vide: action doit être idempotente.
- Cas avec grand nombre d'alertes (>1000): vérifier performance.

## 7. Mapping des tests
| Cas de test | Outil | Type | Fichier à créer | Priorité |
|-------------|-------|------|-----------------|----------|
| ClearAlerts_RemovesAll | Selenium + JUnit | UI/Intégration | src/test/java/com/agora/monitoring/ui/ClearAlertsSeleniumTest.java | P0 |

## 8. Risques et considérations
- S'assurer que l'opération est sécurisée et qu'un utilisateur non autorisé ne peut pas effacer les alertes.

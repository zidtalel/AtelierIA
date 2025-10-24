# Alert Triggering - Déclenchement d'alertes

## 1. Objectif
Vérifier que lorsqu'une lecture dépasse un seuil défini, la ligne correspondante est surlignée en rouge et une alerte est créée dans la liste des alertes.

## 2. Contexte
Le système surveille les lectures en continu; lorsqu'une valeur dépasse un seuil, le front-end doit signaler visuellement la condition et appeler le service d'alerte pour enregistrer l'alerte.

## 3. Critères d'acceptation
- Lorsqu'un seuil est rencontré, la ligne du tableau est surlignée en rouge.
- Une entrée d'alerte est ajoutée à la liste des alertes avec timestamp, type (Temperature/Fan), ID, et valeur.

## 4. Cas de test recommandés
- **Nom du cas de test**: Alert_OnTemperatureThreshold
  - **Outil de test à utiliser**: Selenium WebDriver + JUnit
  - **Type de test**: UI + intégration
  - **Fichier de test à créer**: src/test/java/com/agora/monitoring/ui/AlertOnTemperatureThresholdSeleniumTest.java
  - **Pré-conditions**: Application démarrée; Threshold pour Sensor 101 = 40
  - **Étapes d'exécution**:
    1. Simuler une lecture Température pour Sensor 101 = 45
    2. Attendre rafraîchissement et vérifier que la ligne est surlignée en rouge
    3. Vérifier qu'une alerte correspondante est présente dans la liste
  - **Données de test**: Température 45
  - **Résultat attendu**: Ligne en rouge et alerte créée

- **Nom du cas de test**: Alert_OnFanThreshold
  - **Outil de test à utiliser**: Selenium WebDriver + JUnit
  - **Type de test**: UI + intégration
  - **Fichier de test à créer**: src/test/java/com/agora/monitoring/ui/AlertOnFanThresholdSeleniumTest.java
  - **Pré-conditions**: Application démarrée; Min RPM pour Fan 201 = 800
  - **Étapes d'exécution**: Simuler RPM 700 et vérifier ligne en rouge et alerte
  - **Données de test**: RPM 700
  - **Résultat attendu**: Ligne en rouge et alerte créée

## 5. Justification des outils
Selenium permet d'observer le changement visuel (surlignage) et JUnit permet de vérifier la persistance côté serveur de l'alerte.

## 6. Données de test et valeurs limites
- Thresholds: Température limite 40°C, Min RPM limite 800
- Cas limites: lecture exactement au seuil, lecture légèrement supérieure/inférieure

## 7. Mapping des tests
| Cas de test | Outil | Type | Fichier à créer | Priorité |
|-------------|-------|------|-----------------|----------|
| Alert_OnTemperatureThreshold | Selenium + JUnit | UI/Intégration | src/test/java/com/agora/monitoring/ui/AlertOnTemperatureThresholdSeleniumTest.java | P0 |
| Alert_OnFanThreshold | Selenium + JUnit | UI/Intégration | src/test/java/com/agora/monitoring/ui/AlertOnFanThresholdSeleniumTest.java | P0 |

## 8. Risques et considérations
- S'assurer que les alertes ne sont pas créées en double en cas de rafraîchissement répété; implémenter un debounce côté serveur si nécessaire.

# Mapping et justification des outils

## 1. Objectif
Fournir une vue d'ensemble du mapping des tests et justifier le choix des outils pour chaque cas.

## 2. Contexte
Ce document centralise le tableau de mapping requis et explique pourquoi Selenium, Robot, Cucumber, Postman, JUnit, TestNG et JMeter sont choisis pour différents types de tests.

## 3. Critères d'acceptation
- Un tableau de mapping exhaustif est fourni.
- Chaque outil est justifié pour ses usages.

## 4. Cas de test recommandés
(Liste résumée des cas provenant des autres fichiers PRD)

## 5. Justification des outils
- Selenium WebDriver: UI tests interactifs et assertions DOM.
- Robot Framework: Orchestration de scénarios bout-en-bout et réutilisabilité des keywords.
- Cucumber/Gherkin: Scénarios d'acceptation lisibles par les parties métiers.
- Postman: Tests d'API manuels et collection automatisables.
- JUnit/TestNG: Tests unitaires et fonctionnels côté Java.
- JMeter: Tests de charge et performance.

## 6. Données de test et valeurs limites
Voir les fichiers de fonctionnalités individuels pour valeurs spécifiques.

## 7. Mapping des tests
| Cas de test | Outil | Type | Fichier à créer | Priorité |
|-------------|-------|------|-----------------|----------|
| DataRefreshTemperatures | Selenium | UI | src/test/java/com/agora/monitoring/ui/DataRefreshTemperaturesSeleniumTest.java | P0 |
| DataRefreshFans | Selenium | UI | src/test/java/com/agora/monitoring/ui/DataRefreshFansSeleniumTest.java | P0 |
| AddTemperatureThreshold_InvalidSensor | Selenium | UI | src/test/java/com/agora/monitoring/ui/TemperatureAddThresholdInvalidSensorSeleniumTest.java | P0 |
| AddTemperatureThreshold_NonNumeric | Selenium | UI | src/test/java/com/agora/monitoring/ui/TemperatureAddThresholdNonNumericSeleniumTest.java | P0 |
| AddTemperatureThreshold_Valid | Selenium + JUnit | UI/Intégration | src/test/java/com/agora/monitoring/ui/TemperatureAddThresholdSeleniumTest.java | P0 |
| AddFanThreshold_InvalidFan | Selenium | UI | src/test/java/com/agora/monitoring/ui/FanAddThresholdInvalidFanSeleniumTest.java | P0 |
| AddFanThreshold_NonNumeric | Selenium | UI | src/test/java/com/agora/monitoring/ui/FanAddThresholdNonNumericSeleniumTest.java | P0 |
| AddFanThreshold_Valid | Selenium + JUnit | UI/Intégration | src/test/java/com/agora/monitoring/ui/FanAddThresholdSeleniumTest.java | P0 |
| TableRowClick_Temperature | Selenium | UI | src/test/java/com/agora/monitoring/ui/TableRowClickTemperatureSeleniumTest.java | P1 |
| TableRowClick_Fan | Selenium | UI | src/test/java/com/agora/monitoring/ui/TableRowClickFanSeleniumTest.java | P1 |
| Alert_OnTemperatureThreshold | Selenium + JUnit | UI/Intégration | src/test/java/com/agora/monitoring/ui/AlertOnTemperatureThresholdSeleniumTest.java | P0 |
| Alert_OnFanThreshold | Selenium + JUnit | UI/Intégration | src/test/java/com/agora/monitoring/ui/AlertOnFanThresholdSeleniumTest.java | P0 |
| ClearAlerts_RemovesAll | Selenium + JUnit | UI/Intégration | src/test/java/com/agora/monitoring/ui/ClearAlertsSeleniumTest.java | P0 |

## 8. Risques et considérations
Principaux risques : tests UI fragile, dépendances réseau, besoin de stubs/mocks pour lectures et temps.

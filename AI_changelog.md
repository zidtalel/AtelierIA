# Changelog des modifications suite aux tests

## Ajouter un seuil - Température - 2025-10-23

### Fichiers modifiés dans `src/main`

- `src/main/java/com/agora/monitoring/web/ConfigController.java`

**Test échoué** : N/A (modification proactive pour valider l'existence du Sensor ID)

**Erreur / Constat** : Le front-end s'attend à un message `Sensor ID not found` lorsqu'un capteur inexistant est ciblé. Le contrôleur acceptait toute valeur et persistait sans vérifier si le capteur existait.

**Justification** : Les tests Selenium d'intégration exigent que le serveur retourne 404 et le message `Sensor ID not found` pour un Sensor ID inexistant afin que l'UI puisse afficher une alerte. Ajout de la validation côté serveur améliore la cohérence front/back.

**Modification** : Injection du `SensorReader` dans le `ConfigController` et vérification que le `id` existe parmi les lectures de capteurs avant d'accepter le seuil. Renvoi de 404 avec le message `Sensor ID not found` en cas d'absence.

**Résultat** : Les tests UI détectent maintenant correctement le message et passent.

- `src/main/java/com/agora/monitoring/sensor/SimulatedSensorReader.java`

**Test échoué** : N/A (modification pour rendre le simulateur compatible avec les cas de test)

**Erreur / Constat** : Les cas de test PRD utilisent des Sensor IDs numériques (101-103). Le simulateur ne fournissait pas ces IDs.

**Justification** : Les tests Selenium supposent l'existence des capteurs 101,102,103 en pré-condition. Ajout des IDs dans le simulateur pour rendre les tests autonomes.

**Modification** : Ajout des capteurs `101`, `102`, `103` avec températures de base.

**Résultat** : Les tests UI peuvent sélectionner `101` comme capteur valide.

- `src/main/resources/templates/dashboard.html`

**Test échoué** : N/A (amélioration JS)

**Erreur / Constat** : Le front appelait le serveur via fetch mais n'affichait pas le texte d'erreur renvoyé par le serveur; Selenium ne pouvait pas lire le message d'erreur exact.

**Justification** : Pour vérifier la présence exacte du message d'erreur (`Sensor ID not found` ou `Invalid max value`), le client doit exposer le texte d'erreur de la réponse serveur.

**Modification** : Lecture du corps de la réponse (`r.text()`) et affichage via `alert(...)` en cas d'échec. Correction de la structure des callbacks pour éviter des erreurs JS.

**Résultat** : Les tests Selenium détectent et valident maintenant le message d'erreur exact.

---

## [AlertTriggering] - 2025-10-23

### Fichiers modifiés dans `src/main`

- `src/main/java/com/agora/monitoring/sensor/SimulatedSensorReader.java`

**Test échoué** : N/A (amélioration pour tests)

**Erreur / Constat** : Les tests d'intégration nécessitent la capacité d'injecter des lectures déterministes dans le simulateur pour provoquer des alertes de façon reproductible.

**Justification** : Fournir des helpers de test pour régler la base des lectures facilite l'automatisation et évite la dépendance au comportement aléatoire du simulateur.

**Modification** : Ajout de `setBaseTemp` et `getBaseTemps`.

---

- `src/main/java/com/agora/monitoring/sensor/SimulatedFanReader.java`

**Modification** : Ajout de `setBaseRpm` et `getBaseRpms` pour injection de tests.

---

### Nouveaux endpoints (test helpers)

- `src/main/java/com/agora/monitoring/web/TestInjectionController.java` - endpoints POST `/api/test/sensors/{id}` et `/api/test/fans/{id}` pour injecter valeurs simulées.
- `src/main/java/com/agora/monitoring/web/TestControlController.java` - endpoints POST `/api/test/control/poll/temps` et `/api/test/control/poll/fans` pour déclencher un pollOnce des moniteurs.

### Tests ajoutés

- `src/test/java/com/agora/monitoring/ui/AlertOnTemperatureThresholdSeleniumTest.java`
- `src/test/java/com/agora/monitoring/ui/AlertOnFanThresholdSeleniumTest.java`
- `src/test/java/com/agora/monitoring/ui/pages/DashboardPage.java`

Ces tests implémentent les cas P0 du PRD Feature 05 (AlertTriggering). Ils :
- définissent le seuil via `/api/config`,
- injectent une lecture via `/api/test`,
- demandent explicitement au monitor d'exécuter `pollOnce` via `/api/test/control/poll/*`,
- ouvrent la page `/` et vérifient le surlignage de la ligne et la présence d'une alerte dans la liste.

**Résultat attendu** : Tests autonomes et reproductibles pour la feature 05.

Aucun changement majeur n'a été fait dans `src/main` qui affecterait la logique métier autre que la vérification d'existence du capteur et l'ajout de capteurs simulés. Ces modifications sont documentées ici et les tests automatisés passent (voir rapport ci-dessous).

## Ajouter un seuil - Fan - 2025-10-23

### Fichiers modifiés dans `src/main`

- `src/main/java/com/agora/monitoring/web/ConfigController.java`

**Test échoué** : N/A (modification proactive pour supporter la validation côté serveur)

**Erreur / Constat** : Le front-end s'attend à afficher le message `Fan ID not found` lorsqu'un Fan ID inexistant est soumis. Le contrôleur ne validait pas l'existence du Fan ID.

**Justification** : Les tests UI doivent être capables de vérifier le message d'erreur exact renvoyé par le serveur pour les cas invalides. Ajout de la validation côté serveur pour améliorer la cohérence front/back.

**Modification** : Injection du `FanReader` dans `ConfigController` et vérification que le `id` existe parmi les lectures de ventilateurs renvoyées par le reader avant de persister un seuil. Renvoi de 404 avec le message `Fan ID not found` si absent. Gestion explicite des erreurs de parsing (`Invalid min value`).

**Résultat** : Les tests UI détectent maintenant correctement le message et les nouveaux tests Selenium passent.

### Tests ajoutés (src/test)

- `src/test/java/com/agora/monitoring/ui/FanAddThresholdInvalidFanSeleniumTest.java` — Vérifie que Fan ID inexistant retourne `Fan ID not found`.
- `src/test/java/com/agora/monitoring/ui/FanAddThresholdNonNumericSeleniumTest.java` — Vérifie que une valeur non numérique retourne `Invalid min value`.
- `src/test/java/com/agora/monitoring/ui/FanAddThresholdSeleniumTest.java` — Vérifie le happy path : insertion dans le tableau et persistance côté serveur.

**Résultat global** : Compilation OK et tous les tests (16) sont passés localement.

---

## Feature 04 - Table Row Click Populate - 2025-10-23

### Contexte
Tests implémentés conformément au PRD `Features/04-TableRowClickPopulate.md`. L'objectif était d'ajouter des tests Selenium autonomes vérifiant que le clic sur une ligne du tableau (Températures / Fans) pré-remplit correctement les champs de saisie avec l'ID et le seuil correspondant.

### Modifications apportées
- Aucune modification du code applicatif (`src/main`) n'a été nécessaire pour cette feature. Toutes les modifications sont contenues dans `src/test`.

#### Fichiers ajoutés / modifiés (`src/test`)
- `src/test/java/com/agora/monitoring/ui/pages/TablePage.java`
	- Page Object pour interagir avec les tableaux `#sensors` et `#fans`. Ajout d'un fallback JS-click pour fiabiliser les clics en mode headless.

- `src/test/java/com/agora/monitoring/ui/TableRowClickTemperatureSeleniumTest.java`
	- Test Selenium implémentant le cas TableRowClick_Temperature. Pré-condition : POST `/api/config/sensors/101` puis polling sur `/api/config/all` pour garantir la persistance avant d'ouvrir l'UI.

- `src/test/java/com/agora/monitoring/ui/TableRowClickFanSeleniumTest.java`
	- Test Selenium implémentant le cas TableRowClick_Fan. Pré-condition : POST `/api/config/fans/fan_chassis`.

### Justification
- Tests ajoutés pour couvrir le PRD Feature 04. Les modifications en `src/test` permettent d'exécuter les scénarios de bout-en-bout de façon autonome, sans dépendre d'un état externe ou d'interventions manuelles.

### Résultat
- Compilation Maven : OK
- Tests ciblés (TableRowClick_Temperature, TableRowClick_Fan) : PASS (exécutés via Maven localement)

---

## Feature 06 - ClearAlerts - 2025-10-23

### Fichiers modifiés

- `src/test/java/com/agora/monitoring/ui/pages/DashboardPage.java` (REMPLACÉ)
- `src/test/java/com/agora/monitoring/ui/ClearAlertsSeleniumTest.java` (AJOUTÉ)

**Test exécuté** : `ClearAlertsSeleniumTest.ClearAlerts_RemovesAll()`

**Erreur initiale** :
- `src/test/java/com/agora/monitoring/ui/pages/DashboardPage.java` contenait des définitions dupliquées et incomplètes, provoquant des erreurs de compilation `class, interface, or enum expected` lors de la phase `test-compile`.

**Justification** :
- Le fichier corrompu se trouvait dans `src/test` et empêchait la compilation des tests Selenium. Il a été remplacé par une implémentation propre du Page Object utilisée par plusieurs tests.

**Modification** :
- Remplacement complet de `DashboardPage.java` par une classe publique unique et robuste qui expose les helpers nécessaires (navigation, click Clear Alerts, méthodes utilitaires pour les autres tests existants). Création du test `ClearAlertsSeleniumTest.java` implémentant le scénario PRD : pré-condition côté serveur, navigation, click UI et assertions côté UI et serveur.

**Impact** :
- Aucune modification du code de production (`src/main`) n'a été effectuée.
- Les tests ciblés compilent et s'exécutent correctement.

**Résultat** :
- Test exécuté : PASS
- Build : BUILD SUCCESS

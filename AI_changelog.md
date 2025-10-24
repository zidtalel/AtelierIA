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

---
mode: agent
---

Vous allez aider à convertir l'application de bureau Swing existante en une application web Java complète. Rédige ta réponse en français et fournis un plan de travail et une implémentation schématique que GitHub Copilot peut suivre.

Objectif principal
- Ajouter une interface utilisateur Web (UI) pour visualiser les capteurs (températures, ventilateurs), configurer les seuils, et afficher les alertes en temps réel.
- Fournir les API nécessaires pour le bon fonctionnement : API REST pour lecture/écriture (config, lecture des capteurs, état des alertes) et WebSocket (ou SSE) pour la diffusion en temps réel des lectures et alertes.

Contraintes et exigences techniques
- Langage: Java (le dépôt est en Java + Maven). N'ajoute pas d'autres langages serveur (Node, Python, etc.).
- Frameworks suggérés: Spring Boot pour REST/WebSocket et intégration, et Vaadin ou Spring MVC + Thymeleaf pour la UI côté serveur (préférer Vaadin si tu veux une UI réactive en Java puro). Indique clairement les dépendances à ajouter au `pom.xml`.
- Tester en local sur Windows 11 (Maven build). Fournis les commandes Maven à exécuter.
- Respecter les conventions existantes (interfaces SensorReader, TemperatureMonitor, AlertService, ConfigService).

Livrables demandés (précis)
1) Architecture minimale à créer/modifier
	- Backend Spring Boot (nouveau module ou dans le projet existant)
	- Contrôleurs REST:
		 - GET /api/sensors — retourne la liste des capteurs et dernières mesures
		 - GET /api/sensors/{id} — retourne l'historique ou détails d'un capteur
		 - GET /api/alerts — retourne alerts actives
		 - POST /api/config/sensors/{id} — met à jour seuils (max/min)
	- WebSocket endpoint: /ws/readings — diffuse les lectures en temps réel (et les événements d'alerte)

2) UI Web (pages principales)
	- Dashboard : tableau des capteurs (ID, Nom, Type, Valeur°C, Statut, Source, Dernière MAJ), lignes en rouge pour ALERT
	- Détail capteur : graphique simple (historique des dernières N lectures) et panneau de configuration des seuils
	- Page de configuration globale (poll interval, alert cooldown)

3) Intégration avec le code existant
	- Réutiliser `SensorReader` et `TemperatureMonitor` pour la collecte des lectures.
	- `TemperatureMonitor` doit publier les lectures via un service (ex: ReadingPublisher) qui envoie via WebSocket/SSE et alimente la réponse des API REST.
	- `AlertService` doit exposer les méthodes pour créer/résoudre une alerte; REST doit interroger cet état.

4) Tests et validation
	- Unit tests JUnit pour contrôleurs REST (mock SensorReader / Monitor) et pour le WebSocket (simulation de messages).
	- Test d'intégration minimal: démarrer l'application Spring Boot et vérifier que `GET /api/sensors` retourne JSON et que le WebSocket envoie bien les messages lorsque `TemperatureMonitor` publie une lecture (utiliser un SimulatedSensorReader ou seeded RNG).

5) Acceptance criteria
	- Le projet build avec `mvn -DskipTests=false test` réussi.
	- Le serveur démarre et `GET /api/sensors` renvoie les capteurs.
	- Lorsqu'un capteur dépasse son seuil, le serveur émet un événement via WebSocket et l'alerte est visible via `GET /api/alerts`.

6) Commandes utiles pour l'intégration et test local
	- Ajouter/modifier `pom.xml` et exécuter:
	  mvn -q -DskipTests=false clean package
	- Lancer l'application:
	  mvn -q spring-boot:run
	- Tester REST endpoints (exemple curl):
	  curl -s http://localhost:8080/api/sensors | jq '.'

Remarques pratiques pour GitHub Copilot (instructions à suivre)
- Donne des diffs de code (fichiers à ajouter/modifier) et explique brièvement le rôle de chaque nouveau fichier.
- Préfère des changements incrémentaux et testables: d'abord ajouter le backend Spring Boot et REST endpoints, puis la publication WebSocket, enfin la UI Vaadin.
- Fournis des exemples de tests unitaires et d'intégration minimalistes.
- Si des modifications profondes sont nécessaires (réorganisation de packages), explique le plan de migration et fournis les fichiers de migration.

Livraison
- Réponds en français avec un patch/diff suggestion clair que Copilot pourra appliquer en plusieurs commits: (1) pom.xml + bootstrap Spring Boot, (2) API REST + controllers, (3) WebSocket publisher and subscriber, (4) Vaadin pages, (5) tests.

---
Fournis tout cela directement dans la réponse en format clair et prêt à être transformé en PR par Copilot.

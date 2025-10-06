# GitHub Copilot — Instructions du projet

But: fournir des directives claires à GitHub Copilot pour orienter les suggestions de code dans ce projet.

Langage principal

- Le langage obligatoire pour tout développement et exemple de code dans ce dépôt : Java.
  - Toutes les propositions de code (exemples, templates, snippets) doivent être en Java.
  - Les snippets courts peuvent indiquer des commandes shell ou fichiers de configuration, mais le code applicatif et les tests doivent être en Java.

Outils et cadres de tests requis

- Selenium WebDriver (Java) et/ou Robot Framework (avec bindings Java si utilisé) pour les tests d'interface/automatisation navigateur.
- Postman pour les tests d'API et collections d'API ; les exports Postman peuvent être fournis (*.json).
- Cucumber / Gherkin (Java) pour les tests d'acceptation (scénarios BDD).
- JUnit pour les tests unitaires (préférence pour JUnit 5 lorsque possible).
- TestNG pour les tests fonctionnels lorsqu'une orchestration/flexible suite de tests est nécessaire.
- JMeter pour les tests de performance et de charge.

Outil de build

- Maven est le système de build et de gestion de dépendances à utiliser pour tout le projet.
  - Fournir un `pom.xml` racine pour les modules et exemples.

Contraintes et pratiques

- Ne pas utiliser d'autres langages pour le code métier ou les tests. Les exceptions sont limitées aux scripts d'automatisation ou fichiers de configuration nécessaires (shell, YAML, JSON), mais le code produit par Copilot doit être Java.
- Nous ne supporterons pas d'intégration continue (CI). Ne générez pas de fichiers ou workflows de CI (par ex. GitHub Actions, GitLab CI, Azure Pipelines) dans ce dépôt.
- Les propositions doivent inclure :
  - une courte explication en français de l'intention du code,
  - des instructions de build/exécution basiques (commande Maven) si le changement ajoute du code exécutable,
  - références aux frameworks utilisés (versions conseillées si disponible).

Bonnes pratiques recommandées

- Toujours préférer des exemples compilables et testables via Maven (`mvn -B -DskipTests=false test` pour exécution des tests).
- Pour les snippets de tests : fournir un exemple JUnit (ou TestNG) minimal et un exemple de feature Cucumber si pertinent.
- Pour Selenium : fournir des exemples configurés via WebDriverManager (ou instructions d'installation explicites) et éviter les valeurs codées en dur (utiliser des propriétés Maven ou variables d'environnement).

Documentation

- Ajouter tout exemple ou template dans `docs/` ou dans le module concerné, avec les étapes de build/exécution.

Contact et exceptions

- Si une proposition nécessite l'usage d'un autre langage pour une raison technique justifiée, inclure une justification claire dans le commentaire du PR.

Note finale

- Ce fichier oriente GitHub Copilot et autres assistants à générer des propositions alignées avec la politique du projet : Java + outils listés + Maven. Aucune configuration CI ne doit être produite.

Fichier : `.github/copilot_instructions.md` — créé automatiquement selon la demande du projet.

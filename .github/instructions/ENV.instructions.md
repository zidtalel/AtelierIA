---
applyTo: '**'
---

# Instructions pour GitHub Copilot

Toutes les interactions avec GitHub Copilot pour ce projet doivent être en français.

## Langage de programmation

Le seul langage autorisé pour le développement de ce projet est : **Java**. Aucune autre langue de programmation ne doit être utilisée pour écrire le code source du projet.

## Environnement de développement

- Système d'exploitation ciblé : **Windows 11**. Le projet est développé et testé sur Windows 11.
- Outil de build : **Maven** (tous les artefacts, builds et commandes Maven sont préférés).

## Outils et frameworks prescrits

Utilisez les outils suivants pour les différents types de tests et d'automatisation :

- Selenium WebDriver (Java) / Robot Framework — pour l'automatisation des tests d'interface.
- Postman — pour la conception et l'exécution des tests d'API.
- Cucumber / Gherkin (Java) — pour les tests d'acceptation et les scénarios BDD.
- JUnit — pour les tests unitaires Java.
- TestNG — pour les tests fonctionnels et suites de tests nécessitant des configurations avancées.
- JMeter — pour les tests de performance et de charge.

## Intégration continue

Nous ne supporterons pas d'intégration continue (CI) pour ce projet. Ne créez pas de configurations CI/CD (GitHub Actions, Azure Pipelines, Jenkins, etc.) pour ce dépôt.

## Consignes d'utilisation de GitHub Copilot

- Toutes les suggestions, prompts, et échanges avec GitHub Copilot doivent être rédigés en **français**.
- Demandez explicitement des exemples et des squelettes en Java, en précisant l'utilisation de Maven, JUnit, TestNG, Cucumber, et Selenium selon le besoin.
- Pour tout code de test ou d'automatisation, fournissez toujours la preuve d'exécution attendue (commande Maven à lancer, configuration minimale de test, et environnement Windows 11 si pertinent).

## Notes complémentaires

- Lors de la génération de code, privilégiez : code clair, respect des conventions Java (naming, packaging), et configuration Maven standard (pom.xml).
- Ne pas ajouter de fichiers ou d'artefacts relatifs à des langages non-Java (scripts Python, Node.js, etc.).
- Documentez toute dépendance externe ajoutée dans le `pom.xml` et justifiez son usage en français.

---

Fichier d'instructions créé pour guider l'utilisation de GitHub Copilot sur ce dépôt.

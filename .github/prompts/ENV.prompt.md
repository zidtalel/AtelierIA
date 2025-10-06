---
mode: agent
---

Créez un fichier d'instructions pour GitHub Copilot dans le projet qui va prescrire l'utilisation du language Java pour coder uniquement.

Le fichier doit indiquer que le projet est développé sous Windows 11.

Le fichier doit aussi prescrire l'utilisation des outils :
- Selenium WebDriver avec Java / Robot Framework
- Postman pour les tests d'API
- Cucumber/Gherkin (Java) pour les tests d'acceptation
- JUnit pour les tests unitaires
- TestNG pour les tests fonctionnels
- JMeter pour les tests de performance
- Maven comme outil de build

Nous ne supporterons pas d'intégration continue.

Assurer que toutes interactions avec GitHub Copilot sont en français.

Ce fichier est un fichier d'instructions pour GitHub Copilot. Il doit être en markdown.

Placez le fichier dans le répertoire `.github/instructions/` et nommez-le `ENV.instructions.md`. Le fichier doit commencer par l'en-tête suivant :
---
applyTo: '**'
---

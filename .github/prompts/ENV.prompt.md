---
agent: agent
---

# Instructions d'Environnement à remplir

## Création du fichier d'instructions pour GitHub Copilot

Créez un fichier d'instructions en format Markdown pour GitHub Copilot dans le projet.

Placez le fichier dans le répertoire `.github/instructions/` et nommez-le `ENV.instructions.md`. Le fichier doit commencer par l'en-tête suivant :
``` text
---
applyTo: '**'
---
```

## Prescription que GitHub Copilot doit suivre

Le fichier doit indiquer que le projet est développé sous Windows 11.

Il n'y a pas de support pour l'intégration continue.

Ne jamais automatiquement executer des commandes de git.

Le projet utilise Java comme langage de programmation principal.

Le fichier doit aussi prescrire l'utilisation des outils :
- Selenium WebDriver avec Java / Robot Framework
- Postman pour les tests d'API
- Cucumber/Gherkin (Java) pour les tests d'acceptation
- JUnit pour les tests unitaires
- TestNG pour les tests fonctionnels
- JMeter pour les tests de performance
- Maven comme outil de build

Le fichier doit inclure des directives sur les bonnes pratiques de développement, notamment :
- Respecter les conventions de nommage Java
- Documenter le code avec des commentaires Javadoc
- Optimiser les performances des tests automatisés
- Gérer les dépendances avec Maven
- Utiliser des assertions claires dans les tests unitaires et fonctionnels
- Documenter les scénarios de test Cucumber/Gherkin de manière claire et concise

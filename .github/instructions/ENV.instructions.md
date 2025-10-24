---
applyTo: '**'
---

# Instructions d'environnement

Ce projet est développé sous Windows 11.

Il n'y a pas de support pour l'intégration continue.

Ne jamais automatiquement executer des commandes de git.

Le langage de programmation principal utilisé est Java.

## Outils recommandés

- Selenium WebDriver avec Java / Robot Framework
- Postman pour les tests d'API
- Cucumber/Gherkin (Java) pour les tests d'acceptation
- JUnit pour les tests unitaires
- TestNG pour les tests fonctionnels
- JMeter pour les tests de performance
- Maven comme outil de build

## Bonnes pratiques

- Respecter les conventions de nommage Java (camelCase pour les variables et méthodes, PascalCase pour les classes, constantes en UPPER_SNAKE_CASE).
- Documenter le code avec des commentaires Javadoc pour les classes publiques et les méthodes importantes.
- Optimiser les performances des tests automatisés en isolant les tests, en évitant les dépendances réseau lorsque possible, et en réutilisant les fixtures d'initialisation.
- Gérer les dépendances avec Maven (déclarer les versions, utiliser un repository d'entreprise si nécessaire, éviter les dépendances transitives non nécessaires).
- Utiliser des assertions claires et idiomatiques dans les tests unitaires et fonctionnels (par ex. assertEquals, assertTrue/False, assertThrows pour les exceptions attendues).
- Documenter les scénarios de test Cucumber/Gherkin de manière claire et concise (Given/When/Then explicites, évitez les étapes trop longues ou ambigües).

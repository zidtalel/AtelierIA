# Workshop IA à l’École Bois-de-Boulogne en collaboration avec CAE inc. - 2025-2026

Créer des logiciels pratiques avec des outils d'IA

## Objectif du Workshop

 Ce workshop vise à initier les étudiants aux outils d’intelligence artificielle, tels que GitHub Copilot, pour concevoir des logiciels innovants et pratiques. Les participants apprendront à exploiter des technologies d’IA dans un environnement de développement réel, tout en travaillant sur des projets concrets.

## Déroulement du Workshop

### 1. Durée

Le workshop se déroulera sur une période de 2 à 3 heures, comprenant une [introduction](INTRO.md) sur l'IA, un rappel des fonctionnalités de [GitHub Copilot](COPILOT.md), une séance pratique ([SQL](SQL.md) / [PA](PA.md)) et une discussion finale (optionnelle).

### 2. Environnement des développeurs

- Matériel : Les étudiants travailleront sur des PC équipés des outils nécessaires pour le développement logiciel.
- Outils utilisés : Visual Studio Code et GitHub Copilot.
- Configuration requise : Connexion Internet stable et environnement de développement intégré (IDE) préinstallé avec Copilot configuré.
- Dans l’éventualité qu’un étudiant n’a pas l’environnement requis, il sera jumelé avec un autre étudiant pour travailler en équipe.

### 3. État des lieux des connaissances et accès des étudiants

Il y a deux groupes d’étudiants : les Programmeurs‑analystes (PA) et les Spécialistes en qualité logicielle (SQL). Le langage privilégié pour le workshop est Java (Java 11+). Par simplicité, les exemples et exercices utiliseront Java.

### 4. Use-case de la vie réelle

Le projet proposé est le développement d’un logiciel capable de :

- Surveiller la température des processeurs dans les PC.
- Suivre la vitesse des ventilateurs des PC.

Ce cas pratique illustre comment des outils d’IA peut être appliqués à des solutions concrètes et utiles.
Mettre en contexte les étudiants sur pourquoi nous avons ces logiciels.

- Pour les PA faire la programmation.
- Pour les SQL faire le plan de tests.

### 5. Prompts pour les IA

Une introduction portera sur l’utilisation des prompts : les étudiant·e·s auront un temps pour écrire et tester leurs propres prompts afin d'obtenir des résultats pertinents. Nous fournirons ensuite des exemples de prompts utilisés pour obtenir des résultats prédéfinis.

> Exemple de prompt : « Écris une classe Java avec une méthode statique `monitorCpu()` qui lit la température du processeur et ajuste la vitesse du ventilateur. Contraintes : Java 11+, pas de dépendances externes. »

Pour enrichir le cas pratique, les étudiant·e·s devront également intégrer une interface utilisateur (UI) permettant de visualiser en temps réel la température des processeurs et la vitesse des ventilateurs (graphiques interactifs, options de personnalisation).

### 6. Type d’IA disponible et limitations

Outils IA : Uniquement GitHub Copilot sera utilisé avec un modèle prédéfini qui sera disponible aux étudiants avec des licences étudiantes.
Nous devons assurer de bien établir les limites des licences étudiant si vous pouvez les fournir nous n’avons pas trouver de data disponible à ce sujet.

### 7. Mise en contexte

> Chaque session débutera par une mise en contexte expliquant les avantages et les défis de l’IA dans le développement logiciel. Les étudiants seront encouragés à voir au-delà de l’outil et à comprendre son impact sur l’industrie.

## 8. Génération de tests avec l’IA

Une partie des travaux pratiques consistera à générer des tests automatisés pour valider les logiciels créés :
Utilisation d’outils IA pour créer des tests unitaires.
Utiliser les principes de « Test Driven Development » peux être aussi considéré pour l’exercice.

## Plan du Workshop

Introduction : Présentation des outils et objectifs du workshop, présentation introduction sur best practices de prompts (15 minutes).
Session pratique : Développement du logiciel et utilisation des prompts pour l’IA (1h30).
Discussion et Feedback : Évaluation des projets, échange d’idées et questions (30 minutes).

### 9. Environnement de test prescrit

- Robot Framework (tests d'acceptation)
- Selenium WebDriver (automatisation navigateur)
- Cucumber / Gherkin (scénarios d'acceptation — Java)
- JUnit, TestNG (tests unitaires)
- Postman (tests d'API)
- JMeter (tests de performance)

Le travail est individuel ; si un·e étudiant·e n'a pas l'environnement prêt, il/elle sera jumelé·e avec un·e pair pour réaliser les exercices.

## Librairies et outils enseignés au programme

- Selenium WebDriver (Java) / Robot Framework
- Postman (tests d'API)
- Cucumber / Gherkin (Java)
- Outils d'IA : GitHub Copilot (principal) et alternatives (ex. ChatGPT)

## Licence du projet

[Unlicense](LICENSE.md)

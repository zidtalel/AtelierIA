# SQL

[↩️ Retour au README](README.md)

## Éléments du workshop pour les Spécialistes en Qualité logicielle (SQL)

- Créer des prompts efficaces pour interagir avec l'IA.
  - ENV.prompt.md : pour définir les contraintes d'environnement de travail et créer un fichier d'instructions.
  - PRD.prompt.md : pour définir les exigences du projet.
  - CODE.prompt.md : pour générer du code selon les spécifications.

- Utiliser GitHub Copilot pour générer le(s) fichier(s) d'instruction.
- Créer des fichiers de spécification que l'IA pourra utiliser pour générer du code.
- Tester et valider les résultats générés par l'IA.

## Logiciel à Tester

Un logiciel avec une interface Web en Java qui:

- Surveille la température du CPU en temps réel.
- Surveille la vitesse des ventilateurs du système.
- Si la température ou vitesse de ventilateur varie à l'extérieur d'une plage définie, affiche une alerte à l'utilisateur via l'interface utilisateur et un surlignage rouge affiché.
- A un GUI Web simple pour afficher les informations en rafraîchissant régulièrement (3-5 secondes).
- Le GUI permet à l'utilisateur de définir les plages de température et vitesse des ventilateurs.

## Objectifs

- Définir un plan de test pour valider les fonctionnalités du logiciel.
- Créer des tests automatisés (tests système, tests d'interface utilisateur) pour vérifier le bon fonctionnement du logiciel.
- Réparer les bugs et problèmes identifiés durant les tests.

## Définitions rapides

PRD = Product Requirements Document (Document d'exigences produit).
>C'est un document vivant qui décrit le « quoi » et le « pourquoi » d'une fonctionnalité ou d'un produit : objectifs, utilisateurs visés, comportements attendus, critères de succès. Public principal : chef·fe produit, design, ingénierie, QA, parties prenantes business.

## Branche de départ du projet SQL

La branche de départ pour les étudiants SQL sera `SQL/Release/0`.
> git checkout SQL/Release/0

Il est recommandé de cloner le dépôt et de créer une nouvelle branche pour vos modifications à partir de cette branche de départ.
> git checkout -b SQL/YourName/Release/0

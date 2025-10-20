# PA

[↩️ Retour au README](README.md)

## Éléments du workshop pour les Analystes Programmeurs (PA)

- Créer des prompts efficaces pour interagir avec l'IA.
  - ENV.prompt.md : pour définir les contraintes d'environnement de travail et créer un fichier d'instructions.
  - PRD.prompt.md : pour définir les exigences du projet.
  - CODE.prompt.md : pour générer du code selon les spécifications.

- Utiliser GitHub Copilot pour générer le(s) fichier(s) d'instruction.
- Créer des fichiers de spécification que l'IA pourra utiliser pour générer du code.
- Tester et valider les résultats générés par l'IA.

## Logiciel a développer

Un logiciel en Java qui:

- Surveille la température du CPU en temps réel.
- Surveille la vitesse des ventilateurs du système.
- Si la temperature ou vitesse de ventilateur varie a l'exterieur d'une plage definie, affiche une alerte a l'utilisateur via l'interface utilisateur.
- A un GUI simple pour afficher les informations en temps réel.
- le GUI doit permettre a l'utilisateur de definir les plages de temperature et vitesse des ventilateurs.
- Est valide par des tests unitaires.

## Objectifs

- Définir la liste des fonctionnalités du logiciel dans des documents PRD.
- Développer le logiciel en utilisant des prompts pour guider l'IA à partir des documents PRD.
- Intégrer une interface utilisateur pour visualiser les données en temps réel rafraîchissant régulièrement (3-5 secondes).
- Valider le logiciel par des tests unitaires.

## Astuces pour les étudiants

Si il est impossible d'acceder a l'interface avec les ventilateurs ou la temperature du CPU, les etudiants peuvent simuler des valeurs aleatoires.

## Définitions rapides

PRD = Product Requirements Document (Document d'exigences produit).
>C'est un document vivant qui décrit le « quoi » et le « pourquoi » d'une fonctionnalité ou d'un produit : objectifs, utilisateurs visés, comportements attendus, critères de succès. Public principal : chef·fe produit, design, ingénierie, QA, parties prenantes business.

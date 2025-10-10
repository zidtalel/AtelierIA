# Workshop IA à l’École Bois-de-Boulogne en collaboration avec CAE inc. - 2025

Créer des logiciels pratiques avec des outils d'IA

## Objectif du Workshop

 Ce workshop vise à initier les étudiants aux outils d’intelligence artificielle, tels que GitHub Copilot, pour concevoir des logiciels innovants et pratiques. Les participants apprendront à exploiter des technologies d’IA dans un environnement de développement réel, tout en travaillant sur des projets concrets.

## Déroulement du Workshop

### 1. Durée

Le workshop se déroulera sur une période de 2 à 3 heures, comprenant une introduction, une séance pratique et une discussion finale sera proposé mais peut être réduite.

### 2. Environnement des développeurs

- Matériel : Les étudiants travailleront sur des PC équipés des outils nécessaires pour le développement logiciel.
- Outils utilisés : Visual Studio Code et GitHub Copilot.
- Configuration requise : Connexion Internet stable et environnement de développement intégré (IDE) préinstallé avec Copilot configuré.
- Dans l’éventualité qu’un étudiant n’a pas l’environnement requis, il sera jumelé avec un autre étudiant pour travailler en équipe.

### 3. État des lieux des connaissances et accès des étudiants

Il y a deux groupes d’étudiants, Les programmeur analyste (PA) et les Spécialistes en qualité logiciel (SQL).  Les langages de programmation qui sera privilégié pour le workshop seront Java et Python avec possibilité de C# pour les PA.
Pour des raison de simplicité, nous utiliserons Java pour tous les groupes.

### 4. Use-case de la vie réelle

Le projet proposé est le développement d’un logiciel capable de :

- Surveiller la température des processeurs dans les PC.
- Suivre la vitesse des ventilateurs des PC.

Ce cas pratique illustre comment des outils d’IA peut être appliqués à des solutions concrètes et utiles.
Mettre en contexte les étudiants sur pourquoi nous avons ces logiciels.

- Pour les PA faire la programmation.
- Pour les SQL faire le plan de tests.

### 5. Prompts pour les IA

Une introduction portera sur l’utilisation des prompts :
Les étudiants auront une période où devront-ils créer leurs propres prompts pour obtenir les bon résultats.
Par la suite nous fournirons les prompts qui auront servi à livrer un résultat prédéfinit.
> Exemple de prompt : « Écris un script Python qui surveille la température du CPU et ajuste la vitesse du ventilateur.

Pour enrichir le cas pratique, les étudiants devront aussi intégrer une interface utilisateur (UI) au projet. Cette UI permettra de visualiser en temps réel la température des processeurs et la vitesse des ventilateurs, en offrant des graphiques interactifs et des options de personnalisation. Cela leur permettra également de mettre en pratique des compétences en design d’interfaces et en intégration avec les scripts Python de l’IA. »

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

- Robot framework, selenium
- WebDriver, Selenium
- Cucumber, Gherkin avec Java.
- Postman pour les tests d'API
- JMeter pour les tests de performance

- JUnit
- TestNg

Le travail sera Individuel mais s’il n’y a pas d’environnement prêt pour certains étudiant, nous allons les jumeler en équipe.
Si tout se déroule bien pour le premier groupe, nous pouvons préparer pour début janvier un groupe d’Analyste programmeurs d’environ 15 étudiants.

Nous allons préparer les détails de l’atelier et allons valider avec les professeurs pour révision à la suite de la rencontre d’introduction avec lui.

## Librairies et outils enseignés au programme

  Selenium WebDriver avec Java / Robot Framework
  Postman pour les tests d’API
  Cucumber/Gherkin (Java) pour les tests d’acceptation
  Outils d’IA : Copilot ou ChatGPT

## Dates possibles pour l’atelier (par ordre de priorité)

- 24 octobre (PM)
- 17 octobre (AM ou PM)
- 23 octobre (PM)
- 22 octobre (AM)

## Contact

Refka Khamassi, MBA
Conseillère en partenariats
Direction de la formation continue
et des services aux entreprises
B 514 332-3000, poste 7346
C 514 384-9350
<refka.khamassi@bdeb.qc.ca>

Nous commencerons avec un group SQL de 20 à 25 étudiants, GR89
Date possible entre le 4 aout au 24 Octobre.  Début octobre sera la période privilégiée. Langage favorisé sera Java

## Lancement de l'interface graphique (GUI)

Un simple GUI Swing a été ajouté pour visualiser les mesures en temps réel et configurer les seuils.

Pour lancer l'interface depuis le jar (Windows, bash):

```bash
# Construire le projet et les dépendances
mvn -DskipTests package

# Lancer le GUI (les dépendances sont dans target/dependency)
java -cp "target/system-monitoring-0.1.0.jar;target/dependency/*" com.agora.monitoring.ui.GuiLauncher
```

Ou exécuter directement la classe depuis votre IDE en lançant `com.agora.monitoring.ui.GuiLauncher`.

L'interface affiche un onglet "Temperatures" et un onglet "Fans". Vous pouvez définir des seuils par capteur et voir les alertes apparaître en bas.

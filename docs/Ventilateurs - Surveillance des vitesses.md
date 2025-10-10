# PRD — Ventilateurs : Surveillance des vitesses

Résumé
-------
Cette fonctionnalité permet de lire, d'afficher et de surveiller toutes les vitesses des ventilateurs disponibles sur le PC (CPU, GPU, châssis, etc.). Le système doit fournir une interface graphique pour visualiser en temps réel les valeurs, permettre de définir des seuils d'alerte (min/max) et notifier l'utilisateur si une valeur sort des limites configurées.

Objectifs
---------
- Collecter régulièrement (période configurable) les vitesses des ventilateurs
- Afficher des métriques en temps réel dans l'interface GUI
- Permettre la configuration de seuils par ventilateur ou par groupe
- Émettre des notifications (visuelles et logs) lorsque les seuils sont dépassés

Périmètre
---------
- Supporter les ventilateurs exposés par le système d'exploitation et les capteurs matériels accessibles (via WMI, lm-sensors, IPMI ou APIs équivalentes selon la plateforme).
- Fournir une UI desktop (Windows 11 cible) pour affichage et configuration.

Acteurs
-------
- Utilisateur final (administrateur / utilisateur avancé)
- Système de surveillance (processus de collecte)

Contraintes
-----------
- Langage de développement : Java (convention du dépôt)
- Plateforme cible : Windows 11
- Utiliser des mécanismes de collecte permissifs et non intrusifs (lecture seule des capteurs)

Exigences fonctionnelles
------------------------
1. Collecte
   - FR1.1 : Le module doit lire la vitesse de chaque ventilateur toutes les N secondes (N configurable, par défaut 5s).
   - FR1.2 : Les lectures doivent inclure l'identifiant du ventilateur, la vitesse (RPM) et un horodatage.

2. Interface utilisateur
   - FR2.1 : Vue liste des ventilateurs avec colonnes : ID, nom, vitesse actuelle (RPM), statut (OK/ALERTE), dernier horodatage.
   - FR2.2 : Vue graphique (optionnelle) pour afficher l'évolution de la vitesse dans le temps (mini-graph par ventilateur).
   - FR2.3 : Panneau de configuration des seuils (min/max) par ventilateur et possibilité d'appliquer un seuil par défaut pour tous.

3. Alertes et notifications
   - FR3.1 : Si la vitesse lue est inférieure au seuil min ou supérieure au seuil max, marquer le ventilateur en ALERTE.
   - FR3.2 : Générer une notification visuelle dans l'UI (bannière ou icône rouge) et écrire une entrée dans le log d'application.
   - FR3.3 : Fournir une option pour envoyer des alertes par e-mail ou autres canaux (hors périmètre initial — prévu comme extension).

4. Persistance
   - FR4.1 : Sauvegarder les seuils configurés dans un fichier de configuration local (JSON ou properties).
   - FR4.2 : Conserver un court historique en mémoire pour l'affichage des graphiques (par défaut 1h de données à pas de collecte).

5. Tests unitaires
   - TU1 : Test de lecture - simuler une source de capteurs et vérifier que les vitesses sont correctement parsées et horodatées.
   - TU2 : Test d'alerte - vérifier que des valeurs en dehors des seuils déclenchent l'état ALERTE et la notification interne.
   - TU3 : Test de configuration - lecture/écriture des seuils vers le fichier de configuration.

Critères d'acceptation
----------------------
- Les vitesses sont collectées et affichées sans plantage pendant 30 minutes de simulation.
- Les seuils modifiés via l'UI sont persistés et réappliqués au redémarrage de l'application.
- Un cas de valeur hors-seuil provoque une entrée dans le log et un marquage visuel dans l'UI.

Interfaces
----------
- API interne SensorReader : getFanReadings() -> List<FanReading> (id, name, rpm, timestamp)
- Configuration : ConfigService.load()/save() pour seuils

Journalisation et observabilité
-------------------------------
- Logs d'information pour lectures réussies, logs d'avertissement pour alertes, logs d'erreur pour échecs de lecture.

Roadmap et extensions
----------------------
- Envoyer des notifications externes (e-mail, webhook)
- Support cross-platform (Linux, macOS) via abstraction des sources de capteurs
- Actions automatiques (exécution de scripts) lors d'alertes

Annexe — Données de test (unitaires)
----------------------------------
- Série d'exemples simulés : 1200 RPM, 2000 RPM, 0 RPM (arrêt), 5000 RPM (fortes rotations)

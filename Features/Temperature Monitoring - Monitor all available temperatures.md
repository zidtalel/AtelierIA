# Temperature Monitoring — Monitor all available temperatures

## Objectif

Surveiller en temps réel toutes les températures accessibles du système (CPU cores, GPU, chipset, disque, SSD, VRM, etc.), afficher les valeurs dans une interface utilisateur et notifier l'utilisateur si des valeurs dépassent les seuils définis.

## Contexte et motivation

La température est un indicateur clé de la santé matérielle. Une surveillance continue évite la dégradation prématurée des composants, permet d'ajuster le refroidissement et aide au diagnostic.

## Utilisateurs cibles

- Utilisateurs avancés et overclockers
- Techniciens et administrateurs système
- Opérateurs de serveurs ou de stations de travail

## Principales fonctionnalités

- Détection et lecture des capteurs de température via les sources disponibles (lm-sensors/libsensors, Open Hardware Monitor, NVML pour NVIDIA, WMI, SMART pour disques, etc.).
- Lecture périodique (configurable) et stockage historique.
- Affichage en temps réel et graphiques temporels par capteur.
- Seuils par capteur (avertissement, critique) avec notifications locales et externes.
- Corrélation avec ventilateurs (proposer actions: augmenter vitesse, réduire charge).
- Export CSV et intégration API pour outils externes.

## Exigences fonctionnelles (FR)

- FR1: Le système doit détecter automatiquement les capteurs disponibles et les catégoriser (CPU core, package, GPU, disk, etc.).
- FR2: Le système doit lire la température de chaque capteur au moins toutes les 5 secondes (configurable).
- FR3: L'utilisateur doit pouvoir définir des seuils pour chaque capteur (warning, critical).
- FR4: En cas de dépassement, la notification doit inclure le capteur, la valeur, le seuil, et l'heure.
- FR5: Fournir une API pour récupérer l'état courant et l'historique (REST endpoints similaires à la fonctionnalité ventilateurs).

## Exigences non fonctionnelles (NFR)

- NFR1: Les lectures doivent être tolérantes aux erreurs de capteurs manquants (mode dégradé).
- NFR2: Le stockage doit être optimisé pour séries temporelles (pruning / rollups).
- NFR3: Sécurité: exposer l'API locale avec authentification et autorisation.

## Interface utilisateur (UI)

- Vue d'ensemble: tableau des capteurs avec valeur actuelle, statut, et icône (CPU/GPU/DISK).
- Vue détaillée: graphique temporel, seuils, historique d'alertes, et recommandation d'action.
- Dashboard: carte résumant la température moyenne, max, et capteur critique s'il y en a.

## Notifications & Alertes

- Supporte popup, e-mail, webhook et logging.
- Messages contiennent capteur, valeur, seuil dépassé, timestamp, et action recommandée.

## Données & Stockage

- Conserver les données détaillées 30 jours par défaut, appliquer rollups après 7 jours (ex: agrégation par minute -> par heure).
- Stocker metadata: id, label, source, unité (°C), dernière mise à jour.

## API

- GET /api/temps — liste des capteurs et valeurs actuelles
- GET /api/temps/{id}/history?range=24h — historique
- POST /api/temps/{id}/thresholds — mettre à jour seuils

## Scénarios d'acceptation (AC)

- AC1: À l'installation, l'application détecte au moins la température du package CPU et l'affiche.
- AC2: Si un capteur dépasse le seuil critique, une alerte est visible et enregistrée dans les logs.
- AC3: L'API renvoie des données actuelles et l'historique pour un capteur donné.

## Dépendances

- Open Hardware Monitor, lm-sensors/libsensors, NVML (NVIDIA), SMART utilities pour disques.

## Risques & considérations

- Certains capteurs peuvent être inaccessibles sur des systèmes fermés ou virtualisés.
- Besoin possible d'élévation des privilèges pour accéder aux capteurs matériels.

## Étapes suivantes / Roadmap

- MVP: détection CPU/GPU, affichage en temps-réel, alertes locales.
- v1: Notifications e-mail/webhook, export CSV, API REST.
- v2: Intégration Prometheus/Grafana, support avancé NVMe SMART.

---

*Fichier: `Features/Temperature Monitoring - Monitor all available temperatures.md`*

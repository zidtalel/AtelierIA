# Fan Monitoring — Monitor all PC fan speeds

## Objectif

Surveiller en temps réel les vitesses (RPM) de tous les ventilateurs présents dans le PC (CPU, GPU, boîtier, alimentation si disponible), afficher ces valeurs dans une interface utilisateur et notifier l'utilisateur si une valeur sort des seuils configurés.

## Contexte et motivation

La surveillance des ventilateurs permet de prévenir la surchauffe, d'anticiper des pannes mécaniques (ventilateur bloqué ou défaillant) et d'optimiser le refroidissement. Cette fonctionnalité aide les utilisateurs avancés et les administrateurs système à garder le système stable et performant.

## Utilisateurs cibles

- Enthousiastes PC / overclockers
- Techniciens et administrateurs système
- Utilisateurs souhaitant monitoring domestique et alertes

## Principales fonctionnalités

- Détection automatique de tous les ventilateurs accessibles via les interfaces matérielles (ACPI, SMBus, WMI, Open Hardware Monitor, libsensors, etc.).
- Lecture périodique des RPM de chaque ventilateur.
- Affichage en temps réel avec historique (courbe temporelle) par ventilateur.
- Configuration de seuils par ventilateur : seuil bas (RPM mini acceptable), seuil haut (optionnel), et seuil critique.
- Notifications locales (popup, son) et optionnelles (e-mail, webhook) lors de dépassement de seuil.
- Logs et export CSV des lectures historiques.

## Exigences fonctionnelles (FR)

- FR1: Le système doit détecter et nommer chaque ventilateur avec un identifiant unique et une étiquette lisible (ex: "CPU Fan", "Chassis Fan 1", "GPU Fan").
- FR2: Le système doit lire la vitesse (RPM) de chaque ventilateur au moins toutes les 5 secondes (configurable).
- FR3: Le système doit permettre à l'utilisateur de définir trois seuils par ventilateur : avertissement, critique, et hors-service (0 RPM).
- FR4: Lorsque la lecture dépasse un seuil, l'utilisateur doit recevoir une notification visuelle et optionnellement un e-mail/webhook.
- FR5: Le système doit conserver un historique des 30 derniers jours (ou configurable) et permettre l'export CSV.
- FR6: Fournir une API locale (HTTP REST) pour consulter les vitesses et l'état des ventilateurs.

## Exigences non fonctionnelles (NFR)

- NFR1: L'impact CPU mémoire doit rester négligeable (<2% CPU sur une machine moderne lors de la surveillance régulière).
- NFR2: L'interface doit rester réactive (rafraîchissement UI <200ms après réception de données).
- NFR3: Respecter la sécurité des notifications et ne pas exposer d'API sans authentification sur les interfaces réseau.

## Interface utilisateur (UI)

- Vue d'ensemble: liste des ventilateurs avec RPM actuels, étiquettes, statut (OK, Warning, Critical, Stopped).
- Vue détaillée: graphique temporel (last 1h, 24h, 30d), seuils configurables, bouton d'export CSV, historique des alertes.
- Modal de configuration: intervalle de lecture, seuils par ventilateur, canal de notification (popup, système, e-mail, webhook), nommage manuel des ventilateurs.

## Notifications & Alertes

- Types: visuelle (popup), sonore (optionnel), e-mail, webhook (POST JSON), logs.
- Contenu: identifiant ventilateur, valeur lue, seuil dépassé, timestamp, suggestion d'action (ex: "vérifier le ventilateur", "nettoyage recommandé").
- Délai de ré-alerte configurable (ex: ne pas spammer — default 15 minutes pour le même événement).

## Données & Stockage

- Stocker séries temporelles compressées (par ex. bucketed per minute) pour 30 jours par défaut.
- Metadata par ventilateur: id, label, source (SMBus/WMI/OpenHardwareMonitor), dernier test OK/KO.

## API

- GET /api/fans — retourne la liste des ventilateurs et valeurs actuelles
- GET /api/fans/{id}/history?range=24h — retourne l'historique
- POST /api/fans/{id}/thresholds — mettre à jour seuils
- POST /api/alerts/acknowledge — acquitter une alerte

## Scénarios d'acceptation (AC)

- AC1: À l'installation, l'application détecte au moins le ventilateur CPU et affiche sa RPM.
- AC2: Si un ventilateur passe à 0 RPM, l'alerte critique est déclenchée et visible dans l'UI.
- AC3: Les seuils configurés par l'utilisateur sont respectés et les notifications sont envoyées via le canal choisi.
- AC4: L'export CSV génère un fichier contenant timestamp,RPM,ventilateur_id pour une période choisie.

## Dépendances

- Bibliothèques/hyperviseurs matériels possibles: Open Hardware Monitor, lm-sensors/libsensors, WMI (Windows), WinRing0/LibreHardwareMonitor.
- Services optionnels: serveur SMTP pour e-mails, service de webhook tiers.

## Risques & considérations

- Accès limité aux capteurs sur certains matériels/BIOS — prévoir mode dégradé et message utilisateur.
- Droits d'accès (certains drivers demandent privilèges élevés) — documenter besoin d'élévation.

## Étapes suivantes / Roadmap

- MVP: détection, lecture périodique, UI simple, notification locale.
- v1: Export, API REST, notifications e-mail/webhook.
- v2: Intégration systèmes tiers (Prometheus export, Grafana dashboard).

---

*Fichier: `Features/Fan Monitoring - Monitor all PC fan speeds.md`*

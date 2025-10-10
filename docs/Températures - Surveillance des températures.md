# PRD — Températures : Surveillance des capteurs thermiques

Résumé
-------
Cette fonctionnalité permet de surveiller toutes les températures disponibles sur le PC (CPU, cores, GPU, disques, cartes mères, capteurs divers). L'application affiche les valeurs en temps réel, permet de définir des seuils d'alerte (min/max) et notifie l'utilisateur en cas de dépassement.

Objectifs
---------
- Collecter périodiquement les températures des capteurs disponibles
- Afficher les valeurs dans une interface graphique conviviale
- Autoriser la configuration de seuils d'alerte par capteur
- Notifier l'utilisateur et journaliser les alertes

Périmètre
---------
- Capteurs exposés par le système (WMI, APIs GPU, SMART pour disques, etc.)
- Application desktop (Windows 11)

Exigences fonctionnelles
------------------------
1. Collecte
   - FR1.1 : Lire la température de chaque capteur toutes les M secondes (M configurable, par défaut 5s).
   - FR1.2 : Les lectures doivent inclure : id du capteur, type (CPU/GPU/HDD/MB), valeur en °C, horodatage.

2. Interface utilisateur
   - FR2.1 : Vue liste des capteurs avec colonnes : ID, nom, type, valeur actuelle (°C), statut (OK/ALERTE), dernier horodatage.
   - FR2.2 : Détail par capteur avec historique (graphique) et possibilité de calibrer/ajouter des commentaires.
   - FR2.3 : Panneau de gestion des seuils (min/max) avec valeurs par défaut recommandées.

3. Alertes
   - FR3.1 : Si la température dépasse le seuil max, marquer le capteur en état ALERTE.
   - FR3.2 : Écrire une entrée d'erreur dans le log et afficher une notification visuelle.
   - FR3.3 : Fournir une option (paramétrable) pour l'envoi d'alertes externes (hors périmètre initial).

4. Persistance
   - FR4.1 : Sauvegarder les seuils et préférences utilisateur localement (JSON/properties).

5. Tests unitaires
   - TU1 : Test de parsing - simuler lecture de capteur et vérifier la conversion en °C et horodatage.
   - TU2 : Test d'alerte - vérifier que les lectures supérieures au seuil max génèrent l'état ALERTE et le log associé.
   - TU3 : Test de configuration - lecture/écriture des seuils.

Critères d'acceptation
----------------------
- Les capteurs sont affichés et mis à jour correctement pendant 30 minutes de simulation.
- Les seuils modifiés sont persistés et appliqués au redémarrage.
- Les dépassements de seuil déclenchent log et notification visuelle.

Interfaces
----------
- API interne SensorReader : getTemperatureReadings() -> List<TemperatureReading> (id, type, valueC, timestamp)
- Configuration : ConfigService.load()/save()

Annexe — Exemples de tests
--------------------------
- Exemples de valeurs : 35°C (CPU idle), 65°C (CPU charge), 90°C (alerte), 45°C (HDD), 55°C (GPU)

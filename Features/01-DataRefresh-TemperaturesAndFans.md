 # Data Refresh - Températures et Fans

 ## 1. Objectif

 Vérifier que les données de températures et de ventilateurs affichées dans l'interface utilisateur se rafraîchissent automatiquement toutes les 5 secondes.

 ## 2. Contexte

 L'interface Web affiche des tableaux de lectures de capteurs (Températures) et d'état des ventilateurs (Fans). Le front-end doit interroger périodiquement le backend via WebSocket ou requête AJAX pour récupérer les dernières mesures et mettre à jour les tableaux.

 ## 3. Critères d'acceptation

 - Les tableaux de Températures et de Fans se mettent à jour automatiquement toutes les 5 secondes.
 - Lors d'un rafraîchissement, les nouvelles valeurs doivent remplacer les anciennes sans duplication de lignes.
 - Si le backend renvoie une erreur, un message d'erreur discret doit être affiché et le cycle de rafraîchissement doit continuer.

 ## 4. Cas de test recommandés

 - **Nom du cas de test**: DataRefreshTemperatures

   - **Outil de test à utiliser**: Selenium WebDriver

   - **Type de test**: UI (intégration)

   - **Fichier de test à créer**: src/test/java/com/agora/monitoring/ui/DataRefreshTemperaturesSeleniumTest.java

   - **Pré-conditions**: Application démarrée en mode test; endpoint de lecture fournit des données séquentielles modifiables (mockable).

   - **Étapes d'exécution**:

     1. Lancer l'application avec `@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)`.

     2. Naviguer vers la page du dashboard.

     3. Capturer l'état initial du tableau de Températures.

     4. Simuler une nouvelle lecture côté backend (ou utiliser un stub) avec valeurs changées.

     5. Attendre jusqu'à 7 secondes et vérifier que le tableau a été mis à jour.

   - **Données de test**: valeurs initiales [20.0, 21.5, 19.8], nouvelles valeurs [22.0, 21.0, 20.1]

   - **Résultat attendu**: Les nouvelles valeurs apparaissent dans les cellules correspondantes.

   - **Post-conditions**: Le tableau reflète les dernières lectures.

 - **Nom du cas de test**: DataRefreshFans

   - **Outil de test à utiliser**: Selenium WebDriver

   - **Type de test**: UI (intégration)

   - **Fichier de test à créer**: src/test/java/com/agora/monitoring/ui/DataRefreshFansSeleniumTest.java

   - **Pré-conditions**: Application démarrée; endpoint Fans mockable.

   - **Étapes d'exécution**: similaire au cas Températures, mais pour le tableau Fans.

   - **Données de test**: RPM initial [1200, 1300], nouvelles [1250, 1280]

   - **Résultat attendu**: Les valeurs RPM se mettent à jour.

 ## 5. Justification des outils

 Selenium WebDriver est choisi car il permet d'observer le comportement réel du DOM et le rafraîchissement visuel côté client. Robot Framework pourrait être utilisé pour scénarios de bout en bout plus haut niveau, mais Selenium offre un contrôle fin des assertions DOM.

 ## 6. Données de test et valeurs limites

 - Valeurs normales: températures entre 10.0 et 40.0°C, RPM entre 500 et 4000.

 - Limites: rafraîchissement manquant si latence > 5s; vérifier tolérance jusqu'à 7s.

 - Erreurs: backend renvoyant 500, payload vide.

 ## 7. Mapping des tests

 | Cas de test | Outil | Type | Fichier à créer | Priorité |
 |-------------|-------|------|-----------------|----------|
 | DataRefreshTemperatures | Selenium | UI | src/test/java/com/agora/monitoring/ui/DataRefreshTemperaturesSeleniumTest.java | P0 |
 | DataRefreshFans | Selenium | UI | src/test/java/com/agora/monitoring/ui/DataRefreshFansSeleniumTest.java | P0 |

 ## 8. Risques et considérations

 - Tests UI sont sensibles au timing; utiliser des attentes explicites et un environnement stable.
 - Nécessité de pouvoir mocker les endpoints de lecture pour obtenir données contrôlées.

 ## 7. Mapping des tests

 | Cas de test | Outil | Type | Fichier à créer | Priorité |
 |-------------|-------|------|-----------------|----------|
 | DataRefreshTemperatures | Selenium | UI | src/test/java/com/agora/monitoring/ui/DataRefreshTemperaturesSeleniumTest.java | P0 |
 | DataRefreshFans | Selenium | UI | src/test/java/com/agora/monitoring/ui/DataRefreshFansSeleniumTest.java | P0 |

 ## 8. Risques et considérations

 - Tests UI sont sensibles au timing; utiliser des attentes explicites et un environnement stable.
 - Nécessité de pouvoir mocker les endpoints de lecture pour obtenir données contrôlées.

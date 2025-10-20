# COPILOT — Références rapides

[↩️ Retour au README](README.md)

## Fichiers de configuration utilisés à des fins de configuration

Voici une description concise des fichiers utilitaires présents dans le répertoire et de leur usage recommandé.

### .github/instructions/[name].instructions.md

``` markdown
---
applyTo: '**/*'
---
Fournir le contexte du projet et les directives de codage que l'IA doit suivre lors de la génération de code, de la réponse aux questions ou de la révision des modifications.
```

- But : Contenir les instructions système et le guide de style global à appliquer par l'assistant.
- Contenu typique : messages système par défaut, contraintes (sécurité, confidentialité), format de sortie attendu, conventions de nommage et priorités entre règles.
- Usage : Charger ou référencer ce fichier pour appliquer les règles globales à toutes les requêtes/agents.

### .github/prompts/[name].prompt.md

``` markdown
---
mode: agent
---
Fournir des invites spécifiques à la tâche
```

- But : Contenir des invites spécifiques à une tâche ou un scénario particulier.
- Contenu typique : description de la tâche, contexte spécifique, exemples d'entrée/sortie, contraintes particulières.

### Usage : Charger ou référencer ce fichier pour des tâches spécifiques, en complément des instructions globales.

Dans la fenetre d'édition de GitHub Copilot, vous pouvez charger ces fichiers pour guider le comportement de l'IA selon le contexte requis. Pour interpeler les fichier de type prompt, utiliser le / suivi du nom du fichier sans extension. Par exemple, pour charger le fichier `ENV.prompt.md`, taper `/ENV` dans la fenetre d'édition.

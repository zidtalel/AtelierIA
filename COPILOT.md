# COPILOT — Guide pratique

[↩️ Retour au README](README.md)

Ce guide vous aidera à utiliser GitHub Copilot efficacement dans VS Code.

---

## 🎹 Raccourcis clavier essentiels

Mémorisez ces raccourcis pour gagner du temps :

| Action | Windows/Linux | Mac | Description |
| ------ | ------------- | --- | ----------- |
| **Accepter suggestion** | `Tab` | `Tab` | Accepte la suggestion affichée en gris |
| **Rejeter suggestion** | `Échap` | `Échap` | Refuse la suggestion courante |
| **Suggestion suivante** | `Alt + ]` | `Option + ]` | Voir la prochaine suggestion |
| **Suggestion précédente** | `Alt + [` | `Option + [` | Voir la suggestion précédente |
| **Ouvrir Copilot Chat** | `Ctrl + Alt + I` | `Cmd + Option + I` | Ouvrir le panneau de chat |
| **Chat en ligne** | `Ctrl + I` | `Cmd + I` | Chat directement dans l'éditeur |
| **Déclencher suggestion** | `Alt + \` | `Option + \` | Forcer une suggestion |

> 💡 **Astuce** : Si aucune suggestion n'apparaît, appuyez sur `Alt + \` pour en demander une !

---

## 🖥️ Les 3 façons d'utiliser Copilot

### 1️⃣ Auto-complétion (suggestions en gris)

Pendant que vous tapez, Copilot suggère du code en gris. Appuyez sur `Tab` pour accepter.

```java
// Exemple : commencez à taper et Copilot complète
public static int calculerSomme(int[] nombres) {
    // Copilot suggère automatiquement le corps de la méthode
}
```

### 2️⃣ Copilot Chat (panneau latéral)

Ouvrez avec `Ctrl + Alt + I` pour poser des questions ou demander du code.

**Exemples de questions :**
- « Explique ce code »
- « Génère des tests unitaires pour cette classe »
- « Comment optimiser cette boucle ? »
- « Corrige cette erreur : [coller l'erreur] »

### 3️⃣ Chat en ligne (dans l'éditeur)

Appuyez sur `Ctrl + I` pour un chat rapide directement dans votre code.

---

## 🔧 Commandes spéciales (slash commands)

Dans le chat, utilisez ces commandes pour des actions spécifiques :

| Commande | Description |
| -------- | ----------- |
| `/explain` | Explique le code sélectionné |
| `/fix` | Corrige les erreurs dans le code |
| `/tests` | Génère des tests pour le code |
| `/doc` | Ajoute de la documentation |
| `/new` | Crée un nouveau fichier/projet |
| `/clear` | Efface l'historique du chat |

**Exemple :** Sélectionnez du code, ouvrez le chat (`Ctrl + Alt + I`), tapez `/explain` → Copilot explique le code.

---

## 📁 Fichiers de configuration Copilot

Voici une description concise des fichiers utilitaires présents dans le répertoire et de leur usage recommandé.

### .github/instructions/[name].instructions.md

```text
---
applyTo: '**/*'
---
Fournir le contexte du projet et les directives de codage que l'IA doit suivre lors de la génération de code, de la réponse aux questions ou de la révision des modifications.
```

- **But** : Contenir les instructions système et le guide de style global à appliquer par l'assistant.
- **Contenu typique** : messages système par défaut, contraintes (sécurité, confidentialité), format de sortie attendu, conventions de nommage et priorités entre règles.
- **Usage** : Charger ou référencer ce fichier pour appliquer les règles globales à toutes les requêtes/agents.

### .github/prompts/[name].prompt.md

```text
---
mode: agent
---
Fournir des invites spécifiques à la tâche
```

- **But** : Contenir des invites spécifiques à une tâche ou un scénario particulier.
- **Contenu typique** : description de la tâche, contexte spécifique, exemples d'entrée/sortie, contraintes particulières.
- **Usage** : Charger ou référencer ce fichier pour des tâches spécifiques, en complément des instructions globales.

## 🔧 Appeler le prompt

Dans la fenêtre d'édition de GitHub Copilot, vous pouvez charger ces fichiers pour guider le comportement de l'IA selon le contexte requis. Pour interpeler les fichiers de type prompt, utiliser le `/` suivi du nom du fichier sans extension. Par exemple, pour charger le fichier `ENV.prompt.md`, taper `/ENV` dans la fenêtre d'édition.

---

## ✅ Bonnes pratiques pour l'exercice

1. **Commencez par un commentaire** : Décrivez ce que vous voulez avant de coder
2. **Soyez précis** : « fonction qui calcule la température CPU » > « code température »
3. **Itérez** : Si le résultat n'est pas bon, affinez votre demande
4. **Vérifiez toujours** : Lisez et testez le code généré
5. **Utilisez le chat** : Pour des questions complexes, préférez le chat au lieu de l'auto-complétion
6. **Gestion du contexte** : Copilot a une fenêtre de contexte limitée. Ouvrez un nouveau chat si nécessaire.

---

## 🎯 Prêt pour l'exercice ?

Retournez au [README](README.md) pour accéder aux exercices pratiques ([SQL](SQL.md) / [PA](PA.md)).

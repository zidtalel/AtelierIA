# Présentation : IA & LLMs pour coder

[↩️ Retour au README](README.md)

Objectif : donner une vue courte et pratique des principes des LLMs (Large Language Models) appliqués au code (ex. GitHub Copilot), ce qu'ils savent faire et leurs limites.

## 1) En une phrase

Les LLMs pour le code prédisent la suite la plus probable à partir du contexte (fichiers ouverts, commentaires, prompt) et suggèrent du code — ils accélèrent le développement, mais la relecture humaine reste indispensable.

Petit rappel utile : un LLM n'« exécute » pas le code pendant la génération. Il choisit des tokens plausibles selon son entraînement. Donc la qualité du prompt et du contexte guide fortement la précision et la pertinence des réponses.

### Note : c'est quoi un *token* ?

Un *token* est une petite unité de texte que le modèle manipule. Ce n'est pas toujours un mot entier : cela peut être un morceau de mot, un symbole, un espace ou une ponctuation.

Le modèle "voit" et prédit une séquence de tokens, pas des phrases complètes d'un coup.

Deux implications pratiques :

1. Longueur / coût : chaque token compte dans la fenêtre de contexte (limite de taille). Plus vous envoyez de texte, plus vous consommez de tokens.
2. Précision : des noms explicites (fonctions, variables) et des exemples réduisent l'ambiguïté des prochains tokens à prédire → meilleure qualité de sortie.

Astuce : si la réponse devient incomplète ou coupée, c'est parfois parce que la limite de tokens de sortie est atteinte.

### Comment un LLM prédit le prochain token (vue interne simplifiée)

Points importants : Copilot ne « comprend » pas le projet comme un humain ; il prédit la suite la plus probable en se basant sur des patterns vus pendant l'entraînement.

Le cycle principal de génération suit une chaîne déterministe (avec parfois de l'aléa dans l'échantillonnage) :

1. Tokenisation : le texte d'entrée est découpé en tokens (morceaux de mots / symboles).
2. Embeddings : chaque token est converti en vecteur dense (représentation numérique).
3. Position : on ajoute une information de position (sinusoïdale ou apprise) pour l'ordre.
4. Empilement de couches Transformer : chaque couche applique (a) self-attention (le token "regarde" les précédents) puis (b) un réseau feed-forward.
5. Logits : la dernière couche produit, pour chaque position courante, un score brut (logits) pour tout le vocabulaire.
6. Softmax + Température : transformation des logits en distribution de probabilités.
7. Échantillonnage / Décodage : on choisit le prochain token (greedy, top-k, nucleus, etc.).
8. Boucle : on ajoute ce token au contexte et on recommence jusqu'au stop (fin, longueur max, ou token spécial).

Diagramme (simplifié) :

```mermaid
%%{init: {'theme':'dark'}}%%
flowchart TD
  A[1- Prompt_and_Context]
  B[2- Tokenisation]
  C[3- Embeddings_Positions]
  subgraph STACK[4- Transformer_Stack]
    direction TB
    S1[SelfAttn_FFN_C1]
    S2[SelfAttn_FFN_C2]
    S3[...]
    SN[SelfAttn_FFN_CN]
  end
  D[5- Logits_vocab]
  E[6- Softmax_Temperature]
  F[7- Decoding]
  G[7- Token_chosen]
  H[8- Add_to_context]
  I[8- Stop]
  J[8- Final_output]

  A --> B --> C --> STACK --> D --> E --> F --> G --> H --> I
  I -- No --> B
  I -- Yes --> J

  classDef block fill:#2d3748,stroke:#4a5568,stroke-width:2px,color:#e2e8f0
  classDef focus fill:#553c9a,stroke:#805ad5,stroke-width:2px,color:#e2e8f0
  classDef action fill:#276749,stroke:#38a169,stroke-width:2px,color:#e2e8f0
  class A,B,C,S1,S2,S3,SN,D,E,F,G,H,I,J block
  class F focus
  class G,H action
```

Lecture rapide : le modèle ne produit pas un bloc entier d'un coup, mais un token à la fois, réévaluant la distribution complète à chaque itération.

## 2) Flux simplifié

```mermaid
%%{init: {'theme':'dark'}}%%
flowchart TD
subgraph subGraph0 ["Flux"]
  direction LR
    D["Données (code + doc)"] --> P["Pré-entrainement"]
    P --> FT["Fine-tuning"]
    FT --> SUG["Suggestions (Copilot)"]
  n1["Exemples open-source"] --- n2["Apprend les motifs & la syntaxe"]
    n2 --- n4["Ajuste aux tâches"]
    n4 --- n3["Auto-complétion / Tests"]
end

    n1@{ shape: text}
    n2@{ shape: text}
    n4@{ shape: text}
    n3@{ shape: text}
     D:::Sky
     P:::Peach
     FT:::Aqua
     SUG:::Rose
    classDef Sky stroke-width:1px, stroke-dasharray:none, stroke:#374D7C, fill:#E2EBFF, color:#374D7C
    classDef Peach stroke-width:1px, stroke-dasharray:none, stroke:#FBB35A, fill:#FFEFDB, color:#8F632D
    classDef Aqua stroke-width:1px, stroke-dasharray:none, stroke:#46EDC8, fill:#DEFFF8, color:#378E7A
    classDef Rose stroke-width:1px, stroke-dasharray:none, stroke:#FF5978, fill:#FFDFE5, color:#8E2236
    style subGraph0 fill:transparent
    linkStyle 3 stroke:none,fill:none
    linkStyle 4 stroke:none,fill:none
    linkStyle 5 stroke:none
```

Points clefs : pré-entrainement sur un large corpus, fine-tuning pour tâches spécifiques, et fenêtre de contexte limitée (longueur maximale). Ces limites expliquent pourquoi il faut fournir un contexte ciblé.


## 3) Usage concret de l'IA pour coder

- Auto-complétion (lignes, blocs, fonctions)
- Génération de tests unitaires et exemples d'utilisation
- Aide au refactoring et traduction entre langages
- **Génération de logiciels basés sur des exigences textuelles**

## 4) Pourquoi le contexte & la variabilité importent

```mermaid
%%{init: {'theme':'dark'}}%%
flowchart TD
subgraph subGraph0 ["Entrées"]
  direction LR
  PC["Prompt / Contexte"] --> PARM["Paramètres (température, seed)"]
end
subgraph subGraph1 ["Sorties"]
  direction TB
  A_OUT["Sortie A (variante)"]
  B_OUT["Sortie B (variante)"]
  C_OUT["Sortie C (variante)"]
end
  %% Replace labeled link with an explicit transparent label node so label background is none
  EXPL["Même prompt + paramètres différents → sorties différentes"]
  EXPL@{ shape: text}
  subGraph0 --- EXPL ---> subGraph1
  classDef block1 stroke-width:1px, stroke-dasharray:none, stroke:#374D7C, fill:#E2EBFF, color:#374D7C
  classDef block2 stroke-width:1px, stroke-dasharray:none, stroke:#46EDC8, fill:#DEFFF8, color:#378E7A
  classDef explClass fill:none, stroke:none, color:#FFD700
  class PC,PARM block1
  class A_OUT,B_OUT,C_OUT block2
  class EXPL explClass
  style subGraph0 fill:transparent,stroke:#2962FF
  style subGraph1 fill:transparent,stroke:#00C853
```

- Contexte : noms de fonctions, commentaires, fichiers ouverts, exemples d'entrée/sortie.
- Variabilité : paramètres comme la "température" et la seed modifient les sorties (plus ou moins aléatoires).

Conseil pratique : fournir un prompt structuré, des exemples, et indiquer le format exact attendu.

## 5) Guide bref — Écrire un bon prompt (pour débutant)

Un bon prompt contient généralement ces éléments (ordre recommandé) :

1) Objectif clair : que doit produire l'IA ? (ex. "Écrire une fonction Java qui...")
2) Contrainte(s) : langage, version, style, performance, bibliothèques à utiliser ou à éviter.
3) Entrée(s) et sortie(s) attendues : schéma, types, exemples concrets.
4) Critères d'évaluation ou tests rapides : cas limite, complexité attendue, tests unitaires simples.
5) Exemple(s) : un petit exemple d'entrée → sortie pour guider le modèle.

Template court (à copier) :

"Tu es un assistant expert en [langage]. Objectif : [but précis]. Contraintes : [langage/version], ne pas utiliser [lib], respecter [style]. Entrée : [description]. Sortie attendue : [format]. Exemple : [entrée] → [sortie]."

Exemple concret :

"Tu es un assistant Java. Objectif : écrire une méthode statique 'truncate(String text, int n)' qui coupe une chaîne à n caractères en ajoutant '...' si nécessaire. Contraintes : Java 11+, pas de dépendance externe. Entrée : String, int. Sortie : String. Exemple : 'Bonjour', 3 → 'Bon...' ."

Pourquoi c'est important :

- Précision : plus vous donnez d'informations pertinentes (contraintes, exemples), plus la sortie sera proche de ce que vous attendez.
- Réduction des erreurs : indiquer des tests ou des cas limites aide le modèle à éviter les hallucinations.
- Itération rapide : commencez par un prompt précis, vérifiez la sortie, puis demandez des corrections ciblées (ex. "corrige pour les cas où...").

## 6) Limites rapides (à garder en tête)

- Hallucinations : le modèle peut inventer des fonctions, signatures ou APIs — toujours vérifier et exécuter le code.
- Licence / provenance : attention si vous intégrez du code sans vérifier la provenance.
- Sécurité : ne pas exposer de secrets ni de données sensibles dans les prompts.

## 7) Vibe Coding avec l'IA (exploration rapide)

Le "Vibe Coding" (codage par flux ou exploration assistée) désigne des micro-itérations très rapides où l'on génère, teste et ajuste du code avec l'IA sans concevoir immédiatement une solution formelle. Objectif : maximiser la vitesse d'apprentissage et de prototypage tout en limitant les risques (qualité, sécurité, licence).

### Quand l'utiliser

- Prototype / preuve de concept
- Exploration API / librairie inconnue
- Recherche de patterns de refactoring
- Génération d'idées de tests ou de cas limites

### Boucle typique (30–120s)

```mermaid
%%{init: {'theme':'dark'}}%%
flowchart LR
  IDEA[Idée] --> PROMPT[Prompt rapide]
  PROMPT --> AI[Suggestion IA]
  AI --> TEST[Test rapide]
  TEST --> DECIDE{OK?}
  DECIDE -->|Oui| COMMIT[Commit local]
  DECIDE -->|Non| REFINE[Affiner prompt]
  REFINE --> PROMPT

  classDef base fill:#2d3748,stroke:#4a5568,color:#e2e8f0
  classDef action fill:#276749,stroke:#38a169,color:#e2e8f0
  class IDEA,PROMPT,AI,TEST,REFINE base
  class COMMIT action
```

Clés :

- Prompt simple et ciblé
- Tester vite (unitaires / lint)
- Accepter si sûr, sinon ajuster le prompt
- Commit local puis structurer si besoin

> Cycle court — privilégiez petites itérations et feedback automatique.

## 8) Développement piloté par spécifications (Spec-Driven Development)

Le développement piloté par spécifications consiste à rédiger une spec claire (objectifs, interface, exemples, tests) avant de demander à un LLM de générer le code. C'est une approche utile pour les fonctionnalités critiques ou partagées où la fiabilité et la traçabilité sont importantes.

Principaux bénéfices :

- Clarifie les exigences et réduit les malentendus.
- Produit du code plus testable et maintenable.
- Facilite la revue collaborative et la traçabilité entre spec et code.

Checklist minimale pour une spec efficace :

1. Objectif bref et contexte
2. Signature / interface (types)
3. 2–3 exemples entrée→sortie
4. Tests essentiels / cas limites

Mini-template à copier :

```markdown
# Spec: [Nom]
## Contexte
Objectif : []
## Interface
- Langage: Java 11+
- Signature: `public static <Type> f(<params>)`
  (ex: `public static String truncate(String text, int n)`)
## Exemples
- Entrée: [...] -> Sortie: [...]
## Tests
- Cas nominal
- Cas limite
```

Workflow résumé : Rédiger spec → Générer via LLM → Exécuter tests → Revue humaine et commit.

### Workflow Spec-Driven avec LLM

```mermaid
%%{init: {'theme':'dark'}}%%
flowchart LR
  SPEC["Spec claire"] --> GEN["Génération (LLM)"]
  GEN --> TESTS["Exécution des tests"]
  TESTS --> DECIDE{"Tests OK ?"}
  DECIDE -->|Non| FIX["Corriger / Ajuster prompt"] --> GEN
  DECIDE -->|Oui| HUMAN["Revue humaine"] --> COMMIT["Commit + Spec"]
  COMMIT --> MAINT["Maintenance"]
  classDef main fill:#2b6cb0,stroke:#3182ce,color:#e2e8f0
  class SPEC,GEN,TESTS,HUMAN,COMMIT main
```

### Quand privilégier Spec-Driven vs Vibe Coding

| **Critère** | **Spec-Driven** | **Vibe Coding** |
|-------------|----------------|-----------------|
| **Type de projet** | Production, bibliothèque partagée | Prototype, POC, exploration |
| **Criticité** | Code critique (sécurité, finance) | Expérimentation, apprentissage |
| **Équipe** | Collaboration multi-dev | Développement solo |
| **Documentation** | Requise et maintenue | Optionnelle |
| **Tests** | Complets et automatisés | Tests exploratoires |
| **Évolutivité** | Long terme | Court terme |

[💫 Toolkit to help you get started with Spec-Driven Development](https://github.com/github/spec-kit)

### (Annexe) Fenêtre de contexte : entrée / sortie (estimation)

Ce tableau indique, pour chaque modèle, une estimation de la fenêtre totale (tokens), puis une estimation typique de la quantité maximale utilisable pour l'entrée (tokens d'entrée) et pour la sortie générée. Rappel : entrée + sortie ≤ fenêtre totale.

| Modèle (exemples)         | Fenêtre totale (tokens) | Tokens d'entrée max (approx.) | Tokens sortie max (approx.) | Commentaire |
|---------------------------|------------------------:|------------------------------:|----------------------------:|------------|
| GPT-4.1 (Copilot)         | ≈128k                  | ≈96k–120k                     | ≈8k–32k                     | Intégré à Copilot, bon pour multi-fichiers et suggestions contextuelles |
| GPT-5 Mini                | ≈64k                   | ≈56k–60k                      | ≈4k–8k                      | Variante allégée de GPT-5, économique pour usages fréquents |
| Cloud Sonnet 4            | ≈200k                  | ≈176k–192k                    | ≈8k–24k                     | Conçu pour documents volumineux et code long |
| Cloud Sonnet 4.5          | ≈500k                  | ≈452k–488k                    | ≈12k–48k                    | Fenêtre étendue pour très grands contextes |
| GPT-5                     | ≈1M+                   | ≈800k–950k                    | ≈50k–200k+                  | Configurations endpoint-dependent ; forte capacité pour larges projets |
| Grok Code Fast 1         | ≈64k                   | ≈56k–60k                      | ≈4k–8k                      | Optimisé pour le code, faible latence pour suggestions rapides |
| Mistral (dernier)         | ≈64k (varie)           | ≈52k–60k                      | ≈4k–12k                     | Variantes optimisées pour le code et le coût |
| Llama 3 (dernier)         | ≈128k–512k             | ≈96k–480k                     | ≈8k–32k                     | Versions et builds variables (open-source/entreprise) |
| Gemini 2.5                | ≈1M                    | ≈800k–950k                    | ≈50k–200k+                  | Orienté très large contexte, utile pour projets multi-fichiers |

Note : estimations (oct. 2025) — les valeurs réelles dépendent de l'endpoint, des limites imposées par le fournisseur et des configurations de modèle. "Tokens d'entrée max" = fenêtre totale − tokens réservés pour la sortie ; les plages données indiquent des allocations typiques selon usage (conservateur → agressif).

Astuce pratique : si vous avez besoin d'analyser de longs dépôts, privilégiez les modèles à grande fenêtre ou prétraitez / résumez le code pour n'envoyer que les parties essentielles (signatures, tests, exemples).

---

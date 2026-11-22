# GRIMWOOD: The Cursed Forest

Applicativo Java sviluppato per il corso di *Metodologie di Programmazione* (A.A. 2025/26).  
**Package:** `it.unicam.cs.mpgc.rpg124913` | **Studente:** Kevin Giannini

## Descrizione
*Grimwood: The Cursed Forest* è un Gioco di Ruolo (GDR) a turni ad ambientazione dark fantasy. Il giocatore veste i panni di un *Beast Hunter* che attraversa i nodi di una foresta maledetta affrontando creature ostili, gestendo parametri vitali e di sanità mentale. L'applicativo è costruito su un'architettura **Event-Driven** con persistenza su file JSON (Jackson) e interfaccia grafica **Java Swing**.

## Compilazione ed Esecuzione
Il progetto utilizza il Gradle Wrapper per garantire l'esecuzione multipiattaforma (richiede JDK 17+). Dalla directory principale del repository:

| Azione | Comando |
| :--- | :--- |
| **Compilazione e Test** | `./gradlew build` |
| **Avvio Applicazione (GUI)** | `./gradlew run` |

## Documentazione
Tutta la documentazione tecnica (architettura, responsabilità delle classi, persistenza, estendibilità e analisi dettagliata dell'uso dell'AI) è raccolta nella **[Wiki del repository](INSERISCI-LINK-WIKI)**.

## Dichiarazione sull'uso di strumenti di AI
Durante lo sviluppo sono stati utilizzati strumenti di Intelligenza Artificiale a supporto della revisione architetturale, del refactoring secondo i principi SOLID e della stesura della documentazione Javadoc in lingua inglese. I dettagli completi sono riportati nell'apposita sezione della Wiki.
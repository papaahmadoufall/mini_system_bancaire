# Mini Système Bancaire

Une application bancaire simple développée avec JavaFX, Hibernate et MySQL.

## Prérequis

- Java 17 ou supérieur
- Maven
- MySQL 8.0 ou supérieur
- SceneBuilder (pour l'édition des fichiers FXML)

## Configuration

1. Clonez le repository
2. Configurez la base de données MySQL :
   - Créez une base de données nommée `bank_db`
   - Modifiez les informations de connexion dans `src/main/resources/hibernate.cfg.xml` si nécessaire

## Installation

1. Assurez-vous que MySQL est en cours d'exécution
2. Exécutez la commande suivante pour compiler le projet :
   ```bash
   mvn clean install
   ```

## Exécution

Pour lancer l'application :
```bash
mvn javafx:run
```

## Structure du projet

- `src/main/java/com/bank/`
  - `controller/` : Contrôleurs JavaFX
  - `model/` : Classes d'entités
  - `dao/` : Classes d'accès aux données
  - `Main.java` : Point d'entrée de l'application
- `src/main/resources/`
  - `fxml/` : Fichiers FXML pour les vues
  - `hibernate.cfg.xml` : Configuration Hibernate

## Fonctionnalités

- Connexion des clients
- Inscription des nouveaux clients
- Gestion des comptes
- Opérations bancaires (dépôt, retrait, virement)
# 🏥 Gestion des Patients - Application JavaFX

## 📋 Description brève
Application permettant de gérer des fiches patients, en offrant des fonctionnalités pour ajouter, modifier, supprimer et consulter les informations telles que le nom, prénom, téléphone, adresse et date de naissance des patients.

## ✨ Fonctionnalités principales
- **➕ Ajouter un patient** : Enregistrement des informations complètes d'un patient
- **✏️ Modifier un patient** : Mise à jour des données existantes
- **🗑️ Supprimer un patient** : Suppression des fiches patients du système
- **📊 Afficher la liste des patients** : Consultation de tous les patients enregistrés
- **✅ Validation des champs** : Notamment la validation du format de la date (dd/mm/yyyy)
- **⚠️ Message d'erreur** : Affichage d'un message si la date est manquante ou incorrecte

## 🛠️ Technologies utilisées
- **☕ Java 17** : Langage de programmation principal
- **🖥️ JavaFX 17.0.2** : Framework pour l'interface graphique
- **📝 FXML** : Langage de balisage pour définir l'interface utilisateur
- **🎨 CSS** : Stylisation de l'interface utilisateur
- **🗄️ MySQL** : Système de gestion de base de données
- **🔌 JDBC** : API Java pour la connexion à la base de données
- **🔧 Maven** : Outil de gestion de projet et de dépendances

## 📥 Instructions d'installation et d'exécution

### 📋 Prérequis
- Java Development Kit (JDK) 17 ou supérieur
- MySQL Server 8.0 ou supérieur
- Maven 3.6 ou supérieur

### 🗄️ Configuration de la base de données
1. Créez une base de données MySQL nommée `gestion_consultation`.

## 📸 Captures d'écran

### ➕ Ajout d'un patient
<img src="captures/screen1.png" alt="Ajout d'un patient" width="500">
<img src="captures/screen2.png" alt="Ajout d'un patient" width="500">

*Capture d'écran montrant le formulaire d'ajout d'un nouveau patient*


### ✏️ Modification d'un patient
<img src="captures/screen3.png" alt="Ajout d'un patient" width="500">
<img src="captures/screen4.png" alt="Ajout d'un patient" width="500">

*Capture d'écran montrant le formulaire de modification des informations d'un patient son numero de telephone*

### 🗑️ Suppression d'un patient
<img src="captures/screen5.png" alt="Ajout d'un patient" width="500">
<img src="captures/screen6.png" alt="Ajout d'un patient" width="500">

*Capture d'écran montrant la confirmation de suppression d'un patient*

## 📸 Captures d'écran des consultations

### 🗓️ Ajout d'une consultation
<img src="captures/screen7.png" alt="Ajout d'un patient" width="500">

*Formulaire d'ajout d'une consultation avec la sélection du patient, la date, le motif et le statut de la consultation.*

### 🗑️ Suppression d'une consultation
<img src="captures/screen9.png" alt="Ajout d'un patient" width="500">

*Capture d'écran montrant la confirmation de suppression d'une consultation*

### ✏️ Modification d'un statut de consultation
<img src="captures/screen10.png" alt="Ajout d'un patient" width="500">

*Capture d'écran montrant la modification de statut d'un patient*

### 🔍 Recherche de consultation par ID
<img src="captures/screen8.png" alt="Ajout d'un patient" width="500">

*Fonction de recherche de consultation par ID du patient.*
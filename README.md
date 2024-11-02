# SensorIN

SensorIN est une application Android permettant aux utilisateurs de visualiser et d’interagir avec les données des capteurs disponibles sur leur appareil. L’application fournit un affichage en temps réel des données, une visualisation graphique et des alertes pour divers capteurs environnementaux et de position, ce qui en fait un outil idéal pour explorer les données des capteurs.

## Fonctionnalités

### 1. Page d'accueil
- Résume les capteurs disponibles sur l’appareil.
- Contient des liens vers les pages de données de chaque capteur.

### 2. Pages de Données des Capteurs
- Affiche les données en temps réel pour chaque capteur, y compris :
  - **Capteurs de Mouvement** : Accéléromètre, Gyroscope, Détecteur de Pas, Vecteur de Rotation.
  - **Capteurs Environnementaux** : Température, Pression Atmosphérique, Humidité, Intensité Lumineuse.
  - **Capteurs de Position** : Magnétomètre, Proximité, Orientation.
- Les données en temps réel sont visualisées sous forme de graphiques pour une interprétation facilitée.
  
### 3. Page de Profil
- Affiche les informations de profil de l'utilisateur, incluant une photo de profil et la liste des capteurs disponibles sur l’appareil.

### 4. Partage des Données
- Permet aux utilisateurs de partager les données des capteurs via d’autres applications.

### 5. Notifications
- Envoie des notifications lorsque des seuils spécifiques des capteurs sont atteints, permettant d’alerter l’utilisateur.

## Structure du Projet

L’application est organisée comme suit :

### MainActivity
- Utilise un `DrawerLayout` comme conteneur principal.
- Inclut un `Toolbar` pour les actions principales et un `NavigationView` pour un accès facile aux différentes sections de l’application.

### Fragments et Activités
- **HomeFragment** : Affiche un résumé des capteurs et des boutons pour naviguer vers chaque page de capteur.
- **SensorFragment** : Visualise les données de chaque type de capteur individuel, en utilisant :
  - `GraphView` pour des graphiques de données en temps réel.
  - `TextView` pour afficher les valeurs en temps réel.
  - Boutons Démarrer/Arrêter pour contrôler le flux de données.
- **ProfileActivity** : Affiche les informations de l’utilisateur et la liste de tous les capteurs disponibles.

## Composants Clés et Bibliothèques Utilisées

- **SensorManager** : La classe principale pour accéder aux capteurs.
- **Sensor** : Représente chaque capteur individuel de l’appareil.
- **SensorEventListener** : Interface pour recevoir des mises à jour en temps réel des capteurs.
- **GraphView** : Bibliothèque pour afficher les graphiques en temps réel des données des capteurs.
- **RecyclerView** : Utilisé pour un affichage efficace des listes, notamment dans la liste des capteurs.
- **NavigationDrawer** : Fournit un menu latéral pour une navigation aisée.
- **FloatingActionButton** : Bouton flottant pour déclencher des notifications ou d’autres actions rapides.

## Style et Conception de l’Interface

L'application utilise un thème clair cohérent, avec des espacements et des marges appropriés pour une meilleure lisibilité :
- **Couleurs** : Une palette de couleurs harmonieuse pour les composants de l'application.
- **Typographie** : Polices lisibles avec des tailles de texte adaptées à chaque élément de l’interface.
- **Marges/Espaces** : Assure un design propre et aéré de l'interface.

## Installation et Configuration

1. Clonez le dépôt :
   ```bash
   git clone https://github.com/Salma-CHAJARI/SensorsProject.git
   ```

2. Ouvrez le projet dans Android Studio.

3. Vérifiez que toutes les permissions nécessaires sont définies dans le fichier `AndroidManifest.xml`, notamment pour accéder aux capteurs et aux notifications.

4. Compilez et exécutez le projet sur un appareil Android.

## Permissions

Assurez-vous que les permissions suivantes sont définies :
- **Capteurs** : Pour accéder aux capteurs de mouvement, d’environnement et de position.
- **Notifications** : Requises pour déclencher des alertes en fonction des seuils de capteurs.

## Auteur

- *CHAJARI Salma* - Étudiante à l'Université Chouaib Doukkali, Ecole Nationale des Sciences Appliquées d'El Jadida.

## Encadré par

- *Pr. M. LACHGAR*




## Vidéo démonstratif


https://github.com/user-attachments/assets/58066c14-2bcf-46d8-beb9-687b614f5f5e




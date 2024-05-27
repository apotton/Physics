# Particules 🔵⚪🔴🟢🟡🟣🟠

## Introduction

Ce programme est un petit environnement dans lequel interagissent des particules (représentées par des cercles), uniquement par collisions inélastiques. Les variables statiques définies au début de la classe `AnimationMain` permettent de contrôler différents paramètres de la simulation. Parmi les plus importants: le rayon des particules, leur direction et leur vitesse initiale. Pour voir la simulation, il suffit de lancer le `main` de cette classe. En haut de la fenêtre d'affichent les fps ainsi que le nombre de particules à l'écran.

## Fonctionnement du code

### Détection des collisions

Pour savoir si deux cercles se chevauchent, il faudrait naïvement parcourir tous les cercles, puis, pour chacun d'entre eux, reparcourir tous les autres cercles et vérifier si ils sont distants d'une distance inférieure à la somme de leurs rayons. Ce genre d'algorithme est évidemment extrêmement couteux, avec une complexité temporelle en O(n²), avec n le nombre de cercles.

Mais la plupart des cercles sont en réalité très distants les uns des autres, et donc peu probables d'être superposés. Dans ce genre de cas, il est une bonne idée de créer un quadrillage dans toute la scène. Chaque cercle aura son centre dans une des cases de ce quadrillage. Et, si la largeur de chaque case est supérieure au rayon le plus grand de la scène, alors deux cercles qui se chevauchent se trouvent nécessairement dans la même case ou dans deux cases adjacentes.

Chaque case stocke d'index des cercles qu'elle contient. Le programme calcule ainsi le nombre maximal de cercles par case pour initialiser le tableau, et utilise ce quadrillage pour la détection de collisions.

### Scene.java

La classe `Scene` contient le coeur du programme, c'est-à-dire un tableau de cercles définis par la classe `Cercle`, ainsi que le grand cercle dans lequel la simulation a lieu (la contrainte). C'est également là où est défini et dimensionné le quadrillage.

La fonction `maj` réalise les calculs de la physique de la scène. Elle met à jour la position des cercles liée à l'accélération. La fonction utilise l'algorithme d'intégration de Verlet, qui permet une approximation précise à l'ordre 2 en connaissant uniquement la position actuelle et la position précédente.

Ensuite, les positions des cercles sont recalées à l'intérieur de la scène par la fonction `contrainte`. Avant de résoudre les problèmes de collision créés par les précédents déplacements, on met à jour la position des cercles dans le quadrillage.

### Circle.java

Chaque cercle est défini par sa position, son rayon, sa couleur, ses coordonnées sur le quadrillage et son index. La fonction `reglerCollision` vérifie si les deux cercles en entrée se chevauchent. Si c'est le cas, elle met à jour leurs positions en prenant en compte la différence de quantité de mouvement de chacun (rayon).

### RGB.java

Ce fichier définit une classe possédant une unique fonction, `RGBsuivante`, qui renvoie la couleur suivante de l'arc-en-ciel à chaque fois qu'elle est appelée. Le code est absolument cryptique, je l'ai écrit il y a longtemps, mais il donne les résultats escomptés.

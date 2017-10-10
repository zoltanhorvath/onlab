# Önnálló laboratórium - Catan telepesei

### Feladat
A Catan telepesei című társasjáték online, böngészőben játszható verziójának elkészítése.

### Követelmények
* A játék regisztációhoz kötött. A felhasználó az alábbi módokon regisztrálhat:
  * Email címmel
  * Facebook fiókkal
  * Google fiókkal
  * Microsoft fiókkal
* A regisztrált felhasználók az alábbi funkcionalitásokat érik el bejelentkezés után:
  * Személyes adataik kezelése: Nickname, Avatar, jelszó email címes regisztáció esetén
  * Barátok kezelése
  * Private, Public channel-ek létrehozása, megszüntetése, tetszőleges channel-hez való csatlakozás lehetősége
  * Valós idejű kommunikáció bejelentkezett felhasználókkal
    * Global channel
    * Private channel - jelszóval védett
    * Public channel
  * Játék lobby létrehozása, megszüntetése, tetszőleges játék lobby-hoz való csatlakozás lehetősége
    * Privát - jelszóval védett
    * Publikus
  * Játék indítása lobby-ból

### Entitások
* User
* Channel
* Lobby
* Game
* Map
* Hexagon

### Ábra
![Services Overview](https://github.com/zoltanhorvath/onlab/blob/master/Services%20Overview.jpg)

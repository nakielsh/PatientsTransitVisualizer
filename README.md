# Patients Transit Visualizer

An app illustrating patients being transferred to hospitals. 
To create a map with hospitals - first load the .txt file that has an information about hospitals, objects and paths in it. 
The file should look like this:
```
# Szpitale (id | nazwa | wsp. x | wsp. y | Liczba łóżek | Liczba wolnych łóżek)
1 | Szpital Wojewódzki nr 997 | 10 | 10 | 1000 | 1
2 | Krakowski Szpital Kliniczny | 100 | 120 | 999 | 1
3 | Pierwszy Szpital im. Prezesa RP | 120 | 130 | 99 | 1
4 | Drugi Szpital im. Naczelnika RP | 10 | 140 | 70 | 1
5 | Trzeci Szpital im. Króla RP | 140 | 10 | 996 | 1
6 | Szapital dodany |  140 | 70 | 10 | 1

# Obiekty (id | nazwa | wsp. x | wsp. y)
1 | Pomnik Wikipedii | -1 | 50
2 | Pomnik Fryderyka Chopina | 110 | 55
3 | Pomnik Anonimowego Przechodnia | 40 | 70

# Drogi (id | id_szpitala | id_szpitala | odległość)
1 | 1 | 2 | 700
2 | 1 | 4 | 550
3 | 1 | 5 | 800
4 | 2 | 3 | 300
5 | 2 | 4 | 550
6 | 3 | 5 | 600
7 | 4 | 5 | 750
8 | 6 | 4 | 600

```
The map fot the following example looks like this:


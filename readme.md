
# Proiect Retele 

## Descrierea Proiectului
Acest proiect Java demonstrează utilizarea mai multor concepte importante:
- **Fluxuri și serializare**
- **Fire de execuție**
- **Arhitectură Client-Server**
- **Interfețe grafice și evenimente**
- **Resurse comune și concurență**

## Structura Proiectului
Proiectul este organizat astfel:
- **Retele**: Directorul principal al proiectului.
  - **README.md**: Fișierul care explică proiectul.
  - **src**: Directorul care conține codul sursă.
    - **client**: Directorul pentru codul clientului.
      - **Client.java**: Fișierul principal care rulează pe computerul client.
    - **server**: Directorul pentru codul serverului.
      - **Server.java**: Fișierul principal care rulează pe computerul server.
      - **SharedResource.java**: Fișierul care conține resursa pe care o împart toți clienții.
    - **ui**: Directorul pentru interfața grafică.
      - **ClientUI.java**: Fișierul care definește interfața grafică a clientului.
    - **util**: Directorul pentru utilitare.
      - **Semaphore.java**: Fișierul care implementează semaforul pentru controlul accesului.
      - **Monitor.java**: Fișierul care implementează monitorul pentru controlul accesului.
      - **SerializationUtil.java**: Fișierul care ajută la salvarea și citirea obiectelor.

## Fișiere și Funcții

### Client.java
Acest fișier conține codul care rulează pe computerul client și se conectează la server.
Funcții principale:
- **main(String[] args)**: Inițializează conexiunea la server și gestionează intrările utilizatorului. Creează interfața grafică a clientului și începe să citească mesajele de la server.

### Server.java
Acest fișier conține codul care rulează pe computerul server și gestionează conexiunile de la clienți.
Funcții principale:
- **main(String[] args)**: Pornește serverul și așteaptă conexiuni de la clienți. Creează un fir de execuție pentru fiecare client nou.
- **ClientHandler implements Runnable**: Gestionează cererile de la fiecare client într-un fir de execuție separat. Asigură închiderea corectă a conexiunilor și trimite istoricul cererilor și răspunsurilor către clienți.

### SharedResource.java
Acest fișier conține resursa comună accesată de toți clienții. Stochează și procesează cererile și păstrează un istoric al acestora.
Funcții principale:
- **processRequest(String request)**: Procesează cererile primite de la clienți și adaugă răspunsul în istoric.
- **getHistory()**: Returnează istoricul complet al cererilor și răspunsurilor pentru a fi trimis către clienți.

### ClientUI.java
Acest fișier conține interfața grafică pentru client, oferind o modalitate de a trimite cereri și de a vedea istoricul răspunsurilor.
Funcții principale:
- **ClientUI(ObjectOutputStream out, ObjectInputStream in)**: Constructorul interfeței grafice, inițializează componentele UI și gestionează evenimentele.
- **sendMessage()**: Trimite mesaje către server când utilizatorul apasă pe butonul "Send" sau apasă Enter.
- **IncomingReader implements Runnable**: Cititor de mesaje primite de la server într-un fir de execuție separat. Afișează răspunsurile și istoricul cererilor în interfața grafică.

### Semaphore.java
Acest fișier conține implementarea unui semafor, care ajută la controlul accesului la resurse partajate între mai multe fire de execuție.
Funcții principale:
- **acquire()**: Blochează firul de execuție până când semaforul permite accesul.
- **release()**: Eliberează semaforul și permite altor fire de execuție să acceseze resursa.

### Monitor.java
Acest fișier conține implementarea unui monitor, care este o altă metodă de control al accesului la resurse partajate între fire de execuție.
Funcții principale:
- **enter()**: Blochează firul de execuție până când monitorul permite accesul.
- **exit()**: Eliberează monitorul și permite altor fire de execuție să acceseze resursa.

### SerializationUtil.java
Acest fișier conține utilitare pentru serializarea și deserializarea obiectelor, adică pentru salvarea obiectelor în fișiere și citirea lor din fișiere.
Funcții principale:
- **serialize(Object obj, String fileName)**: Salvează un obiect în fișier.
- **deserialize(String fileName)**: Citește un obiect din fișier.

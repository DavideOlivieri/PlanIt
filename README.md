# PLAN IT
## Intoduzione
L'obiettivo di questo progetto è la creazione di un calendario che coniughi la semplicità d'uso, un design accattivante e la versatilità d'impiego 
sia per utenti individuali che per gruppi di persone. \
Questo calendario è stato progettato per essere impiegato anche in contesti di lavoro di squadra, consentendo l'assegnazione di livelli di accesso personalizzati 
a ciascun utente coinvolto, in base alle azioni che sono autorizzati a compiere all'interno del calendario. \
Sebbene l'applicazione del calendario potrebbe non presentare innovazioni rivoluzionarie, 
riteniamo che possa costituire un valido esercizio pratico per consolidare le competenze acquisite durante il corso e metterle in pratica in un contesto concreto. 

## Requisiti Funzionali
L’applicazione deve mettere a disposizione una pagina iniziale per l’accesso degli utenti e la registrazione dei nuovi utenti. 

L’applicazione deve mettere a disposizione dell’utente una prima pagina in cui può creare un nuovo calendario oppure 
partecipare ad uno già esistente, nella stessa pagina potrà visualizzare tutti i calendari a cui può accedere. 

L’applicazione deve mettere a disposizione una pagina in cui viene mostrato il calendario che è stato selezionato nella pagina precedente. 

In questa pagina sarà possibile visualizzare i giorni che possiedono uno o più eventi tramite un pallino posizionato sotto il numero del giorno. 
Cliccando su qualunque data verrà aperta una nuova pagina dove sarà possibile visualizzare gli eventi della data selezionata (se presenti), 
con la possibilità di aggiungere nuovi eventi. 

Dalla pagina del calendario sarà anche possibile accedere alle informazioni del calendario attraverso il bottone info, questo ci porterà 
ad una nuova pagina nella quale verranno visualizzati i vari dettagli sul calendario. 

L’applicazione ha come obbiettivo principale quello di creare dei calendari condivisi tra più utenti, ma sarà possibile anche avere dei calendari privati.

## Requisiti Non Funzionali
L'applicazione deve essere intuitiva e facile da usare, in modo che gli utenti possano sfruttarne appieno le funzionalità senza difficoltà.

L'applicazione deve essere compatibile con una vasta gamma di dispositivi Android e versioni del sistema operativo.

L'applicazione dovrebbe consentire agli utenti di accedere al calendario e ai dati degli eventi anche quando non sono connessi a Internet.

## Database
L’applicazione è strettamente legata al concetto di Database, dopotutto una base di dati è essenziale per la memorizzazione dei dati degli utenti, degli eventi e dei calendari condivisi sia sul dispositivo locale, sia su dispositivi cloud al fine di permettere la condivisione dei dati. \
PlanIt utilizza due tipologie differenti di Database per garantire una reperibilità ottima dei dati salvati. 

Essa utilizza un database locale chiamato ‘RoomDatabase’ e un Cloud database chiamato ‘FirebaseDatabse’. \
In particolare, l’applicazione si interfaccia sempre con Room per reperire i dati, questo fa si che le operazioni siano molto più veloci e anche in assenza di connessione, permette di ottenere dei dati e quindi mantenere attive certe funzionalità. \
Ovviamente si è reso necessario anche l’utilizzo di un database su cloud per garantire le funzionalità di PlanIt quali: la condivisione dei dati, la reperibilità degli stessi su più dispositivi, ecc.… 

NOTA: con Firebase sarebbe stato possibile effettuare la procedura di login/sign-up rendendo sicuramente quest’ultima più efficiente e sicura; tuttavia avendo iniziato lo sviluppo dell’applicazione prima delle lezioni inerenti e avendo già sviluppato una procedura di login e sign-up, abbiamo deciso di mantenere la nostra versione anche considerando il tempo impiegato allo sviluppo.

## Interfaccia utente



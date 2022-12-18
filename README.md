Έχω 3 αρχεία κλάσεων, όπως στο παράδειγμα με το Fibonacci, Client, Server, ServerThread.

Η κλάση Client απλά μεταφέρει το input του terminal με ένα ArrayList στην κλάση Server.

Η κλάση Server έχει δύο δομές HashMap, η μία αντιστοιχεί username με authToken και η άλλη username με Account object. Μέσα στο αρχείο υλοποιούνται και οι κλάσεις Message και Account, όπως ορίζονται στην εκφώνηση, μαζί με κάποιους getters. Η main ανοίγει το socket και φτιάχνει αντικείμενα ServerThread.

Η κλάση ServerThread υλοποιεί την run(). Δημιουργεί τα κατάλληλα streams και ύστερα καλεί την handleInput με ορίσματα τα arguments του terminal του Client και το output stream προς το Client. Η συνάρτηση handleInput αντιστοιχεί το FN_ID με την συνάρτηση που έχει υλοποιήσει την λειτουργία του.

Υποθέτω ότι ο αριθμός των arguments που δίνει ο client σε κάθε περίπτωση είναι είναι σωστός, δεν έχει υλοποιηθεί έλεγχος εισόδου για περισσότερα ή λιγότερα arguments από αυτά που απαιτεί η κάθε λειτουργία.
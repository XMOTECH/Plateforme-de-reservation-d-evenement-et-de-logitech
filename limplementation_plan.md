# MVP Phase 1 : Le Cœur Transactionnel (Réservation & Paiement)

L'objectif de cette première phase est de construire le moteur de réservation. Un utilisateur doit pouvoir sélectionner un événement, payer, et recevoir un billet valide et sécurisé.

## 1. Architecture de la Base de Données (JPA)

Nous devons étendre le modèle existant pour gérer l'inventaire et les transactions de manière robuste.

### A. Entité [Event](file:///c:/Users/bmd%20tech/Documents/spring_app/src/main/java/com/example/spring_app/models/Event.java#12-33) (Mise à jour)
- Ajout de la gestion des stocks : `capacity` (Integer) et `ticketsSold` (Integer).
- **CRITIQUE : Verrouillage Optimiste**. Ajout de `@Version private Long version;`. Cela garantit que si deux utilisateurs essaient d'acheter la dernière place à la milliseconde près, seul le premier réussira (Hibernate lèvera une `OptimisticLockException` pour le second).

### B. Entité `TicketCategory` (Catégories de billets)
- Champs : `id`, [name](file:///c:/Users/bmd%20tech/Documents/spring_app/src/main/java/com/example/spring_app/services/UserService.java#37-48) (ex: "Pass Étudiant", "VIP"), `price`, `event_id` (ManyToOne), `quantityAvailable`.
- Permet à un seul événement d'avoir plusieurs tarifs.

### C. Entité `Order` (La transaction financière)
- L'entité centrale pour la traçabilité.
- Champs : `id`, `user` (ManyToOne - l'acheteur), `orderDate`, `totalAmount`, `status` (PENDING, PAID, CANCELLED), `stripeSessionId` (String).

### D. Entité `Ticket` (Le billet final)
- Ce qui donne le droit d'entrée. 1 Order = 1 ou plusieurs Tickets.
- Champs : `id`, `order` (ManyToOne), `ticketCategory` (ManyToOne), `uniqueCode` (UUID, pour le QR Code), `isScanned` (Boolean, false par défaut).

## 2. Intégration du Paiement (Stripe)

Pour accepter des paiements sans gérer les données sensibles de cartes bancaires, nous allons utiliser **Stripe Checkout**.

**Workflow Technique :**
1. **Création (Client -> Backend)** : Le client choisit ses billets. Le backend crée une `Order` en `PENDING`, réserve temporairement les places, et appelle Stripe pour créer une "Checkout Session".
2. **Paiement (Client -> Stripe)** : Le backend renvoie l'URL de la session Stripe. Le client est redirigé vers Stripe pour payer.
3. **Confirmation (Stripe -> Backend Webhook)** : Le client a payé. Stripe fait un appel POST (Webhook) sur `/api/stripe/webhook` avec l'événement `checkout.session.completed`.
4. **Validation (Backend)** : Notre application trouve l'`Order` grâce au `stripeSessionId`, la passe en `PAID`, génère de manière permanente les `Ticket`s, et déclenche l'envoi de l'email.

## 3. Génération des PDF et Envoi (Post-Paiement)

Dès que la commande est `PAID` :
1. **Génération du QR Code** : Nous allons ajouter la librairie **Google ZXing** pour transformer le `uniqueCode` du billet en image QR Code.
2. **Création du Billet PDF** : Utilisation d'**OpenPDF** ou **Thymeleaf + Flying Saucer** pour générer un beau fichier PDF contenant le nom de l'événement, la date, et le QR Code à scanner.
3. **Envoi d'Email** : Utilisation de `spring-boot-starter-mail` (connecté à un serveur SMTP ou un service comme SendGrid) pour envoyer le PDF en pièce jointe (avec traitement asynchrone `@Async` pour ne pas bloquer le webhook).

---

### Prochaines Étapes pour le Code

Si vous êtes prêt à coder, voici les **Sprints immédiats** :

- **Sprint 1.1** : Créer les entités JPA (`Order`, `Ticket`, `TicketCategory`) et mettre à jour [Event](file:///c:/Users/bmd%20tech/Documents/spring_app/src/main/java/com/example/spring_app/models/Event.java#12-33). Générer les Repositories correspondants.
- **Sprint 1.2** : Coder le `OrderService` pour créer une commande et implémenter le "Verrouillage Optimiste" pour la gestion des stocks.
- **Sprint 1.3** : Ajouter `stripe-java` dans le [pom.xml](file:///c:/Users/bmd%20tech/Documents/spring_app/pom.xml), configurer la clé API, et coder le contrôleur Webhook pour écouter Stripe.

> [!TIP]
> Êtes-vous prêt à lancer le **Sprint 1.1** (Création des Entités) ? Je peux commencer à créer les classes `Order.java`, `Ticket.java` et `TicketCategory.java` directement dans `src/main/java`.

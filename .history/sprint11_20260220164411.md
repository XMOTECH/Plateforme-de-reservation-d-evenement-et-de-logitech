 Document de Conception : Sprint 1.1 (Cœur Transactionnel)
Ce document détaille l'architecture des données et les spécifications d'implémentation pour le système de réservation et de billetterie. L'objectif est de mettre en place les fondations JPA avant de coder la logique métier.

1. Architecture des Données (Diagramme de Classes)
Voici comment les nouvelles entités interagissent avec les entités existantes (

User
 et 

Event
).

mermaid
classDiagram
    direction TB
    
    class User {
        +Long id
        +String name
        +String email
        +String password
    }
    
    class Event {
        +Long id
        +String title
        +LocalDateTime eventDateTime
        +Integer capacity
        +Integer ticketsSold
        +Long version
        +...()
    }
    
    class TicketCategory {
        +Long id
        +String name
        +Double price
        +Integer quantityAvailable
    }
    
    class Order {
        +Long id
        +LocalDateTime orderDate
        +Double totalAmount
        +String stripeSessionId
        +OrderStatus status
    }
    
    class Ticket {
        +Long id
        +String uniqueCode
        +Boolean isScanned
    }
    
    class OrderStatus {
        <<enumeration>>
        PENDING
        PAID
        CANCELLED
    }
    User "1" -- "*" Event : organise >
    User "1" -- "*" Order : passe >
    Event "1" -- "*" TicketCategory : possède >
    Order "1" -- "*" Ticket : contient >
    TicketCategory "1" -- "*" Ticket : définit le type de >
2. Spécifications d'Implémentation par Entité
Toutes ces classes doivent être créées dans le package com.example.spring_app.models.

A. L'énumération OrderStatus
Avant de créer l'entité Order, créez l'énumération qui gèrera son état.

Type : enum Java.
Valeurs : PENDING, PAID, CANCELLED.
B. L'entité TicketCategory (Les tarifs)
Gère les différents types de billets pour un événement (ex: VIP, Fosse, Étudiant).

Annotations de classe : @Entity, @Getter, @Setter, @NoArgsConstructor.
Champs :
id : Long -> Annoté avec @Id et @GeneratedValue(strategy = GenerationType.IDENTITY).

name
 : String -> Le nom de la catégorie (ex: "VIP").
price : Double -> Le prix spécifique de cette catégorie. (Vous pouvez utiliser BigDecimal si vous préférez une précision absolue pour la monnaie).
quantityAvailable : Integer -> Le quota de billets pour ce tarif précis.
Relations :
event : De type 

Event
. C'est la relation inverse. Annoté avec @ManyToOne et @JoinColumn(name = "event_id").
C. L'entité Order (La commande / transaction)
Représente l'acte d'achat d'un utilisateur.

Annotations de classe : @Entity, @Table(name = "orders") (⚠️ Très important car "order" est un mot réservé en SQL).
Champs :
id : Long (Standard JPA Id).
orderDate : LocalDateTime -> Annoté avec @CreationTimestamp (Hibernate) pour s'auto-remplir lors du save().
totalAmount : Double -> Le prix total calculé avant l'envoi au paiement.
stripeSessionId : String -> Peut être nul au départ. Stockera l'ID renvoyé par Stripe. Annoté avec @Column(unique = true).
status : Type OrderStatus -> Annoté obligatoirement avec @Enumerated(EnumType.STRING) pour stocker le nom du statut en clair dans la base.
Relations :
user : De type 

User
 (l'acheteur). Annoté avec @ManyToOne et @JoinColumn(name = "user_id").
D. L'entité Ticket (Le billet d'entrée)
Le billet final que l'utilisateur présentera à l'entrée. Une commande (Order) peut générer plusieurs Ticket.

Annotations de classe : @Entity.
Champs :
id : Long (Standard JPA Id).
uniqueCode : String -> Le code qui sera scanné. Annoté avec @Column(unique = true, updatable = false).
isScanned : Boolean -> Défaut à false (utiliser private Boolean isScanned = false; directement dans la classe).
Relations :
order : De type Order (la commande d'origine). Annoté avec @ManyToOne.
ticketCategory : De type TicketCategory (le type exact de ce billet). Annoté avec @ManyToOne.
E. Mise à jour de l'entité 

Event
 existante
Il faut modifier votre classe 

Event.java
 actuelle pour supporter la vente de billets de manière sécurisée.

Nouveaux Champs Réguliers :
capacity : Integer -> Le nombre max de personnes autorisées.
ticketsSold : Integer -> Défaut à 0.
Le Champ Critique (Gestion de Concurrence) :
version : Long.
Annotation vitale : Ajoutez simplement @Version (de jakarta.persistence.Version) au-dessus de ce champ.
Explication Architecture : C'est ce qu'on appelle l'Optimistic Locking. Spring Data JPA va automatiquement incrémenter ce chiffre à chaque modification en base. Si le script SQL généré par Hibernate tente de mettre à jour la base avec une version 1 alors qu'un autre achat a entre-temps passé la base à 2, Hibernate rejettera la transaction (évitant ainsi de vendre la même place à deux personnes en même temps).
3. Couche Accès aux Données (Repositories)
Dans src/main/java/com/example/spring_app/repositories, vous devrez créer trois nouvelles interfaces. Elles suivent la même logique que votre UserRepository.

1. TicketCategoryRepository.java

java
public interface TicketCategoryRepository extends JpaRepository<TicketCategory, Long> {
    List<TicketCategory> findByEventId(Long eventId);
}
2. OrderRepository.java

java
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
    Optional<Order> findByStripeSessionId(String stripeSessionId); // Très utile pour le retour du Webhook Stripe !
}
3. TicketRepository.java

java
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByOrderId(Long orderId);
    Optional<Ticket> findByUniqueCode(String uniqueCode); // Pour l'application de scan à l'entrée
}
Dès que vous avez implémenté ce modèle de données et que l'application compile et que les tables sont créées dans PostgreSQL (grâce à ddl-auto=create), la fondation de votre système de réservation sera prête pour la logique transactionnelle ! Bon code !
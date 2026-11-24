# Activité Pratique N°2 – Implémentation d'une architecture micro-services

Application de gestion de **factures** contenant des **produits** et appartenant à un **client**.

## Architecture

```
                         ┌────────────────────┐
   Angular (4200) ─────► │  gateway-service    │ :8888  (Spring Cloud Gateway)
                         └─────────┬──────────┘
                                   │  lb://  (routes dynamiques via Eureka)
         ┌─────────────────────────┼─────────────────────────┐
         ▼                         ▼                         ▼
 customer-service :8081   inventory-service :8082   billing-service :8083
   (clients, H2)            (produits, H2)            (factures, H2)
                                                    │  OpenFeign
                                                    └──► customer + inventory

 discovery-service :8761 (Eureka)      config-service :9999 (Spring Cloud Config)
                                               │
                                          config-repo/
```

| Service | Port | Rôle |
|---|---|---|
| discovery-service | 8761 | Annuaire Eureka |
| config-service | 9999 | Configuration centralisée (lit `config-repo/`) |
| gateway-service | 8888 | Point d'entrée unique, routage statique ou dynamique |
| customer-service | 8081 | CRUD clients (Spring Data REST) |
| inventory-service | 8082 | CRUD produits (Spring Data REST) |
| billing-service | 8083 | Factures + agrégation client/produits via OpenFeign |
| angular-front | 4200 | Client web |

Stack : Java 21, Spring Boot 3.3.5, Spring Cloud 2023.0.3, H2, Lombok, Angular 18 + Bootstrap 5.

## Ordre de démarrage

```bash
# 1. Annuaire
cd discovery-service && mvn spring-boot:run
# 2. Configuration
cd config-service    && mvn spring-boot:run
# 3. Services métier
cd customer-service  && mvn spring-boot:run
cd inventory-service && mvn spring-boot:run
# 4. Facturation (APRÈS customer et inventory : il génère des factures avec leurs données)
cd billing-service   && mvn spring-boot:run
# 5. Gateway
cd gateway-service   && mvn spring-boot:run
# 6. Front
cd angular-front && npm install && npm start     # http://localhost:4200
```

Dans IntelliJ : ouvrir le dossier racine (le `pom.xml` parent importe tous les modules) et lancer chaque classe `*Application`.

## Étapes du travail et comment les tester

### 1. customer-service
Entité `Customer`, `CustomerRepository` exposé par `@RepositoryRestResource`, données de test dans un `CommandLineRunner`.
- http://localhost:8081/api/customers
- http://localhost:8081/api/customers/1
- http://localhost:8081/api/customers?projection=fullCustomer
- Console H2 : http://localhost:8081/h2-console (JDBC URL `jdbc:h2:mem:customer-db`)

### 2. inventory-service
Entité `Product` (id UUID, name, price, quantity).
- http://localhost:8082/api/products

### 3 & 4. Gateway + configuration statique
Fichier `gateway-service/src/main/resources/application-static.yml`. Lancer avec le profil `static` :
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=static
```
- http://localhost:8888/api/customers
- http://localhost:8888/api/products

(Avant d'avoir Eureka, remplacer `lb://CUSTOMER-SERVICE` par `http://localhost:8081`, comme dans la vidéo.)

### 5. Eureka
- Tableau de bord : http://localhost:8761 → tous les services doivent apparaître (CUSTOMER-SERVICE, INVENTORY-SERVICE, BILLING-SERVICE, GATEWAY-SERVICE, CONFIG-SERVICE).

### 6. Routes dynamiques
Bean `DiscoveryClientRouteDefinitionLocator` dans `GatewayServiceApplication` (profil par défaut). L'URL contient le nom du service :
- http://localhost:8888/customer-service/api/customers
- http://localhost:8888/inventory-service/api/products
- http://localhost:8888/billing-service/api/bills

### 7. billing-service avec OpenFeign
- `CustomerRestClient` et `ProductRestClient` (`@FeignClient(name = "customer-service")` …) : aucune URL en dur, résolution via Eureka.
- `Bill` et `ProductItem` sont des entités JPA ; `Customer` et `Product` sont de simples modèles marqués `@Transient`.
- `@JsonProperty(access = WRITE_ONLY)` sur `ProductItem.bill` évite la boucle infinie en JSON.

Tests :
- http://localhost:8083/fullBill/1 → facture + client + produits
- http://localhost:8888/billing-service/fullBill/1 (via la gateway)
- http://localhost:8083/api/bills/search/findByCustomerId?customerId=1

### 8. config-service
- `config-repo/application.properties` : config commune à tous les services.
- `config-repo/<service>.properties` : config propre à chaque service, + `-dev` / `-prod` pour customer-service.
- Chaque service charge sa config via `spring.config.import=optional:configserver:http://localhost:9999`.

Tests :
- http://localhost:9999/customer-service/default
- http://localhost:9999/customer-service/dev
- http://localhost:8081/testConfig1
- http://localhost:8081/globalConfig

**Rafraîchir à chaud** : modifier `customer.params.x` dans `config-repo/customer-service.properties`, puis
```bash
curl -X POST http://localhost:8081/actuator/refresh
```
puis recharger `/testConfig1` : la nouvelle valeur apparaît sans redémarrer (`@RefreshScope`).

Par défaut le config-service utilise le profil `native` (lecture directe du dossier). Pour utiliser Git comme dans la vidéo, voir les commentaires dans `config-service/src/main/resources/application.properties`.

### 9. Client Angular
Toutes les requêtes passent par la gateway (`http://localhost:8888`, CORS autorisé pour `localhost:4200`).
- **Clients** → bouton *Factures* → liste des factures du client → *Détails* → facture complète avec total.
- **Produits** → liste des produits.

## Captures à ajouter pour le rendu
1. Dashboard Eureka avec tous les services
2. `/api/customers` et `/api/products`
3. Accès via la gateway (statique puis dynamique)
4. `/fullBill/1`
5. `/customer-service/default` sur le config-service + test du refresh
6. Les pages Angular

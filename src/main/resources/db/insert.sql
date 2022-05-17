SET FOREIGN_KEY_CHECKS=0;

truncate table country;
truncate table states;
truncate table local_government;
truncate table address;
truncate table app_user;
truncate table  app_user_addresses;
truncate table user_role;
truncate table app_user_user_roles;
truncate table customer;
truncate table vendor;
truncate table employee;
truncate table category;

INSERT INTO country (id,country_name)
VALUES (1,'Nigeria'),(2,'Republic of Benin'),(3,'Ghana');

INSERT INTO states(id,state_name,country_id)
VALUES (1,'Lagos',1),(2,'Ogun',1),(3,'Oyo',1),(4,'Osun',1),(5,'Ekiti',1),(6,'Ondo',1);

INSERT INTO local_government (id,local_government_name,state_id)
VALUES (1,'Agege',1),(2,'Alimosho',1),(3,'Amuwo Odofin',1),(4,'Epe',1),(5,'Ojo',1),(6,'Lagos Mainland',1),
       (7,'Lagos Island',1),(8,'Eti-Osa',1),(9,'Ifako Ijaiye',1);

INSERT INTO address (id,street_number,street_name,city,post_zip_code,land_mark,local_Government_id)
VALUES (1,'Block 202, Flat 2','Amuwo Odofin Housing Estate','Mile 2','','',3),
       (2,'17','Wakeman Street Alagomeji','Yaba','','',6),
       (3,'20','Agege Motor Way','Oni gbongbo','','',1),
       (4,'79','Ipaye Street','Ojo Alaba Internationa','','',5),
       (5,'20','Bodillon Road','Ikoyi','100011','',8),
       (6,'8','Adeogun Crescent','Victoria Island','110001','',8),
       (7,'36','Ray Power Road','Alakuko','100011','',9);

INSERT INTO app_user_addresses (app_users_id,addresses_id)
VALUES (1,2),(2,1),(3,3),(4,4),(5,5),(6,6),(7,7);

INSERT INTO app_user(id,email,password,phone,username,user_type)
VALUES (1,'adebukolafaolujo@gmail.com','$2a$12$XNsrlHHhbkAczUcuUqpks.x9Fnx/bqWNgl1ai8QosWOdgJSPReXs6','08080472478','BukolaFako','CUSTOMER'),
       (2,'fakolujos@gmail.com','$2a$12$XNsrlHHhbkAczUcuUqpks.x9Fnx/bqWNgl1ai8QosWOdgJSPReXs6','08028424682','AkinEmma','CUSTOMER'),
       (3,'lgisltd@hotmail.com','$2a$12$XNsrlHHhbkAczUcuUqpks.x9Fnx/bqWNgl1ai8QosWOdgJSPReXs6','08097566543','LogicGate','VENDOR'),
       (4,'zerowastefarms@gmail.com','$2a$12$XNsrlHHhbkAczUcuUqpks.x9Fnx/bqWNgl1ai8QosWOdgJSPReXs6','09067543236','ZeroWaste','VENDOR'),
       (5,'hephzibah@gmail.com','$2a$12$XNsrlHHhbkAczUcuUqpks.x9Fnx/bqWNgl1ai8QosWOdgJSPReXs6','09045682654','HephzibahPam','EMPLOYEE'),
       (6,'akinola.adewunmi@oal.com','$2a$12$XNsrlHHhbkAczUcuUqpks.x9Fnx/bqWNgl1ai8QosWOdgJSPReXs6','08097566542','AkinSquare','EMPLOYEE'),
       (7,'akintudeokedara@firstachiever.ng','$2a$12$XNsrlHHhbkAczUcuUqpks.x9Fnx/bqWNgl1ai8QosWOdgJSPReXs6','08097653445','FirstAchiever','VENDOR');

INSERT INTO app_user_user_roles(app_users_id,user_roles_id)
VALUES (1,1),
       (2,1),
       (3,2),
       (4,2),
       (5,3),
       (5,8),
       (6,4),
       (7,2);

INSERT INTO user_role(id,role_name)
VALUES (1,'ROLE_CUSTOMER'),(2,'ROLE_VENDOR'),
       (3,'ROLE_ADMIN'),(4,'ROLE_MANAGER'),
       (5,'ROLE_SUPER_ADMIN'),(6,'ROLE_GM'),
       (7,'ROLE_INTERN'),(8,'ROLE_EMPLOYEE'),(9,'ROLE_ADMIN_ASSISTANT');

INSERT INTO customer(id,first_name,last_name,gender,age_range,app_user_id)
VALUES (1,'Adebukola','Fakolujo','FEMALE','FORTY_TO_FIFTY_NINE',1),
       (2,'Akin','Emmanuel','MALE','SIXTY_TO_SEVENTY_NINE',2);

INSERT INTO vendor(id,business_entity,name,rc_number,tax_id,representative,means_of_identification,means_of_id_number,
                    means_of_id_issue_date,means_of_id_expiry_date,facility,nature_of_business,app_user_id)
VALUES (1,'BUSINESS','Logic Gate Integrated Services Limited','RC158854','170907564','Olukoya Daniel','DRIVERS_LICENSE',
        '15689090','2019-01-10','2023-01-09','OWN','Irriation Equipment & Tools',3),
       (2,'BUSINESS','Zero Waste Farms & Agro Business Limited','RC19889','Y1986567','Adeleke Kujore','DRIVERS_LICENSE',
       'FG79989654','2020-06-20','2024-06-08','OWN','Landscaping materials, soil fertility and composting,',4),
       (3,'BUSINESS','First Achiever','RC257388','2577893679','Akintunde Okekunle','INTERNATIONAL_PASSPORT',
       'A0675446','2018-12-03','2023-12-02','OWN','Agro Training',7);

INSERT INTO employee(id,first_name,last_name,other_names,gender,dob,age,next_of_kin,relationship_with_next_of_kin,hired_date,
            end_date,app_user_id)
values (1,'Hephzibah','Fakolujo','Oluwapamilerin','MALE','2008-04-12',14,'Folawemi Fakolujo','BROTHER','2007-09-10','',5),
       (2,'Akinwunmi','Akinola','Aderagbemi','FEMALE','1980-06-20',42,'Atolagbe Ijaduola','BROTHER','2008-09-01','',6);

INSERT INTO category(id,category_name)
VALUES (1,'Horticulture'),(2,'Poultry'),(3,'Aquatic'),(4,'Livestock'),(5,'Fruit Crops'),(6,'Cash Crops'),
       (7,'Cereal Crops'),(8,'Vegetable Crops'),(9,'Tuber Crops'),(10,'Timber'),(11,'Equipment and Tools'),
       (12,'Machinery'),(13,'Plumbing Pipe, Fittings and Accessories'),(14,'Agro Chemical'),(15,'Animal Feed'),(16,'Herbs Spices'),
       (17,'Seed Starting Supplies'),(18,'Seeds and Seedlings'),(19,'Irrigation Equipment and Accessories'),
       (20,'Pruning Tools'),(21,'Mechanical System'),(22,'Pest Control Services'),(23,'Weed Control Services'),
       (24,'Livestock Breeder'),(25,'Borehole and Well Driller services'),(26,'Farm Worker'),(27,'Machinery Hiring Services'),
       (28,'Tools and Equipment Hiring Services'),(29,'Quality Grading Services'),(30,'Equipment Repair Services'),
       (31,'Auditing Accreditation'),(32,'Organic Accreditation Services'),(33,'Soil Testing Services'),(34,'Cassava Processing'),
       (35,'Palm Kennel Processing Services'),(36,'Soil Fertility and Composting Services'),(37,'Land Management Services'),
       (38,'Fertilizer and Soil Amendment'),(39,'Agricultural Training'),(40,'Logistics and Transportation Services'),
       (41,'Warehousing Services'),(42,'Veterinary Services'),(43,'Plant Support'),(44,'Silage Tarps'),
       (45,'Harvesting Supplies'),(46,'Pest Control'),(47, 'Fencing Services');

INSERT INTO colour(id, colour_name)
VALUES(1,'Green'),(2,'Purple'),(3,'Brown'),(4,'Yellow'),(5,'Red'),(6,'Black'),(7,'Blue'),(8,'Sun Gold');

INSERT INTO product_colours(products_id,colours_id)
VALUES (1,1),(2,1),(3,8),(4,6);

INSERT INTO product(id,brand,part_number,price,product_availability,product_condition,product_description,product_dimension,
            product_name,productsku,product_style,product_type,stock_quantity,unit_of_measure,weight,imageurl,category_id,vendor_id)
VALUES(1,'Hoss','',5000,'IN_STOCK','DRY','Fruits have a high sugar content with sweet flavour and great texture. Round melons are up to 30 pounds with a hearty centre',
        '','Crimson Sweet','HVS081','','Watermelon',50,'PKT','','https://hosstools.com/wp-content/uploads/2020/10/sugar-baby-watermelon.jpg',18,2),
       (2,'Hoss','',5000,'IN_STOCK','DRY','Cucumber, this heirloom was developed by the National Pickles Packers Association and is consistently a great producer',
       '','National Pickling','HVS078','','Cucumber',50,'PKT','','https://hosstools.com/wp-content/uploads/2020/10/national-pickling-cucumber.jpg',18,2),
       (3,'Hoss','',6000,'IN_STOCK','DRY','These indeterminate, golden cherry tomatoes product long clusters of 10-15 fruits that have a sweet, fruity flavour.',
       '','Sun Gold','HVS051','','Tomato',100,'PKT','','https://hosstools.com/wp-content/uploads/2020/10/sun-gold-tomato.jpg',18,2),
       (4,'Hoss','H2346316',15000,'IN_STOCK','NEW','The Filter Regulator Combo has everything needed to go between the water source and mainline tubing in a drip tape irrigation system. Easily attach the swivel end to a faucet or 3/4″ water hose. Attach the other end to 5/8″ Mainline Tubing to begin your branching drip tape system. We pre-assemble these for your convenience with Teflon pipe tape to secure all connections. Filter includes: 3/4″ Hose Swivel, Drip Irrigation Filter, 12 PSI Pressure Regulator, 3/4″ Threaded Nipple, Easy Lock Male End',
       '12 x 10 x 6 inch','Filter Regulator Combo','','','Filter Regulator',10,'PCS','2 lbs','https://hosstools.com/wp-content/uploads/2020/10/Irrigation-Filter-Regulator-Combo1.jpg',19,1);

INSERT INTO service(id, service_name, service_description,category_id,vendor_id)
VALUES(1, 'Grooming','Grooming of Livestocks and Aquatic Animals',24,2),
      (2,'Animal Grading','Grade marks represent the flavour and quality of animal products',29,2),
      (3,'Composting','Coverting farm wastes into soil nutrient, amendment and wealth',36,2);

INSERT INTO cart(id,order_quantity,product_id,app_user_id)
VALUES (1,2,1,1),(2,3,3,1);

--INSERT INTO order(id,app_user_id,amount_total,order_date,)
--VALUES (1,1,50000,now()), (2,1,16500,now()),(3,1,6000,now());

--INSERT INTO wish_list(id,created_date,app_user_id,product_id)
--VALUES ()

SET FOREIGN_KEY_CHECKS=1;
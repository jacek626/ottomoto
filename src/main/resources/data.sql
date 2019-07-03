INSERT INTO ROLE(id,name) values (1, 'ROLE_ADMIN');
INSERT INTO ROLE(id,name) values (2, 'ROLE_USER');


INSERT INTO public.user_os(
	id, active, city, description, email, login, password, first_name, last_name)
	VALUES ( -1,true, 'Lublin', 'opis osadmin' , 'osadmin@sdd.pl', 'osadmin', '$2a$10$yRZmDPRpBNP7u7TFfYyCLuGbQAb0Jd9eDbjCsglUd5Dt3YhERmaf6','Janusz', 'Nowak');

	
INSERT INTO public.user_os(
	id, active, city, description, email, login, password, first_name, last_name, phone_number, zip_code, street, province)
	VALUES ( -2,true, 'Lublin', 'opis osuser' , 'osuser1@sdd.pl', 'osuser', '$2a$10$yRZmDPRpBNP7u7TFfYyCLuGbQAb0Jd9eDbjCsglUd5Dt3YhERmaf6',
	'Jan','Kowalski',434545756,'34-234','ul Lubelska','PODKARPACKIE');	
INSERT INTO public.user_os(
	id, active, city, description, email, login, password, first_name, last_name)
	VALUES ( -3,true, 'Lublin', 'opis osuser' , 'osuser2@sdd.pl', 'osuser2', '$2a$10$yRZmDPRpBNP7u7TFfYyCLuGbQAb0Jd9eDbjCsglUd5Dt3YhERmaf6','Wacław','Rutkowski');
INSERT INTO public.user_os(
	id, active, city, description, email, login, password, first_name, last_name)
	VALUES ( -4,true, 'Lublin', 'opis osuser' , 'osuser3@sdd.pl', 'osuser3', '$2a$10$yRZmDPRpBNP7u7TFfYyCLuGbQAb0Jd9eDbjCsglUd5Dt3YhERmaf6','Ksawery','Ptak');
INSERT INTO public.user_os(
	id, active, city, description, email, login, password)
	VALUES ( -5,true, 'Lublin', 'opis osuser' , 'osuser4@sdd.pl', 'osuser4', '$2a$10$yRZmDPRpBNP7u7TFfYyCLuGbQAb0Jd9eDbjCsglUd5Dt3YhERmaf6');
INSERT INTO public.user_os(
	id, active, city, description, email, login, password)
	VALUES ( -6,true, 'Lublin', 'opis osuser' , 'osuser5@sdd.pl', 'osuser5', '$2a$10$yRZmDPRpBNP7u7TFfYyCLuGbQAb0Jd9eDbjCsglUd5Dt3YhERmaf6');
INSERT INTO public.user_os(
	id, active, city, description, email, login, password)
	VALUES ( -7,true, 'Lublin', 'opis osuser' , 'osuser6@sdd.pl', 'osuser6', '$2a$10$yRZmDPRpBNP7u7TFfYyCLuGbQAb0Jd9eDbjCsglUd5Dt3YhERmaf6');
INSERT INTO public.user_os(
	id, active, city, description, email, login, password)
	VALUES ( -8,true, 'Lublin', 'opis osuser' , 'osuser7@sdd.pl', 'osuser7', '$2a$10$yRZmDPRpBNP7u7TFfYyCLuGbQAb0Jd9eDbjCsglUd5Dt3YhERmaf6');
INSERT INTO public.user_os(
	id, active, city, description, email, login, password)
	VALUES ( -9,true, 'Lublin', 'opis osuser' , 'osuser8@sdd.pl', 'osuser8', '$2a$10$yRZmDPRpBNP7u7TFfYyCLuGbQAb0Jd9eDbjCsglUd5Dt3YhERmaf6');
INSERT INTO public.user_os(
	id, active, city, description, email, login, password)
	VALUES ( -10,true, 'Lublin', 'opis osuser' , 'osuser9@sdd.pl', 'osuser9', '$2a$10$yRZmDPRpBNP7u7TFfYyCLuGbQAb0Jd9eDbjCsglUd5Dt3YhERmaf6');
INSERT INTO public.user_os(
	id, active, city, description, email, login, password)
	VALUES ( -11,true, 'Lublin', 'opis osuser' , 'osuser10@sdd.pl', 'osuser10', '$2a$10$yRZmDPRpBNP7u7TFfYyCLuGbQAb0Jd9eDbjCsglUd5Dt3YhERmaf6');
INSERT INTO public.user_os(
	id, active, city, description, email, login, password)
	VALUES ( -12,true, 'Lublin', 'opis osuser' , 'osuser11@sdd.pl', 'osuser11', '$2a$10$yRZmDPRpBNP7u7TFfYyCLuGbQAb0Jd9eDbjCsglUd5Dt3YhERmaf6');
INSERT INTO public.user_os(
	id, active, city, description, email, login, password)
	VALUES ( -13,true, 'Lublin', 'opis osuser' , 'osuser12@sdd.pl', 'osuser12', '$2a$10$yRZmDPRpBNP7u7TFfYyCLuGbQAb0Jd9eDbjCsglUd5Dt3YhERmaf6');
INSERT INTO public.user_os(
	id, active, city, description, email, login, password)
	VALUES ( -14,false, 'Lublin', 'opis osuser' , 'osuser13@sdd.pl', 'osuser13', '$2a$10$yRZmDPRpBNP7u7TFfYyCLuGbQAb0Jd9eDbjCsglUd5Dt3YhERmaf6');
INSERT INTO public.user_os(
	id, active, city, description, email, login, password)
	VALUES ( -15,false, 'Lublin', 'opis osuser' , 'osuser14@sdd.pl', 'osuser14', '$2a$10$yRZmDPRpBNP7u7TFfYyCLuGbQAb0Jd9eDbjCsglUd5Dt3YhERmaf6');

INSERT INTO user_role(role_id,user_id) VALUES(1,-1);
INSERT INTO user_role(role_id,user_id) VALUES(2,-2);
INSERT INTO user_role(role_id,user_id) VALUES(2,-3);
INSERT INTO user_role(role_id,user_id) VALUES(2,-4);
INSERT INTO user_role(role_id,user_id) VALUES(2,-5);
INSERT INTO user_role(role_id,user_id) VALUES(2,-6);
INSERT INTO user_role(role_id,user_id) VALUES(2,-7);
INSERT INTO user_role(role_id,user_id) VALUES(2,-8);
INSERT INTO user_role(role_id,user_id) VALUES(2,-9);
INSERT INTO user_role(role_id,user_id) VALUES(2,-10);
INSERT INTO user_role(role_id,user_id) VALUES(2,-11);
INSERT INTO user_role(role_id,user_id) VALUES(2,-12);
INSERT INTO user_role(role_id,user_id) VALUES(2,-13);
INSERT INTO user_role(role_id,user_id) VALUES(2,-14);
INSERT INTO user_role(role_id,user_id) VALUES(2,-15);

	
	
INSERT INTO public.manufacturer (id, name) VALUES (-1, 'Opel');
INSERT INTO public.manufacturer (id, name) VALUES (-2, 'Fiat');
INSERT INTO public.manufacturer (id, name) VALUES (-3, 'Peugeot');

INSERT INTO public.vehicle_model (id, name, vehicle_type, manufacturer_id) VALUES (-1, 'Astra', 'CAR', -1);
INSERT INTO public.vehicle_model (id, name, vehicle_type, manufacturer_id) VALUES (-2, 'Corsa',  'CAR', -1);
INSERT INTO public.vehicle_model (id, name, vehicle_type, manufacturer_id) VALUES (-3, 'Zafira', 'CAR', -1);
INSERT INTO public.vehicle_model (id, name, vehicle_type, manufacturer_id) VALUES (-4, '208',  'CAR', -3);
INSERT INTO public.vehicle_model (id, name, vehicle_type, manufacturer_id) VALUES (-5, '308',  'CAR', -3);
INSERT INTO public.vehicle_model (id, name, vehicle_type, manufacturer_id) VALUES (-6, '508',  'CAR', -3);
INSERT INTO public.vehicle_model (id, name, vehicle_type, manufacturer_id) VALUES (-7, 'Tipo',  'CAR', -2);
INSERT INTO public.vehicle_model (id, name, vehicle_type, manufacturer_id) VALUES (-8, '500',  'CAR', -2);
INSERT INTO public.vehicle_model (id, name, vehicle_type, manufacturer_id) VALUES (-9, '126p', 'CAR', -2);
INSERT INTO public.vehicle_model (id, name, vehicle_type, manufacturer_id) VALUES (-10, 'Motocykl1',  'MOTORCYCLE', -1);
INSERT INTO public.vehicle_model (id, name, vehicle_type, manufacturer_id) VALUES (-11, 'Motocykl2',  'MOTORCYCLE', -1);
INSERT INTO public.vehicle_model (id, name, vehicle_type, manufacturer_id) VALUES (-12, 'Motocykl3',  'MOTORCYCLE', -1);
INSERT INTO public.vehicle_model (id, name, vehicle_type, manufacturer_id) VALUES (-13, 'Motocykl4',  'MOTORCYCLE', -2);
INSERT INTO public.vehicle_model (id, name, vehicle_type, manufacturer_id) VALUES (-14, 'Motocykl5',  'MOTORCYCLE', -3);
INSERT INTO public.vehicle_model (id, name, vehicle_type, manufacturer_id) VALUES (-15, 'Motocykl6',  'MOTORCYCLE', -3);

INSERT INTO public.vehicle_model (id, name, vehicle_type, manufacturer_id) VALUES (-16, 'ciezarowy1',  'TRUCK', -1);
INSERT INTO public.vehicle_model (id, name, vehicle_type, manufacturer_id) VALUES (-17, 'ciezarowy2', 'TRUCK', -1);
INSERT INTO public.vehicle_model (id, name, vehicle_type, manufacturer_id) VALUES (-18, 'dostawczy1', 'TRUCK', -1);
INSERT INTO public.vehicle_model (id, name, vehicle_type, manufacturer_id) VALUES (-19, 'dostawczy2', 'TRUCK', -1);

INSERT INTO public.vehicle_model (id, name, vehicle_type, manufacturer_id) VALUES (-20, 'ciezarowy3',  'TRUCK', -2);
INSERT INTO public.vehicle_model (id, name, vehicle_type, manufacturer_id) VALUES (-21, 'ciezarowy4',  'TRUCK', -2);
INSERT INTO public.vehicle_model (id, name, vehicle_type, manufacturer_id) VALUES (-22, 'dostawczy3',  'TRUCK', -2);
INSERT INTO public.vehicle_model (id, name, vehicle_type, manufacturer_id) VALUES (-23, 'dostawczy4',  'TRUCK', -2);

INSERT INTO public.manufacturer_vehicle_model (manufacturer_id, vehicle_model_id) VALUES (-1, -1);
INSERT INTO public.manufacturer_vehicle_model (manufacturer_id, vehicle_model_id) VALUES (-1, -2);
INSERT INTO public.manufacturer_vehicle_model (manufacturer_id, vehicle_model_id) VALUES (-1, -3);
INSERT INTO public.manufacturer_vehicle_model (manufacturer_id, vehicle_model_id) VALUES (-3, -4);
INSERT INTO public.manufacturer_vehicle_model (manufacturer_id, vehicle_model_id) VALUES (-3, -5);
INSERT INTO public.manufacturer_vehicle_model (manufacturer_id, vehicle_model_id) VALUES (-3, -6);
INSERT INTO public.manufacturer_vehicle_model (manufacturer_id, vehicle_model_id) VALUES (-2, -7);
INSERT INTO public.manufacturer_vehicle_model (manufacturer_id, vehicle_model_id) VALUES (-2, -8);
INSERT INTO public.manufacturer_vehicle_model (manufacturer_id, vehicle_model_id) VALUES (-2, -9);
INSERT INTO public.manufacturer_vehicle_model (manufacturer_id, vehicle_model_id) VALUES (-1, -10);
INSERT INTO public.manufacturer_vehicle_model (manufacturer_id, vehicle_model_id) VALUES (-1, -11);
INSERT INTO public.manufacturer_vehicle_model (manufacturer_id, vehicle_model_id) VALUES (-1, -12);
INSERT INTO public.manufacturer_vehicle_model (manufacturer_id, vehicle_model_id) VALUES (-2, -13);
INSERT INTO public.manufacturer_vehicle_model (manufacturer_id, vehicle_model_id) VALUES (-3, -14);
INSERT INTO public.manufacturer_vehicle_model (manufacturer_id, vehicle_model_id) VALUES (-3, -15);

INSERT INTO public.manufacturer_vehicle_model (manufacturer_id, vehicle_model_id) VALUES (-1, -16);
INSERT INTO public.manufacturer_vehicle_model (manufacturer_id, vehicle_model_id) VALUES (-1, -17);
INSERT INTO public.manufacturer_vehicle_model (manufacturer_id, vehicle_model_id) VALUES (-1, -18);
INSERT INTO public.manufacturer_vehicle_model (manufacturer_id, vehicle_model_id) VALUES (-1, -19);

INSERT INTO public.manufacturer_vehicle_model (manufacturer_id, vehicle_model_id) VALUES (-2, -20);
INSERT INTO public.manufacturer_vehicle_model (manufacturer_id, vehicle_model_id) VALUES (-2, -21);
INSERT INTO public.manufacturer_vehicle_model (manufacturer_id, vehicle_model_id) VALUES (-2, -22);
INSERT INTO public.manufacturer_vehicle_model (manufacturer_id, vehicle_model_id) VALUES (-2, -23);

insert into announcement (id, car_color, currency, description, fuel_type, mileage, net_price, price, price_negotiate, production_year,
						  title, vehicle_subtype, vehicle_type, vin, manufacturer_id, user_id, vehicle_model_id, creation_date)
values (-1,'RED','PLN',22222222222222222,'PETROL',22222,false,20000.00,false,2019,222222222222,'CITY_CAR','CAR',111111111,-1,-1,-1, NOW());

insert into picture (id,file_name,repository_name,miniature_repository_name,announcement_id) values (-1,'picture1.jpg','NdnEnexdjVXwGC52kIwJ6nDaYUHU7j.jpg','NdnEnexdjVXwGC52kIwJ6nDaYUHU7j-small.jpg',-1);
insert into picture (id,file_name,repository_name,miniature_repository_name,announcement_id) values (-2,'picture2.jpg','fGFTJEKhScemVfcqSdb5eWgYxi3kHz.jpg','fGFTJEKhScemVfcqSdb5eWgYxi3kHz-small.jpg',-1);
insert into picture (id,file_name,repository_name,miniature_repository_name,announcement_id) values (-3,'picture3.jpg','15599911776532472509249.jpg','15599911776532472509249-small.jpg',-1);
insert into picture (id,file_name,repository_name,miniature_repository_name,announcement_id) values (-4,'picture4.jpg','15599910474527047691461.jpg','15599910474527047691461-small.jpg',-1);
insert into picture (id,file_name,repository_name,miniature_repository_name,announcement_id) values (-5,'picture5.jpg','15600057494514904227829.png','15600057494514904227829-small.png',-1);
insert into picture (id,file_name,repository_name,miniature_repository_name,announcement_id) values (-6,'picture6.jpg','15599910474527047691461.jpg','15599910474527047691461-small.jpg',-1);
insert into picture (id,file_name,repository_name,miniature_repository_name,announcement_id) values (-7,'picture7.jpg','15599911776532472509249.jpg','15599911776532472509249-small.jpg',-1);
insert into picture (id,file_name,repository_name,miniature_repository_name,announcement_id) values (-8,'picture8.jpg','15599910474527047691461.jpg','15599910474527047691461-small.jpg',-1);
insert into picture (id,file_name,repository_name,miniature_repository_name,announcement_id) values (-9,'picture9.jpg','15599911776532472509249.jpg','15599911776532472509249-small.jpg',-1);
insert into picture (id,file_name,repository_name,miniature_repository_name,announcement_id) values (-10,'picture10.jpg','15599910474527047691461.jpg','15599910474527047691461-small.jpg',-1);


insert into announcement (id, car_color, currency, description, fuel_type, mileage, net_price, price, price_negotiate, production_year,
						  title, vehicle_subtype, vehicle_type, vin, manufacturer_id, user_id, vehicle_model_id, creation_date)
values (-2,'BLUE','PLN','To jest opis','DIESEL',250000,false,49000,true, 2008, 'pieknie auto','SUV','CAR',34634343,-1,-2,-1, NOW());

insert into announcement (id, car_color, currency, description, fuel_type, mileage, net_price, price, price_negotiate, production_year,
						  title, vehicle_subtype, vehicle_type, vin, manufacturer_id, user_id, vehicle_model_id, creation_date)
values (-3,'GREY','PLN','To jest opis2','DIESEL',190000,false,33000,true, 2006, 'pieknie auto','COMPACT','CAR',7984343,-1,-3,-1, NOW());

insert into announcement (id, car_color, currency, description, fuel_type, mileage, net_price, price, price_negotiate, production_year,
						  title, vehicle_subtype, vehicle_type, vin, manufacturer_id, user_id, vehicle_model_id, creation_date)
values (-4,'SILVER','PLN','To jest opis3','PETROL',130000,false,12000,true, 2009, 'pieknie auto','COMBI','CAR',166554343,-1,-4,-1, NOW());

insert into announcement (id, car_color, currency, description, fuel_type, mileage, net_price, price, price_negotiate, production_year,
						  title, vehicle_subtype, vehicle_type, vin, manufacturer_id, user_id, vehicle_model_id, creation_date)
values (-5,'BLUE','PLN','To jest opis4','PETROL_WITH_LPG',21900,false,15000,true, 2017, 'pieknie auto','COUPE','CAR',675834343,-1,-5,-1, NOW());

insert into announcement (id, car_color, currency, description, fuel_type, mileage, net_price, price, price_negotiate, production_year,
						  title, vehicle_subtype, vehicle_type, vin, manufacturer_id, user_id, vehicle_model_id, creation_date)
values (-6,'GREEN','PLN','To jest opis5','DIESEL',260000,false,27000,true, 2011, 'pieknie auto','COMBI','CAR',75656834343,-1,-6,-1, NOW());

insert into announcement (id, car_color, currency, description, fuel_type, mileage, net_price, price, price_negotiate, production_year,
						  title, vehicle_subtype, vehicle_type, vin, manufacturer_id, user_id, vehicle_model_id, creation_date)
values (-7,'BLUE','PLN','To jest opis6','PETROL',50000,false,40000,true, 2018, 'pieknie auto','SMALL','CAR',4346834343,-1,-7,-1, NOW());

insert into announcement (id, car_color, currency, description, fuel_type, mileage, net_price, price, price_negotiate, production_year,
						  title, vehicle_subtype, vehicle_type, vin, manufacturer_id, user_id, vehicle_model_id, creation_date)
values (-8,'GOLD','PLN','To jest opis7','ELECTRIC',67000,false,17800,true, 2014, 'pieknie auto','COMPACT','CAR',54545434343,-1,-8,-1, NOW());

insert into announcement (id, car_color, currency, description, fuel_type, mileage, net_price, price, price_negotiate, production_year,
						  title, vehicle_subtype, vehicle_type, vin, manufacturer_id, user_id, vehicle_model_id, creation_date)
values (-9,'BLACK','PLN','To jest opis8','PETROL',55000,false,199000,true, 2019, 'pieknie auto','CABRIO','CAR',2156834543,-1,-1,-9, NOW());


--INSERT INTO admin(id, email, login, password,role) VALUES (-1, 'jacek626@gmail.com', 'jacek', 'haslo',1);
--INSERT INTO admin(id, email, login, password,role) VALUES (-2, 'mieczyslaw@gmail.com', 'mietek', 'haslomietka',1);
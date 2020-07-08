INSERT INTO ROLE(id,name) values (1, 'ROLE_ADMIN');
INSERT INTO ROLE(id,name) values (2, 'ROLE_USER');


INSERT INTO public.user_os(
	id, active, city, province, description, email, login, password, first_name, last_name, creation_date, phone_number)
	VALUES ( -1,true, 'Lublin','LUBELSKIE', 'opis osadmin' , 'osadmin@sdd.pl', 'osadmin', '$2a$10$yRZmDPRpBNP7u7TFfYyCLuGbQAb0Jd9eDbjCsglUd5Dt3YhERmaf6','Janusz', 'Nowak',NOW(), 792453756);




INSERT INTO public.user_os(
	id, active, city, description, email, login, password, first_name, last_name, phone_number, zip_code, street, province, creation_date)
	VALUES ( -2,true, 'Lublin', 'opis osuser' , 'osuser1@sdd.pl', 'osuser', '$2a$10$yRZmDPRpBNP7u7TFfYyCLuGbQAb0Jd9eDbjCsglUd5Dt3YhERmaf6',
	'Jan','Kowalski',434545756,'34-234','ul Lubelska','PODKARPACKIE',NOW());
INSERT INTO public.user_os(
	id, active, city, description, email, login, password, first_name, last_name, creation_date)
	VALUES ( -3,true, 'Lublin', 'opis osuser' , 'osuser2@sdd.pl', 'osuser2', '$2a$10$yRZmDPRpBNP7u7TFfYyCLuGbQAb0Jd9eDbjCsglUd5Dt3YhERmaf6','Wacław','Rutkowski',NOW());
INSERT INTO public.user_os(
	id, active, city, description, email, login, password, first_name, last_name, creation_date)
	VALUES ( -4,true, 'Lublin', 'opis osuser' , 'osuser3@sdd.pl', 'osuser3', '$2a$10$yRZmDPRpBNP7u7TFfYyCLuGbQAb0Jd9eDbjCsglUd5Dt3YhERmaf6','Ksawery','Ptak',NOW());
INSERT INTO public.user_os(
	id, active, city, description, email, login, password, creation_date)
	VALUES ( -5,true, 'Lublin', 'opis osuser' , 'osuser4@sdd.pl', 'osuser4', '$2a$10$yRZmDPRpBNP7u7TFfYyCLuGbQAb0Jd9eDbjCsglUd5Dt3YhERmaf6',NOW());
INSERT INTO public.user_os(
	id, active, city, description, email, login, password, creation_date)
	VALUES ( -6,true, 'Lublin', 'opis osuser' , 'osuser5@sdd.pl', 'osuser5', '$2a$10$yRZmDPRpBNP7u7TFfYyCLuGbQAb0Jd9eDbjCsglUd5Dt3YhERmaf6',NOW());
INSERT INTO public.user_os(
	id, active, city, description, email, login, password, creation_date)
	VALUES ( -7,true, 'Lublin', 'opis osuser' , 'osuser6@sdd.pl', 'osuser6', '$2a$10$yRZmDPRpBNP7u7TFfYyCLuGbQAb0Jd9eDbjCsglUd5Dt3YhERmaf6',NOW());
INSERT INTO public.user_os(
	id, active, city, description, email, login, password, creation_date)
	VALUES ( -8,true, 'Lublin', 'opis osuser' , 'osuser7@sdd.pl', 'osuser7', '$2a$10$yRZmDPRpBNP7u7TFfYyCLuGbQAb0Jd9eDbjCsglUd5Dt3YhERmaf6',NOW());
INSERT INTO public.user_os(
	id, active, city, description, email, login, password, creation_date)
	VALUES ( -9,true, 'Lublin', 'opis osuser' , 'osuser8@sdd.pl', 'osuser8', '$2a$10$yRZmDPRpBNP7u7TFfYyCLuGbQAb0Jd9eDbjCsglUd5Dt3YhERmaf6',NOW());
INSERT INTO public.user_os(
	id, active, city, description, email, login, password, creation_date)
	VALUES ( -10,true, 'Lublin', 'opis osuser' , 'osuser9@sdd.pl', 'osuser9', '$2a$10$yRZmDPRpBNP7u7TFfYyCLuGbQAb0Jd9eDbjCsglUd5Dt3YhERmaf6',NOW());
INSERT INTO public.user_os(
	id, active, city, description, email, login, password, creation_date)
	VALUES ( -11,true, 'Lublin', 'opis osuser' , 'osuser10@sdd.pl', 'osuser10', '$2a$10$yRZmDPRpBNP7u7TFfYyCLuGbQAb0Jd9eDbjCsglUd5Dt3YhERmaf6',NOW());
INSERT INTO public.user_os(
	id, active, city, description, email, login, password, creation_date)
	VALUES ( -12,true, 'Lublin', 'opis osuser' , 'osuser11@sdd.pl', 'osuser11', '$2a$10$yRZmDPRpBNP7u7TFfYyCLuGbQAb0Jd9eDbjCsglUd5Dt3YhERmaf6',NOW());
INSERT INTO public.user_os(
	id, active, city, description, email, login, password, creation_date)
	VALUES ( -13,true, 'Lublin', 'opis osuser' , 'osuser12@sdd.pl', 'osuser12', '$2a$10$yRZmDPRpBNP7u7TFfYyCLuGbQAb0Jd9eDbjCsglUd5Dt3YhERmaf6',NOW());
INSERT INTO public.user_os(
	id, active, city, description, email, login, password, creation_date)
	VALUES ( -14,false, 'Lublin', 'opis osuser' , 'osuser13@sdd.pl', 'osuser13', '$2a$10$yRZmDPRpBNP7u7TFfYyCLuGbQAb0Jd9eDbjCsglUd5Dt3YhERmaf6',NOW());
INSERT INTO public.user_os(
	id, active, city, description, email, login, password, creation_date)
	VALUES ( -15,false, 'Lublin', 'opis osuser' , 'osuser14@sdd.pl', 'osuser14', '$2a$10$yRZmDPRpBNP7u7TFfYyCLuGbQAb0Jd9eDbjCsglUd5Dt3YhERmaf6',NOW());



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

/*insert into observed_announcement(id, announcement_id, user_id) values (-1,-10,-2);
insert into observed_announcement(id, announcement_id, user_id) values (-2,-13,-2);
insert into observed_announcement(id, announcement_id, user_id) values (-3,-14,-2);*/

/*Opel Corsa*/
insert into announcement (id, accidents, active, car_color, creation_date, currency, damaged, description, doors, engine_capacity, engine_power, first_owner, fuel_type, mileage, net_price, price, price_negotiate, production_year, title, vehicle_subtype, vin, user_id, vehicle_model_id)
values (-20,	false,	true,	'SILVER',	NOW()	,'PLN'	,false	,'Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt', 5, 1200,	90,	false	,'PETROL'	,35000,	false	,40000.00,	true,	2018,	'Sprzedam Opel Corsa',	'CITY_CAR', 'ABC3435454545'	,	-1	,-2);

insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-20,'15942346947208979861516.jpg','false','15942346947208979861516.jpg','15942346947208979861516-small.jpg',-20);
insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-21,'15942347017588083274234.jpg','false','15942347017588083274234.jpg','15942347017588083274234-small.jpg',-20);
insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-22,'15942347056434844033709.jpg','true','15942347056434844033709.jpg','15942347056434844033709-small.jpg',-20);


insert into announcement (id, accidents, active, car_color, creation_date, currency, damaged, description, doors, engine_capacity, engine_power, first_owner, fuel_type, mileage, net_price, price, price_negotiate, production_year, title, vehicle_subtype, vin, user_id, vehicle_model_id)
values (-21,	false,	true,	'SILVER',	NOW()	,'PLN'	,false	,'Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt', 5, 1200,	90,	false	,'PETROL'	,80000,	false	,25000.00,	true,	2016,	'Sprzedam Opel Corsa',	'CITY_CAR', 'ABC3435454545'	,	-1	,-2);

insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-23,'15942346947208979861516.jpg','false','15942346947208979861516.jpg','15942346947208979861516-small.jpg',-21);
insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-24,'15942347017588083274234.jpg','false','15942347017588083274234.jpg','15942347017588083274234-small.jpg',-21);
insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-25,'15942347056434844033709.jpg','true','15942347056434844033709.jpg','15942347056434844033709-small.jpg',-21);


insert into announcement (id, accidents, active, car_color, creation_date, currency, damaged, description, doors, engine_capacity, engine_power, first_owner, fuel_type, mileage, net_price, price, price_negotiate, production_year, title, vehicle_subtype, vin, user_id, vehicle_model_id)
values (-22,	false,	true,	'SILVER',	NOW()	,'PLN'	,false	,'Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt', 5, 1000,	65,	false	,'DIESEL'	,170000,	false	,20000.00,	true,	2015,	'Sprzedam Opel Corsa',	'CITY_CAR', 'ABC3435454545'	,	-1	,-2);

insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-26,'15942346947208979861516.jpg','false','15942346947208979861516.jpg','15942346947208979861516-small.jpg',-22);
insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-27,'15942347017588083274234.jpg','false','15942347017588083274234.jpg','15942347017588083274234-small.jpg',-22);
insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-28,'15942347056434844033709.jpg','true','15942347056434844033709.jpg','15942347056434844033709-small.jpg',-22);


insert into announcement (id, accidents, active, car_color, creation_date, currency, damaged, description, doors, engine_capacity, engine_power, first_owner, fuel_type, mileage, net_price, price, price_negotiate, production_year, title, vehicle_subtype, vin, user_id, vehicle_model_id)
values (-23,	false,	true,	'SILVER',	NOW()	,'PLN'	,false	,'Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt', 5, 900,	60,	false	,'PETROL'	,200,	false	,66000.00,	true,	2020,	'Sprzedam Opel Corsa',	'CITY_CAR', 'ABC3435454545'	,	-1	,-2);

insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-29,'15942346947208979861516.jpg','false','15942346947208979861516.jpg','15942346947208979861516-small.jpg',-23);
insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-30,'15942347017588083274234.jpg','false','15942347017588083274234.jpg','15942347017588083274234-small.jpg',-23);
insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-31,'15942347056434844033709.jpg','true','15942347056434844033709.jpg','15942347056434844033709-small.jpg',-23);


insert into announcement (id, accidents, active, car_color, creation_date, currency, damaged, description, doors, engine_capacity, engine_power, first_owner, fuel_type, mileage, net_price, price, price_negotiate, production_year, title, vehicle_subtype, vin, user_id, vehicle_model_id)
values (-24,	false,	true,	'SILVER',	NOW()	,'PLN'	,false	,'Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt', 5, 1600,	120,	false	,'PETROL_WITH_LPG'	,10200,	false	,37000.00,	true,	2017,	'Sprzedam Opel Corsa',	'CITY_CAR', 'ABC3435454545'	,	-1	,-2);

insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-32,'15942346947208979861516.jpg','false','15942346947208979861516.jpg','15942346947208979861516-small.jpg',-24);
insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-33,'15942347017588083274234.jpg','false','15942347017588083274234.jpg','15942347017588083274234-small.jpg',-24);
insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-34,'15942347056434844033709.jpg','true','15942347056434844033709.jpg','15942347056434844033709-small.jpg',-24);


/*Opel Astra*/

insert into announcement (id, accidents, active, car_color, creation_date, currency, damaged, description, doors, engine_capacity, engine_power, first_owner, fuel_type, mileage, net_price, price, price_negotiate, production_year, title, vehicle_subtype, vin, user_id, vehicle_model_id)
values (-25,	false,	true,	'WHITE',	NOW()	,'PLN'	,false	,'Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt', 5, 1200,	90,	false	,'PETROL'	,35000,	false	,40000.00,	true,	2018,	'Sprzedam Opel Corsa',	'COMPACT', 'ABC3435454545'	,	-1	,-1);

insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-49,'15942383060533060749011.jpg','false','15942383060533060749011.jpg','15942383060533060749011-small.jpg',-25);
insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-35,'15942383113570275009967.jpg','false','15942383113570275009967.jpg','15942383113570275009967-small.jpg',-25);
insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-36,'15942383146517048367644.jpg','true','15942383146517048367644.jpg','15942383146517048367644-small.jpg',-25);


insert into announcement (id, accidents, active, car_color, creation_date, currency, damaged, description, doors, engine_capacity, engine_power, first_owner, fuel_type, mileage, net_price, price, price_negotiate, production_year, title, vehicle_subtype, vin, user_id, vehicle_model_id)
values (-26,	false,	true,	'WHITE',	NOW()	,'PLN'	,false	,'Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt', 5, 1200,	90,	false	,'PETROL'	,80000,	false	,25000.00,	true,	2016,	'Sprzedam Opel Corsa',	'COMPACT', 'ABC3435454545'	,	-1	,-1);

insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-37,'15942383060533060749011.jpg','false','15942383060533060749011.jpg','15942383060533060749011-small.jpg',-26);
insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-38,'15942383113570275009967.jpg','false','15942383113570275009967.jpg','15942383113570275009967-small.jpg',-26);
insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-39,'15942383146517048367644.jpg','true','15942383146517048367644.jpg','15942383146517048367644-small.jpg',-26);


insert into announcement (id, accidents, active, car_color, creation_date, currency, damaged, description, doors, engine_capacity, engine_power, first_owner, fuel_type, mileage, net_price, price, price_negotiate, production_year, title, vehicle_subtype, vin, user_id, vehicle_model_id)
values (-27,	false,	true,	'WHITE',	NOW()	,'PLN'	,false	,'Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt', 5, 1000,	65,	false	,'DIESEL'	,170000,	false	,20000.00,	true,	2015,	'Sprzedam Opel Corsa',	'COMPACT', 'ABC3435454545'	,	-1	,-1);

insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-40,'15942383060533060749011.jpg','false','15942383060533060749011.jpg','15942383060533060749011-small.jpg',-27);
insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-41,'15942383113570275009967.jpg','false','15942383113570275009967.jpg','15942383113570275009967-small.jpg',-27);
insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-42,'15942383146517048367644.jpg','true','15942383146517048367644.jpg','15942383146517048367644-small.jpg',-27);


insert into announcement (id, accidents, active, car_color, creation_date, currency, damaged, description, doors, engine_capacity, engine_power, first_owner, fuel_type, mileage, net_price, price, price_negotiate, production_year, title, vehicle_subtype, vin, user_id, vehicle_model_id)
values (-28,	false,	true,	'WHITE',	NOW()	,'PLN'	,false	,'Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt', 5, 900,	60,	false	,'PETROL'	,200,	false	,66000.00,	true,	2020,	'Sprzedam Opel Corsa',	'COMPACT', 'ABC3435454545'	,	-1	,-1);

insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-43,'15942383060533060749011.jpg','false','15942383060533060749011.jpg','15942383060533060749011-small.jpg',-28);
insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-44,'15942383113570275009967.jpg','false','15942383113570275009967.jpg','15942383113570275009967-small.jpg',-28);
insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-45,'15942383146517048367644.jpg','true','15942383146517048367644.jpg','15942383146517048367644-small.jpg',-28);


insert into announcement (id, accidents, active, car_color, creation_date, currency, damaged, description, doors, engine_capacity, engine_power, first_owner, fuel_type, mileage, net_price, price, price_negotiate, production_year, title, vehicle_subtype, vin, user_id, vehicle_model_id)
values (-29,	false,	true,	'WHITE',	NOW()	,'PLN'	,false	,'Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt', 5, 1600,	120,	false	,'PETROL_WITH_LPG'	,10200,	false	,37000.00,	true,	2017,	'Sprzedam Opel Corsa',	'COMPACT', 'ABC3435454545'	,	-1	,-1);

insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-46,'15942383060533060749011.jpg','false','15942383060533060749011.jpg','15942383060533060749011-small.jpg',-29);
insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-47,'15942383113570275009967.jpg','false','15942383113570275009967.jpg','15942383113570275009967-small.jpg',-29);
insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-48,'15942383146517048367644.jpg','true','15942383146517048367644.jpg','15942383146517048367644-small.jpg',-29);

/*Opel Zafira*/

insert into announcement (id, accidents, active, car_color, creation_date, currency, damaged, description, doors, engine_capacity, engine_power, first_owner, fuel_type, mileage, net_price, price, price_negotiate, production_year, title, vehicle_subtype, vin, user_id, vehicle_model_id)
values (-30,	false,	true,	'RED',	NOW()	,'PLN'	,false	,'Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt', 5, 1700,	190,	false	,'PETROL'	,35000,	false	,40000.00,	true,	2018,	'Sprzedam Opel Zafira',	'MINIVAN', 'ABC3435454545'	,	-1	,-3);

insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-50,'15942394085949748677959.jpg','false','15942394085949748677959.jpg','15942394085949748677959-small.jpg',-30);
insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-51,'15942394048202445943120.jpg','false','15942394048202445943120.jpg','15942394048202445943120-small.jpg',-30);
insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-52,'15942394118974115717260.jpg','true','15942394118974115717260.jpg','15942394118974115717260-small.jpg',-30);

insert into announcement (id, accidents, active, car_color, creation_date, currency, damaged, description, doors, engine_capacity, engine_power, first_owner, fuel_type, mileage, net_price, price, price_negotiate, production_year, title, vehicle_subtype, vin, user_id, vehicle_model_id)
values (-31,	false,	true,	'RED',	NOW()	,'PLN'	,false	,'Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt', 5, 1700,	160,	false	,'PETROL'	,80000,	false	,25000.00,	true,	2016,	'Sprzedam Opel Zafira',	'MINIVAN', 'ABC3435454545'	,	-1	,-3);

insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-53,'15942394085949748677959.jpg','false','15942394085949748677959.jpg','15942394085949748677959-small.jpg',-31);
insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-54,'15942394048202445943120.jpg','false','15942394048202445943120.jpg','15942394048202445943120-small.jpg',-31);
insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-55,'15942394118974115717260.jpg','true','15942394118974115717260.jpg','15942394118974115717260-small.jpg',-31);

insert into announcement (id, accidents, active, car_color, creation_date, currency, damaged, description, doors, engine_capacity, engine_power, first_owner, fuel_type, mileage, net_price, price, price_negotiate, production_year, title, vehicle_subtype, vin, user_id, vehicle_model_id)
values (-32,	false,	true,	'RED',	NOW()	,'PLN'	,false	,'Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt', 5, 2000,	115,	false	,'DIESEL'	,170000,	false	,20000.00,	true,	2015,	'Sprzedam Opel Zafira',	'MINIVAN', 'ABC3435454545'	,	-1	,-3);

insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-56,'15942394085949748677959.jpg','false','15942394085949748677959.jpg','15942394085949748677959-small.jpg',-32);
insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-57,'15942394048202445943120.jpg','false','15942394048202445943120.jpg','15942394048202445943120-small.jpg',-32);
insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-58,'15942394118974115717260.jpg','true','15942394118974115717260.jpg','15942394118974115717260-small.jpg',-32);

insert into announcement (id, accidents, active, car_color, creation_date, currency, damaged, description, doors, engine_capacity, engine_power, first_owner, fuel_type, mileage, net_price, price, price_negotiate, production_year, title, vehicle_subtype, vin, user_id, vehicle_model_id)
values (-33,	false,	true,	'RED',	NOW()	,'PLN'	,false	,'Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt', 5, 1900,	160,	false	,'PETROL'	,200,	false	,66000.00,	true,	2020,	'Sprzedam Opel Zafira',	'MINIVAN', 'ABC3435454545'	,	-1	,-3);

insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-59,'15942394085949748677959.jpg','false','15942394085949748677959.jpg','15942394085949748677959-small.jpg',-33);
insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-60,'15942394048202445943120.jpg','false','15942394048202445943120.jpg','15942394048202445943120-small.jpg',-33);
insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-61,'15942394118974115717260.jpg','true','15942394118974115717260.jpg','15942394118974115717260-small.jpg',-33);

insert into announcement (id, accidents, active, car_color, creation_date, currency, damaged, description, doors, engine_capacity, engine_power, first_owner, fuel_type, mileage, net_price, price, price_negotiate, production_year, title, vehicle_subtype, vin, user_id, vehicle_model_id)
values (-17,	false,	true,	'RED',	NOW()	,'PLN'	,false	,'Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt', 5, 2600,	220,	false	,'PETROL_WITH_LPG'	,10200,	false	,37000.00,	true,	2019,	'Sprzedam Opel Zafira',	'MINIVAN', 'ABC3435454545'	,	-1	,-3);

insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-62,'15942394085949748677959.jpg','false','15942394085949748677959.jpg','15942394085949748677959-small.jpg',-17);
insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-63,'15942394048202445943120.jpg','false','15942394048202445943120.jpg','15942394048202445943120-small.jpg',-17);
insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-64,'15942394118974115717260.jpg','true','15942394118974115717260.jpg','15942394118974115717260-small.jpg',-17);

/*308*/

insert into announcement (id, accidents, active, car_color, creation_date, currency, damaged, description, doors, engine_capacity, engine_power, first_owner, fuel_type, mileage, net_price, price, price_negotiate, production_year, title, vehicle_subtype, vin, user_id, vehicle_model_id)
values (-35,	false,	true,	'BLACK',	NOW()	,'PLN'	,false	,'Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt', 5, 1700,	190,	false	,'PETROL'	,35000,	false	,40000.00,	true,	2018,	'Sprzedam 308',	'COMPACT', 'ABC3435454545'	,	-1	,-5);

insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-65,'15942407765432112418038.jpg','false','15942407765432112418038.jpg','15942407765432112418038-small.jpg',-35);
insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-66,'15942407807310301031734.jpg','false','15942407807310301031734.jpg','15942407807310301031734-small.jpg',-35);
insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-67,'15942407838147654470195.jpg','true','15942407838147654470195.jpg','15942407838147654470195-small.jpg',-35);


insert into announcement (id, accidents, active, car_color, creation_date, currency, damaged, description, doors, engine_capacity, engine_power, first_owner, fuel_type, mileage, net_price, price, price_negotiate, production_year, title, vehicle_subtype, vin, user_id, vehicle_model_id)
values (-36,	false,	true,	'BLACK',	NOW()	,'PLN'	,false	,'Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt', 5, 1700,	160,	false	,'PETROL'	,80000,	false	,25000.00,	true,	2016,	'Sprzedam 308',	'COMPACT', 'ABC3435454545'	,	-1	,-5);

insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-68,'15942407765432112418038.jpg','false','15942407765432112418038.jpg','15942407765432112418038-small.jpg',-36);
insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-69,'15942407807310301031734.jpg','false','15942407807310301031734.jpg','15942407807310301031734-small.jpg',-36);
insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-70,'15942407838147654470195.jpg','true','15942407838147654470195.jpg','15942407838147654470195-small.jpg',-36);


insert into announcement (id, accidents, active, car_color, creation_date, currency, damaged, description, doors, engine_capacity, engine_power, first_owner, fuel_type, mileage, net_price, price, price_negotiate, production_year, title, vehicle_subtype, vin, user_id, vehicle_model_id)
values (-37,	false,	true,	'BLACK',	NOW()	,'PLN'	,false	,'Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt', 5, 2000,	115,	false	,'DIESEL'	,170000,	false	,20000.00,	true,	2015,	'Sprzedam 308',	'COMPACT', 'ABC3435454545'	,	-1	,-5);

insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-71,'15942407765432112418038.jpg','false','15942407765432112418038.jpg','15942407765432112418038-small.jpg',-37);
insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-72,'15942407807310301031734.jpg','false','15942407807310301031734.jpg','15942407807310301031734-small.jpg',-37);
insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-73,'15942407838147654470195.jpg','true','15942407838147654470195.jpg','15942407838147654470195-small.jpg',-37);


insert into announcement (id, accidents, active, car_color, creation_date, currency, damaged, description, doors, engine_capacity, engine_power, first_owner, fuel_type, mileage, net_price, price, price_negotiate, production_year, title, vehicle_subtype, vin, user_id, vehicle_model_id)
values (-38,	false,	true,	'BLACK',	NOW()	,'PLN'	,false	,'Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt', 5, 1900,	160,	false	,'PETROL'	,200,	false	,66000.00,	true,	2020,	'Sprzedam 308',	'COMPACT', 'ABC3435454545'	,	-1	,-5);

insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-74,'15942407765432112418038.jpg','false','15942407765432112418038.jpg','15942407765432112418038-small.jpg',-38);
insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-75,'15942407807310301031734.jpg','false','15942407807310301031734.jpg','15942407807310301031734-small.jpg',-38);
insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-76,'15942407838147654470195.jpg','true','15942407838147654470195.jpg','15942407838147654470195-small.jpg',-38);


insert into announcement (id, accidents, active, car_color, creation_date, currency, damaged, description, doors, engine_capacity, engine_power, first_owner, fuel_type, mileage, net_price, price, price_negotiate, production_year, title, vehicle_subtype, vin, user_id, vehicle_model_id)
values (-18,	false,	true,	'BLACK',	NOW()	,'PLN'	,false	,'Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt', 5, 2000,	220,	false	,'HYBRID'	,25000,	false	,29000.00,	true,	2017,	'Sprzedam 308',	'COMPACT', 'ABC3435454545'	,	-1	,-5);

insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-77,'15942407765432112418038.jpg','false','15942407765432112418038.jpg','15942407765432112418038-small.jpg',-18);
insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-78,'15942407807310301031734.jpg','false','15942407807310301031734.jpg','15942407807310301031734-small.jpg',-18);
insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-79,'15942407838147654470195.jpg','true','15942407838147654470195.jpg','15942407838147654470195-small.jpg',-18);


/*Tipo*/

insert into announcement (id, accidents, active, car_color, creation_date, currency, damaged, description, doors, engine_capacity, engine_power, first_owner, fuel_type, mileage, net_price, price, price_negotiate, production_year, title, vehicle_subtype, vin, user_id, vehicle_model_id)
values (-40,	false,	true,	'RED',	NOW()	,'PLN'	,false	,'Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt', 5, 1700,	190,	false	,'PETROL'	,35000,	false	,40000.00,	true,	2018,	'Sprzedam Tipo',	'SEDAN', 'ABC3435454545'	,	-1	,-7);

insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-80,'15942413361909608247741.jpg','false','15942413361909608247741.jpg','15942413361909608247741-small.jpg',-40);
insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-81,'15942413405646693791991.jpg','false','15942413405646693791991.jpg','15942413405646693791991-small.jpg',-40);
insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-82,'15942413438117814578915.jpg','true','15942413438117814578915.jpg','15942413438117814578915-small.jpg',-40);


insert into announcement (id, accidents, active, car_color, creation_date, currency, damaged, description, doors, engine_capacity, engine_power, first_owner, fuel_type, mileage, net_price, price, price_negotiate, production_year, title, vehicle_subtype, vin, user_id, vehicle_model_id)
values (-41,	false,	true,	'RED',	NOW()	,'PLN'	,false	,'Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt', 5, 1700,	160,	false	,'PETROL'	,80000,	false	,25000.00,	true,	2016,	'Sprzedam Tipo',	'SEDAN', 'ABC3435454545'	,	-1	,-7);

insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-83,'15942413361909608247741.jpg','false','15942413361909608247741.jpg','15942413361909608247741-small.jpg',-41);
insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-84,'15942413405646693791991.jpg','false','15942413405646693791991.jpg','15942413405646693791991-small.jpg',-41);
insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-85,'15942413438117814578915.jpg','true','15942413438117814578915.jpg','15942413438117814578915-small.jpg',-41);


insert into announcement (id, accidents, active, car_color, creation_date, currency, damaged, description, doors, engine_capacity, engine_power, first_owner, fuel_type, mileage, net_price, price, price_negotiate, production_year, title, vehicle_subtype, vin, user_id, vehicle_model_id)
values (-42,	false,	true,	'RED',	NOW()	,'PLN'	,false	,'Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt', 5, 2000,	115,	false	,'DIESEL'	,170000,	false	,20000.00,	true,	2015,	'Sprzedam Tipo',	'SEDAN', 'ABC3435454545'	,	-1	,-7);

insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-86,'15942413361909608247741.jpg','true','15942413361909608247741.jpg','15942413361909608247741-small.jpg',-42);
insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-87,'15942413405646693791991.jpg','false','15942413405646693791991.jpg','15942413405646693791991-small.jpg',-42);
insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-88,'15942413438117814578915.jpg','false','15942413438117814578915.jpg','15942413438117814578915-small.jpg',-42);


insert into announcement (id, accidents, active, car_color, creation_date, currency, damaged, description, doors, engine_capacity, engine_power, first_owner, fuel_type, mileage, net_price, price, price_negotiate, production_year, title, vehicle_subtype, vin, user_id, vehicle_model_id)
values (-43,	false,	true,	'RED',	NOW()	,'PLN'	,false	,'Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt', 5, 1900,	160,	false	,'PETROL'	,200,	false	,66000.00,	true,	2020,	'Sprzedam Tipo',	'SEDAN', 'ABC3435454545'	,	-1	,-7);

insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-89,'15942413361909608247741.jpg','false','15942413361909608247741.jpg','15942413361909608247741-small.jpg',-43);
insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-90,'15942413405646693791991.jpg','true','15942413405646693791991.jpg','15942413405646693791991-small.jpg',-43);
insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-91,'15942413438117814578915.jpg','false','15942413438117814578915.jpg','15942413438117814578915-small.jpg',-43);


insert into announcement (id, accidents, active, car_color, creation_date, currency, damaged, description, doors, engine_capacity, engine_power, first_owner, fuel_type, mileage, net_price, price, price_negotiate, production_year, title, vehicle_subtype, vin, user_id, vehicle_model_id)
values (-19,	false,	true,	'RED',	NOW()	,'PLN'	,false	,'Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt', 5, 2600,	220,	false	,'DIESEL'	,63000,	false	,26500.00,	true,	2016,	'Sprzedam Tipo',	'SEDAN', 'ABC3435454545'	,	-1	,-7);

insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-92,'15942413361909608247741.jpg','false','15942413361909608247741.jpg','15942413361909608247741-small.jpg',-19);
insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-93,'15942413405646693791991.jpg','false','15942413405646693791991.jpg','15942413405646693791991-small.jpg',-19);
insert into picture (id,file_name,main_Photo_In_Announcement,repository_name,miniature_repository_name,announcement_id) values (-94,'15942413438117814578915.jpg','true','15942413438117814578915.jpg','15942413438117814578915-small.jpg',-19);


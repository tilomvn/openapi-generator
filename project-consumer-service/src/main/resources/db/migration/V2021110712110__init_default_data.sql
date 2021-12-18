INSERT INTO city(id, name)
VALUES ('1', 'CityA');
INSERT INTO city(id, name)
VALUES ('2', 'CityB');


INSERT INTO district(id, name, city_id)
VALUES ('1', 'Distric1', '1');
INSERT INTO district(id, name, city_id)
VALUES ('2', 'District2', '1');
INSERT INTO district(id, name, city_id)
VALUES ('3', 'Distric3', '2');
INSERT INTO district(id, name, city_id)
VALUES ('4', 'District4', '2');


INSERT INTO `commune`(id, name, district_id)
VALUES ('1', 'Commune1', '1');
INSERT INTO `commune`(id, name, district_id)
VALUES ('2', 'Commune2', '1');
INSERT INTO `commune`(id, name, district_id)
VALUES ('3', 'Commune3', '2');
INSERT INTO `commune`(id, name, district_id)
VALUES ('4', 'Commune4', '2');
INSERT INTO `commune`(id, name, district_id)
VALUES ('5', 'Commune5', '3');
INSERT INTO `commune`(id, name, district_id)
VALUES ('6', 'Commune6', '3');
INSERT INTO `commune`(id, name, district_id)
VALUES ('7', 'Commune7', '4');
INSERT INTO `commune`(id, name, district_id)
VALUES ('8', 'Commune8', '4');

INSERT INTO `category`(`id`, `names`, `code`)
VALUES ('1', '{"km":"Food & Beverage","en":"Food & Beverage"}', '1'),
       ('2', '{"km":"Travelling & Accommodation","en":"Travelling & Accommodation"}', '2'),
       ('3', '{"km":"Education","en":"Education"}', '3'),
       ('4', '{"km":"Education","en":"Education"}', '4'),
       ('5', '{"km":"Government","en":"Government"}', '5'),
       ('6', '{"km":"Shopping & Life Style","en":"Shopping & Life Style"}', '6'),
       ('7', '{"km":"Grocery & Retail Stores","en":"Grocery & Retail Stores"}', '7'),
       ('8', '{"km":"Medical Care","en":"Medical Care"}', '8'),
       ('9', '{"km":"Financial Service","en":"Financial Service"}', '9'),
       ('10', '{"km":"Other Category","en":"Other Category"}', '10'),
       ('11', '{"km":"Transportation","en":"Transportation"}', '11'),
       ('12', '{"km":"Utilities","en":"Utilities"}', '12');

INSERT INTO `user`(`id`, `status`) VALUE ('0e7a462b-301f-4b8d-89af-9ddf4c6892bc', 0);



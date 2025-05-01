INSERT INTO cars (price_per_day, make, model, registration_number, booked, in_service)
VALUES
    (500.00,'Volvo','V50','HRK 863',true,false),
    (3000.00,'Ferrari','F1','JOF 739',true,false),
    (500.00,'Ford','Transit','KOR 739',true,false),
    (2000.00,'Porsche','911 GT3','KLJ 798',true,false),
    (300.00,'Hyundai','Atos Prime','AWE 876',true,false);

INSERT INTO customers (social_security_number, first_name, last_name, email, phone)
VALUES
    ('19850101-1234','Anna','Svensson','',''),
    ('19900215-5678','Erik','Johansson','',''),
    ('19751230-9101','Maria','Lindberg','',''),
    ('19881122-3456','Johan','Karlsson','',''),
    ('19950505-7890','Elin','Andersson','','');

INSERT INTO orders (order_placed, start_date, return_date, total_cost, car_id, customer_id)
VALUES
    ('2025-01-01','2025-01-02','2026-06-04',1500, 1, 1),
    ('2025-02-23','2025-02-25','2026-06-27',9000, 2, 2),
    ('2025-03-03','2025-03-03','2026-06-05',1500, 3, 3),
    ('2025-03-09','2025-04-21','2026-06-23',6000, 4, 4),
    ('2025-12-31','2025-04-20','2026-06-25',900, 5, 5);
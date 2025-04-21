-------------------------------------------------------------------------------------------------------------------------------------------
-- Role: "WORKS"
CREATE ROLE "WORKS" WITH
  NOLOGIN
  NOSUPERUSER
  INHERIT
  NOCREATEDB
  CREATEROLE
  NOREPLICATION
  NOBYPASSRLS;
  CONNECTION LIMIT -1;
COMMENT ON ROLE "WORKS" IS 'Мастера. Могут изменять и просматривать Наряды, просматривать список работ которые нужно сделать и список необходимых комплектующих';

GRANT SELECT, UPDATE ON TABLE public.work_orders TO "WORKS";
GRANT SELECT ON TABLE public.work_order_status TO "WORKS";
GRANT SELECT ON TABLE public.order_services TO "WORKS";
GRANT SELECT ON TABLE public.order_components TO "WORKS";
-------------------------------------------------------------------------------------------------------------------------------------------
CREATE ROLE worker WITH
  LOGIN
  NOSUPERUSER
  INHERIT
  NOCREATEDB
  NOCREATEROLE
  NOREPLICATION
  NOBYPASSRLS
  ENCRYPTED PASSWORD 'SCRAM-SHA-256$4096:By6IvY3JKDCdNWJvIirQzw==$NVFpp0uuQU1IUG399IMOIimxn8iy/XW+2dfFfSpMda8=:87EwjPDkzrVEvr1g+EgyX8j37hStIN0xlTTplaTyKVU=';

GRANT "WORKS" TO worker;
-------------------------------------------------------------------------------------------------------------------------------------------
-- Role: "ANALYSTS"
CREATE ROLE "ANALYSTS" WITH
	NOLOGIN
	NOSUPERUSER
	NOCREATEDB
	CREATEROLE
	INHERIT
	NOREPLICATION
	NOBYPASSRLS
	CONNECTION LIMIT -1;
COMMENT ON ROLE "ANALYSTS" IS 'Аналитики имеют доступ ко всем таблицам на чтение, кроме Администрирования';
--Добавить гранты

GRANT SELECT ON TABLE public.bonus_account TO "ANALYSTS";
GRANT SELECT ON TABLE public.bonus_account_operation TO "ANALYSTS";
GRANT SELECT ON TABLE public.category_of_component TO "ANALYSTS";
GRANT SELECT ON TABLE public.client_status TO "ANALYSTS";
GRANT SELECT ON TABLE public.clients TO "ANALYSTS";
GRANT SELECT ON TABLE public.component TO "ANALYSTS";
GRANT SELECT ON TABLE public.employee_service TO "ANALYSTS";
GRANT SELECT ON TABLE public.employees TO "ANALYSTS";
GRANT SELECT ON TABLE public.employees_moving TO "ANALYSTS";
GRANT SELECT ON TABLE public.inventory TO "ANALYSTS";
GRANT SELECT ON TABLE public.inventory_issue TO "ANALYSTS";
GRANT SELECT ON TABLE public.inventory_receipt TO "ANALYSTS";
GRANT SELECT ON TABLE public.invoice_for_payment TO "ANALYSTS";
GRANT SELECT ON TABLE public.locations TO "ANALYSTS";
GRANT SELECT ON TABLE public.locations_type TO "ANALYSTS";
GRANT SELECT ON TABLE public.order_components TO "ANALYSTS";
GRANT SELECT ON TABLE public.order_services TO "ANALYSTS";
GRANT SELECT ON TABLE public.order_status TO "ANALYSTS";
GRANT SELECT ON TABLE public.orders TO "ANALYSTS";
GRANT SELECT ON TABLE public.schedule TO "ANALYSTS";
GRANT SELECT ON TABLE public.services TO "ANALYSTS";
GRANT SELECT ON TABLE public.staffing_table TO "ANALYSTS";
GRANT SELECT ON TABLE public.type_of_device TO "ANALYSTS";
GRANT SELECT ON TABLE public.work_order_status TO "ANALYSTS";
GRANT SELECT ON TABLE public.work_orders TO "ANALYSTS";
-------------------------------------------------------------------------------------------------------------------------------------------
-- Role: "SALES"
CREATE ROLE "SALES" WITH
	NOLOGIN
	NOSUPERUSER
	NOCREATEDB
	NOCREATEROLE
	INHERIT
	NOREPLICATION
	NOBYPASSRLS
	CONNECTION LIMIT -1;
COMMENT ON ROLE "SALES" IS 'Менеджеры имеют доступы на создание, изменение, просмотр к клиентам, заказам, нарядам, офисам (просмотр)';
--Добавить гранты
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.clients TO "SALES";
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.orders TO "SALES";
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.order_services TO "SALES";
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.order_components TO "SALES";
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.order_status TO "SALES";
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.work_orders TO "SALES";
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.work_order_status TO "SALES";
GRANT SELECT ON TABLE public.locations TO "SALES";
GRANT SELECT ON TABLE public.locations_type TO "SALES";
-------------------------------------------------------------------------------------------------------------------------------------------
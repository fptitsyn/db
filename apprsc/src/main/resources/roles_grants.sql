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
-------------------------------------------------------------------------------------------------------------------------------------------
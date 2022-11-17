/* data.sql 이라는 파일을 resources폴더 하위에 두면 JPA가 로딩을 할 때 자동으로 해당 쿼리를 한번 실행한다 */
call next value for hibernate_sequence;
insert into user(`id`, `name`, `email`, `created_at`, `updated_at`) values (1, 'martin', 'martin@fastcampus.com', now(), now());

call next value for hibernate_sequence;
insert into user(`id`, `name`, `email`, `created_at`, `updated_at`) values (2, 'dennis', 'dennis@fastcampus.com', now(), now());

call next value for hibernate_sequence;
insert into user(`id`, `name`, `email`, `created_at`, `updated_at`) values (3, 'sophia', 'sophia@slowcampus.com', now(), now());

call next value for hibernate_sequence;
insert into user(`id`, `name`, `email`, `created_at`, `updated_at`) values (4, 'james', 'james@slowcampus.com', now(), now());

call next value for hibernate_sequence;
insert into user(`id`, `name`, `email`, `created_at`, `updated_at`) values (5, 'martin', 'martin@another.com', now(), now());
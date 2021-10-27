INSERT INTO app.users(username, name) values ('adam.lerner', 'Adam Lerner MAW team'),
('peter.robinson', 'Peter Robinson MAW team'),
('mark.rees', 'Mark Rees MAW team');

INSERT into app.user_role(user_id, role_id, last_updated, last_updated_by) values ((select id from app.users where username = 'adam.lerner'), 3, current_timestamp, 'AUTOMATED'),
((select id from app.users where username = 'peter.robinson'), 3, current_timestamp, 'AUTOMATED'),
((select id from app.users where username = 'mark.rees'), 3, current_timestamp, 'AUTOMATED');
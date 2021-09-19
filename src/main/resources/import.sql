INSERT INTO role VALUES(nextval('role_sequence'), 'ADMIN');
INSERT INTO role VALUES(nextval('role_sequence'), 'EMPLOYEE');
INSERT INTO role VALUES(nextval('role_sequence'), 'USER');
INSERT INTO role VALUES(nextval('role_sequence'), 'CARDIO_TRAINER');
INSERT INTO role VALUES(nextval('role_sequence'), 'WEIGHT_LIFTING_TRAINER');

INSERT INTO iron_user VALUES (nextval('user_sequence'), 'admin@admin.com',       true, '',      false, '',      '$2a$10$tpA0.LhVCAzZzN9jP90AAuokZOghqi3Pd3.hzTec8mCZKC8MdCaBy', 'admin');
INSERT INTO iron_user VALUES (nextval('user_sequence'), 'employee@employee.com', true, '',      false, '',      '$2a$10$uFsgfCqAfzywMaq1poI2MOe4VTbTsKVEleYp8qRoHv7xwzfbv5ZtG', 'employee');
INSERT INTO iron_user VALUES (nextval('user_sequence'), 'user@user.com',         true, 'user',  false, 'user',  '$2a$10$xcIdSU3V0eKuqdyJsvZxL.q6h6lYjgNAqzscqKbZUurZQ3iRzqrDy', 'user');

INSERT INTO iron_user_roles VALUES (1, 1);
INSERT INTO iron_user_roles VALUES (1, 2);
INSERT INTO iron_user_roles VALUES (2, 2);
INSERT INTO iron_user_roles VALUES (3, 3);

INSERT INTO exercise VALUES(nextval('exercise_sequence'), 'https://thumbs.gfycat.com/BlaringTornBelugawhale-small.gif',                   'Jumping jacks',                    '1b98WrRrmUs');
INSERT INTO exercise VALUES(nextval('exercise_sequence'), 'https://i.imgur.com/UJAnRhJ.gif',                                              'Abdominal crunches',               '_YVhhXc2pSY');
INSERT INTO exercise VALUES(nextval('exercise_sequence'), 'https://thumbs.gfycat.com/BlaringTornBelugawhale-small.gif',                   'Russian twist',                    'l4kQd9eWclE');
INSERT INTO exercise VALUES(nextval('exercise_sequence'), 'https://thumbs.gfycat.com/BlaringTornBelugawhale-small.gif',                   'Mountain climber',                 '1b98WrRrmUs');
INSERT INTO exercise VALUES(nextval('exercise_sequence'), 'https://thumbs.gfycat.com/BlaringTornBelugawhale-small.gif',                   'Heel touch',                       '1b98WrRrmUs');
INSERT INTO exercise VALUES(nextval('exercise_sequence'), 'https://i.imgur.com/JtgnFH1.gif',                                              'Leg raises',                       'l4kQd9eWclE');
INSERT INTO exercise VALUES(nextval('exercise_sequence'), 'https://i.pinimg.com/originals/cf/b5/67/cfb5677a755fe7288b608a4fec6f09a0.gif', 'Plank',                            'y1hXARQhHZM');

INSERT INTO exercise VALUES(nextval('exercise_sequence'), 'https://thumbs.gfycat.com/BlaringTornBelugawhale-small.gif',                   'Cobra stretch',                    '1b98WrRrmUs');
INSERT INTO exercise VALUES(nextval('exercise_sequence'), 'https://thumbs.gfycat.com/BlaringTornBelugawhale-small.gif',                   'Spine lumbar twist stretch left',  '1b98WrRrmUs');
INSERT INTO exercise VALUES(nextval('exercise_sequence'), 'https://thumbs.gfycat.com/BlaringTornBelugawhale-small.gif',                   'Spine lumbar twist stretch right', '1b98WrRrmUs');
INSERT INTO exercise VALUES(nextval('exercise_sequence'), 'https://i.imgur.com/UJAnRhJ.gif',                                              'Incline push-ups',                 '_YVhhXc2pSY');
INSERT INTO exercise VALUES(nextval('exercise_sequence'), 'https://thumbs.gfycat.com/BlaringTornBelugawhale-small.gif',                   'Knee push-ups',                    'l4kQd9eWclE');
INSERT INTO exercise VALUES(nextval('exercise_sequence'), 'https://thumbs.gfycat.com/BlaringTornBelugawhale-small.gif',                   'Push-ups',                         '1b98WrRrmUs');
INSERT INTO exercise VALUES(nextval('exercise_sequence'), 'https://thumbs.gfycat.com/BlaringTornBelugawhale-small.gif',                   'Wide arm push-ups',                '1b98WrRrmUs');
INSERT INTO exercise VALUES(nextval('exercise_sequence'), 'https://i.imgur.com/JtgnFH1.gif',                                              'Box push-ups',                     'l4kQd9eWclE');
INSERT INTO exercise VALUES(nextval('exercise_sequence'), 'https://i.pinimg.com/originals/cf/b5/67/cfb5677a755fe7288b608a4fec6f09a0.gif', 'Hindu push-ups',                   'y1hXARQhHZM');
INSERT INTO exercise VALUES(nextval('exercise_sequence'), 'https://thumbs.gfycat.com/BlaringTornBelugawhale-small.gif',                   'Chest stretch',                    '1b98WrRrmUs');

INSERT INTO training VALUES (nextval('training_sequence'), 'Abdominal',     '', 'Beginner',     10, 1);
INSERT INTO training VALUES (nextval('training_sequence'), 'Chest',     '', 'Beginner',     10, 1);

INSERT INTO training_exercise VALUES(nextval('training_exercise_sequence'),  0, 20,  1, 1);
INSERT INTO training_exercise VALUES(nextval('training_exercise_sequence'), 20,  0,  2, 1);
INSERT INTO training_exercise VALUES(nextval('training_exercise_sequence'), 32,  0,  3, 1);
INSERT INTO training_exercise VALUES(nextval('training_exercise_sequence'), 30,  0,  4, 1);
INSERT INTO training_exercise VALUES(nextval('training_exercise_sequence'), 20,  0,  5, 1);
INSERT INTO training_exercise VALUES(nextval('training_exercise_sequence'), 16,  0,  6, 1);
INSERT INTO training_exercise VALUES(nextval('training_exercise_sequence'),  0, 30,  7, 1);
INSERT INTO training_exercise VALUES(nextval('training_exercise_sequence'), 20,  0,  2, 1);
INSERT INTO training_exercise VALUES(nextval('training_exercise_sequence'), 32,  0,  3, 1);
INSERT INTO training_exercise VALUES(nextval('training_exercise_sequence'), 30,  0,  4, 1);
INSERT INTO training_exercise VALUES(nextval('training_exercise_sequence'), 20,  0,  5, 1);
INSERT INTO training_exercise VALUES(nextval('training_exercise_sequence'), 16,  0,  6, 1);
INSERT INTO training_exercise VALUES(nextval('training_exercise_sequence'),  0, 30,  7, 1);
INSERT INTO training_exercise VALUES(nextval('training_exercise_sequence'),  0, 30,  8, 1);
INSERT INTO training_exercise VALUES(nextval('training_exercise_sequence'),  0, 30,  9, 1);
INSERT INTO training_exercise VALUES(nextval('training_exercise_sequence'),  0, 30, 10, 1);

INSERT INTO training_exercise VALUES(nextval('training_exercise_sequence'),  0, 20,   1, 2);
INSERT INTO training_exercise VALUES(nextval('training_exercise_sequence'), 16,  0,  11, 2);
INSERT INTO training_exercise VALUES(nextval('training_exercise_sequence'), 12,  0,  12, 2);
INSERT INTO training_exercise VALUES(nextval('training_exercise_sequence'), 10,  0,  13, 2);
INSERT INTO training_exercise VALUES(nextval('training_exercise_sequence'), 10,  0,  14, 2);
INSERT INTO training_exercise VALUES(nextval('training_exercise_sequence'), 12,  0,  11, 2);
INSERT INTO training_exercise VALUES(nextval('training_exercise_sequence'), 12,  0,  15, 2);
INSERT INTO training_exercise VALUES(nextval('training_exercise_sequence'), 10,  0,  14, 2);
INSERT INTO training_exercise VALUES(nextval('training_exercise_sequence'), 10,  0,  16, 2);
INSERT INTO training_exercise VALUES(nextval('training_exercise_sequence'), 0,  20,  10, 2);
INSERT INTO training_exercise VALUES(nextval('training_exercise_sequence'), 0,  20,  17, 2);

INSERT INTO user_trainings VALUES (nextval('user_trainings_sequence'), 3, 1);
INSERT INTO user_trainings VALUES (nextval('user_trainings_sequence'), 3, 2);


-- DADOS DE TESTE FICTÍCIOS. Execute uma vez em esquema de demonstração vazio.
-- Os hashes pertencem a valores aleatórios descartados. Cadastre sua conta pela API.

-- Contas de motoristas
INSERT INTO tb_usuario (id_usuario, nm_usuario, ds_senha_hash, dt_cadastro)
VALUES (sq_tb_usuario.NEXTVAL, 'teste_vagai_1', 'wn1jVxrVzXXHeJbeLpZTmg==:8pa2tOWU3nBtmEt9uTeGlz+4bsraHuYqgOBp7ykbR3s=', TIMESTAMP '2026-01-01 15:00:00');
INSERT INTO tb_usuario (id_usuario, nm_usuario, ds_senha_hash, dt_cadastro)
VALUES (sq_tb_usuario.NEXTVAL, 'teste_vagai_2', '/jx/vv5KmR6NWUYxwmyprQ==:MyiYUyRxT7d4eizgMNt9PXqOjgKsSUlJ+RzZFmyjJZA=', TIMESTAMP '2026-01-02 15:00:00');
INSERT INTO tb_usuario (id_usuario, nm_usuario, ds_senha_hash, dt_cadastro)
VALUES (sq_tb_usuario.NEXTVAL, 'teste_vagai_3', 'A1C9H4s0a5Fr08UKyEpLuA==:kclf0v0oQWjern5wjGsP1cFmLtz6iWx63pD7Zu0cLb0=', TIMESTAMP '2026-01-03 15:00:00');
INSERT INTO tb_usuario (id_usuario, nm_usuario, ds_senha_hash, dt_cadastro)
VALUES (sq_tb_usuario.NEXTVAL, 'teste_vagai_4', 'Q/LyYb8GiV/SXRF4QJ82Bg==:zSmuOt7gmJmaiw9slOPlHzwpxWn8pendKRhb0UcEbcs=', TIMESTAMP '2026-01-04 15:00:00');
INSERT INTO tb_usuario (id_usuario, nm_usuario, ds_senha_hash, dt_cadastro)
VALUES (sq_tb_usuario.NEXTVAL, 'teste_vagai_5', 'g094m2XTaW6pxL3bP2JWsw==:sInc8qLqRyGzsK6iVoAf5kSV8lgZsu7mVro0yLF+kpU=', TIMESTAMP '2026-01-05 15:00:00');
INSERT INTO tb_usuario (id_usuario, nm_usuario, ds_senha_hash, dt_cadastro)
VALUES (sq_tb_usuario.NEXTVAL, 'teste_vagai_6', 'zzsGEcDR0mNDcUJp3PqhSQ==:K1ddMG/WCIWb4ciSBURUJ2c0eeFONCTgUBDOaB5WSlk=', TIMESTAMP '2026-01-06 15:00:00');
INSERT INTO tb_usuario (id_usuario, nm_usuario, ds_senha_hash, dt_cadastro)
VALUES (sq_tb_usuario.NEXTVAL, 'teste_vagai_7', 'mDbfSwlR39WXY3GNaDL2+A==:6jx6oyVBVadRFQd/fVqFmplxhxhNdst5hy3LdvbdC7g=', TIMESTAMP '2026-01-07 15:00:00');
INSERT INTO tb_usuario (id_usuario, nm_usuario, ds_senha_hash, dt_cadastro)
VALUES (sq_tb_usuario.NEXTVAL, 'teste_vagai_8', 'jE2vJpQUpPKdACEvnIbW4A==:jSl4Dlo35FAHb6L7ricSAhkn0xbUjsRrTUt0sCPt9OY=', TIMESTAMP '2026-01-08 15:00:00');
INSERT INTO tb_usuario (id_usuario, nm_usuario, ds_senha_hash, dt_cadastro)
VALUES (sq_tb_usuario.NEXTVAL, 'teste_vagai_9', 'NdPT/AuTl+haJj8uMVaCGQ==:UwKtld2OZrZw/80oFJHF/N4tm1jDFNaL0BYFELoY22c=', TIMESTAMP '2026-01-09 15:00:00');
INSERT INTO tb_usuario (id_usuario, nm_usuario, ds_senha_hash, dt_cadastro)
VALUES (sq_tb_usuario.NEXTVAL, 'teste_vagai_10', 'xgQW4V7wY8R4tzjCd+Qogw==:doM2wFlHRVw/v3zmtuxnDpKvr/e3vlp/xSb/OZ/JBns=', TIMESTAMP '2026-01-10 15:00:00');
INSERT INTO tb_usuario (id_usuario, nm_usuario, ds_senha_hash, dt_cadastro)
VALUES (sq_tb_usuario.NEXTVAL, 'teste_vagai_11', 'uE8Oq/BT2nA2NG6qUsItYA==:bdvImdTq56hMLHykOR7dfjWtoH5A2E/26ZtBjmK8r9g=', TIMESTAMP '2026-01-11 15:00:00');
INSERT INTO tb_usuario (id_usuario, nm_usuario, ds_senha_hash, dt_cadastro)
VALUES (sq_tb_usuario.NEXTVAL, 'teste_vagai_12', 'ADzz9VVS9RI1B5feklj+RQ==:D0wuPO6dvyf6H7OM8CkMVi8wdYwnMGs8o7qYwjNiuwc=', TIMESTAMP '2026-01-12 15:00:00');

-- Locais de teste para pesquisa por rua
INSERT INTO tb_estacionamento (id_estacionamento, id_usuario_criador, nm_estacionamento, tp_estacionamento, nm_rua, nm_bairro, ds_numero, vl_primeira_hora, vl_demais_horas, st_ativo)
VALUES (sq_tb_estacionamento.NEXTVAL, (SELECT id_usuario FROM tb_usuario WHERE nm_usuario = 'teste_vagai_1'), 'TESTE - Local 1', 'GRATUITO', 'Rua de Teste', 'Bairro de Teste', '10', NULL, NULL, 1);
INSERT INTO tb_estacionamento (id_estacionamento, id_usuario_criador, nm_estacionamento, tp_estacionamento, nm_rua, nm_bairro, ds_numero, vl_primeira_hora, vl_demais_horas, st_ativo)
VALUES (sq_tb_estacionamento.NEXTVAL, (SELECT id_usuario FROM tb_usuario WHERE nm_usuario = 'teste_vagai_2'), 'TESTE - Local 2', 'ROTATIVO', 'Rua de Teste', 'Bairro de Teste', '20', NULL, NULL, 1);
INSERT INTO tb_estacionamento (id_estacionamento, id_usuario_criador, nm_estacionamento, tp_estacionamento, nm_rua, nm_bairro, ds_numero, vl_primeira_hora, vl_demais_horas, st_ativo)
VALUES (sq_tb_estacionamento.NEXTVAL, (SELECT id_usuario FROM tb_usuario WHERE nm_usuario = 'teste_vagai_3'), 'TESTE - Local 3', 'PRIVADO', 'Rua de Teste', 'Bairro de Teste', '30', 18, 8, 1);
INSERT INTO tb_estacionamento (id_estacionamento, id_usuario_criador, nm_estacionamento, tp_estacionamento, nm_rua, nm_bairro, ds_numero, vl_primeira_hora, vl_demais_horas, st_ativo)
VALUES (sq_tb_estacionamento.NEXTVAL, (SELECT id_usuario FROM tb_usuario WHERE nm_usuario = 'teste_vagai_4'), 'TESTE - Local 4', 'GRATUITO', 'Rua de Teste', 'Bairro de Teste', '40', NULL, NULL, 1);
INSERT INTO tb_estacionamento (id_estacionamento, id_usuario_criador, nm_estacionamento, tp_estacionamento, nm_rua, nm_bairro, ds_numero, vl_primeira_hora, vl_demais_horas, st_ativo)
VALUES (sq_tb_estacionamento.NEXTVAL, (SELECT id_usuario FROM tb_usuario WHERE nm_usuario = 'teste_vagai_5'), 'TESTE - Local 5', 'ROTATIVO', 'Rua de Teste', 'Bairro de Teste', '50', NULL, NULL, 1);
INSERT INTO tb_estacionamento (id_estacionamento, id_usuario_criador, nm_estacionamento, tp_estacionamento, nm_rua, nm_bairro, ds_numero, vl_primeira_hora, vl_demais_horas, st_ativo)
VALUES (sq_tb_estacionamento.NEXTVAL, (SELECT id_usuario FROM tb_usuario WHERE nm_usuario = 'teste_vagai_6'), 'TESTE - Local 6', 'PRIVADO', 'Rua de Teste', 'Bairro de Teste', '60', 18, 8, 1);
INSERT INTO tb_estacionamento (id_estacionamento, id_usuario_criador, nm_estacionamento, tp_estacionamento, nm_rua, nm_bairro, ds_numero, vl_primeira_hora, vl_demais_horas, st_ativo)
VALUES (sq_tb_estacionamento.NEXTVAL, (SELECT id_usuario FROM tb_usuario WHERE nm_usuario = 'teste_vagai_7'), 'TESTE - Local 7', 'GRATUITO', 'Rua de Teste', 'Bairro de Teste', '70', NULL, NULL, 1);
INSERT INTO tb_estacionamento (id_estacionamento, id_usuario_criador, nm_estacionamento, tp_estacionamento, nm_rua, nm_bairro, ds_numero, vl_primeira_hora, vl_demais_horas, st_ativo)
VALUES (sq_tb_estacionamento.NEXTVAL, (SELECT id_usuario FROM tb_usuario WHERE nm_usuario = 'teste_vagai_8'), 'TESTE - Local 8', 'ROTATIVO', 'Rua de Teste', 'Bairro de Teste', '80', NULL, NULL, 1);
INSERT INTO tb_estacionamento (id_estacionamento, id_usuario_criador, nm_estacionamento, tp_estacionamento, nm_rua, nm_bairro, ds_numero, vl_primeira_hora, vl_demais_horas, st_ativo)
VALUES (sq_tb_estacionamento.NEXTVAL, (SELECT id_usuario FROM tb_usuario WHERE nm_usuario = 'teste_vagai_9'), 'TESTE - Local 9', 'PRIVADO', 'Rua de Teste', 'Bairro de Teste', '90', 18, 8, 1);
INSERT INTO tb_estacionamento (id_estacionamento, id_usuario_criador, nm_estacionamento, tp_estacionamento, nm_rua, nm_bairro, ds_numero, vl_primeira_hora, vl_demais_horas, st_ativo)
VALUES (sq_tb_estacionamento.NEXTVAL, (SELECT id_usuario FROM tb_usuario WHERE nm_usuario = 'teste_vagai_10'), 'TESTE - Local 10', 'GRATUITO', 'Rua de Teste', 'Bairro de Teste', '100', NULL, NULL, 1);
INSERT INTO tb_estacionamento (id_estacionamento, id_usuario_criador, nm_estacionamento, tp_estacionamento, nm_rua, nm_bairro, ds_numero, vl_primeira_hora, vl_demais_horas, st_ativo)
VALUES (sq_tb_estacionamento.NEXTVAL, (SELECT id_usuario FROM tb_usuario WHERE nm_usuario = 'teste_vagai_11'), 'TESTE - Local 11', 'ROTATIVO', 'Rua de Teste', 'Bairro de Teste', '110', NULL, NULL, 1);
INSERT INTO tb_estacionamento (id_estacionamento, id_usuario_criador, nm_estacionamento, tp_estacionamento, nm_rua, nm_bairro, ds_numero, vl_primeira_hora, vl_demais_horas, st_ativo)
VALUES (sq_tb_estacionamento.NEXTVAL, (SELECT id_usuario FROM tb_usuario WHERE nm_usuario = 'teste_vagai_12'), 'TESTE - Local 12', 'PRIVADO', 'Rua de Teste', 'Bairro de Teste', '120', 18, 8, 1);

-- Histórico de relatos de disponibilidade
INSERT INTO tb_reporte (id_reporte, id_usuario, id_estacionamento, tp_reporte, ds_observacao, dt_reporte)
VALUES (sq_tb_reporte.NEXTVAL, (SELECT id_usuario FROM tb_usuario WHERE nm_usuario = 'teste_vagai_1'), (SELECT id_estacionamento FROM tb_estacionamento WHERE nm_estacionamento = 'TESTE - Local 1'), 'VAGA_ENCONTRADA', 'Relato fictício', TIMESTAMP '2026-01-01 15:00:00');
INSERT INTO tb_reporte (id_reporte, id_usuario, id_estacionamento, tp_reporte, ds_observacao, dt_reporte)
VALUES (sq_tb_reporte.NEXTVAL, (SELECT id_usuario FROM tb_usuario WHERE nm_usuario = 'teste_vagai_2'), (SELECT id_estacionamento FROM tb_estacionamento WHERE nm_estacionamento = 'TESTE - Local 2'), 'SEM_VAGA', 'Relato fictício', TIMESTAMP '2026-01-02 15:00:00');
INSERT INTO tb_reporte (id_reporte, id_usuario, id_estacionamento, tp_reporte, ds_observacao, dt_reporte)
VALUES (sq_tb_reporte.NEXTVAL, (SELECT id_usuario FROM tb_usuario WHERE nm_usuario = 'teste_vagai_3'), (SELECT id_estacionamento FROM tb_estacionamento WHERE nm_estacionamento = 'TESTE - Local 2'), 'VAGA_ENCONTRADA', 'Relato fictício', TIMESTAMP '2026-01-03 15:00:00');
INSERT INTO tb_reporte (id_reporte, id_usuario, id_estacionamento, tp_reporte, ds_observacao, dt_reporte)
VALUES (sq_tb_reporte.NEXTVAL, (SELECT id_usuario FROM tb_usuario WHERE nm_usuario = 'teste_vagai_4'), (SELECT id_estacionamento FROM tb_estacionamento WHERE nm_estacionamento = 'TESTE - Local 4'), 'SEM_VAGA', 'Relato fictício', TIMESTAMP '2026-01-04 15:00:00');
INSERT INTO tb_reporte (id_reporte, id_usuario, id_estacionamento, tp_reporte, ds_observacao, dt_reporte)
VALUES (sq_tb_reporte.NEXTVAL, (SELECT id_usuario FROM tb_usuario WHERE nm_usuario = 'teste_vagai_5'), (SELECT id_estacionamento FROM tb_estacionamento WHERE nm_estacionamento = 'TESTE - Local 5'), 'VAGA_ENCONTRADA', 'Relato fictício', TIMESTAMP '2026-01-05 15:00:00');
INSERT INTO tb_reporte (id_reporte, id_usuario, id_estacionamento, tp_reporte, ds_observacao, dt_reporte)
VALUES (sq_tb_reporte.NEXTVAL, (SELECT id_usuario FROM tb_usuario WHERE nm_usuario = 'teste_vagai_6'), (SELECT id_estacionamento FROM tb_estacionamento WHERE nm_estacionamento = 'TESTE - Local 5'), 'SEM_VAGA', 'Relato fictício', TIMESTAMP '2026-01-06 15:00:00');
INSERT INTO tb_reporte (id_reporte, id_usuario, id_estacionamento, tp_reporte, ds_observacao, dt_reporte)
VALUES (sq_tb_reporte.NEXTVAL, (SELECT id_usuario FROM tb_usuario WHERE nm_usuario = 'teste_vagai_7'), (SELECT id_estacionamento FROM tb_estacionamento WHERE nm_estacionamento = 'TESTE - Local 7'), 'VAGA_ENCONTRADA', 'Relato fictício', TIMESTAMP '2026-01-07 15:00:00');
INSERT INTO tb_reporte (id_reporte, id_usuario, id_estacionamento, tp_reporte, ds_observacao, dt_reporte)
VALUES (sq_tb_reporte.NEXTVAL, (SELECT id_usuario FROM tb_usuario WHERE nm_usuario = 'teste_vagai_8'), (SELECT id_estacionamento FROM tb_estacionamento WHERE nm_estacionamento = 'TESTE - Local 8'), 'SEM_VAGA', 'Relato fictício', TIMESTAMP '2026-01-08 15:00:00');
INSERT INTO tb_reporte (id_reporte, id_usuario, id_estacionamento, tp_reporte, ds_observacao, dt_reporte)
VALUES (sq_tb_reporte.NEXTVAL, (SELECT id_usuario FROM tb_usuario WHERE nm_usuario = 'teste_vagai_9'), (SELECT id_estacionamento FROM tb_estacionamento WHERE nm_estacionamento = 'TESTE - Local 8'), 'VAGA_ENCONTRADA', 'Relato fictício', TIMESTAMP '2026-01-09 15:00:00');
INSERT INTO tb_reporte (id_reporte, id_usuario, id_estacionamento, tp_reporte, ds_observacao, dt_reporte)
VALUES (sq_tb_reporte.NEXTVAL, (SELECT id_usuario FROM tb_usuario WHERE nm_usuario = 'teste_vagai_10'), (SELECT id_estacionamento FROM tb_estacionamento WHERE nm_estacionamento = 'TESTE - Local 10'), 'SEM_VAGA', 'Relato fictício', TIMESTAMP '2026-01-10 15:00:00');
INSERT INTO tb_reporte (id_reporte, id_usuario, id_estacionamento, tp_reporte, ds_observacao, dt_reporte)
VALUES (sq_tb_reporte.NEXTVAL, (SELECT id_usuario FROM tb_usuario WHERE nm_usuario = 'teste_vagai_11'), (SELECT id_estacionamento FROM tb_estacionamento WHERE nm_estacionamento = 'TESTE - Local 11'), 'VAGA_ENCONTRADA', 'Relato fictício', TIMESTAMP '2026-01-11 15:00:00');
INSERT INTO tb_reporte (id_reporte, id_usuario, id_estacionamento, tp_reporte, ds_observacao, dt_reporte)
VALUES (sq_tb_reporte.NEXTVAL, (SELECT id_usuario FROM tb_usuario WHERE nm_usuario = 'teste_vagai_12'), (SELECT id_estacionamento FROM tb_estacionamento WHERE nm_estacionamento = 'TESTE - Local 11'), 'SEM_VAGA', 'Relato fictício', TIMESTAMP '2026-01-12 15:00:00');

-- Notas e comentários sobre locais
INSERT INTO tb_avaliacao (id_avaliacao, id_usuario, id_estacionamento, nr_nota, ds_comentario, dt_avaliacao)
VALUES (sq_tb_avaliacao.NEXTVAL, (SELECT id_usuario FROM tb_usuario WHERE nm_usuario = 'teste_vagai_1'), (SELECT id_estacionamento FROM tb_estacionamento WHERE nm_estacionamento = 'TESTE - Local 1'), 2, 'Comentário fictício', TIMESTAMP '2026-01-01 15:00:00');
INSERT INTO tb_avaliacao (id_avaliacao, id_usuario, id_estacionamento, nr_nota, ds_comentario, dt_avaliacao)
VALUES (sq_tb_avaliacao.NEXTVAL, (SELECT id_usuario FROM tb_usuario WHERE nm_usuario = 'teste_vagai_2'), (SELECT id_estacionamento FROM tb_estacionamento WHERE nm_estacionamento = 'TESTE - Local 2'), 3, 'Comentário fictício', TIMESTAMP '2026-01-02 15:00:00');
INSERT INTO tb_avaliacao (id_avaliacao, id_usuario, id_estacionamento, nr_nota, ds_comentario, dt_avaliacao)
VALUES (sq_tb_avaliacao.NEXTVAL, (SELECT id_usuario FROM tb_usuario WHERE nm_usuario = 'teste_vagai_3'), (SELECT id_estacionamento FROM tb_estacionamento WHERE nm_estacionamento = 'TESTE - Local 3'), 4, 'Comentário fictício', TIMESTAMP '2026-01-03 15:00:00');
INSERT INTO tb_avaliacao (id_avaliacao, id_usuario, id_estacionamento, nr_nota, ds_comentario, dt_avaliacao)
VALUES (sq_tb_avaliacao.NEXTVAL, (SELECT id_usuario FROM tb_usuario WHERE nm_usuario = 'teste_vagai_4'), (SELECT id_estacionamento FROM tb_estacionamento WHERE nm_estacionamento = 'TESTE - Local 4'), 5, 'Comentário fictício', TIMESTAMP '2026-01-04 15:00:00');
INSERT INTO tb_avaliacao (id_avaliacao, id_usuario, id_estacionamento, nr_nota, ds_comentario, dt_avaliacao)
VALUES (sq_tb_avaliacao.NEXTVAL, (SELECT id_usuario FROM tb_usuario WHERE nm_usuario = 'teste_vagai_5'), (SELECT id_estacionamento FROM tb_estacionamento WHERE nm_estacionamento = 'TESTE - Local 5'), 1, 'Comentário fictício', TIMESTAMP '2026-01-05 15:00:00');
INSERT INTO tb_avaliacao (id_avaliacao, id_usuario, id_estacionamento, nr_nota, ds_comentario, dt_avaliacao)
VALUES (sq_tb_avaliacao.NEXTVAL, (SELECT id_usuario FROM tb_usuario WHERE nm_usuario = 'teste_vagai_6'), (SELECT id_estacionamento FROM tb_estacionamento WHERE nm_estacionamento = 'TESTE - Local 6'), 2, 'Comentário fictício', TIMESTAMP '2026-01-06 15:00:00');
INSERT INTO tb_avaliacao (id_avaliacao, id_usuario, id_estacionamento, nr_nota, ds_comentario, dt_avaliacao)
VALUES (sq_tb_avaliacao.NEXTVAL, (SELECT id_usuario FROM tb_usuario WHERE nm_usuario = 'teste_vagai_7'), (SELECT id_estacionamento FROM tb_estacionamento WHERE nm_estacionamento = 'TESTE - Local 7'), 3, 'Comentário fictício', TIMESTAMP '2026-01-07 15:00:00');
INSERT INTO tb_avaliacao (id_avaliacao, id_usuario, id_estacionamento, nr_nota, ds_comentario, dt_avaliacao)
VALUES (sq_tb_avaliacao.NEXTVAL, (SELECT id_usuario FROM tb_usuario WHERE nm_usuario = 'teste_vagai_8'), (SELECT id_estacionamento FROM tb_estacionamento WHERE nm_estacionamento = 'TESTE - Local 8'), 4, 'Comentário fictício', TIMESTAMP '2026-01-08 15:00:00');
INSERT INTO tb_avaliacao (id_avaliacao, id_usuario, id_estacionamento, nr_nota, ds_comentario, dt_avaliacao)
VALUES (sq_tb_avaliacao.NEXTVAL, (SELECT id_usuario FROM tb_usuario WHERE nm_usuario = 'teste_vagai_9'), (SELECT id_estacionamento FROM tb_estacionamento WHERE nm_estacionamento = 'TESTE - Local 9'), 5, 'Comentário fictício', TIMESTAMP '2026-01-09 15:00:00');
INSERT INTO tb_avaliacao (id_avaliacao, id_usuario, id_estacionamento, nr_nota, ds_comentario, dt_avaliacao)
VALUES (sq_tb_avaliacao.NEXTVAL, (SELECT id_usuario FROM tb_usuario WHERE nm_usuario = 'teste_vagai_10'), (SELECT id_estacionamento FROM tb_estacionamento WHERE nm_estacionamento = 'TESTE - Local 10'), 1, 'Comentário fictício', TIMESTAMP '2026-01-10 15:00:00');
INSERT INTO tb_avaliacao (id_avaliacao, id_usuario, id_estacionamento, nr_nota, ds_comentario, dt_avaliacao)
VALUES (sq_tb_avaliacao.NEXTVAL, (SELECT id_usuario FROM tb_usuario WHERE nm_usuario = 'teste_vagai_11'), (SELECT id_estacionamento FROM tb_estacionamento WHERE nm_estacionamento = 'TESTE - Local 11'), 2, 'Comentário fictício', TIMESTAMP '2026-01-11 15:00:00');
INSERT INTO tb_avaliacao (id_avaliacao, id_usuario, id_estacionamento, nr_nota, ds_comentario, dt_avaliacao)
VALUES (sq_tb_avaliacao.NEXTVAL, (SELECT id_usuario FROM tb_usuario WHERE nm_usuario = 'teste_vagai_12'), (SELECT id_estacionamento FROM tb_estacionamento WHERE nm_estacionamento = 'TESTE - Local 12'), 3, 'Comentário fictício', TIMESTAMP '2026-01-12 15:00:00');

-- Restrições e horários por local
INSERT INTO tb_regra_estacionamento (id_regra, id_estacionamento, ds_regra, nr_dia_semana, nr_inicio, nr_fim, tp_regra, st_ativo)
VALUES (sq_tb_regra_estacionamento.NEXTVAL, (SELECT id_estacionamento FROM tb_estacionamento WHERE nm_estacionamento = 'TESTE - Local 1'), 'Conferir sinalização de teste', 1, 480, 1080, 'PERMITIDO', 1);
INSERT INTO tb_regra_estacionamento (id_regra, id_estacionamento, ds_regra, nr_dia_semana, nr_inicio, nr_fim, tp_regra, st_ativo)
VALUES (sq_tb_regra_estacionamento.NEXTVAL, (SELECT id_estacionamento FROM tb_estacionamento WHERE nm_estacionamento = 'TESTE - Local 2'), 'Conferir sinalização de teste', 1, 480, 1080, 'PERMITIDO', 1);
INSERT INTO tb_regra_estacionamento (id_regra, id_estacionamento, ds_regra, nr_dia_semana, nr_inicio, nr_fim, tp_regra, st_ativo)
VALUES (sq_tb_regra_estacionamento.NEXTVAL, (SELECT id_estacionamento FROM tb_estacionamento WHERE nm_estacionamento = 'TESTE - Local 3'), 'Conferir sinalização de teste', 1, 480, 1080, 'PERMITIDO', 1);
INSERT INTO tb_regra_estacionamento (id_regra, id_estacionamento, ds_regra, nr_dia_semana, nr_inicio, nr_fim, tp_regra, st_ativo)
VALUES (sq_tb_regra_estacionamento.NEXTVAL, (SELECT id_estacionamento FROM tb_estacionamento WHERE nm_estacionamento = 'TESTE - Local 4'), 'Conferir sinalização de teste', 1, 480, 1080, 'PERMITIDO', 1);
INSERT INTO tb_regra_estacionamento (id_regra, id_estacionamento, ds_regra, nr_dia_semana, nr_inicio, nr_fim, tp_regra, st_ativo)
VALUES (sq_tb_regra_estacionamento.NEXTVAL, (SELECT id_estacionamento FROM tb_estacionamento WHERE nm_estacionamento = 'TESTE - Local 5'), 'Conferir sinalização de teste', 1, 480, 1080, 'PERMITIDO', 1);
INSERT INTO tb_regra_estacionamento (id_regra, id_estacionamento, ds_regra, nr_dia_semana, nr_inicio, nr_fim, tp_regra, st_ativo)
VALUES (sq_tb_regra_estacionamento.NEXTVAL, (SELECT id_estacionamento FROM tb_estacionamento WHERE nm_estacionamento = 'TESTE - Local 6'), 'Conferir sinalização de teste', 1, 480, 1080, 'PERMITIDO', 1);
INSERT INTO tb_regra_estacionamento (id_regra, id_estacionamento, ds_regra, nr_dia_semana, nr_inicio, nr_fim, tp_regra, st_ativo)
VALUES (sq_tb_regra_estacionamento.NEXTVAL, (SELECT id_estacionamento FROM tb_estacionamento WHERE nm_estacionamento = 'TESTE - Local 7'), 'Conferir sinalização de teste', 1, 480, 1080, 'PERMITIDO', 1);
INSERT INTO tb_regra_estacionamento (id_regra, id_estacionamento, ds_regra, nr_dia_semana, nr_inicio, nr_fim, tp_regra, st_ativo)
VALUES (sq_tb_regra_estacionamento.NEXTVAL, (SELECT id_estacionamento FROM tb_estacionamento WHERE nm_estacionamento = 'TESTE - Local 8'), 'Conferir sinalização de teste', 1, 480, 1080, 'PERMITIDO', 1);
INSERT INTO tb_regra_estacionamento (id_regra, id_estacionamento, ds_regra, nr_dia_semana, nr_inicio, nr_fim, tp_regra, st_ativo)
VALUES (sq_tb_regra_estacionamento.NEXTVAL, (SELECT id_estacionamento FROM tb_estacionamento WHERE nm_estacionamento = 'TESTE - Local 9'), 'Conferir sinalização de teste', 1, 480, 1080, 'PERMITIDO', 1);
INSERT INTO tb_regra_estacionamento (id_regra, id_estacionamento, ds_regra, nr_dia_semana, nr_inicio, nr_fim, tp_regra, st_ativo)
VALUES (sq_tb_regra_estacionamento.NEXTVAL, (SELECT id_estacionamento FROM tb_estacionamento WHERE nm_estacionamento = 'TESTE - Local 10'), 'Conferir sinalização de teste', 1, 480, 1080, 'PERMITIDO', 1);
INSERT INTO tb_regra_estacionamento (id_regra, id_estacionamento, ds_regra, nr_dia_semana, nr_inicio, nr_fim, tp_regra, st_ativo)
VALUES (sq_tb_regra_estacionamento.NEXTVAL, (SELECT id_estacionamento FROM tb_estacionamento WHERE nm_estacionamento = 'TESTE - Local 11'), 'Conferir sinalização de teste', 1, 480, 1080, 'PERMITIDO', 1);
INSERT INTO tb_regra_estacionamento (id_regra, id_estacionamento, ds_regra, nr_dia_semana, nr_inicio, nr_fim, tp_regra, st_ativo)
VALUES (sq_tb_regra_estacionamento.NEXTVAL, (SELECT id_estacionamento FROM tb_estacionamento WHERE nm_estacionamento = 'TESTE - Local 12'), 'Conferir sinalização de teste', 1, 480, 1080, 'PERMITIDO', 1);

-- Associação entre motorista e local favorito
INSERT INTO tb_usuario_favorito (id_usuario_favorito, id_usuario, id_estacionamento, dt_favorito)
VALUES (sq_tb_usuario_favorito.NEXTVAL, (SELECT id_usuario FROM tb_usuario WHERE nm_usuario = 'teste_vagai_1'), (SELECT id_estacionamento FROM tb_estacionamento WHERE nm_estacionamento = 'TESTE - Local 1'), TIMESTAMP '2026-01-01 15:00:00');
INSERT INTO tb_usuario_favorito (id_usuario_favorito, id_usuario, id_estacionamento, dt_favorito)
VALUES (sq_tb_usuario_favorito.NEXTVAL, (SELECT id_usuario FROM tb_usuario WHERE nm_usuario = 'teste_vagai_2'), (SELECT id_estacionamento FROM tb_estacionamento WHERE nm_estacionamento = 'TESTE - Local 2'), TIMESTAMP '2026-01-02 15:00:00');
INSERT INTO tb_usuario_favorito (id_usuario_favorito, id_usuario, id_estacionamento, dt_favorito)
VALUES (sq_tb_usuario_favorito.NEXTVAL, (SELECT id_usuario FROM tb_usuario WHERE nm_usuario = 'teste_vagai_3'), (SELECT id_estacionamento FROM tb_estacionamento WHERE nm_estacionamento = 'TESTE - Local 3'), TIMESTAMP '2026-01-03 15:00:00');
INSERT INTO tb_usuario_favorito (id_usuario_favorito, id_usuario, id_estacionamento, dt_favorito)
VALUES (sq_tb_usuario_favorito.NEXTVAL, (SELECT id_usuario FROM tb_usuario WHERE nm_usuario = 'teste_vagai_4'), (SELECT id_estacionamento FROM tb_estacionamento WHERE nm_estacionamento = 'TESTE - Local 4'), TIMESTAMP '2026-01-04 15:00:00');
INSERT INTO tb_usuario_favorito (id_usuario_favorito, id_usuario, id_estacionamento, dt_favorito)
VALUES (sq_tb_usuario_favorito.NEXTVAL, (SELECT id_usuario FROM tb_usuario WHERE nm_usuario = 'teste_vagai_5'), (SELECT id_estacionamento FROM tb_estacionamento WHERE nm_estacionamento = 'TESTE - Local 5'), TIMESTAMP '2026-01-05 15:00:00');
INSERT INTO tb_usuario_favorito (id_usuario_favorito, id_usuario, id_estacionamento, dt_favorito)
VALUES (sq_tb_usuario_favorito.NEXTVAL, (SELECT id_usuario FROM tb_usuario WHERE nm_usuario = 'teste_vagai_6'), (SELECT id_estacionamento FROM tb_estacionamento WHERE nm_estacionamento = 'TESTE - Local 6'), TIMESTAMP '2026-01-06 15:00:00');
INSERT INTO tb_usuario_favorito (id_usuario_favorito, id_usuario, id_estacionamento, dt_favorito)
VALUES (sq_tb_usuario_favorito.NEXTVAL, (SELECT id_usuario FROM tb_usuario WHERE nm_usuario = 'teste_vagai_7'), (SELECT id_estacionamento FROM tb_estacionamento WHERE nm_estacionamento = 'TESTE - Local 7'), TIMESTAMP '2026-01-07 15:00:00');
INSERT INTO tb_usuario_favorito (id_usuario_favorito, id_usuario, id_estacionamento, dt_favorito)
VALUES (sq_tb_usuario_favorito.NEXTVAL, (SELECT id_usuario FROM tb_usuario WHERE nm_usuario = 'teste_vagai_8'), (SELECT id_estacionamento FROM tb_estacionamento WHERE nm_estacionamento = 'TESTE - Local 8'), TIMESTAMP '2026-01-08 15:00:00');
INSERT INTO tb_usuario_favorito (id_usuario_favorito, id_usuario, id_estacionamento, dt_favorito)
VALUES (sq_tb_usuario_favorito.NEXTVAL, (SELECT id_usuario FROM tb_usuario WHERE nm_usuario = 'teste_vagai_9'), (SELECT id_estacionamento FROM tb_estacionamento WHERE nm_estacionamento = 'TESTE - Local 9'), TIMESTAMP '2026-01-09 15:00:00');
INSERT INTO tb_usuario_favorito (id_usuario_favorito, id_usuario, id_estacionamento, dt_favorito)
VALUES (sq_tb_usuario_favorito.NEXTVAL, (SELECT id_usuario FROM tb_usuario WHERE nm_usuario = 'teste_vagai_10'), (SELECT id_estacionamento FROM tb_estacionamento WHERE nm_estacionamento = 'TESTE - Local 10'), TIMESTAMP '2026-01-10 15:00:00');
INSERT INTO tb_usuario_favorito (id_usuario_favorito, id_usuario, id_estacionamento, dt_favorito)
VALUES (sq_tb_usuario_favorito.NEXTVAL, (SELECT id_usuario FROM tb_usuario WHERE nm_usuario = 'teste_vagai_11'), (SELECT id_estacionamento FROM tb_estacionamento WHERE nm_estacionamento = 'TESTE - Local 11'), TIMESTAMP '2026-01-11 15:00:00');
INSERT INTO tb_usuario_favorito (id_usuario_favorito, id_usuario, id_estacionamento, dt_favorito)
VALUES (sq_tb_usuario_favorito.NEXTVAL, (SELECT id_usuario FROM tb_usuario WHERE nm_usuario = 'teste_vagai_12'), (SELECT id_estacionamento FROM tb_estacionamento WHERE nm_estacionamento = 'TESTE - Local 12'), TIMESTAMP '2026-01-12 15:00:00');

UPDATE tb_regra_estacionamento SET ds_regra = 'Regra fictícia revisada' WHERE id_estacionamento = (SELECT id_estacionamento FROM tb_estacionamento WHERE nm_estacionamento = 'TESTE - Local 1');
DELETE FROM tb_usuario_favorito WHERE id_usuario = (SELECT id_usuario FROM tb_usuario WHERE nm_usuario = 'teste_vagai_12') AND id_estacionamento = (SELECT id_estacionamento FROM tb_estacionamento WHERE nm_estacionamento = 'TESTE - Local 12');
COMMIT;

ALTER TABLE TB_CLIENTE DROP FOREIGN KEY FK_TB_CLIENTE_ID_CARTAO
ALTER TABLE TB_PEDIDO DROP FOREIGN KEY FK_TB_PEDIDO_ID_CLIENTE
ALTER TABLE TB_PEDIDO_BEBIDA DROP FOREIGN KEY FK_TB_PEDIDO_BEBIDA_ID_PEDIDO
ALTER TABLE TB_PEDIDO_BEBIDA DROP FOREIGN KEY FK_TB_PEDIDO_BEBIDA_ID_BEBIDA
DROP TABLE TB_CLIENTE
DROP TABLE TB_CARTAO
DROP TABLE TB_PEDIDO
DROP TABLE TB_BEBIDA
DROP TABLE TB_PEDIDO_BEBIDA

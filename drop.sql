ALTER TABLE TB_CLIENTE DROP FOREIGN KEY FK_TB_CLIENTE_ID_CARTAO
ALTER TABLE TB_PEDIDO DROP FOREIGN KEY FK_TB_PEDIDO_ID_CLIENTE
ALTER TABLE TB_ITEM_SELECIONADO DROP FOREIGN KEY FK_TB_ITEM_SELECIONADO_ID_PEDIDO
ALTER TABLE TB_ITEM_SELECIONADO DROP FOREIGN KEY FK_TB_ITEM_SELECIONADO_ID_BEBIDA
DROP TABLE TB_CLIENTE
DROP TABLE TB_CARTAO
DROP TABLE TB_PEDIDO
DROP TABLE TB_BEBIDA
DROP TABLE TB_ITEM_SELECIONADO

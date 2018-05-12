CREATE TABLE TB_CLIENTE (ID BIGINT AUTO_INCREMENT NOT NULL, TXT_EMAIL VARCHAR(50), TXT_LOGIN VARCHAR(255), TXT_NOME VARCHAR(255), TXT_SENHA VARCHAR(255), TXT_TELEFONE VARCHAR(255), END_TXT_BAIRRO VARCHAR(255), END_TXT_CEP VARCHAR(255), END_TXT_CIDADE VARCHAR(255), END_TXT_COMPLEMENTO VARCHAR(255), END_TXT_ESTADO VARCHAR(255), END_TXT_LOGRADOURO VARCHAR(255), END_INT_NUMERO INTEGER, ID_CARTAO BIGINT, PRIMARY KEY (ID))
CREATE TABLE TB_CARTAO (ID BIGINT AUTO_INCREMENT NOT NULL, TXT_BANDEIRA VARCHAR(255) NOT NULL, DT_EXPIRACAO DATE, TXT_NUMERO VARCHAR(255), PRIMARY KEY (ID))
CREATE TABLE TB_PEDIDO (ID BIGINT AUTO_INCREMENT NOT NULL, DT_PEDIDO DATE, HR_PEDIDO TIME, TXT_STATUS_COMPRA VARCHAR(255), ID_CLIENTE BIGINT, PRIMARY KEY (ID))
CREATE TABLE TB_BEBIDA (ID BIGINT AUTO_INCREMENT NOT NULL, ALCOOLICO VARCHAR(3), NUM_ESTOQUE INTEGER NOT NULL, TXT_NOME VARCHAR(50) NOT NULL, NUM_PRECO DOUBLE NOT NULL, PERCENT_TEOR_ALCOOL FLOAT, QUANT_GRAMAS_ACUCAR INTEGER, PRIMARY KEY (ID))
CREATE TABLE TB_PEDIDO_BEBIDA (ID_PEDIDO BIGINT NOT NULL, ID_BEBIDA BIGINT NOT NULL, PRIMARY KEY (ID_PEDIDO, ID_BEBIDA))
ALTER TABLE TB_CLIENTE ADD CONSTRAINT FK_TB_CLIENTE_ID_CARTAO FOREIGN KEY (ID_CARTAO) REFERENCES TB_CARTAO (ID)
ALTER TABLE TB_PEDIDO ADD CONSTRAINT FK_TB_PEDIDO_ID_CLIENTE FOREIGN KEY (ID_CLIENTE) REFERENCES TB_CLIENTE (ID)
ALTER TABLE TB_PEDIDO_BEBIDA ADD CONSTRAINT FK_TB_PEDIDO_BEBIDA_ID_PEDIDO FOREIGN KEY (ID_PEDIDO) REFERENCES TB_PEDIDO (ID)
ALTER TABLE TB_PEDIDO_BEBIDA ADD CONSTRAINT FK_TB_PEDIDO_BEBIDA_ID_BEBIDA FOREIGN KEY (ID_BEBIDA) REFERENCES TB_BEBIDA (ID)

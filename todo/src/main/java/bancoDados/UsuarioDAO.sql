INSERT INTO usuarios ( login, senha, nome, email, ativo ) VALUES ( ?,?,?,?,? )

DELETE FROM usuarios where usuario_id=?

UPDATE usuarios SET login=?, senha=?, nome=?,email=?, ativo=? where usuario_id=?

SELECT usuario_id, login, senha, nome, email, ativo from usuarios

SELECT usuario_id, login, senha, nome, email, ativo from usuarios WHERE usuario_id=?

UPDATE usuarios SET senha=? where usuario_id=?

UPDATE usuarios SET ativo=? where usuario_id=?
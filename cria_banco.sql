-- PRAGMA foreign_keys = ON;

drop table Usuario;
drop table Recurso;
drop table Disciplina;
drop table Recurso_has_Sala;
drop table Reserva;
drop table Tipo_reserva;
drop table Horario;
drop table Horario_Turma;
drop table Turma;
drop table Turma_has_recurso;
drop table Semestre;
drop table Horario_Sala;
drop table CursoAno;
drop table Sala;
drop table Preferencia;
drop table Andar;
drop table Bloco;

create table Usuario(
    numusp	integer,
    nome		varchar,
    email		varchar,
    senha   varchar,
    PRIMARY KEY(numusp)
);

create table Recurso(
    codigo		integer, 
    descricao	varchar,
    PRIMARY KEY(codigo)
);

create table Turma_has_Recurso(
    codigo_turma		integer,
    codigo_recurso	integer,
    PRIMARY KEY(codigo_turma, codigo_recurso),
    FOREIGN KEY(codigo_turma) REFERENCES Turma(codigo),
    FOREIGN KEY(codigo_recurso) REFERENCES Recurso(codigo)
);

create table Disciplina(
    codigo	varchar,
    nome		varchar,
    numero_creditos integer,
    PRIMARY KEY(codigo)
);

create table CursoAno(
    codigo	varchar,
    nome		varchar,
    periodo	smallint,
    PRIMARY KEY(codigo)
);

create table Recurso_has_Sala(
    codigo_recurso 	integer,
    nome_sala 			varchar,
    PRIMARY KEY(codigo_recurso, nome_sala)
    FOREIGN KEY(codigo_recurso) REFERENCES Recurso(codigo),
    FOREIGN KEY(nome_sala) REFERENCES Sala(nome)
);

-- nome da sala substitui o id e o numero da sala
-- fiz essa mudanca por que nao tem como o usuario diferenciar
-- duas salas com o mesmo numero no excel, mesmo tendo IDs diferentes
create table Sala(
    id_andar integer,
    nome varchar,
    capacidade integer,
    PRIMARY KEY(nome),
    FOREIGN KEY(id_andar) REFERENCES Andar(id)
);

-- pavimento agora chama andar
create table Andar(
	id 					integer,
    id_bloco 		varchar,
    numero_andar 	integer,
	PRIMARY KEY(id),
    FOREIGN KEY(id_bloco) REFERENCES Bloco_Instituto(id)
);

-- mudei o nome de bloco_instituto para bloco
create table Bloco(
	id			integer,
    nome		varchar,
    instituto	varchar,
    PRIMARY KEY(id)
);

create table Tipo_reserva(
    tipo			varchar,
    descricao	varchar,
    observacao varchar,
    PRIMARY KEY(tipo)
);

create table Horario(
    id integer,
    inicio integer,
    fim integer,
    diasem integer,
    PRIMARY KEY(id)
);

create table Turma(
    codigo integer,
    codigo_curso varchar,
    codigo_disciplina varchar,
    numero_alunos smallint,
    PRIMARY KEY(codigo),
    FOREIGN KEY(codigo_curso) REFERENCES CursoAno(codigo),
    FOREIGN KEY(codigo_disciplina) REFERENCES Disciplina(codigo)
);

create table Horario_Turma(
    codigo_turma integer,
    id_horario integer,
    PRIMARY KEY(codigo_turma, id_horario),
    FOREIGN KEY(codigo_turma) REFERENCES Turma(codigo),
    FOREIGN KEY(id_horario) REFERENCES Horario(id)
);

create table Semestre(
    ano integer,
    numsem integer,
    inicio text,
    fim text,
    PRIMARY KEY(ano, numsem)
);

create table Horario_Sala(
    nome_sala varchar,
    id_horario integer,
    codigo_turma integer,
    Data_2 text,
    codigo_reserva integer,
    PRIMARY KEY(nome_sala, id_horario, codigo_turma, Data_2),
    FOREIGN KEY(nome_sala) REFERENCES Sala(nome),
    FOREIGN KEY(id_horario) REFERENCES Horario_Turma(id_horario),
    FOREIGN KEY(codigo_turma) REFERENCES Horario_Turma(codigo_turma)
);

-- agora a prioridade e' por sala e nao por bloco
create table Preferencia(
    codigo_curso varchar,
    nome_sala varchar,
    prioridade integer,
    PRIMARY KEY(codigo_curso, nome_sala),
    FOREIGN KEY(codigo_curso) REFERENCES CursoAno(codigo),
    FOREIGN KEY(nome_sala) REFERENCES Sala(nome)
);

create table Reserva(
    codigo integer,
    Usuario_numusp integer,
    Tipo_reserva_nomtip varchar,
    horario integer,
    IP integer,
    periodica boolean,
    manaut boolean,
    PRIMARY KEY(codigo)
--    FOREIGN KEY(Usuario_numusp) REFERENCES Usuario(numusp),
--    FOREIGN KEY(Tipo_reserva_nomtip) REFERENCES Tipo_reserva(nomtip)
);

insert into usuario values(7277670, 'marcelo', 'admin@a.com', '123');
